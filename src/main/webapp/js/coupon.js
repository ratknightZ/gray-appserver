$(".open_btn").click(function(){
	$(".open_btn").hide();
	$('.top').hide();
	shake();
	count=setInterval("$('.circle_font').text(parseInt(Math.random()*20))",100);
	setTimeout("clearInterval(count);$('.package').hide();$('.open_package').show();$('.view_btn').show()",2000);
})

function shake() {
	$(".package").css({"animation":"shake 0.7s","animation-iteration-count":"infinite","transform-origin":"center 70%"})
}