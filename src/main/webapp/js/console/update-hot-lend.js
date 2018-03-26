// 解决ajax上传文件的问题，且页面上无需额外的form表单
$(".update-hot-lend").click(function () {
    if (!validateForm()) {
        return;
    }
    var options = {
        beforeSubmit: function () {
            // showLoader();
        },
        success: function (data) {
            // hideLoader();
            json = $.parseJSON(data);
            if (json.isSuccess == 1) {
                alert("添加成功");
            } else {
                alert(json.msg);
            }
        },
    };
    $("#update-hot-lend-from").ajaxSubmit(options);
});

function validateForm() {

    return true;
}