package com.arms.appserver.controller.console;

import com.arms.common.util.VerifyUtil;
import com.arms.common.vo.JsonVO;
import com.arms.core.context.SystemContext;
import com.arms.core.encryption.MD5;
import com.arms.service.enums.LoginType;
import com.arms.service.model.User;
import com.arms.service.service.SmsCodeService;
import com.arms.service.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

/**
 * Created by zhangchaojie on 2017/6/9.
 */
@Controller
public class WebLoginController {

    @Resource
    private UserService userService;

    @Resource
    private SmsCodeService smsCodeService;

    private static String  COOKIE_LOGIN_TICKET            = "clt";

    private static String  DEFAULT_USER_ROLE_AUTH         = "ROLE_USER";

    private static String  DEFAULT_CONSOLE_USER_ROLE_AUTH = "ROLE_PRODUCT";

    @RequestMapping(value = "/login_page", method = RequestMethod.GET)
    public String webLoginPage(Model model,
                               @RequestParam(required = false, defaultValue = "/welcome") String redirectUrl,
                               @RequestParam(required = false, defaultValue = "ROLE_PRODUCT") String roleAuth) {
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("roleAuth", roleAuth);
        return "page/console/web-login";
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String webLogin(@RequestParam String account,
                           @RequestParam(required = false, defaultValue = "") String password,
                           @RequestParam(required = false, defaultValue = "email") String loginTypeStr,
                           @RequestParam(required = false, defaultValue = "") String smsCode,
                           @RequestParam String redirectUrl,
                           @RequestParam(required = false, defaultValue = "ROLE_USER") String roleAuth) {
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(account)) {
            json.setMsg("请填写账户");
            json.setIsSuccess(0);
            return json.toString();
        }
        if (StringUtils.isBlank(loginTypeStr)) {
            json.setMsg("请选择登录类型");
            json.setIsSuccess(0);
            return json.toString();
        }

        LoginType loginType = LoginType.getType(loginTypeStr);
        if (loginType == null || loginType != LoginType.EMAIL) {
            json.setMsg("无效的登录类型，目前后台只支持邮箱注册和登录");
            json.setIsSuccess(0);
            return json.toString();
        }
        User user = null;
        switch (loginType) {
            case EMAIL:
                user = userService.getByEmail(account);
                break;
        //            case WECHAT:
        //                user = userService.getByWechat(account);
        //                break;
        //            case CELLPHONE:
        //                user = userService.getByCellphone(account);
        //                break;
        }
        if (user == null) {
            json.setMsg("该用户不存在");
            json.setIsSuccess(0);
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
        //        if (StringUtils.isNotBlank(smsCode)) {
        //            if (!smsCodeService.check(account, smsCode)) {
        //                json.setMsg("验证码错误");
        //                json.setIsSuccess(0);
        //                return json.toString();
        //            }
        //        }
        if (user.getRoleAuth().indexOf(roleAuth) == -1
            && user.getRoleAuth().indexOf("ROLE_ADMIN") == -1) {
            json.setMsg("您登录的用户没有访问该页面的权限!!");
            json.setIsSuccess(0);
            return json.toString();
        }
        Cookie loginCookie = getLoginCookie();
        if (loginCookie == null) {
            loginCookie = new Cookie(COOKIE_LOGIN_TICKET, user.getId() + ":" + user.getRoleAuth());
        } else {
            loginCookie.setValue(user.getId() + ": " + user.getRoleAuth());
        }
        //cookie.setDomain(COOKIE_DOMAIN);
        loginCookie.setPath("/");
        //cookies的过期时间设置为1天
        loginCookie.setMaxAge(3600 * 24);
        SystemContext.getResponse().addCookie(loginCookie);
        json.setIsSuccess(1);
        json.setIsRedirect(1);
        json.setRedirectURL(redirectUrl);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout() {
        JsonVO json = new JsonVO();
        Cookie loginCookie = getLoginCookie();
        if (loginCookie != null) {
            loginCookie = new Cookie(COOKIE_LOGIN_TICKET, null);
            loginCookie.setMaxAge(0);
            loginCookie.setPath("/");
            SystemContext.getResponse().addCookie(loginCookie);
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @RequestMapping(value = "/register_page", method = RequestMethod.GET)
    public String webRegisterPage(Model model) {
        return "/page/console/web-register";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String webRegister(@RequestParam String account,
                              @RequestParam(required = false, defaultValue = "1") String loginTypeStr,
                              @RequestParam(required = false, defaultValue = "") String password,
                              @RequestParam(required = false, defaultValue = "") String confirmPassword,
                              @RequestParam(required = false, defaultValue = "") String smsCode,
                              @RequestParam(required = false, defaultValue = "抓机无名氏") String nick,
                              @RequestParam(required = false, defaultValue = "") String avatar) {
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(loginTypeStr)) {
            json.setMsg("请选择登录类型");
            json.setIsSuccess(0);
            return json.toString();
        }

        LoginType loginType = LoginType.getType(loginTypeStr);
        if (loginType == null || loginType != LoginType.EMAIL) {
            json.setMsg("无效的登录类型，目前后台只支持邮箱注册和登录");
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
        //            case WECHAT:
        //                if (userService.getByWechat(account) != null) {
        //                    json.setMsg("该微信号已注册!!");
        //                    json.setIsSuccess(0);
        //                    return json.toString();
        //                }
        //                user.setWechatOpenId(account);
        //                break;
        //            case CELLPHONE:
        //                if (StringUtils.isBlank(password)) {
        //                    json.setMsg("请输入密码");
        //                    json.setIsRedirect(0);
        //                    return json.toString();
        //                }
        //                if (StringUtils.isBlank(confirmPassword)) {
        //                    json.setMsg("请输入确认密码");
        //                    json.setIsRedirect(0);
        //                    return json.toString();
        //                }
        //                if (!password.equals(confirmPassword)) {
        //                    json.setMsg("两次密码输入不一致，请重新输入");
        //                    json.setIsRedirect(0);
        //                    return json.toString();
        //                }
        //                if (!VerifyUtil.checkCellphoneNumber(account)) {
        //                    json.setMsg("手机格式不正确");
        //                    json.setIsSuccess(0);
        //                    return json.toString();
        //                }
        //                if (userService.getByCellphone(account) != null) {
        //                    json.setMsg("该手机号已注册");
        //                    json.setIsSuccess(0);
        //                    return json.toString();
        //                }
        //                if (StringUtils.isBlank(smsCode)) {
        //                    json.setMsg("请输入验证码");
        //                    json.setIsSuccess(0);
        //                    return json.toString();
        //                }
        //                if (!smsCodeService.check(account, smsCode)) {
        //                    json.setMsg("验证码错误");
        //                    json.setIsSuccess(0);
        //                    return json.toString();
        //                }
        //                user.setCellphone(account);
        //                user.setPassword(MD5.MakeMD5(password));
        //                break;
        }
        if (StringUtils.isNotBlank(nick)) {
            user.setNick(nick);
        }
        if (StringUtils.isNotBlank(avatar)) {
            user.setAvatar(avatar);
        }
        user.setRoleAuth(DEFAULT_USER_ROLE_AUTH);

        try {
            userService.add(user);
        } catch (Exception e) {
            json.setMsg("服务异常，注册失败!!");
            json.setIsSuccess(0);
            return json.toString();
        }

        Cookie loginCookie = getLoginCookie();
        if (loginCookie == null) {
            loginCookie = new Cookie(COOKIE_LOGIN_TICKET, user.getId() + ":" + user.getRoleAuth());
        } else {
            loginCookie.setValue(user.getId() + ": " + user.getRoleAuth());
        }
        //cookie.setDomain(COOKIE_DOMAIN);
        loginCookie.setPath("/");
        //cookies的过期时间设置为1天
        loginCookie.setMaxAge(3600 * 24);
        SystemContext.getResponse().addCookie(loginCookie);
        json.setData(user.getId());
        json.setIsSuccess(1);
        return json.toString();
    }

    public Cookie getLoginCookie() {
        Cookie[] cookies = SystemContext.getRequest().getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie c : cookies) {
            if (COOKIE_LOGIN_TICKET.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

}
