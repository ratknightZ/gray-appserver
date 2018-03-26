$("button.signup").click(function () {
    //统一输入校验
    if (!validateRegisterForm()) {
        return;
    }
    var registerUrl = $(this).attr("data-url");
    $.ajax({
        url: registerUrl,
        type: "post",
        data: {
            email: $("input.email").val(),
            password: $("input.password").val(),
            confirmPassword: $("input.confirm-password").val(),
            nick: $("input.nick").val()
        },
        success: function (data) {
            json = $.parseJSON(data);
            if (1 === json.isSuccess) {
                window.location.href = "/welcome";
            } else {
                alert(json.msg);
            }
        }
    });
});

function validateRegisterForm() {
    var email = $("input.email").val();
    var password = $("input.password").val();
    var confirmPassword = $("input.confirm-password").val();
    var nick = $("input.nick").val();
    var emailReg = /^[a-z0-9](\w|\.|-)*@([a-z0-9]+-?[a-z0-9]+\.){1,3}[a-z]{2,4}$/i;//邮箱正则表达
    if (email == "") {
        alert("请输入email!");
        return false;
    }
    if (email.match(emailReg) == null) {
        alert("请输入有效的email!");
        return false;
    }
    if (password == "") {
        alert("请输入密码!");
        return false;
    }
    // if (password.length < 6) {
    //     alert("密码长度必须为6-16位!");
    //     return false;
    // }
    if (confirmPassword == "") {
        alert("请输入确认密码!");
        return false;
    }
    if (password != confirmPassword) {
        alert("两次输入密码不一致!");
        return false;
    }
    if (nick == "") {
        alert("请输入昵称!");
        return false;
    }
    return true;
}