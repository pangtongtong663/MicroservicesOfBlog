# <font size=5>MicroservicesOfBlog</font>
  <font size=3>本项目是我对大二上半学期所写的myblog项目的一个重构，将原本的SpringBoot单体结构的项目重构为SpringCloud微服务。本项目于大二寒假完成。</font>
## <font size=3>本项目的结构以及所涉及的技术</font>
### <font size=3>Service Discovery</font>
  <font size=3>本项目采用**Netflix Eureka**来实现服务的注册与发现，同时两个注册中心作为服务彼此向对方注册，从而实现**高可用注册中心**。</font>
### <font size=3>Auth Service</font>
  <font size=3>本项目采用**OAuth2**标准来使用户授权客户端获取资源。其中**auth-service**模块负责搭建**Authorization server**，即**认证服务器**。本项目的授权模式包括：**授权码模式**和**密码模式**。认证服务器在验证用户信息的时候，通过**mybatis-plus**框架从**mysql**中提取出用户的信息进行验证，并发放**OAuth2 tokens**,同时认证服务器还会将发放的**token**存储在**redis**中。客户端携带**token**、**client_id**和**client_secret**即可获取相应的资源。</font>
### <font size=3>Account Service与Blog Service</font>
  <font size=3>**Account Service**负责实现用户账号相关的功能。比如用户注册、获取当前用户信息、获取当前用户的点赞数、获取当前用户的浏览量等功能。用户注册后，密码会通过**BCrypt**加密后存入数据库。**Blog Service**负责实现博客文章相关的功能，比如博客的撰写、点赞、收藏、评论、修改，删除等功能。在这两个服务的实现中，项目采用**mybatis-plus**框架来实现与**mysql**数据库的交互，利用**mybatis-plus**的**自动填充**功能来记录数据的创建和修改时间，同时还实现了数据的**逻辑删除**。</font>
### <font size=3>Feign Service</font>
  <font size=3>该模块负责调用Account Service和Blog Service。通过**Feign**来整合**Ribbon**和**circuitbreaker**实现负载均衡和服务的降级处理。**注意**：在当前版本**openfeign**的官方文档中并没有采用**Hystrix**进行配置，而是采用**circuitbreaker**。</font>
### <font size=3>API Gateway</font> 
  <font size=3>该模块为整个项目配置了一个API网关，实现**路由转发**。并采用令牌桶算法实现了**流量限制**功能。同时为整个项目配置了一个全局的**服务降级**处理。**注意**：当前版本**SpringCloud**已经不集成**Zuul**来实现网关服务，而是采用**Spring Cloud Gateway**来实现网关服务。</font>
### <font size=3>Config Server</font>   
  <font size=3>本项目利用**Spring Cloud Config**构建了一个分布式配置中心。本项目除了注册中心，其他的模块均配置```bootstrap.yml```文件，从gitee的仓库中加载相应的配置.</font>
### <font size=3>Spring Cloud Bus</font>
  <font size=3>本项目将**Spring Cloud Bus**与**RabbitMQ**整合构成消息总线，从而实现服务配置的**动态刷新**。</font>
### <font size=3>Distributed Tracing</font>
  <font size=3>本项目采用**Spring Cloud Sleuth**+**Zipkin**+**RabbitMQ**+**ElasticSearch**，将微服务与**Zipkin**整合来追踪服务调用链路，利用消息中间件**RabbitMQ**方便数据异步处理，同时将数据存储在**ElasticSearch**中，从而方便对链路数据的存储和分析。**注意**：在**Spring Boot 2.x**以后，**Zipkin Server**采用官方的jar包来进行搭建，在环境中配置好**RabbitMQ**和**Elasticsearch**之后，通过命令调用即可。在**Zipkin官方文档**中```storage```的配置采用的是**AWS ElasticSearch Service**，但是该服务只有12个月是免费的，对学生不太友好。</font>
