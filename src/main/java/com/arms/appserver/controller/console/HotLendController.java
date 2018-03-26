package com.arms.appserver.controller.console;

import com.arms.common.vo.JsonVO;
import com.arms.service.exception.KvsException;
import com.arms.service.model.Kvs;
import com.arms.service.service.KvsService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2018/3/5
 */
@Controller
public class HotLendController {

    @Resource
    private KvsService kvsService;

    @RequestMapping(value = "/hot_lend/update_page",method = RequestMethod.GET)
    public String updatePage(Model model){
        Kvs kvs = kvsService.get("hotLend");
        model.addAttribute("url",kvs.getKvsValue());
        return "page/console/update-hot-lend";
    }

    @ResponseBody
    @RequestMapping(value = "/hot_lend/update",method = RequestMethod.POST)
    public String update(@RequestParam(required = false,defaultValue = "") String url){
        JsonVO json = new JsonVO();
        Kvs kvs = kvsService.get("hotLend");
        kvs.setKvsValue(url);
        try {
            kvsService.update(kvs);
        } catch (KvsException e) {
            json.setIsSuccess(0);
            json.setMsg(e.getMessage());
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
