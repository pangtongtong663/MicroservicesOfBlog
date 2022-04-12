# MicroservicesOfBlog
## Background
本项目是我对大二上半学期所写的myblog项目的一个重构，将原本的SpringBoot单体结构的项目重构为SpringCloud微服务。本项目于大二寒假完成，旨在练习和尝试springcloud相关知识和中间件的使用。
## Structure
![image2](https://github.com/pangtongtong663/picture/blob/main/picture6%20(2).jpg)
## Usages
### Service Discovery
本项目采用Netflix Eureka来实现服务的注册与发现，同时两个注册中心作为服务彼此向对方注册，从而实现高可用注册中心。

启动注册中心
```java
@EnableEurekaServer
@SpringBootApplication
public class MicroservicesOfBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicesOfBlogApplication.class, args);
    }

}
```
两个注册中心分别向对方注册
```yml
server:
  port: 1111

spring:
  application:
    name: eureka-server

eureka:
  instance:
    hostname: peer1

  client:
    service-url:
      defaultZone: http://peer2:1112/eureka/
```
```yml
server:
  port: 1112

spring:
  application:
    name: eureka-server

eureka:
  instance:
    hostname: peer2

  client:
    service-url:
      defaultZone: http://peer1:1111/eureka/
```
### Auth Service
本项目采用OAuth2标准来使用户授权客户端获取资源。auth-service模块负责搭建Authorization server，即授权服务器。

配置客户端服务信息

本项目的授权模式包括：授权码模式、密码模式和客户端模式。
```java
@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() //使用内存存储
                .withClient("account-service")//客户端名称
                .authorizedGrantTypes("client_credentials", "refresh_token","authorization_code","password")//授权模式
                .secret(passwordEncoder().encode("ptSecret"))
                .scopes("server")//授权范围
                .redirectUris("https://www.baidu.com")
                .and()
                .withClient("blog-service")
                .authorizedGrantTypes("client_credentials", "refresh_token","authorization_code","password")
                .secret(passwordEncoder().encode("ptSecret"))
                .scopes("server")
                .redirectUris("https://www.baidu.com");
    }
```
配置授权authorization以及令牌token的访问端点和令牌服务token services
```java
@Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.userDetailsService(userDetailsService())
                .authenticationManager(authenticationManager())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore);
    }
```
配置密码模式下，用户账号密码的验证。本项目通过mybatis-plus框架从mysql中提取出用户的信息进行验证，并发放OAuth2 tokens。
```java
public class MyUserDetails implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        if (userMapper.selectList(qw).size() != 1) {
            throw new UsernameNotFoundException(username);
        }
        User user = userMapper.selectList(qw).get(0);
        logger.info("表单登录用户名: " + username);
        String password = user.getPassword();
        logger.info("密码是: " + password);
        return user;
    }
}
```
认证服务器会将发放的token存储在redis中，如下配置：
```java
@Bean
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }
```
接下来，客户端携带token、client_id和client_secret即可获取相应的资源。
### Account Service
AccountService相当于一个资源服务器。

配置令牌解析（通过访问授权服务器解析令牌）
```java
@Bean
public ResourceServerTokenServices tokenServices() {
   return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
}

@Override
public void configure(HttpSecurity http) throws Exception {
   http.authorizeRequests()
           .antMatchers("/public/**").permitAll()
           .antMatchers("/oauth/**").permitAll()
           .antMatchers("/account/**").authenticated();
}
```
其中ResourceServerTokenServices实现大致如下
```java
@Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        if (map.containsKey("error")) {
            this.logger.debug("userinfo returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map);
 }
 
 private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        Object principal = getPrincipal(map);
        OAuth2Request request = getRequest(map);
        List<GrantedAuthority> authorities = this.authoritiesExtractor
                .extractAuthorities(map);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }
```
Account Service实现了用户账号相关的功能，比如用户注册、获取当前用户信息、获取当前用户的点赞数、获取当前用户的浏览量等功能。其中用户注册后，密码会通过BCrypt加密后存入数据库。
 
用户账号功能实现逻辑相似，下面以用户注册和获取用户点赞数为例。

用户注册
```java
@RequestMapping(path = "/public/create", method = RequestMethod.POST)
public String createUser(@RequestBody SignUpDto user) {
   accountService.create(user);
    return "创建成功！";
}
```
```java
private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
@Override
public void create(SignUpDto userDto) {
    DuplicateInfoException e = new DuplicateInfoException();
    QueryWrapper<User> qw = new QueryWrapper<>();
    qw.eq("username", userDto.getUsername());
    if (userMapper.selectList(qw).size() != 0) {
        e.addDuplicateInfoField("username");
    }
    String password = encoder.encode(userDto.getPassword());
    User user = User.builder().username(userDto.getUsername())
                .password(password)
                .likes(0).pageViews(0)
                .build();
    userMapper.insert(user);
    logger.info("User has been saved");
   }
}
```
获取用户当前点赞数
```java
@RequestMapping(path = "/account/pageviews", method = RequestMethod.GET)
public Integer getCurrentPageviews(Principal principal) {
    return accountService.getUserPageViewsByName(principal.getName());
}
```
```java
@Override
public Integer getUserPageViewsByName(String username) {
    User user = getUserByName(username);
    if (user.getUsername().equals("error")) {
        return -1;
    } else {
       return user.getPageViews();
    }
}
```
### Blog Service
BlogService相当于一个资源服务器。其配置和AccountService一致，在此不再赘述。

BlogService主要实现博客相关功能，比如博客的撰写、点赞、收藏、评论、修改，删除等功能。具体实现逻辑和AccountService相似，可以通过源码查看。

服务采用mybatis-plus框架来实现与mysql数据库的交互，利用mybatis-plus的自动填充功能来记录数据的创建和修改时间，同时还实现了数据的逻辑删除。主要配置如下：
```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "editTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "editTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

### Feign Service
该模块负责调用Account Service和Blog Service。通过Feign来整合Ribbon和circuitbreaker实现负载均衡和服务的降级处理。

降级处理实现如下：
```java
@Component
public class BlogServiceFallback implements BlogService {

    @Override
    public String write(BlogDto blogDto) {
        return "创建失败！";
    }

    @Override
    public String collect(CollectDto collectDto) {
        return "收藏失败！";
    }

    @Override
    public String comment(CommentDto commentDto) {
        return "评论失败！";
    }

    @Override
    public String modify(ModifyDto modifyDto) {
        return "修改失败！";
    }

    @Override
    public List<Blog> select(Integer userId) {
        return null;
    }

    @Override
    public String look(LookDto lookDto) {
        if (lookDto.getLike().equals("true")){
            return "点赞失败!";
        } else {
            return "浏览失败!";
        }
    }

    @Override
    public String deleteBlog(Integer record) {
        return "删除失败！";
    }

    @Override
    public String deleteComment(Integer record) {
        return "删除失败！";
    }

    @Override
    public String deleteCollection(Integer record) {
        return "删除失败！";
    }
}
```
负载均衡只需要如下配置:
```yml
feign:
  circuitbreaker:
    enabled: true
```
**注意**：在当前版本openfeign的官方文档中并没有采用Hystrix进行配置，而是采用circuitbreaker。依赖如下：
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```
### API Gateway
该模块为整个项目配置了一个API网关，实现路由转发，并采用令牌桶算法实现了流量限制功能。同时该模块也为整个项目配置了一个全局的服务降级处理。

配置如下：
```yml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true

      routes:
        - id: account-service
          uri: lb://account-service
          predicates:
            - Path=/account/**
          filters:
            - name: CircuitBreaker
              args:
                name: fallbackAccount
                fallbackUri: forward:/fallback-account
            - name: RequestRateLimiter
              args:
                key-resolver: '#{@hostAddrKeyResolver}'
                redis-rate-limiter.replenishRate: 1
                redis-rate-limiter.burstCapacity: 3

        - id: blog-service
          uri: lb://blog-service
          predicates:
            - Path=/blog/**
          filters:
            - name: CircuitBreaker
              args:
                name: fallbackBlog
                fallbackUri: forward:/fallback-blog
            - name: RequestRateLimiter
              args:
                key-resolver: '#{@hostAddrKeyResolver}'
                redis-rate-limiter.replenishRate: 1
                redis-rate-limiter.burstCapacity: 3

        - id: feign-service
          uri: lb://feign-service
          predicates:
            - Path=/api/**
          filters:
            - name: CircuitBreaker
              args:
                name: fallbackFeign
                fallbackUri: forward:/fallback-feign
            - name: RequestRateLimiter
              args:
                key-resolver: '#{@hostAddrKeyResolver}'
                redis-rate-limiter.replenishRate: 1
                redis-rate-limiter.burstCapacity: 3
```
**注意**：当前版本SpringCloud已经不集成Zuul来实现网关服务，而是采用Spring Cloud Gateway来实现网关服务。

依赖如下：
```
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```
### Config Server  
本项目利用Spring Cloud Config构建了一个分布式配置中心。本项目除了注册中心，其他的模块均配置```bootstrap.yml```文件，从gitee的仓库中加载相应的配置。

配置中心如下启动：
```java
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication { 
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```
### Spring Cloud Bus
本项目将Spring Cloud Bus与RabbitMQ整合构成消息总线，从而实现服务配置的动态刷新。
### Distributed Tracing
本项目采用Spring Cloud Sleuth + Zipkin+RabbitMQ + ElasticSearch，将微服务与Zipkin整合来追踪服务调用链路，利用消息中间件RabbitMQ方便数据异步处理，同时将数据存储在ElasticSearch中，从而方便对链路数据的存储和分析。

**注意**：在Spring Boot 2.x以后，Zipkin Server采用官方的jar包来进行搭建，在环境中配置好RabbitMQ和Elasticsearch之后，通过命令调用即可。在Zipkin官方文档中```storage```的配置采用的是AWS ElasticSearch Service，但是该服务只有12个月是免费的，对学生不太友好。
### 其他
其他配置和功能实现可以查看源码。
## 总结
springcloud的发展实在太过迅速。在实现本项目的过程中，我学习所用的书籍和博客，对于springcloud的配置大多已经不符合版本，出现各种各样的错误，甚至有些配置和依赖springcloud已经完全摒弃了，最后只好自己翻着官网文档去找到正确的版本。虽然实现了这个springcloud的项目，但是自己对于其中的源码并不了解，仅仅停留在应用水平，以后的学习依然路途漫长。
