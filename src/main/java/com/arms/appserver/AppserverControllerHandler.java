package com.arms.appserver;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.arms.core.context.SystemContext;
import com.arms.service.model.User;
import com.arms.service.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.arms.mvc.spring.ControllerHandler;

@Component("ControllerHandler")
public class AppserverControllerHandler implements ControllerHandler {

    @Resource
    private UserService userService;

    private static String COOKIE_LOGIN_TICKET = "clt";

    private static Logger logger              = LoggerFactory
            .getLogger(AppserverControllerHandler.class);

    @Override
    public void postSyncHandle(HttpServletRequest request, HttpServletResponse response,
                               ModelAndView modelAndView) throws Exception {
        boolean isLogin = false;
        Cookie loginCookie = getLoginCookie();
        if (loginCookie != null && StringUtils.isNotBlank(loginCookie.getValue())) {
            try {
                int loginUserId = Integer.parseInt(loginCookie.getValue().split(":")[0]);
                User user = userService.get(loginUserId);
                modelAndView.addObject("loginUser", user);
                isLogin = true;
            } catch (Exception e) {
                logger.info("", e);
            }
        }
        modelAndView.addObject("isLogin", isLogin);
    }

    @Override
    public void postAsyncHandle(HttpServletRequest request, HttpServletResponse response,
                                ModelAndView modelAndView) throws Exception {
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
