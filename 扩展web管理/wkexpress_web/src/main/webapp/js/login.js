function do_login(){
    let login_code = $("#login_code").val();
    console.log("登录码：", login_code);
    if(login_code === ""){
        alert("请输入密码");
        return;
    }
    $.ajax({
        url: "login_code",  // 后端接收的 URL
        type: "POST",  // 请求类型
        data: {
            loginCode: login_code  // 发送的数据，作为表单字段
        },
        success: function(data) {
            if (data.code === 200) {
                document.cookie="token="+data.data;
                window.location.href = "index";
            } else {
                alert(data.message);
            }
        },
        fail: function (res) {
            alert("连接异常, 请稍后重试")
        }
    });
}