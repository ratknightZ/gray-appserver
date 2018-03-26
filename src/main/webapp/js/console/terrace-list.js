$("button.terrace-delete").click(function () {
    if(confirm("确认删除吗？")){
        var updateUrl = $(this).attr("data-url");
        window.location.href = updateUrl;
    }
});

$("button.update-terrace").click(function () {
    var updateUrl = $(this).attr("data-url");
    window.location.href = updateUrl;
});

$("button.search").click(function () {
    var terraceName = $("#terrace-name").val();
    window.location.href = "/terrace/list_page?terraceName=" + terraceName;
});