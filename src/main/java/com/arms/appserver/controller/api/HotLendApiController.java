package com.arms.appserver.controller.api;

import com.arms.common.vo.JsonVO;
import com.arms.service.model.Kvs;
import com.arms.service.service.KvsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2018/2/26
 */
@Controller
public class HotLendApiController {

    @Resource
    private KvsService kvsService;

    @ResponseBody
    @RequestMapping(value = "/api/hot_lend/get_h5",method = RequestMethod.GET)
    public String getH5(){
        JsonVO json = new JsonVO();
        Kvs kvs = kvsService.get("hotLend");
        if (kvs != null){
            json.setData(kvs.getKvsValue());
        }else {
            json.setMsg("暂未开放");
            json.setIsSuccess(0);
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
