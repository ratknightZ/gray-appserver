package com.arms.appserver.controller.api;

import com.arms.common.vo.JsonVO;
import com.arms.service.exception.UserException;
import com.arms.service.service.UserSourceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2017/12/29
 */
@Controller
public class UserSourceApiController {

    @Resource
    private UserSourceService userSourceService;

    private static final Logger logger = LoggerFactory.getLogger(UserSourceApiController.class);

    @ResponseBody
    @RequestMapping(value = "/api/user_source/set_source",method = RequestMethod.POST)
    public String setSource(@RequestParam int userId,
                            @RequestParam String source){
        JsonVO json = new JsonVO();
        if (userId <= 0){
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("参数错误，source: " + source);
            return json.toString();
        }
        if (StringUtils.isBlank(source)){
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("source不能为空");
            return json.toString();
        }
        try {
            userSourceService.add(userId,source);
        } catch (UserException e) {
            logger.info("",e);
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsg(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/user_source/get_source",method = RequestMethod.GET)
    public String getSource(@RequestParam int userId){
        JsonVO json = new JsonVO();
        String source = userSourceService.get(userId);
        if (StringUtils.isNotBlank(source)){
            json.setData(source);
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
