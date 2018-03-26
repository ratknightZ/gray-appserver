package com.arms.appserver.controller.api;

import com.arms.common.vo.JsonVO;
import com.arms.service.service.StatisticsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author liuchen
 * @since 2017/12/29
 */
@Controller
public class StatisticsApiController {

    @Resource
    private StatisticsService statisticsService;

    @ResponseBody
    @RequestMapping(value = "/api/statistics/visit_record",method = {RequestMethod.GET,RequestMethod.POST})
    public String visitRecord(HttpServletRequest request, @RequestParam String source){
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(source)){
            json.setIsSuccess(0);
            json.setMsg("渠道来源不能为空");
            return json.toString();
        }
        statisticsService.addVisitRecord(source,request.getRemoteAddr());
        json.setIsSuccess(1);
        return json.toString();
    }
}
