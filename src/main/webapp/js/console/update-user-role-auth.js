$(".update-user-role-auth").click(function () {
    var options = {
        beforeSubmit: function () {
            // showLoader();
        },
        success: function (data) {
            // hideLoader();
            json = $.parseJSON(data);
            if (json.isSuccess == 1) {
                alert("修改成功");
            } else {
                alert(json.msg);
            }
        },
    };
    $("#update-user-role-auth-form").ajaxSubmit(options);
});