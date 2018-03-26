package com.arms.appserver.controller.api;

import com.arms.appserver.vo.LendInfoVO;
import com.arms.common.vo.JsonVO;
import com.arms.service.exception.LendInfoException;
import com.arms.service.model.LendInfo;
import com.arms.service.service.LendInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2017/12/7
 */
@Controller
public class LendInfoApiController {

    @Resource
    private LendInfoService lendInfoService;

    @ResponseBody
    @RequestMapping(value = "/api/lend_info/add",method = RequestMethod.POST)
    public String add(LendInfo lendInfo){
        JsonVO json = new JsonVO();
        if (lendInfo.getUserId() <= 0){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，userId: " + lendInfo.getUserId());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (lendInfo.getTerraceId() <= 0){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，terraceId: " + lendInfo.getTerraceId());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (lendInfo.getTimeLimit() <= 0){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，timeLimit: " + lendInfo.getTimeLimit());
            json.setIsSuccess(0);
            return json.toString();
        }
        try {
            lendInfoService.add(lendInfo);
        } catch (LendInfoException e) {
            json.setMsg("请求错误");
            json.setMsgForDebug(e.getMessage());
            json.setIsSuccess(0);
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

//    @ResponseBody
//    @RequestMapping(value = "/api/lend_info/update",method = RequestMethod.POST)
//    public String update(LendInfo lendInfo){
//        JsonVO json = new JsonVO();
//        if (lendInfo.getUserId() <= 0){
//            json.setMsg("请求错误");
//            json.setMsgForDebug("非法参数，userId: " + lendInfo.getUserId());
//            json.setIsSuccess(0);
//            return json.toString();
//        }
//        lendInfoService.update(lendInfo);
//        json.setIsSuccess(1);
//        return json.toString();
//    }

    @ResponseBody
    @RequestMapping(value = "/api/lend_info/get_user_lend_info",method = RequestMethod.GET)
    public String getUserLendInfo(@RequestParam int userId){
        JsonVO json = new JsonVO();
        if (userId <= 0){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，userId: " + userId);
            json.setIsSuccess(0);
            return json.toString();
        }
        LendInfo lendInfo = lendInfoService.get(userId);
        if (lendInfo != null){
            LendInfoVO lendInfoVO = new LendInfoVO();
            lendInfoVO.setUserId(lendInfo.getUserId());
            lendInfoVO.setName(lendInfo.getName());
            lendInfoVO.setIdCard(lendInfo.getIdCard());
            lendInfoVO.setApplyForAmount(lendInfo.getApplyForAmount());
            lendInfoVO.setLendPurpose(lendInfo.getLendPurpose());
            lendInfoVO.setTimeLimit(lendInfo.getTimeLimit());
            lendInfoVO.setProfession(lendInfo.getProfession());
            json.setData(lendInfoVO);
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
