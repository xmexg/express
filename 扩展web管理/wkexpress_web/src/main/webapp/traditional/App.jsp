<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<jsp:useBean id="env" class="org.looom.wkexpress_web.model.EnvModel" scope="request"/>
<jsp:useBean id="user" class="org.looom.wkexpress_web.entity.User" scope="request"/>
<html lang="zh">
    <head>
        <meta charset="UTF-8">
        <link rel="icon" type="image/svg+xml" href="<jsp:getProperty name="env" property="webUi_resPath"/>res/logo.svg">
        <link rel="stylesheet" type="text/css" href="<jsp:getProperty name="env" property="webUi_resPath"/>css/App.css">
        <title>管理 - 校园快递</title>
        <script src="<jsp:getProperty name="env" property="webUi_resPath"/>js/jquery-3.7.1.min.js"></script>
        <script src="<jsp:getProperty name="env" property="webUi_resPath"/>js/vue.global.js"></script>
    </head>
    <body class="full-screen">
        <div id="app" class="full-screen">
            <!-- 页面顶部 -->
            <div class="top">
                <ul class="top_ul">
                    <li><a href="https://github.com/xmexg/express">项目地址</a></li>
                    <li><a>openid: {{openid}}</a></li>
                </ul>
            </div>
            <div class="bodyCon">
                <div class="left"> <!-- 页面顶部 -->
                    <a href="#content_courier" v-if="userType===1 || userType===9"><img src="<jsp:getProperty name="env" property="webUi_resPath"/>res/courier.svg" class="left_a_img" alt="派送员"><span class="left_a_text">派送员</span></a>
                    <a href="#content_manager" v-if="userType===9"><img src="<jsp:getProperty name="env" property="webUi_resPath"/>res/manager.svg" class="left_a_img" alt="管理员"><span class="left_a_text">管理员</span></a>
                    <a href="#content_about"><img src="<jsp:getProperty name="env" property="webUi_resPath"/>res/about.svg" class="left_a_img" alt="关于"><span class="left_a_text">关于</span></a>
                    <a href="#content_yjfk_forCourier" v-if="userType===1 || userType===9"><img src="<jsp:getProperty name="env" property="webUi_resPath"/>res/yjfk_forCourier.svg" class="left_a_img" alt="意见反馈"><span class="left_a_text">意见反馈</span></a>
                    <a href="#content_yjfk_forManager" v-if="userType===9"><img src="<jsp:getProperty name="env" property="webUi_resPath"/>res/yjfk_forManager.svg" class="left_a_img" alt="意见反馈(管理员)"><span class="left_a_text">意见反馈(管理员)</span></a>
                </div>
                <div class="hw">
                    <!-- 使用 iframe 加载页面 -->
                    <iframe :src="currentIframeSrc" class="iframe" frameborder="0"></iframe>
                </div>
            </div>
        </div>

        <script type="module">
            const { createApp } = Vue;

            createApp({
                data() {
                    return {
                        openid: <jsp:getProperty name="user" property="user_openid"/>, // 获取 openid
                        userType: <jsp:getProperty name="user" property="user_type"/>, // 获取用户类型
                        currentPath: window.location.hash || '#content_about', // 默认加载 "关于" 页面
                        routes: {
                            content_about: '<jsp:getProperty name="env" property="webUi_resPath"/>content/about.jsp',
                            content_yjfk_forCourier: '<jsp:getProperty name="env" property="webUi_resPath"/>content/yjfk_forCourier.html',
                            content_yjfk_forManager: '<jsp:getProperty name="env" property="webUi_resPath"/>content/yjfk_forManager.html',  // 关于页面
                            content_manager: '<jsp:getProperty name="env" property="webUi_resPath"/>content/manager.jsp',  // 管理员页面
                            content_courier: '<jsp:getProperty name="env" property="webUi_resPath"/>content/courier.jsp',  // 派送员页面
                            p404: '<jsp:getProperty name="env" property="webUi_resPath"/>content/404.jsp'  // 404 页面
                        }
                    };
                },
                computed: {
                    currentIframeSrc() {
                        const path = this.currentPath.slice(1); // 去掉前面的 #
                        return this.routes[path] || this.routes.p404; // 路径映射到具体页面
                    }
                },
                mounted() {
                    // 监听 hash 变化
                    window.addEventListener('hashchange', () => {
                        this.currentPath = window.location.hash;
                    });
                },
                methods: {
                    // 获取指定token
                    getCookie(name) {
                        const cookieString = document.cookie; // 获取所有 cookies
                        const cookies = cookieString.split("; "); // 分割成键值对数组
                        for (const cookie of cookies) {
                            const [key, value] = cookie.split("="); // 拆分键和值
                            if (key === name) {
                                return decodeURIComponent(value); // 返回对应的值
                            }
                        }
                        return null; // 如果未找到，返回 null
                    }
                },
                beforeMount() {
                    // 检查是否登录
                    const token = this.getCookie('token');
                    if (token == null || token === '') {
                        window.location.href = 'login'; // 未登录，跳转到登录页面
                    }
                }
            }).mount('#app');
        </script>
    </body>
</html>
