package com.arms.appserver.controller.api;

import com.arms.appserver.manager.ActivityManager;
import com.arms.appserver.vo.ShareVO;
import com.arms.common.vo.JsonVO;
import com.arms.service.enums.UnitOfTime;
import com.arms.service.exception.UserException;
import com.arms.service.service.InviteCodeService;
import com.arms.service.service.SuperiorRelationService;
import com.arms.service.service.UserSourceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/11/26.
 */
@Controller
public class ActivityApiController {

    @Resource
    private ActivityManager activityManager;

    @Resource
    private SuperiorRelationService superiorRelationService;

    @Resource
    private InviteCodeService inviteCodeService;

    @Resource
    private UserSourceService userSourceService;

    private static final Logger logger = LoggerFactory.getLogger(ActivityApiController.class);

    @ResponseBody
    @RequestMapping(value = "/api/activity/get_invite_code",method = RequestMethod.GET)
    public String getInvateCode(@RequestParam int userId){
        JsonVO json = new JsonVO();
        if (userId <= 0 ){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }

        json.setData(inviteCodeService.getInviteCode(userId));
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/activity/submit_invite_code",method = RequestMethod.POST)
    public String submitInvateCode(@RequestParam int userId,@RequestParam String invateCode){
        JsonVO json = new JsonVO();
        if (userId <= 0 ){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }

        if (StringUtils.isBlank(invateCode)){
            json.setIsSuccess(0);
            json.setMsg("邀请码不能为空");
            return json.toString();
        }

        int superiorUserId = inviteCodeService.getUserId(invateCode);

        if (superiorUserId > 0){
            try {
                activityManager.bindingSuperior(userId,superiorUserId);
            } catch (UserException e) {
                logger.info("",e);
                json.setIsSuccess(0);
                json.setMsg(e.getMessage());
                json.setMsgForDebug(e.getMessage());
            }
            String source = userSourceService.get(superiorUserId);
            if (StringUtils.isNotBlank(source)){
                try {
                    userSourceService.add(userId,source);
                } catch (UserException e) {
                    logger.info("",e);
                }
            }
        }

        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/activity/get_submit_invite_code",method = RequestMethod.GET)
    public String getSubmitInvateCode(@RequestParam int userId){
        JsonVO json = new JsonVO();
        if (userId <= 0 ){
            json.setMsg("请求错误");
            json.setMsgForDebug("非法的请求参数: [userId: " + userId + "]");
            json.setIsSuccess(0);
            return json.toString();
        }
        Integer superiorUserId = superiorRelationService.getSuperiorId(userId);
        if (superiorUserId == null){
            json.setIsSuccess(1);
            json.setData("");
            return json.toString();
        }
        json.setData(inviteCodeService.getInviteCode(superiorUserId));
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping("/api/activity/invite_share")
    public String inviteShare(@RequestParam int userId,@RequestParam String packet){
        JsonVO json = new JsonVO();
        ShareVO shareVO = new ShareVO();
        shareVO.setTitle("简速贷，分享邀好友");
        shareVO.setContent("我在用简速贷");
        shareVO.setLogo("http://47.97.115.58:8080/images/91/9194347a3fab4823a1a68074b611f568.png");
        String source = userSourceService.get(userId);
        String inviteCode = inviteCodeService.getInviteCode(userId);
        if (StringUtils.isNotBlank(source)){
            shareVO.setLinkUrl("/wap/invite_share?inviteCode=" + inviteCode + "&source=" + source + "_" + packet);
        }else {
            shareVO.setLinkUrl("/wap/invite_share?inviteCode=" + inviteCode + "&source=" + packet);
        }
        json.setIsSuccess(1);
        json.setData(shareVO);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/activity/daily_attendance",method = RequestMethod.GET)
    public String dailyAttendance(@RequestParam int userId){
        JsonVO json = new JsonVO();
        if (userId < 0){
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数,userId: " + userId);
            return json.toString();
        }
        activityManager.dailyAttendance(userId);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/activity/get_user_join_count", method = RequestMethod.GET)
    public String getUserJoinCount(@RequestParam int activityId,
                                   @RequestParam int userId,
                                   @RequestParam(required = false, defaultValue = "0") int unitCode) {
        JsonVO json = new JsonVO();
        if (activityId <= 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数,activityId: " + activityId);
            return json.toString();
        }
        int count = activityManager.getUserJoinCount(userId,activityId, UnitOfTime.getUnitOfTime(unitCode));
        json.setData(count);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/activity/countJunior",method = RequestMethod.GET)
    public String countJunior(@RequestParam int userId){
        JsonVO json = new JsonVO();
        int count = superiorRelationService.countJunior(userId);
        json.setData(count);
        json.setIsSuccess(1);
        return json.toString();
    }
}
