package com.arms.appserver.controller.console;

import com.arms.appserver.vo.console.ListOpinionVO;
import com.arms.common.util.PageUtil;
import com.arms.service.model.Opinion;
import com.arms.service.model.User;
import com.arms.service.service.OpinionService;
import com.arms.service.service.UserService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchen
 * @since 2018/3/16
 */
@Controller
public class OpinionController {

    @Resource
    private UserService userService;

    @Resource
    private OpinionService opinionService;

    @RequestMapping(value = "/opinion/list_page",method = RequestMethod.GET)
    public String listPage(Model model, @RequestParam(required = false,defaultValue = "1") int page,
                           @RequestParam(required = false,defaultValue = "12") int pageSize){
        int totalNum = opinionService.count();
        int pageCount = PageUtil.calcPageTotal(totalNum,pageSize);
        List<Opinion> opinionList = opinionService.getByLimit(page,pageSize);
        List<ListOpinionVO> listOpinionVOList = new ArrayList<>();
        for (Opinion opinion : opinionList){
            if (opinion == null){
                continue;
            }
            ListOpinionVO listOpinionVO = new ListOpinionVO();
            listOpinionVO.setOpinionId(opinion.getId());
            listOpinionVO.setUserId(opinion.getUserId());
            User user = userService.get(opinion.getUserId());
            if (user != null){
                listOpinionVO.setCellphone(user.getCellphone());
            }
            listOpinionVO.setTheme(opinion.getTheme());
            listOpinionVO.setEmail(opinion.getEmail());
            listOpinionVO.setDetail(opinion.getDetail());
            listOpinionVO.setStatus(opinion.getStatus());
            listOpinionVO.setCreateTime(new DateTime(opinion.getGmtCreate().getTime()).toString("yyyy-MM-dd HH:mm:ss"));
            listOpinionVOList.add(listOpinionVO);
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("listOpinionVOList",listOpinionVOList);
        return "page/console/opinion-list";
    }
}
