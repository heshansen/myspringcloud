# Demo client for configuration server

## 概述

这个demo的目的是演示如何在一个项目中集成配置服务器

* jar 包依赖

```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
```

* xml 添加配置服务地址

```xml
    spring:
      cloud:
        config:
          uri: http://localhost:8080
```




