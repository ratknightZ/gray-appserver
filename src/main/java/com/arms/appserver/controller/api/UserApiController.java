package com.arms.appserver.controller.api;

import com.arms.appserver.vo.UserVO;
import com.arms.common.vo.JsonVO;
import com.arms.core.encryption.MD5;
import com.arms.service.enums.LoginType;
import com.arms.service.exception.UserException;
import com.arms.service.model.User;
import com.arms.service.service.SmsCodeService;
import com.arms.service.service.UserService;
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
 * @author jinxuan
 * @since 2017/11/16
 */
@Controller
public class UserApiController {

    @Resource
    private UserService userService;

    @Resource
    private SmsCodeService smsCodeService;

    private static Logger logger = LoggerFactory.getLogger(UserApiController.class);

//    @ResponseBody
//    @RequestMapping(value = "/api/user/set_password", method = RequestMethod.POST)
//    public String setUserPassword(@RequestParam int userId, @RequestParam String password,
//                                  @RequestParam String confirmPassword) {
//        JsonVO json = new JsonVO();
//        if (StringUtils.isBlank(password)) {
//            json.setMsg("请输入密码");
//            json.setIsRedirect(0);
//            return json.toString();
//        }
//        if (StringUtils.isBlank(confirmPassword)) {
//            json.setMsg("请输入确认密码");
//            json.setIsRedirect(0);
//            return json.toString();
//        }
//        if (!password.equals(confirmPassword)) {
//            json.setMsg("两次密码输入不一致，请重新输入");
//            json.setIsRedirect(0);
//            return json.toString();
//        }
//        User user = userService.get(userId);
//        if (user == null) {
//            json.setMsg("用户不存在");
//            json.setIsSuccess(0);
//            return json.toString();
//        }
//        // 验证码或三方校验通过后设置密码
//        user.setPassword(MD5.MakeMD5(password));
//        try {
//            userService.update(user);
//        } catch (UserException e) {
//            logger.info("", e);
//        }
//        json.setIsSuccess(1);
//        return json.toString();
//    }

    @ResponseBody
    @RequestMapping(value = "/api/user/change_password",method = RequestMethod.POST)
    public String changePassword(@RequestParam String cellphone,
                                 @RequestParam String smsCode,
                                 @RequestParam String password){
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(smsCode)){
            json.setIsSuccess(0);
            json.setMsg("验证码不能为空");
            return json.toString();
        }
        if (StringUtils.isBlank(password) || password.length() < 6 || password.length() > 16){
            json.setIsSuccess(0);
            json.setMsg("密码格式错误");
            json.setMsgForDebug("非法的请求参数，password: " + password);
            return json.toString();
        }
        if (!smsCodeService.check(cellphone,smsCode)){
            json.setIsSuccess(0);
            json.setMsg("验证码错误");
            json.setMsgForDebug("验证码错误");
            return json.toString();
        }
        User user = userService.getByCellphone(cellphone);
        if (user == null){
            json.setIsSuccess(0);
            json.setMsg("用户不存在");
            json.setMsgForDebug("用户不存在");
            return json.toString();
        }
        user.setPassword(MD5.MakeMD5(password));
        try {
            userService.update(user);
        } catch (UserException e) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/get", method = RequestMethod.GET)
    public String userInfo(@RequestParam int userId) {
        JsonVO json = new JsonVO();
        if (userId <= 0) {
            json.setMsg("请求错误");
            json.setMsgForDebug("非法的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = userService.get(userId);
        if (user == null) {
            json.setMsg("用户不存在");
            json.setMsgForDebug("无效的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setNick(user.getNick());
        if (StringUtils.isNotBlank(user.getAvatar())) {
            userVO.setAvatar(user.getAvatar());
        }
        if (StringUtils.isNotBlank(user.getCellphone())) {
            userVO.setCellphone(user.getCellphone());
        }
        json.setData(userVO);
        json.setIsSuccess(1);
        return json.toString();
    }

    /*@ResponseBody
    @RequestMapping(value = "/api/user/profile_page", method = RequestMethod.GET)
    public String userProfilePage(@RequestParam int userId) {
        JsonVO json = new JsonVO();
        if (userId <= 0) {
            json.setMsg("请求错误");
            json.setMsgForDebug("非法的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = userService.get(userId);
        if (user == null) {
            json.setMsg("用户不存在");
            json.setMsgForDebug("无效的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        //设置用户基本信息
        UserProfilePageVO userProfilePageVO = new UserProfilePageVO();
        userProfilePageVO.setUserId(user.getId());
        userProfilePageVO.setNick(user.getNick());
        userProfilePageVO.setAvatar(user.getAvatar());
        //设置用户游戏信息
        userProfilePageVO.setPlayedTimes(0);
        json.setIsSuccess(1);
        return json.toString();
    }*/

    @ResponseBody
    @RequestMapping(value = "/api/user/get_by_account", method = RequestMethod.GET)
    public String getUserByAccount(@RequestParam String account, @RequestParam String loginTypeStr) {
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(loginTypeStr)) {
            json.setMsgForDebug("loginTypeStr不能为空!!");
            json.setIsSuccess(0);
            return json.toString();
        }

        LoginType loginType = LoginType.getType(loginTypeStr);
        if (loginType == null) {
            json.setMsg("无效的登录类型: " + loginTypeStr);
            json.setIsSuccess(0);
            return json.toString();
        }

        User user;
        switch (loginType) {
            case EMAIL:
                user = userService.getByEmail(account);
                break;
            case WECHAT:
                user = userService.getByWechat(account);
                break;
            case CELLPHONE:
                user = userService.getByCellphone(account);
                break;
            default:
                user = null;
        }
        if (user == null) {
            json.setMsgForDebug("无效的请求参数:[account: " + account + "], [loginTypeStr: "
                    + loginTypeStr + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        if (StringUtils.isNotBlank(user.getNick())) {
            userVO.setNick(user.getNick());
        }
        if (StringUtils.isNotBlank(user.getAvatar())) {
            userVO.setAvatar(user.getAvatar());
        }
        json.setData(userVO);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/user/update", method = RequestMethod.POST)
    public String updateUserInfo(@RequestParam int userId,
                                 @RequestParam(required = false, defaultValue = "") String nick,
                                 @RequestParam(required = false, defaultValue = "") String avatar) {
        JsonVO json = new JsonVO();
        if (userId <= 0) {
            json.setMsg("更新用户信息失败");
            json.setMsgForDebug("非法的请求参数：[userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = userService.get(userId);
        if (user == null) {
            json.setMsg("用户不存在");
            json.setMsgForDebug("无效的请求参数：[userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        if (StringUtils.isNotBlank(nick)) {
            user.setNick(nick);
        }
        if (StringUtils.isNotBlank(avatar)) {
            user.setAvatar(avatar);
        }
        try {
            userService.update(user);
        } catch (UserException e) {
            json.setMsg("更新用户信息失败");
            json.setMsgForDebug(e.getMessage());
            json.setIsSuccess(0);
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

}
