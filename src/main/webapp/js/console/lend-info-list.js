$('.search').click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    var source = $('#source').val();
    var terraceName = $("#terrace-name").val();
    if(startDate != "" && endDate != ""){
        var startTime = startDate + " 00:00:00";
        var endTime = endDate + " 23:59:59";
        window.location.href = "/lend_info/list_page?startTime=" + startTime + "&endTime=" + endTime + "&source=" + source + "&terraceName=" + terraceName;
    }else {
        alert("请选择开始日期和结束日期");
    }
})

$('.export-list').click(function () {
    var startDate = $('#start-date').val();
    var endDate = $('#over-date').val();
    var source = $('#source').val();
    var terraceName = $("#terrace-name").val();
    if(startDate != "" && endDate != ""){
        var startTime = startDate + " 00:00:00";
        var endTime = endDate + " 23:59:59";
        window.location.href = "/lend_info/export_list?startTime=" + startTime + "&endTime=" + endTime + "&source=" + source + "&terraceName=" + terraceName;
    }else {
        window.location.href = "/lend_info/export_list";
    }
});