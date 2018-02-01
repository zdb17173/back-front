<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>freemarker test</title>
</head>
<body>
    <#include "common/head.ftl">

    <!-- curUser -->
    <#assign uid=security.getUserId()>
    ${uid}
    <#assign role=security.hasRole("ROLE_role1")>
    <#if role == true>
        has role1
    </#if>

    <h1>freemarker test ${name}</h1>
    <h1>user name: ${user.name}</h1>
    <h1>user age: ${user.age}</h1>
    <#list user.tag as tg>
        <h5>tg: ${tg}</h5>
    </#list>

    <#include "common/bottom.ftl">
</body>
</html>