$('.search').click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    if(startDate != "" && endDate != ""){
        var startTime = startDate + " 00:00:00";
        var endTime = endDate + " 23:59:59";
        window.location.href = "/user/list_page?startTime=" + startTime + "&endTime=" + endTime;
    }else {
        alert("请选择开始日期和结束日期");
    }
})

$('.export-list').click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    if(startDate != "" && endDate != ""){
        var startTime = startDate + " 00:00:00";
        var endTime = endDate + " 23:59:59";
        window.location.href = "/user/export_list?startTime=" + startTime + "&endTime=" + endTime;
    }else {
        window.location.href = "/user/export_list";
    }
});