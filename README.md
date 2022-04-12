# MicroservicesOfBlog
## Background
本项目是我对大二上半学期所写的myblog项目的一个重构，将原本的SpringBoot单体结构的项目重构为SpringCloud微服务。本项目于大二寒假完成，旨在练习和尝试springcloud相关知识和中间件的使用。
## Structure
## Usages
### Service Discovery
  本项目采用Netflix Eureka来实现服务的注册与发现，同时两个注册中心作为服务彼此向对方注册，从而实现高可用注册中心。
### Auth Service
  本项目采用OAuth2标准来使用户授权客户端获取资源。其中auth-service模块负责搭建Authorization server，即认证服务器。本项目的授权模式包括：授权码模式和密码模式。认证服务器在验证用户信息的时候，通过mybatis-plus框架从mysql中提取出用户的信息进行验证，并发放OAuth2 tokens,同时认证服务器还会将发放的token存储在redis中。客户端携带token、client_id和client_secret即可获取相应的资源。
### Account Service与Blog Service
  Account Service负责实现用户账号相关的功能。比如用户注册、获取当前用户信息、获取当前用户的点赞数、获取当前用户的浏览量等功能。用户注册后，密码会通过BCrypt加密后存入数据库。Blog Service负责实现博客文章相关的功能，比如博客的撰写、点赞、收藏、评论、修改，删除等功能。在这两个服务的实现中，项目采用mybatis-plus框架来实现与mysql数据库的交互，利用mybatis-plus的自动填充功能来记录数据的创建和修改时间，同时还实现了数据的逻辑删除。
### Feign Service
  该模块负责调用Account Service和Blog Service。通过Feign来整合Ribbon和circuitbreaker实现负载均衡和服务的降级处理。注意：在当前版本openfeign的官方文档中并没有采用Hystrix进行配置，而是采用circuitbreaker。
### API Gateway
  该模块为整个项目配置了一个API网关，实现路由转发。并采用令牌桶算法实现了流量限制功能。同时为整个项目配置了一个全局的服务降级处理。注意：当前版本SpringCloud已经不集成Zuul来实现网关服务，而是采用Spring Cloud Gateway来实现网关服务。
### Config Server  
  本项目利用Spring Cloud Config构建了一个分布式配置中心。本项目除了注册中心，其他的模块均配置```bootstrap.yml```文件，从gitee的仓库中加载相应的配置.
### Spring Cloud Bus
  本项目将Spring Cloud Bus与RabbitMQ整合构成消息总线，从而实现服务配置的动态刷新。
### Distributed Tracing
  本项目采用Spring Cloud Sleuth+Zipkin+RabbitMQ+ElasticSearch，将微服务与Zipkin整合来追踪服务调用链路，利用消息中间件RabbitMQ方便数据异步处理，同时将数据存储在ElasticSearch中，从而方便对链路数据的存储和分析。注意：在Spring Boot 2.x以后，Zipkin Server采用官方的jar包来进行搭建，在环境中配置好RabbitMQ和Elasticsearch之后，通过命令调用即可。在Zipkin官方文档中```storage```的配置采用的是AWS ElasticSearch Service，但是该服务只有12个月是免费的，对学生不太友好。
