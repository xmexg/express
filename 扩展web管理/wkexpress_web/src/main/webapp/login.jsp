<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
    <head>
        <title>登录 - 快递管理</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" type="text/css" href="css/login.css">
        <link rel="icon" type="image/svg+xml" href="res/logo.svg">
    </head>
    <body>
        <div id="main" data-tilt data-tilt-glare data-tilt-max-glare="0.7" data-tilt-scale="1" data-tilt-max="3" data-speed="1000">
            <div id="login_div">
                <div data-tilt data-tilt-full-page-listening data-tilt-glare data-tilt-max-glare="0.5" data-tilt-max="10" data-speed="1000">
                    <div id="login_content">
                        <h1>用户登录</h1>
                        <p>请输入客户端显示的登录码</p>
                        <br>
                        <div id="input_div">
                            <input type="text" id="login_code" class="login_input" maxlength="10" required />
                            <label for="login_code" id="input_label" class="login_label">登录码</label>
                            <img id="input_img" src="./res/bookmarks.svg" alt="登录码图标" />
                        </div>
                        <button id="login_btn" onclick="do_login()">登录</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script src="js/vanilla-tilt.min.js" defer></script>
    <script src="js/jquery-3.7.1.min.js" defer></script>
    <script src="js/login.js" defer></script>
</html>