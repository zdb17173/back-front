<!DOCTYPE html>
<html>
<head>
    <title>Login page</title>
    <meta charset="utf-8" />
</head>
<body>
<h1>Login page</h1>
<p>Example user: user / password</p>

<#if loginError?? &&  loginError == true>
<p class="error">Wrong user or password</p>
</#if>

<form action="/login" method="post">
    <label for="username">Username</label>:
    <input type="text" id="username" name="username" autofocus="autofocus" /> <br />
    <label for="password">Password</label>:
    <input type="password" id="password" name="password" /> <br />
    <input type="submit" value="Log in" />
</form>
<#--<p><a href="/index">Back to home page</a></p>-->
</body>
</html>