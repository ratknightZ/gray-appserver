
/*******************************************
 *
 * 创建说明：动态设置网页根目录字体
 * 主要位置：所有页面
 *
*********************************************/

//字体设置函数
function fontSizeChange(){
	var html = document.documentElement;
	var hWidth = html.getBoundingClientRect().width;
	var fontSize = hWidth/15;
	var fontSizeVal = 0;
	if( fontSize < 12 ){
		fontSizeVal = 12;
	}else if( fontSize > 12 && fontSize <= 50 ){
		fontSizeVal = fontSize;
	}else{
		fontSizeVal = 50;
	}
	html.style.fontSize = fontSizeVal + "px";
}

//调用字体设置函数
fontSizeChange();

//窗口大小改变时再次调用
window.onresize = function(){
	fontSizeChange();
}