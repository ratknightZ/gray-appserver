$("a.logout").click(function () {
    $.ajax({
        url: "/logout",
        type: "post",
        success: function (data) {
            json = $.parseJSON(data);
            if (1 === json.isSuccess) {
                location.reload();
            } else {
                alert(json.msg);
            }
        }
    });
});

$("a.login").click(function () {
    window.location.href = "/login_page?redirectUrl=/welcome&&roleAuth=ROLE_USER";
});