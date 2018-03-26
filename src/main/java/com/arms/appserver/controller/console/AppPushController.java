package com.arms.appserver.controller.console;

import com.arms.appserver.controller.util.AppPushUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.annotation.Sniff;
import com.arms.service.model.Terrace;
import com.arms.service.service.GetuiService;
import com.arms.service.service.TerraceService;
import com.gexin.rp.sdk.base.impl.Target;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author liuchen
 * @since 2018/3/20
 */
@Controller
public class AppPushController {

    @Resource
    private GetuiService getuiService;

    @Resource
    private TerraceService terraceService;

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/app_push/push_page",method = RequestMethod.GET)
    public String pushPage(){
        return "page/console/app-push";
    }

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/app_push/condition_push",method = RequestMethod.POST)
    public String conditionPush(@RequestParam int terraceId,
                                @RequestParam String content,
                                @RequestParam(required = false,defaultValue = "0") int device,
                                @RequestParam(required = false,defaultValue = "0") int isApplyFor,
                                @RequestParam(required = false) Timestamp startTime,
                                @RequestParam(required = false) Timestamp endTime){
        JsonVO json = new JsonVO();
        Terrace terrace = terraceService.get(terraceId);
        if (terrace == null){
            json.setIsSuccess(0);
            json.setMsg("平台不存在");
            return json.toString();
        }
        List<Target> targetList = getuiService.getTargetList(device,isApplyFor,startTime,endTime);
        AppPushUtil.pushToList(content,"anyloan://TAHomePage/TerraceDetailsViewController?terraceId=" + terraceId,targetList);
        json.setIsSuccess(1);
        return json.toString();
    }
}
