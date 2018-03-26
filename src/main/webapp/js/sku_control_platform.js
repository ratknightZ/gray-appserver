// $("#btn-deploy").click(function(){
// 	// $(this).hide();
// 	$("#btn-deploy").hide();
// 	alert($("#select-country").val());
// });
//
// $("#btn-test").click(function(){
// 	// $.get("http://www.baidu.com",function(data,status){
//  //    	// alert("Data: " + data + "\nStatus: " + status);
//  //    	console.log(data);
//  //  	});
// 	 $.ajax({
// 	  url: "http://localhost:8080/getCountry?countryId=3",
// 	  type: "GET",
// 	  success: function(data){
// 	  	console.log($.parseJSON(data).data.name);
// 		  $("input[name='test-url']").val($.parseJSON(data).data.name);
// 	  }
// 	});
// });

$("#btn-cate").click(function () {
	checkCates();
});

$("#btn-tag").click(function () {
	checkTags();
});

$("#btn-show").click(function () {
	checkShowRules();
});

$("#btn-clothes").click(function () {
	checkClothes();
});

$("#btn-shoes").click(function () {
	checkShoes();
});

$("#btn-test").click(function () {
	if (!checkCates() || !checkTags() || !checkShowRules() || !checkClothes() || !checkShoes()){
		return;
	}
	console.log("All params checked!!");
	// 封装参数，发送请求至服务端，将服务端返回的测试链接显示在文本框中
	$.ajax({
	  url: "/sort/test",
	  data: {
		  target_country:$("#select-country").val(),
		  good_num:$("#select-count input").val(),
		  display_num:$("#show-count input").val(),
		  good_ratio:$("#show-ratio-table input[name='selected']").val(),
		  bad_ratio:$("#show-ratio-table input[name='other']").val(),
		  ratio_Home:$("#cate-table input[name='Home']").val(),
		  ratio_Beauty:$("#cate-table input[name='Beauty']").val(),
		  ratio_Shoes:$("#cate-table input[name='Shoes']").val(),
		  ratio_Jewelry:$("#cate-table input[name='Jewelry']").val(),
		  ratio_Bags:$("#cate-table input[name='Bags']").val(),
		  ratio_Clothing:$("#cate-table input[name='Clothing']").val(),
		  ratio_Gadgets:$("#cate-table input[name='Gadgets']").val(),
		  ratio_Watches:0,
		  ratio_clothing_spring_autumn:$("#tag-table input[name='Spring-Autumn-Clothes']").val(),
		  ratio_clothing_summer:$("#tag-table input[name='Summer-Clothes']").val(),
		  ratio_clothing_winter:$("#tag-table input[name='Winter-Clothes']").val(),
		  ratio_shoes_spring_autumn:$("#tag-table input[name='Spring-Autumn-Clothes']").val(),
		  ratio_shoes_summer:$("#tag-table input[name='Summer-Clothes']").val(),
		  ratio_shoes_winter:$("#tag-table input[name='Winter-Clothes']").val(),
		  rest_category_num_map_shoes:$("#set-all-shoes input").val(),
		  rest_category_num_map_clothing:$("#set-all-clothes input").val()
	  },
	  type: "POST",
	  success: function(data){
		  // 返回JSON字符串，而非JSON对象时：$("input[name='test-url']").val($.parseJSON(data).data.name);
		  // 返回JSON对象时：$("input[name='test-url']").val(data.data.name);
		  $("input[name='test-url']").val($.parseJSON(data).isSuccess);
	  }
	});

	//parameters
	// alert($("#select-country").val());
	// alert($("#select-count input").val());
	// alert($("#cate-table input[name='Clothing']").val());
	// alert($("#cate-table input[name='Jewelry']").val());
	// alert($("#cate-table input[name='Home']").val());
	// alert($("#cate-table input[name='Bags']").val());
	// alert($("#cate-table input[name='Shoes']").val());
	// alert($("#cate-table input[name='Beauty']").val());
	// alert($("#cate-table input[name='Gadgets']").val());
	// alert($("#tag-table input[name='Spring-Autumn-Clothes']").val());
	// alert($("#tag-table input[name='Summer-Clothes']").val());
	// alert($("#tag-table input[name='Winter-Clothes']").val());
	// alert($("#week-sale-value-new input").val());
	// alert($("#week-sale-value-new input").val());
	// alert($("#discount-value-new input").val());
	// alert($("#listing-num-value-new input").val());
	// alert($("#click-rate-value-new input").val());
	// alert($("#price-value-new input").val());
	// alert($("#show-count input").val());
	// alert($("#show-ratio-table input[name='selected']").val());
	// alert($("#show-ratio-table input[name='other']").val());
	// alert($("#set-all-clothes input").val());
	// alert($("#set-all-shoes input").val());

});

$("#btn-deploy").click(function () {
	
});

function checkCates() {
	var checkInput = true;
	$("#cate-table input").each(function () {
		// 1.校验输入，若输入不为(0, 100)的数字则标红输入框，并对正常输入框的边框正常化
		if(isNaN($(this).val()) || $(this).val() <= 0 || $(this).val() >= 100){
			$(this).css("border","1px solid red");
			checkInput = false;
			return;
		} else {
			$(this).css("border","1px solid #bbb");
		}
	});
	if (!checkInput){
		return false;
	}

	//2.校验输入框数值总和，若总和不为100，则按比例重新赋值，取小数点后两位，不保证其总和为100
	var sum = 0;
	$("#cate-table input").each(function () {
		sum += Number($(this).val());
	})
	// alert(sum);
	if (sum != 100){
		$("#cate-table input").each(function () {
			$(this).val((100 * $(this).val() / sum).toFixed(2));
		})
	}
	return true;
}

function checkTags() {
	var checkInput = true;
	$("#tag-table input").each(function () {
		// 1.校验输入，若输入不为(0, 100)的数字则标红输入框，并对正常输入框的边框正常化
		if(isNaN($(this).val()) || $(this).val() <= 0 || $(this).val() >= 100){
			$(this).css("border","1px solid red");
			checkInput = false;
			return;
		} else {
			$(this).css("border","1px solid #bbb");
		}
	});
	if (!checkInput){
		return false;
	}

	//2.校验输入框数值总和，若总和不为100，则按比例重新赋值，取小数点后两位，不保证其总和为100
	var sum = 0;
	$("#tag-table input").each(function () {
		sum += Number($(this).val());
	})
	// alert(sum);
	if (sum != 100){
		$("#tag-table input").each(function () {
			$(this).val((100 * $(this).val() / sum).toFixed(2));
		})
	}
	return true;
}

function checkShowRules() {
	var checkInput = true;
	$("#show-ratio-table input").each(function () {
		// 1.校验输入，若输入不为(0, 100)的数字则标红输入框，并对正常输入框的边框正常化
		if(isNaN($(this).val()) || $(this).val() <= 0 || $(this).val() >= 100){
			$(this).css("border","1px solid red");
			checkInput = false;
			return;
		} else {
			$(this).css("border","1px solid #bbb");
		}
	});
	// js中的return只会跳出当前function，而不会结束整个事件
	if (!checkInput){
		return false;
	}

	//2.校验输入框数值总和，若总和不为100，则按比例重新赋值，取小数点后两位，不保证其总和为100
	var sum = 0;
	$("#show-ratio-table input").each(function () {
		sum += Number($(this).val());
	})
	// alert(sum);
	if (sum != 100){
		$("#show-ratio-table input").each(function () {
			$(this).val((100 * $(this).val() / sum).toFixed(2));
		})
	}
	return true;
}

function checkClothes() {
	//1. 对输入进行校验，校验是否为正数，若是则进行四舍五入
	var checkInput = true;
	$("#clothes-detail-table input").each(function () {
		if (isNaN($(this).val()) || $(this).val() < 0){
			$(this).css("border","1px solid red");
			checkInput = false;
			return;
		} else {
			$(this).css("border","1px solid #bbb");
			var roundValue = Math.round($(this).val());
			$(this).val(roundValue);
		}
	})
	if (!checkInput){
		return false;
	}


	//2.以春夏秋冬的数值为基准，计算出的总和设置于全部输入框中，最后校验各输入框的前置数值是否超过现有数值
	var setSpringAutumnClothes = Math.round($("#set-spring-autumn-clothes input").val());
	var setWinterClothes = Math.round($("#set-winter-clothes input").val());
	var setSummerClothes = Math.round($("#set-summer-clothes input").val());
	var setAllClothes = setSpringAutumnClothes + setWinterClothes + setSummerClothes;
	$("#set-all-clothes input").val(setAllClothes);
	if (Number(setAllClothes) > Number($("#current-all-clothes").text())){
		$("#set-all-clothes input").css("border","1px solid red");
	} else{
		$("#set-all-clothes input").css("border","1px solid #bbb");
	}
	if (Number(setSpringAutumnClothes) > Number($("#current-spring-autumn-clothes").text())){
		$("#set-spring-autumn-clothes input").css("border","1px solid red");
	} else{
		$("#set-spring-autumn-clothes input").css("border","1px solid #bbb");
	}
	if (Number(setWinterClothes) > Number($("#current-winter-clothes").text())){
		$("#set-winter-clothes input").css("border","1px solid red");
	} else{
		$("#set-winter-clothes input").css("border","1px solid #bbb");
	}
	if (Number(setSummerClothes) > Number($("#current-summer-clothes").text())){
		$("#set-summer-clothes input").css("border","1px solid red");
	} else{
		$("#set-summer-clothes input").css("border","1px solid #bbb");
	}
	return true;
}

function checkShoes() {
	//1. 对输入进行校验，校验是否为正数，若是则进行四舍五入
	var checkInput = true;
	$("#shoes-detail-table input").each(function () {
		if (isNaN($(this).val()) || $(this).val() < 0){
			$(this).css("border","1px solid red");
			checkInput = false;
			return;
		} else {
			$(this).css("border","1px solid #bbb");
			var roundValue = Math.round($(this).val());
			$(this).val(roundValue);
		}
	})
	if (!checkInput){
		return false;
	}


	//2.以春夏秋冬的数值为基准，计算出的总和设置于全部输入框中，最后校验各输入框的前置数值是否超过现有数值
	var setSpringAutumnShoes = Math.round($("#set-spring-autumn-shoes input").val());
	var setWinterShoes = Math.round($("#set-winter-shoes input").val());
	var setSummerShoes = Math.round($("#set-summer-shoes input").val());
	var setAllShoes = setSpringAutumnShoes + setWinterShoes + setSummerShoes;
	$("#set-all-shoes input").val(setAllShoes);
	if (Number(setAllShoes) > Number($("#current-all-shoes").text())){
		$("#set-all-shoes input").css("border","1px solid red");
	} else{
		$("#set-all-shoes input").css("border","1px solid #bbb");
	}
	if (Number(setSpringAutumnShoes) > Number($("#current-spring-autumn-shoes").text())){
		$("#set-spring-autumn-shoes input").css("border","1px solid red");
	} else{
		$("#set-spring-autumn-shoes input").css("border","1px solid #bbb");
	}
	if (Number(setWinterShoes) > Number($("#current-winter-shoes").text())){
		$("#set-winter-shoes input").css("border","1px solid red");
	} else{
		$("#set-winter-shoes input").css("border","1px solid #bbb");
	}
	if (Number(setSummerShoes) > Number($("#current-summer-shoes").text())){
		$("#set-summer-shoes input").css("border","1px solid red");
	} else{
		$("#set-summer-shoes input").css("border","1px solid #bbb");
	}
	return true;
}