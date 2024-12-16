<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/svg+xml" href="res/logo.svg">
    <title>路由 - 校园快递</title>
    <style>
        body{
            width: 100vw;
            height: 100vh;
            padding: 0;
            margin: 0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>
    <h1>正在检测浏览器...</h1>
    <h3>若长时间无反应,请刷新页面</h3>
<br/>

</body>
<script>
    // 获取cookie的某值
    function getCookieValue(name) {
        const cookies = document.cookie.split('; ');
        for (const cookie of cookies) {
            const [key, value] = cookie.split('=');
            if (key === name) {
                return decodeURIComponent(value);
            }
        }
        return null; // 如果未找到对应的cookie，返回null
    }

    // 检测cookie，如果token为空，跳转到login.html
    const token = getCookieValue("token");
    if (token == null || token === "") {
        window.location.href = "login.html";
    }

    // 5秒后刷新当前页面
    setTimeout(() => {
        location.reload();
    }, 5000);

</script>
</html>