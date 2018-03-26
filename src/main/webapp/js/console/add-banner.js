// 解决ajax上传文件的问题，且页面上无需额外的form表单
$(".add-banner").click(function () {
    if (!validateAddBannerForm()) {
        return;
    }
    var pic = $("#pic-input")[0].files[0];
    var fd = new FormData();
    fd.append('file', pic);
    $.ajax({
        url: "http://47.97.115.58:8080/saveFile",
        type: "post",
        // Form数据
        data: fd,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            json = $.parseJSON(data);
            if (json.isSuccess == 1) {
                $("#pic-url-input").val(json.data);
                var options = {
                    beforeSubmit: function () {
                        // alert("beforeSubmit");
                    },
                    success: function (data) {
                        json = $.parseJSON(data);
                        if (json.isSuccess == 1) {
                            // window.location.href = "/spu/list";
                            alert("添加成功");
                        }
                    },
                };
                $("#add-banner-form").ajaxSubmit(options);
            } else {
                alert(json.msg);
            }
        }
    });
});

function validateAddBannerForm() {
    if ($("#banner-name") == "") {
        alert("请输入单品名称!");
        return false;
    }
    if ($("#pic-input").val() == "") {
        alert("请上传单品缩略图！");
        return false;
    }
    if ($("#pic-input")[0].files[0].size > 1048576) {
        alert("图片大小不得超过1M，请重新上传！");
        return false;
    }
    return true;
}