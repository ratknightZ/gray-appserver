package com.arms.appserver.controller.console;

import com.arms.common.vo.JsonVO;
import com.arms.service.service.GoldStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author liuchen
 * @since 2017/12/15
 */
@Controller
public class AssetsController {

    @Resource
    private GoldStatisticsService goldStatisticsService;

    private static final Logger logger = LoggerFactory.getLogger(AssetsController.class);

    @ResponseBody
    @RequestMapping(value = "/assets/gold_statistic",method = RequestMethod.GET)
    public String goldStatistic(){
        JsonVO json = new JsonVO();
        try {
            goldStatisticsService.statistic();
        } catch (Exception e) {
            logger.info("统计任务出错",e);
            json.setIsSuccess(0);
            json.setMsg("统计任务出错");
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
