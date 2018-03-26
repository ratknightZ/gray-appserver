package com.arms.appserver.controller.api;

import com.arms.appserver.manager.ActivityManager;
import com.arms.appserver.vo.UserVO;
import com.arms.common.util.VerifyUtil;
import com.arms.common.vo.JsonVO;
import com.arms.core.encryption.MD5;
import com.arms.service.enums.LoginType;
import com.arms.service.exception.UserException;
import com.arms.service.model.User;
import com.arms.service.model.UserIp;
import com.arms.service.service.InviteCodeService;
import com.arms.service.service.SmsCodeService;
import com.arms.service.service.UserIpService;
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
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginApiController {

    @Resource
    private UserService userService;

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    private ActivityManager activityManager;

    @Resource
    private InviteCodeService inviteCodeService;

    @Resource
    private UserIpService userIpService;

    private static Logger  logger = LoggerFactory.getLogger(LoginApiController.class);

    @ResponseBody
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public String login(@RequestParam String account,
                        @RequestParam(required = false, defaultValue = "") String password,
                        @RequestParam() String loginTypeStr) {

        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(account)) {
            json.setMsg("请填写手机号码");
            json.setIsSuccess(0);
            return json.toString();
        }
        if (StringUtils.isBlank(loginTypeStr)) {
            json.setMsg("请选择登录类型");
            json.setIsSuccess(0);
            return json.toString();
        }

        LoginType loginType = LoginType.getType(loginTypeStr);
        if (loginType == null) {
            json.setMsg("无效的登录类型");
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = null;
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
        }
        if (user == null) {
            json.setMsg("该用户不存在");
            json.setIsSuccess(0);
            json.setRedirectURL("/api/register");
            return json.toString();
        }
        if (StringUtils.isNotBlank(password)) {
            String storedEncryptPassword = user.getPassword();
            if (StringUtils.isBlank(storedEncryptPassword)
                || !MD5.MakeMD5(password).equals(storedEncryptPassword)) {
                json.setMsg("密码错误");
                json.setIsSuccess(0);
                return json.toString();
            }
        }
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setNick(user.getNick());
        if (StringUtils.isNotBlank(user.getCellphone())){
            userVO.setCellphone(user.getCellphone());
        }
        json.setData(userVO);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request,
                           @RequestParam String account,
                           @RequestParam String loginTypeStr,
                           @RequestParam(required = false, defaultValue = "") String password,
                           @RequestParam(required = false, defaultValue = "") String confirmPassword,
                           @RequestParam(required = false, defaultValue = "") String smsCode,
                           @RequestParam(required = false, defaultValue = "") String nick,
                           @RequestParam(required = false, defaultValue = "") String avatar,
                           @RequestParam(required = false, defaultValue = "") String inviteCode) {

        JsonVO json = new JsonVO();

        if (StringUtils.isBlank(loginTypeStr)) {
            json.setMsg("请选择登录类型!!");
            json.setIsSuccess(0);
            return json.toString();
        }

        LoginType loginType = LoginType.getType(loginTypeStr);
        if (loginType == null) {
            json.setMsg("当前登录类型无效!!");
            json.setIsSuccess(0);
            return json.toString();
        }

        User user = new User();
        switch (loginType) {
            case EMAIL:
                if (StringUtils.isBlank(password)) {
                    json.setMsg("请输入密码");
                    json.setIsRedirect(0);
                    return json.toString();
                }
                if (StringUtils.isBlank(confirmPassword)) {
                    json.setMsg("请输入确认密码");
                    json.setIsRedirect(0);
                    return json.toString();
                }
                if (!password.equals(confirmPassword)) {
                    json.setMsg("两次密码输入不一致，请重新输入");
                    json.setIsRedirect(0);
                    return json.toString();
                }
                if (!VerifyUtil.checkEmail(account)) {
                    json.setMsg("邮箱格式不正确");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                if (userService.getByEmail(account) != null) {
                    json.setMsg("该邮箱已注册");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                if (StringUtils.isBlank(smsCode)) {
                    json.setMsg("请输入验证码");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                if (!smsCodeService.check(account, smsCode)) {
                    json.setMsg("验证码错误");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                user.setEmail(account);
                user.setPassword(MD5.MakeMD5(password));
                break;
            case WECHAT:
                if (userService.getByWechat(account) != null) {
                    json.setMsg("该微信号已注册!!");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                user.setWechatOpenId(account);
                break;
            case CELLPHONE:
                if (StringUtils.isBlank(password)) {
                    password = account.substring(5,11);
                    confirmPassword = password;
                }
                if (StringUtils.isBlank(confirmPassword)) {
                    json.setMsg("请输入确认密码");
                    json.setIsRedirect(0);
                    return json.toString();
                }
                if (!password.equals(confirmPassword)) {
                    json.setMsg("两次密码输入不一致，请重新输入");
                    json.setIsRedirect(0);
                    return json.toString();
                }
                if (!VerifyUtil.checkCellphoneNumber(account)) {
                    json.setMsg("手机格式不正确");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                if (userService.getByCellphone(account) != null) {
                    json.setMsg("该手机号已注册");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                if (StringUtils.isBlank(smsCode)) {
                    json.setMsg("请输入验证码");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                if (!smsCodeService.check(account, smsCode)) {
                    json.setMsg("验证码错误");
                    json.setIsSuccess(0);
                    return json.toString();
                }
                user.setCellphone(account);
                user.setPassword(MD5.MakeMD5(password));
                break;
        }
        if (StringUtils.isNotBlank(nick)) {
            user.setNick(nick);
        }

        try {
            userService.add(user);
        } catch (Exception e) {
            json.setMsg("服务异常，注册失败!!");
            json.setIsSuccess(0);
            return json.toString();
        }
        try {
            UserIp userIp = new UserIp();
            userIp.setUserId(user.getId());
            userIp.setIp(request.getRemoteAddr());
            userIpService.add(userIp);
        } catch (Exception e) {
            logger.info("记录注册用户IP失败",e);
        }
        if (StringUtils.isNotBlank(inviteCode)){
            try {
                int superiorUserId = inviteCodeService.getUserId(inviteCode);
                activityManager.bindingSuperior(user.getId(),superiorUserId);
            } catch (UserException e) {
                logger.info("",e);
            }
        }
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setNick(user.getNick());
        if (StringUtils.isNotBlank(avatar)) {
            userVO.setAvatar(user.getAvatar());
        }
        if (StringUtils.isNotBlank(user.getCellphone())){
            userVO.setCellphone(user.getCellphone());
        }
        json.setData(userVO);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping("/api/sms_code_login")
    public String smsCodeLogin(@RequestParam String cellphone,
                               @RequestParam String smsCode){
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(smsCode)){
            json.setIsSuccess(0);
            json.setMsg("验证码不能为空");
            json.setMsgForDebug("验证码不能为空");
            return json.toString();
        }
        if (!VerifyUtil.checkCellphoneNumber(cellphone)){
            json.setIsSuccess(0);
            json.setMsg("手机号格式错误");
            json.setMsgForDebug("非法参数，cellphone: " + cellphone);
            return json.toString();
        }
        User user = userService.getByCellphone(cellphone);
        if (user == null){
            json.setIsSuccess(0);
            json.setMsg("该手机号未注册");
            json.setMsgForDebug("该手机号未注册");
            return json.toString();
        }
        if (!smsCodeService.check(cellphone,smsCode)){
            json.setIsSuccess(0);
            json.setMsg("验证码错误");
            json.setMsgForDebug("验证码错误");
            return json.toString();
        }
        UserVO userVO = new UserVO();
        userVO.setCellphone(cellphone);
        userVO.setAvatar(user.getAvatar());
        userVO.setNick(user.getNick());
        userVO.setUserId(user.getId());
        json.setData(userVO);
        json.setIsSuccess(1);
        return json.toString();
    }
}
