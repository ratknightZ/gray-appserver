$("button.push").click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    var device = $("#device").val();
    var isApplyFor = $("#is-apply-for").val();
    var terraceId = $("#terrace-id").val();
    var content = $("#content").val();
    var startTime;
    var endTime;
    if (terraceId  == ""){
        alert("请输入平台ID");
    }
    if(startDate != "") {
        startTime = startDate + " 00:00:00";
    }
    if (endDate != ""){
        endTime = endDate + " 23:59:59";
    }
    $.post("/app_push/condition_push",{device : device,isApplyFor : isApplyFor,startTime : startTime, endTime : endTime, content : content, terraceId :terraceId},function (data) {
        json = $.parseJSON(data);
        if (json.isSuccess == 1){
            alert("推送成功");
        }else {
            alert(json.msg);
        }
    })
})