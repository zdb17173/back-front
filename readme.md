

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
