$("button.banner-delete").click(function () {
    var updateUrl = $(this).attr("data-url");
    window.location.href = updateUrl;
});

$("button.update-banner").click(function () {
    var updateUrl = $(this).attr("data-url");
    window.location.href = updateUrl;
});