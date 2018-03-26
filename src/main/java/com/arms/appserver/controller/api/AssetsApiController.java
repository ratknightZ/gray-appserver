package com.arms.appserver.controller.api;

import com.arms.appserver.vo.GoldAccountVO;
import com.arms.appserver.vo.GoldStaitisticRankListVO;
import com.arms.common.vo.JsonVO;
import com.arms.service.exception.UserException;
import com.arms.service.model.GoldAccount;
import com.arms.service.model.GoldStatistics;
import com.arms.service.model.User;
import com.arms.service.service.GoldAccountService;
import com.arms.service.service.GoldStatisticsService;
import com.arms.service.service.UserService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */
@Controller
public class AssetsApiController {

    @Resource
    private GoldAccountService goldAccountService;

    @Resource
    private GoldStatisticsService goldStatisticsService;

    @Resource
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AssetsApiController.class);

    @ResponseBody
    @RequestMapping(value = "/api/assets/get_user_gold",method = RequestMethod.GET)
    public String getUserGold(@RequestParam int userId){
        JsonVO json = new JsonVO();
        if (userId < 0){
            json.setIsSuccess(0);
            json.setMsg("非法的用户ID，userId: " + userId);
            return json.toString();
        }
        try {
            json.setData(goldAccountService.getUserGold(userId));
            json.setIsSuccess(1);
        } catch (UserException e) {
            logger.info("",e);
            json.setIsSuccess(0);
            json.setMsg(e.getMessage());
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/assets/get_gold_account",method = RequestMethod.GET)
    public String getGoldAccount(@RequestParam int userId,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "12") int pageSize){

        JsonVO json = new JsonVO();
        if (userId <= 0) {
            json.setMsg("请求错误");
            json.setMsgForDebug("非法的请求参数：[userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        List<GoldAccount> goldAccountList = goldAccountService.getUserAccountList(userId,page,pageSize);
        List<GoldAccountVO> goldAccountVOList = new ArrayList<>();
        if (goldAccountList != null && goldAccountList.size() > 0) {
            for (GoldAccount goldAccount : goldAccountList) {
                if (goldAccount == null) {
                    continue;
                }
                GoldAccountVO goldAccountVO = new GoldAccountVO();
                goldAccountVO.setGoldAccountId(goldAccount.getId());
                goldAccountVO.setUserId(goldAccount.getUserId());
                goldAccountVO.setType(goldAccount.getGoldType());
                goldAccountVO.setTypeReferencePrimaryKey(goldAccount.getTypeReferencePrimaryKey());
                goldAccountVO.setValue(goldAccount.getValue());
                goldAccountVO.setSketch(goldAccount.getSketch());
                goldAccountVO.setCreateTime(new DateTime(goldAccount.getGmtCreate().getTime())
                        .toString("yyyy-MM-dd HH:mm:ss"));
                goldAccountVOList.add(goldAccountVO);
            }
        }
        json.setData(goldAccountVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/assets/get_gold_rank_list",method = RequestMethod.GET)
    public String getGoldRankList(@RequestParam(required = false,defaultValue = "1") int page,
                                  @RequestParam(required = false,defaultValue = "12") int pageSize){
        JsonVO json = new JsonVO();
        List<GoldStatistics> goldStatisticsList = goldStatisticsService.getRankList(page,pageSize);
        List<GoldStaitisticRankListVO> goldStaitisticRankListVOList = new ArrayList<>();
        for (GoldStatistics goldStatistics : goldStatisticsList){
            if (goldStatistics == null){
                continue;
            }
            GoldStaitisticRankListVO goldStaitisticRankListVO = new GoldStaitisticRankListVO();
            goldStaitisticRankListVO.setGold(goldStatistics.getGold());
            User user = userService.get(goldStatistics.getUserId());
            if (user != null){
                goldStaitisticRankListVO.setCellphone(user.getCellphone());
            }
            goldStaitisticRankListVOList.add(goldStaitisticRankListVO);
        }
        json.setData(goldStaitisticRankListVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/assets/get_gold_rank_location",method = RequestMethod.GET)
    public String getGoldRankLocation(@RequestParam int userId){
        JsonVO json = new JsonVO();
        int location = goldStatisticsService.getGoldRankLocation(userId);
        json.setData(location);
        json.setIsSuccess(1);
        return json.toString();
    }
}
