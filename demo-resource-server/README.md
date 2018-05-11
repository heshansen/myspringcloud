# Resource Server Demo

## 概要

This demo project is for how to config resource server in spring cloud.

## Configuration Server config

请参考demo-client-config demo project

## 依赖

1. 添加jar包依赖

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
```

2. 配置Resource Server

* 启用资源服务器注解
```java
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig{

}
```

* 继承ResourceServerConfigurerAdapter 资源配置类
```java
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuthRequestedMatcher())
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }
}
```

configure(HttpSecurity http) 用来配置访问此资源服务器上的哪些资源需要认证的规则，

configure(ResourceServerSecurityConfigurer resources) 用来配置资源服务认证相关的信息。

* 配置jwt相关的beans， 这些beans配置和auth server 上的配置保持一致, 包括: JwtTokenStore、JwtAccessTokenConverter、
ResourceTokenService，我们这里使用的DefaultTokenServices实现

```
@Bean
public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    Resource resource = new ClassPathResource("public.txt");
    String publicKey = null;
    try{
        publicKey = resource.getInputStream().toString();
    }catch (final IOException ex){
        throw new RuntimeException(ex);
    }

    converter.setVerifierKey(publicKey);
    return converter;
}

@Bean
public TokenStore tokenStore(){
    return new JwtTokenStore(accessTokenConverter());
}

@Bean
@Primary
public DefaultTokenServices tokenServices(){
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    return defaultTokenServices;
}
```

* 配置认证服务颁发的token的签名公钥

1. 在resources目录下创建public.txt用于存放公钥。
2. 将auth-server README.md 公钥部分的值copy 到public.txt中

## ResourceId 配置

资源服务器的ResourceId配置用来确定client的token是否有访问当前资源服务的权限，token中的resourceIds， 来自于clientDetails
中的resourceIds字段，指定此client 具有哪些资源访问的权限。体现在token中为aud:[] 的值，当使用此token访问某一个
资源服务器的资源的时候，resource server 会比较token中的resourceIds是否包含当前resource server 的resourceId，
如果不包含则返回访问拒绝的异常，反之则可以正常访问资源.

```
@Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices())
                .resourceId("demo-resource-server");
    }
```

