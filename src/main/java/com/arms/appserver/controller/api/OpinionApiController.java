package com.arms.appserver.controller.api;

import com.arms.common.util.VerifyUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.exception.UserException;
import com.arms.service.model.Opinion;
import com.arms.service.service.OpinionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2017/12/12
 */
@Controller
public class OpinionApiController {

    @Resource
    private OpinionService opinionService;

    @ResponseBody
    @RequestMapping(value = "/api/opinion/submit",method = RequestMethod.POST)
    public String submit(@RequestParam int userId,
                         @RequestParam String theme,
                         @RequestParam String detail,
                         @RequestParam String email){
        JsonVO json = new JsonVO();
        if (userId <= 0){
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法请求参数，userId: " + userId);
            return json.toString();
        }
        if (StringUtils.isBlank(theme)){
            json.setIsSuccess(0);
            json.setMsg("反馈主题不能为空");
            json.setMsgForDebug("反馈主题不能为空");
            return json.toString();
        }
        if (StringUtils.isBlank(detail)){
            json.setIsSuccess(0);
            json.setMsg("反馈详情不能为空");
            json.setMsgForDebug("反馈详情不能为空");
            return json.toString();
        }
        if (StringUtils.isBlank(email) || !VerifyUtil.checkEmail(email)){
            json.setIsSuccess(0);
            json.setMsg("email格式非法");
            json.setMsgForDebug("email格式非法");
            return json.toString();
        }
        Opinion opinion = new Opinion();
        opinion.setUserId(userId);
        opinion.setTheme(theme);
        opinion.setDetail(detail);
        opinion.setEmail(email);
        try {
            opinionService.add(opinion);
        } catch (UserException e) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
