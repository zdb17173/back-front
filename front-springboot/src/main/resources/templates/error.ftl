<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>freemarker test</title>
</head>
<body>
<#include "common/head.ftl">

<h4>status: ${status}</h4>
<h4>error: ${error}</h4>
<h4>message: ${message}</h4>

<#include "common/bottom.ftl">
</body>
</html>