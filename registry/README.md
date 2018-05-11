# 注册服务

* profiles

 系统默认配置两个profile

 * development
    这个profile 对应Eureka 的standalone 模式， 用于开发人员使用
 * production
    对应Eureka 的peerAware 模式， 用于生产部署

* 如果启动时不指定任何的profile,默认profile为development, 使用以下命令可以启动:

```
    mvn spring-boot:run [-Dspring.profiles.active=development -DappPort=8889]
```

**注意** 中括号中的参数为系统默认值， 如果需要修改，按照以上方式修改即可


在spring_cloud bridge network 中， 目前我们只能通过container service name 或者IP 地址来互相访问， 默认的HOSTNAME 为container id 但是设置以后不能通过hostname
来互相访问了， 所以registry server 目前的名称暂时只能使用container name来互相访问，记住--name 的值，在启动client 注册时候需要设置REGISTRY_SERVER 这个环境变量，指向
registry container 的--name 值，

```
docker run --network spring_cloud --name registry-server -p 8081:8081 topbaby/registry
```