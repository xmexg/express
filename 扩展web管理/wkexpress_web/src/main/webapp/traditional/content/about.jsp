<%--
  Created by IntelliJ IDEA.
  User: vina
  Date: 2024/12/19
  Time: 22:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>关于本项目</title>
    <style>
        /* 基本样式 */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #a7c7e7, #4a90e2, #2e88d7);
            background-size: 400% 400%;
            animation: gradientBG 15s ease infinite;
            color: #fff;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            overflow: hidden;
        }

        @keyframes gradientBG {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }

        .container {
            background: rgba(255, 255, 255, 0.9);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
            max-width: 800px;
            width: 100%;
            text-align: center;
            backdrop-filter: blur(10px);
        }

        header {
            background-color: #2e88d7;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        header h1 {
            font-size: 2.5em;
            margin: 0;
            color: white;
            animation: fadeIn 2s ease-in-out;
            cursor: pointer;
        }

        header h1:hover {
            color: #ff7f50;  /* 悬停时的颜色变化 */
            transform: scale(1.1); /* 悬停时放大 */
            transition: all 0.3s ease;
        }

        .section {
            margin-bottom: 25px;
            animation: fadeUp 1.5s ease-in-out;
        }

        .section h3 {
            font-size: 1.8em;
            color: #2e88d7;
            text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
            margin-bottom: 10px;
            cursor: pointer;
        }

        .section h3:hover {
            color: #ff7f50;
            transform: scale(1.1);
            transition: all 0.3s ease;
            text-shadow: 3px 3px 8px rgba(0, 0, 0, 0.3);
        }

        .section p {
            font-size: 1.1em;
            color: #333;
            line-height: 1.6;
            text-align: left;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.15);
            transition: color 0.3s ease;
        }

        .section p:hover {
            color: #ff7f50;  /* 悬停时改变文字颜色 */
            transform: translateX(5px); /* 悬停时文字轻微向右移动 */
        }

        .footer {
            position: absolute;
            bottom: 20px;
            width: 100%;
            text-align: center;
            color: #bd27ff;
            font-size: 0.9em;
            animation: fadeIn 2s ease-in-out;
        }

        .footer p {
            margin: 0;
        }

        /* 动画效果 */
        @keyframes fadeIn {
            0% { opacity: 0; }
            100% { opacity: 1; }
        }

        @keyframes fadeUp {
            0% { opacity: 0; transform: translateY(20px); }
            100% { opacity: 1; transform: translateY(0); }
        }

        /* 屏幕小于600px时的响应式样式 */
        @media (max-width: 600px) {
            .container {
                padding: 20px;
            }

            header h1 {
                font-size: 2em;
            }

            .section h3 {
                font-size: 1.5em;
            }

            .section p {
                font-size: 1em;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <header>
        <h1>关于本项目</h1>
    </header>

    <div class="section">
        <h3>这是什么项目？</h3>
        <p>本项目是“校园快递帮”微信小程序的后端扩展，旨在帮助派送员和管理员在网页端查看订单数据。</p>
    </div>

    <div class="section">
        <h3>为什么左侧栏目和其他人的不一样？</h3>
        <p>栏目因身份而异，不同身份看到的内容不一样。这是为了提高用户体验，确保每位用户看到最相关的信息。</p>
    </div>

    <div class="section">
        <h3>如何设置账号密码登录？</h3>
        <p>本项目不支持也不计划支持账号密码登录。当前的登录方式是通过微信小程序端授权登录，这种方式更加安全。</p>
    </div>

    <div class="section">
        <h3>我发现当前页面本质是iframe加载jsp，为什么不能直接打开jsp？</h3>
        <p>我们计划用jsp和node实现两个截然不同的Web UI，通过 Web UI servlet 来引导加载哪个UI。这也导致目录jsp页面加载的资源是按照web_ui相对路径加载的，直接访问jsp页面导致资源404。未来可能支持单独加载某个区域的页面，等我交上作业就不会碰这个项目了，实现的可能性不大。</p>
    </div>

    <div class="footer">
        <p>© 2024 校园快递帮 | <a href="https://github.com/xmexg/express">express</a></p>
    </div>
</div>

</body>
</html>
