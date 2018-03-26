// 解决ajax上传文件的问题，且页面上无需额外的form表单
$(".add-terrace").click(function () {
    if (!validateAddBannerForm()) {
        return;
    }
    var pic = $("#logo-input")[0].files[0];
    var fdp = new FormData();
    fdp.append('file', pic);
    $.ajax({
        url: "http://47.97.115.58:8080/saveFile",
        type: "post",
        // Form数据
        data: fdp,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            json = $.parseJSON(data);
            if (json.isSuccess == 1) {
                $("#logo-url-input").val(json.data);
                // var options = {
                //     beforeSubmit: function () {
                //         // alert("beforeSubmit");
                //     },
                //     success: function (data) {
                //         json = $.parseJSON(data);
                //         if (json.isSuccess == 1) {
                //             // window.location.href = "/spu/list";
                //              alert("添加成功");
                //         }
                //     },
                // };
                // $("#add-terrace-form").ajaxSubmit(options);
                var terraceName = $("#terrace-name").val();
                var rate = $("#rate").val();
                var lendType = $("#lend-type").val();
                var quickestTime = $("#quickest-time").val();
                var applyForCondition = $("#apply-for-condition").val();
                var needInformation = $("#need-information").val();
                var repaymentWay = $("#repayment-way").val();
                var haveCreditCard = $("#have-credit-card").val();
                var minRepaymentTimeLimit = $("#min-repayment-time-limit").val();
                var maxRepaymentTimeLimit =$("#max-repayment-time-limit").val();
                var choosableRepaymentTimeLimit = $("#choosable-repayment-time-limit").val();
                var maxAmount = $("#max-amount").val();
                var minAmount = $("#min-amount").val();
                var status = $("#status").val();
                var url = $("#url").val();
                var successRate = $("#success-rate").val();
                var isHot = $("#is-hot").val();
                var profession = $("#profession").val();
                var professionStr = "";
                for (var i = 0; i < profession.length; i++) {
                    if (professionStr == "") {
                        professionStr += profession[i];
                    } else {
                        professionStr += "," + profession[i];
                    }
                }
                var credit = $("#credit").val();
                var creditStr = "";
                for (var i = 0; i < credit.length; i++) {
                    if (creditStr == "") {
                        creditStr += credit[i];
                    } else {
                        creditStr += "," + credit[i];
                    }
                }
                var lendPurpose = $("#lend-purpose").val();
                var lendPurposeStr = "";
                for (var i = 0; i < lendPurpose.length; i++) {
                    if (lendPurposeStr == "") {
                        lendPurposeStr += lendPurpose[i];
                    } else {
                        lendPurposeStr += "," + lendPurpose[i];
                    }
                }
                var terraceType = $("#terrace-type").val();
                var terraceTypeStr = "";
                for (var i = 0; i < terraceType.length; i++) {
                    if (terraceTypeStr == "") {
                        terraceTypeStr += terraceType[i];
                    } else {
                        terraceTypeStr += "," + terraceType[i];
                    }
                }
                var showChannel = $("#show-channel").val();
                var showChannelStr = "";
                for (var i = 0; i < showChannel.length; i++) {
                    if (showChannelStr == "") {
                        showChannelStr += showChannel[i];
                    } else {
                        showChannelStr += "," + showChannel[i];
                    }
                }
                var logo = $("#logo-url-input").val();
                var messageTitle = $("#message-title").val();
                var unitOfQuickestTime = $("#unit-of-quickest-time").val();
                var unitOfRepaymentTime = $("#unit-of-repayment-time").val();
                var unitOfRate = $("#unit-of-rate").val();
                var sortCode = $("#sort-code").val();
                var fd = new FormData();
                fd.append('name', terraceName);
                fd.append('rate', rate);
                fd.append('lendType', lendType);
                fd.append('quickestTime', quickestTime);
                fd.append('applyForCondition', applyForCondition);
                fd.append('needInformation', needInformation);
                fd.append('repaymentWay', repaymentWay);
                fd.append('haveCreditCard', haveCreditCard);
                fd.append('minRepaymentTimeLimit', minRepaymentTimeLimit);
                fd.append('maxRepaymentTimeLimit', maxRepaymentTimeLimit);
                fd.append('choosableRepaymentTimeLimit', choosableRepaymentTimeLimit);
                fd.append('maxAmount', maxAmount);
                fd.append('minAmount', minAmount);
                fd.append('status', status);
                fd.append('url', url);
                fd.append('successRate', successRate);
                fd.append('isHot', isHot);
                fd.append('profession', professionStr);
                fd.append('credit', creditStr);
                fd.append('lendPurpose', lendPurposeStr);
                fd.append('terraceTypes',terraceTypeStr);
                fd.append('showChannels',showChannelStr);
                fd.append('messageTitle',messageTitle);
                fd.append('logo',logo);
                fd.append("unitOfQuickestTime",unitOfQuickestTime);
                fd.append("unitOfRepaymentTime",unitOfRepaymentTime);
                fd.append("unitOfRate",unitOfRate);
                fd.append("sortCode",sortCode);
                $.ajax({
                    url: "/terrace/add",
                    type: "post",
                    // Form数据
                    data: fd,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        json = $.parseJSON(data);
                                if (json.isSuccess == 1) {
                                    // window.location.href = "/spu/list";
                                     alert("添加成功");
                                }
                    }
                })
            } else {
                alert(json.msg);
            }
        }
    });
});

function validateAddBannerForm() {
    if ($("#terrace-name").val() == "") {
        alert("请输入平台名称!");
        return false;
    }
    if($("#profession").val() == ""){
        alert("请输入职业身份!")
        return false;
    }
    if($("#credit").val() == ""){
        alert("请输入信用情况!")
        return false;
    }
    if($("#lend-purpose").val() == ""){
        alert("请输入借款用途!")
        return false;
    }
    if ($("#logo-input").val() == "") {
        alert("请上传单品缩略图！");
        return false;
    }
    if ($("#logo-input")[0].files[0].size > 1048576) {
        alert("图片大小不得超过1M，请重新上传！");
        return false;
    }
    return true;
}