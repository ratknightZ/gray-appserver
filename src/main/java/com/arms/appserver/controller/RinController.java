package com.arms.appserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arms.common.vo.JsonVO;

/**
 * Created by zhangchaojie on 2016/9/30.
 */
@Controller
public class RinController {

    @ResponseBody
    @RequestMapping(value = "/sort/test", method = { RequestMethod.GET, RequestMethod.POST })
    public String getCountry(@RequestParam String target_country, @RequestParam int good_num,
                             @RequestParam int display_num, @RequestParam double good_ratio,
                             @RequestParam double bad_ratio, @RequestParam double ratio_Home,
                             @RequestParam double ratio_Beauty, @RequestParam double ratio_Shoes,
                             @RequestParam double ratio_Jewelry, @RequestParam double ratio_Bags,
                             @RequestParam double ratio_Clothing,
                             @RequestParam double ratio_Gadgets,
                             @RequestParam double ratio_Watches,
                             @RequestParam double ratio_clothing_spring_autumn,
                             @RequestParam double ratio_clothing_summer,
                             @RequestParam double ratio_clothing_winter,
                             @RequestParam double ratio_shoes_spring_autumn,
                             @RequestParam double ratio_shoes_summer,
                             @RequestParam double ratio_shoes_winter,
                             @RequestParam double rest_category_num_map_shoes,
                             @RequestParam double rest_category_num_map_clothing) {
        JsonVO json = new JsonVO();
        json.setData("success");
        json.setIsSuccess(1);
        return json.toString();
    }

    @RequestMapping(value = "/test_js_and_css", method = RequestMethod.GET)
    public String getTempPage() {
        return "page/rin/sku_control_platform";
    }

    @RequestMapping(value = "/coupon", method = RequestMethod.GET)
    public String getCouponPage() {
        return "page/rin/coupon";
    }

}
