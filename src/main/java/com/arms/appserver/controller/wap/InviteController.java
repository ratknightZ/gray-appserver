package com.arms.appserver.controller.wap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jinxuan
 * @since 2017/11/24
 */
@Controller
public class InviteController {

    @RequestMapping(value = "/wap/invite_share", method = RequestMethod.GET)
    public String inviteSharePage(Model model,
                                  @RequestParam(required = false) String inviteCode,
                                  @RequestParam(required = false,defaultValue = "H5") String source) {
        model.addAttribute("inviteCode", inviteCode);
        model.addAttribute("source", source);
        if (!"H5".equals(source)){
            return "page/app/yaoxin";
        }
        return "page/app/invite-share";
    }

    @RequestMapping(value = "/wap/download_page",method = RequestMethod.GET)
    public String download(){
        return "page/app/download";
    }

}
