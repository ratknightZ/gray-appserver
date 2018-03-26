

var Global_telphone = "";
var Global_checkreg = /^13[\d]{9}$|^14[0-9]{1}\d{8}$|^15[^4]{1}\d{8}$|^17[0-9]{1}\d{8}$|^18[\d]{9}$/
var Global_debug = false;
var Global_use_download_page = true;
var Global_URL = {
	//iOS的跳转链接，直接跳转到AppStore
	ios:"https://itunes.apple.com/cn/app/id1332558607",
	//Android下面的下载链接
	android:Global_debug?"./com.borrow.anytime.apk":"/apk/com.borrow.anytime.apk",
	//微信下面会跳转过去的界面
	micromessenger_url:"./download.html",
	//下载页面
	download_url:"/wap/download_page",
	//第一次发起发送短线验证码接口
	get_sms_code_url:Global_debug?"api/sms_code.json":"/wap/common/get_sms_code",
	get_sms_code_type:"get",
	//输入图形验证码后再发起发送短线验证码接口
	get_sms_code_with_img_code_url:Global_debug?"api/sms_code0.json":"/wap/common/get_sms_code",
	get_sms_code_with_img_code_type:"get",
	//图形验证码的链接
	img_verify_code_url:Global_debug?"https://seo.zljianjie.com/Promote/Index/verifyCode.html":"/wap/common/get_img_verify_code",
	//注册接口
	register_type:"get",
	register_url:Global_debug?"api/register.json":"/wap/register",
}
var Global_timeout = -1;
function log(msg) {
	if(Global_debug){
		console.info(msg);
	}
}

function go_success_page() {
	if(Global_use_download_page){
		go_page_download();
		return ;
	}
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		go_page_wechat();
	}else{
		if(/iphone|ipad|ipod/.test(ua)) {
			go_page_ios();
		}else if(/android/.test(ua)) {
			go_page_android();
		}else{
			$('#pop_dialog').show();
		}
	}
}
function go_page_ios(){
	window.location.href=Global_URL['ios'];
}
function go_page_android(){
	window.location.href=Global_URL['android'];
}
function go_page_wechat(){
	window.location.href=Global_URL['micromessenger_url'];
}
function go_page_download(){
	window.location.href=Global_URL['download_url'];
}


function show_tip(msg, timeout) {
	if(Global_timeout > 0){
		clearTimeout(Global_timeout);
		log("clearTimeout:" + Global_timeout);
	}
	log("show_tip:" + msg);
	$('#div_tip span').text(msg).show();
	$('#div_tip').show();
	Global_timeout = setTimeout(function(){
		$('#div_tip').hide();
	},timeout||2000)
}

function visitRecord() {
	var source = $('#source').val();
	if(source != ""){
		$.ajax({
			url: "/api/statistics/visit_record",
			type: "post",
			data: {
				source : source
			}
		});
	}
}
function getImgVerifyCodeUrl(){
	var url = Global_URL["img_verify_code_url"];
	var newUrl = url;
	if(url.indexOf("?")>-1){
		newUrl = url + "&_=" + Date.parse(new Date());
	}else{
		newUrl = url + "?_=" + Date.parse(new Date());
	}
	return newUrl;
}

log("document is ready, set onclick function");
//page load finish
$(document).ready(function(){
	visitRecord();

	log("pop_dialog hide, tip hide, clear smscode");
	$('#input_sms_code').val('');
	$('#div_tip span').text("").hide();
	$('#pop_dialog').hide();
	$('#input_telphone').focus();

	log('set click function for pop_dialog, go ios and android');
	//弹出框的点击事件
	$('#div_pop_ios').click(function(){
			log('click pop_dialog for go ios');
			go_page_ios();
	});
	$('#div_pop_android').click(function(){
			log('click pop_dialog for go android');
			go_page_android();
	});



	//发送短信验证码的点击事件
	$("#send_sms_code").click(function(){
		log('click send_sms_code');
		var tel = $('#input_telphone').val();
		log("input_telphone=" + tel);
		if(tel==""){
			show_tip("请输入手机号码");
		}else if(!Global_checkreg.test(tel)){
			show_tip("请输入正确的手机号码");
		}else{
			log("telphone check pass, so ajax send sms");
			$.ajax({
				type:Global_URL["get_sms_code_type"],
				url:Global_URL["get_sms_code_url"],
				async:true,
				dataType:"json",
				cache:false,
				data:{cellphone:tel},
				success:function(resp){
					log("sms_code http success："+resp);
					if(resp.isSuccess==1){
						show_tip("验证码已发送，请留意你的短信");
						$('#input_sms_code').focus();
					}else{
						log("sms_code send fail.");
						$('#div_img_code_box').show();
						//需要图形验证码
						var img_url = getImgVerifyCodeUrl();
						$('#img_code_box_url').attr("src", img_url).show();
						$('#img_code_inp').val('').focus();
						Global_telphone=tel
					}
				},
				error:function(resp){
					log("sms_code error");
					log(resp);
					if(400<=resp.status&&resp.status<500){
						show_tip("服务器错误，请联系管理员");
					}else if(500<=resp.status){
						show_tip("服务器繁忙，请稍后再试");
					}else{
						show_tip("验证码发送失败，请检查网络");
					}
				}
			})
		}
	});

	//重新获取图形验证码
	$('#img_code_box_url').click(function(){
		log("img_code_box_url be click")
		var url = $(this).attr('src');
		var newUrl = getImgVerifyCodeUrl();

		log("img_code_box_url url:"+url+" newurl:"+newUrl)
		$(this).attr('src', newUrl);
	});



	//提交图形验证码 以发送短信验证码的逻辑
	$('#div_img_code_submit').click(function(){
		log("div_img_code_submit be click:"+Global_telphone)

		var tel = Global_telphone;
		var img_code = $('#img_code_inp').val();
		if(img_code==""){
			show_tip("请输入图形验证码");
			return;
		}

		$('#div_img_code_box').hide();

		if(tel==""){
			show_tip("请输入手机号码");
		}else if(!Global_checkreg.test(tel)){
			show_tip("请输入正确的手机号码");
		}else{
			$.ajax({
				type:Global_URL["get_sms_code_with_img_code_type"],
				url:Global_URL["get_sms_code_with_img_code_url"],
				async:true,
				dataType:"json",
				cache:false,
				data:{cellphone:tel,imgVerifyCode:img_code},
				success:function(resp){
					log("sms_code http success："+resp);
					if(resp.isSuccess==1){
						show_tip("验证码已发送，请留意你的短信");
						$('#input_sms_code').focus();
					}else{
						log("sms_code send fail.");
						show_tip("图形验证错误，请重新输入", 1000);
						$('#div_img_code_box').show();
						//需要图形验证码
						var img_url = getImgVerifyCodeUrl();
						$('#img_code_box_url').attr("src", img_url).show();
						$('#img_code_inp').val('').focus();
					}
				},
				error:function(resp){
					log("sms_code error");
					log(resp);
					if(400<=resp.status&&resp.status<500){
						show_tip("服务器错误，请联系管理员");
					}else if(500<=resp.status){
						show_tip("服务器繁忙，请稍后再试");
					}else{
						show_tip("验证码发送失败，请检查网络");
					}
				}
			})
		}
	});


	//短信验证码注册的逻辑
	$('#span_register').click(function(){
		log("click span_register");
		var sms_code = $('#input_sms_code').val();
		var tel = Global_telphone;
		log("Global_telphone:"+Global_telphone);
		if(tel==""){
			log("first show Global_telphone is empty, but input maybe is not empty");
			tel = $('#input_telphone').val();
		}
		if(sms_code==""){
			show_tip("请输入短信验证码");
			return;
		}
		if(tel==""){
			show_tip("请输入手机号码");
		}else if(!Global_checkreg.test(tel)){
			show_tip("请输入正确的手机号码");
		}else{
			var source = $('#source').val();
			var inviteCode = $('#invite-code').val();
			$.ajax({
				type:Global_URL["register_type"],
				url:Global_URL["register_url"],
				async:true,
				dataType:"json",
				cache:false,
				data:{account:tel,smsCode:sms_code,loginTypeStr:"cellphone",source:source,inviteCode:inviteCode},
				success:function(resp){
					log("register http success："+resp);
					if(resp.isSuccess==1){
						go_success_page();
					}else{
						log("register fail.");
						show_tip("注册失败，原因："+resp.msg, 1000);
					}
				},
				error:function(resp){
					log("register http error");
					log(resp);
					if(400<=resp.status&&resp.status<500){
						show_tip("服务器错误，请联系管理员");
					}else if(500<=resp.status){
						show_tip("服务器繁忙，请稍后再试");
					}else{
						show_tip("提交失败，请检查网络");
					}
				}
			})
		}
	});





});
