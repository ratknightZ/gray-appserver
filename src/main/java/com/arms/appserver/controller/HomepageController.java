package com.arms.appserver.controller;

import java.net.MalformedURLException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arms.core.configure.ConfigResolver;
import com.arms.core.configure.DefaultConfigResolver;
import com.arms.common.vo.JsonVO;

@Controller
public class HomepageController {

    private static String ENV_MODE        = "";

    private static String PROJECT_VERSION = "";

    private static Logger logger          = LoggerFactory.getLogger(HomepageController.class);

    static {
        ConfigResolver configResolver = new DefaultConfigResolver();
        Configuration configuration = null;
        try {
            configuration = configResolver.getAppConfig();
            ENV_MODE = configuration.getString("app.env");
            PROJECT_VERSION = configuration.getString("app.version");
        } catch (ConfigurationException e) {
            logger.error("", e);
        } catch (MalformedURLException e) {
            logger.error("", e);
        }
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homepage(Model model) {
        model.addAttribute("envMode", ENV_MODE);
        model.addAttribute("projectVersion", PROJECT_VERSION);
        return "home";
    }

    @ResponseBody
    @RequestMapping(value = "/home", method = { RequestMethod.GET, RequestMethod.POST })
    public String home() {
        JsonVO json = new JsonVO();
        json.setData("This is a json data from taotie-appserver-server !!" + ENV_MODE
                     + ", Project Version: " + PROJECT_VERSION);
        json.setMsg("This is a msg from taotie-appserver-server !!" + ENV_MODE
                    + ", Project Version: " + PROJECT_VERSION);
        json.setIsSuccess(1);
        return json.toString();
    }

}
