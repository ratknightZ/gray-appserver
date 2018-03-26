// 解决ajax上传文件的问题，且页面上无需额外的form表单
$(".update-banner").click(function () {
    if (!validateUpdateBannerForm()) {
        return;
    }
    if($("#pic-input").val() != ""){
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
                                alert("更新成功");
                            }
                        },
                    };
                    $("#update-banner-form").ajaxSubmit(options);
                } else {
                    alert(json.msg);
                }
            }
        });
    }else {
        var options = {
            beforeSubmit: function () {
                // alert("beforeSubmit");
            },
            success: function (data) {
                json = $.parseJSON(data);
                if (json.isSuccess == 1) {
                    // window.location.href = "/spu/list";
                    alert("更新成功");
                }
            },
        };
        $("#update-banner-form").ajaxSubmit(options);
    }
});

function validateUpdateBannerForm() {
    if ($("#banner-name") == "") {
        alert("请输入单品名称!");
        return false;
    }
    if ($("#pic-input").val() != "" && $("#pic-input")[0].files[0].size > 1048576) {
        alert("图片大小不得超过1M，请重新上传！");
        return false;
    }
    return true;
}