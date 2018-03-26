$('.search').click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    var source = $('#source').val();
    if(startDate != "" && endDate != ""){
        var startTime = startDate + " 00:00:00";
        var endTime = endDate + " 00:00:00";
        window.location.href = "/statistics/list_page?startTime=" + startTime + "&endTime=" + endTime +"&source=" + source;
    }else {
        alert("请选择开始日期和结束日期");
    }
})

$('.export-list').click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    var source = $('#source').val();
    if(startDate != "" && endDate != ""){
        var startTime = startDate + " 00:00:00";
        var endTime = endDate + " 00:00:00";
        window.location.href = "/statistics/export_list?startTime=" + startTime + "&endTime=" + endTime + "&source=" + source;
    }else {
        window.location.href = "/statistics/export_list";
    }
});