$("button.signin").click(function () {
    //统一输入校验
    if (!validateLoginForm()) {
        return;
    }
    var loginUrl = $(this).attr("data-url");
    $.ajax({
        url: loginUrl,
        type: "post",
        data: {
            account: $("input.account").val(),
            password: $("input.password").val(),
            redirectUrl: $("input.redirect-url").val(),
            roleAuth: $("input.role-auth").val()
        },
        success: function (data) {
            json = $.parseJSON(data);
            if (1 === json.isSuccess && 1 == json.isRedirect) {
                window.location.href = json.redirectURL;
            } else {
                alert(json.msg);
            }
        }
    });
});

function validateLoginForm() {
    var account = $("input.account").val();
    var password = $("input.password").val();
    // var emailReg = /^[a-z0-9](\w|\.|-)*@([a-z0-9]+-?[a-z0-9]+\.){1,3}[a-z]{2,4}$/i;//邮箱正则表达
    if (account == "") {
        alert("请输入账户!");
        return false;
    }
    // if (account.match(emailReg) == null) {
    //     alert("请输入有效的email!");
    //     return false;
    // }
    if (password == "") {
        alert("请输入密码!");
        return false;
    }
    // if (password.length < 6) {
    //     alert("密码长度必须为6-16位!");
    //     return false;
    // }
    return true;
}