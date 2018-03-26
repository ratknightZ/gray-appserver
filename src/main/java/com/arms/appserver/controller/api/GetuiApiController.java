package com.arms.appserver.controller.api;

import com.arms.common.vo.JsonVO;
import com.arms.service.enums.Device;
import com.arms.service.exception.GetuiException;
import com.arms.service.model.GetuiUser;
import com.arms.service.model.User;
import com.arms.service.service.GetuiService;
import com.arms.service.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author jinxuan
 * @since 2017/12/5
 */
@Controller
public class GetuiApiController {

    @Resource
    private GetuiService getuiService;

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/api/user/set_getui_user", method = RequestMethod.POST)
    public String addGetuiUser(@RequestParam int userId, @RequestParam String cid, @RequestParam int device) {
        JsonVO json = new JsonVO();
        if (userId <= 0 || StringUtils.isBlank(cid)) {
            json.setMsgForDebug("非法的请求参数：[userId: " + userId + "], [cid: " + cid + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        if (Device.getDevice(device) == null){
            json.setMsgForDebug("非法的请求参数,device:" + device);
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = userService.get(userId);
        if (user == null) {
            json.setMsgForDebug("无效的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        GetuiUser getuiUser = getuiService.getByUserId(userId);
        // 如果用户已有CID则进行更新，若无CID则添加
        try {
            if (getuiUser != null) {
                getuiUser.setCid(cid);
                getuiUser.setDevice(device);
                getuiService.updateGetuiUser(getuiUser);
            } else {
                getuiUser = new GetuiUser();
                getuiUser.setUserId(userId);
                getuiUser.setCid(cid);
                getuiUser.setDevice(device);
                getuiService.addGetuiUser(getuiUser);
            }
        } catch (GetuiException e) {
            json.setMsgForDebug(e.getMessage());
            json.setIsSuccess(0);
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/delete_getui_user", method = RequestMethod.POST)
    public String deleteGetuiUser(@RequestParam int userId) {
        JsonVO json = new JsonVO();
        if (userId <= 0) {
            json.setMsgForDebug("非法的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = userService.get(userId);
        if (user == null) {
            json.setMsgForDebug("无效的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        GetuiUser getuiUser = getuiService.getByUserId(userId);
        if (getuiUser != null) {
            try {
                getuiService.delete(userId);
            } catch (GetuiException e) {
                json.setMsgForDebug(e.getMessage());
                json.setIsSuccess(0);
                return json.toString();
            }
        }
        json.setIsSuccess(1);
        return json.toString();
    }

}
