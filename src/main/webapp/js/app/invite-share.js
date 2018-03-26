visitRecord();
function visitRecord() {
    var source = $('#source').val();
    if(source != ""){
        $.ajax({
            url: "/api/statistics/visit_record",
            type: "post",
            data: {
                source : source
            }
        });
    }
}
$("div.sms-code-wrapper button").click(function () {
    if (!validateSmsCodeForm()) {
        return;
    }
    var cellphone = $("div.cellphone-wrapper input").val();
    var smsCodeUrl = $(this).attr("data-url");
    $.ajax({
        url: smsCodeUrl,
        type: "post",
        data: {
            cellphone: cellphone
        },
        success: function (data) {
            json = $.parseJSON(data);
            if (json.isSuccess == 1) {
                alert("获取验证码成功，请注意查收！");
            } else {
                alert(json.msg);
            }
        }
    });
});

function validateSmsCodeForm() {
    if ($("div.cellphone-wrapper input").val() == "") {
        alert("请输入手机号码!");
        return false;
    }
    return true;
}

$("div.submit-wrapper button.submit-btn").click(function () {
    if (!validateRegisterForm()) {
        return;
    }
    var inviteCode = $("#invite-code").val();
    var cellphone = $("div.cellphone-wrapper input").val();
    var smsCode = $("div.sms-code-wrapper input").val();
    var submitUrl = $(this).attr("data-url");
    $.ajax({
        url: submitUrl,
        type: "post",
        data: {
            account: cellphone,
            smsCode: smsCode,
            inviteCode: inviteCode,
            loginTypeStr: "cellphone"
        },
        success: function (data) {
            json = $.parseJSON(data);
            if (json.isSuccess == 1) {
                alert("注册成功");
                var source = $('#source').val();
                $.ajax({
                    url : "/api/user_source/set_source",
                    type : "post",
                    data : {
                        userId : json.data.userId,
                        source : source
                    }
                });
            } else {
                alert(json.msg);
            }
        }
    });
});

function validateRegisterForm() {
    if ($("div.cellphone-wrapper input").val() == "") {
        alert("请输入手机号码!");
        return false;
    }
    if ($("div.sms-code-wrapper input").val() == "") {
        alert("请输入验证码!");
        return false;
    }
    return true;
}