
// $.ajax({
//     type: "get",
//     url: "",
//     async: true,
//     dataType: "json",
//     cache: false,
//     data:{},     
//     success: function (res) {
//            console.log(res)
//     },
//     error: function (res) {
//         if (400 <= res.status && res.status < 500) {
//             alert("服务器错误，请联系管理员");
//         } else if (500 <= res.status) {
//             alert("服务器繁忙，请稍后再试");
//         } else {
//             alert("提交失败，请检查网络");
//         }
//     }
// })