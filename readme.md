

# 前后端分离

# back-springboot

后端服务，基于swagger的restful接口


# front-springboot

前端服务，基于springcloud-zuul的路由服务

application.yaml

zuul跳转到后台服务，或其他api
```
zuul:
  routes:
    back:
      url: http://127.0.0.1:8080/
      path: /back/**
    mapi:
      url: https://mapi.cgtn.com/mobileapp
      path: /mapi/**
```

freemarker支持
```
spring:
  freemarker:
    cache: false
    templateLoaderPath:
      - "classpath:/templates/"
      - "file:templates/"

```


# 配置与限制

文件上传超时限制

文件大小限制

文件乱码使用/zuul前缀，强制使用zuul servlet解决。

```
spring:
  http:
    multipart:
      max-file-size: 5000Mb
      max-request-size: 10Mb


zuul:
  #addHostHeader: true
  ignoredHeaders:
  sensitiveHeaders:
  routes:
    back:
      url: http://127.0.0.1:8080/
      path: /back/**
    upload:
      url: http://127.0.0.1:8080/
      #upload file name garbled
      #https://github.com/spring-cloud/spring-cloud-netflix/issues/1385
      #file upload is supported better through the zuul servlet directly rather than the spring mvc controller.
      #Prefix your uploads with /zuul and try.
      #
      #client upload use /zuul/upload/** to upload
      path: /upload/**
  host:
    #api invoke timeout setting
    socket-timeout-millis: 20000
    connect-timeout-millis: 20000

```


# 返回的页面iframe嵌入异常

由于zuul会添加一些安全头，引起iframe嵌入异常
```
ignoredHeaders: "X-Frame-Options"

```


