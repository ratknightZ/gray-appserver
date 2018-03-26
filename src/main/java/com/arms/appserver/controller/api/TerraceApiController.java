package com.arms.appserver.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.arms.appserver.controller.util.AppPushUtil;
import com.arms.appserver.vo.TerraceDetailVO;
import com.arms.appserver.vo.TerraceListVO;
import com.arms.appserver.vo.TerraceNotifyVO;
import com.arms.appserver.vo.api.BroadcastVO;
import com.arms.appserver.vo.api.TerraceHistoryLendVO;
import com.arms.common.util.VerifyUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.enums.Status;
import com.arms.service.enums.UnitOfQuickestTime;
import com.arms.service.enums.UnitOfRate;
import com.arms.service.exception.AppPushException;
import com.arms.service.exception.TerraceException;
import com.arms.service.model.Terrace;
import com.arms.service.model.TerraceNotify;
import com.arms.service.model.TerraceTypeR;
import com.arms.service.service.TerraceNotifyService;
import com.arms.service.service.TerraceService;
import com.arms.service.service.TerraceTypeRService;
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
 * @author liuchen
 * @since 2017/12/5
 */
@Controller
public class TerraceApiController {

    @Resource
    private TerraceTypeRService terraceTypeRService;

    @Resource
    private TerraceService terraceService;

    @Resource
    private TerraceNotifyService terraceNotifyService;

    private static final Logger logger = LoggerFactory.getLogger(TerraceApiController.class);

    @ResponseBody
    @RequestMapping(value = "/api/terrace/get_terrace_list_by_type", method = {RequestMethod.GET, RequestMethod.POST})
    public String getTerraceListByType(@RequestParam int typeId,
                                       @RequestParam(required = false, defaultValue = "0") int showChannel,
                                       @RequestParam(required = false, defaultValue = "0") int maxAmount,
                                       @RequestParam(required = false, defaultValue = "0") int minAmount,
                                       @RequestParam(required = false, defaultValue = "0") int timeLimit,
                                       @RequestParam(required = false, defaultValue = "0") int haveCreditCard,
                                       @RequestParam(required = false, defaultValue = "0") int repaymentWay,
                                       @RequestParam(required = false, defaultValue = "0") int sortWay,
                                       @RequestParam(required = false) String profession,
                                       @RequestParam(required = false) String credit,
                                       @RequestParam(required = false) String lendPurpose,
                                       @RequestParam(required = false, defaultValue = "1") int page,
                                       @RequestParam(required = false, defaultValue = "12") int pageSize) {
        JsonVO json = new JsonVO();
        if (typeId <= 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，typeId: " + typeId);
            return json.toString();
        }
        if (maxAmount < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，maxAmount: " + maxAmount);
            return json.toString();
        }
        if (minAmount < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，minAmount: " + minAmount);
            return json.toString();
        }
        if (timeLimit < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，timeLimit: " + timeLimit);
            return json.toString();
        }
        if (haveCreditCard < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，haveCreditCard: " + haveCreditCard);
            return json.toString();
        }
        if (repaymentWay < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，repaymentWay: " + repaymentWay);
            return json.toString();
        }
        if (sortWay < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，sortWay: " + sortWay);
            return json.toString();
        }

        List<Terrace> terraceList = terraceService.getList(typeId, showChannel, maxAmount, minAmount, timeLimit, haveCreditCard, repaymentWay, sortWay, profession, credit, lendPurpose, page, pageSize);
        List<TerraceListVO> terraceListVOList = new ArrayList<>();
        for (Terrace terrace : terraceList) {
            if (terrace == null) {
                continue;
            }
            TerraceListVO terraceListVO = new TerraceListVO();
            terraceListVO.setTerraceId(terrace.getId());
            terraceListVO.setLogo(terrace.getLogo());
            terraceListVO.setName(terrace.getName());
            terraceListVO.setRate(terrace.getRate());
            terraceListVO.setLendType(terrace.getLendType());
            if (terrace.getQuickestTime() < 60) {
                terraceListVO.setQuickestTime(60);
            } else {
                terraceListVO.setQuickestTime((int) terrace.getQuickestTime());
            }
            terraceListVO.setMaxAmount(terrace.getMaxAmount());
            terraceListVO.setMinAmount(terrace.getMinAmount());
            terraceListVO.setApplyForCount((terrace.getApplyForCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessCount((terrace.getSuccessCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessRate(terrace.getSuccessRate());
            if (terrace.getUnitOfQuickestTime() == UnitOfQuickestTime.MINUTE.getValue()) {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() / 60 + "分钟");
            } else {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() + "秒钟");
            }
            if (terrace.getUnitOfRate() == UnitOfRate.RATE_DAY.getValue()) {
                terraceListVO.setRateStr(terrace.getRateDay() + "%/天");
            } else if (terrace.getUnitOfRate() == UnitOfRate.RATE_MONTH.getValue()) {
                terraceListVO.setRateStr(terrace.getRate() + "%/月");
            }
            terraceListVOList.add(terraceListVO);
        }
        json.setData(terraceListVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/get_terrace_detail", method = RequestMethod.GET)
    public String getTerraceDetail(@RequestParam int terraceId) {
        JsonVO json = new JsonVO();
        if (terraceId <= 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，terraceId: " + terraceId);
            return json.toString();
        }
        Terrace terrace = terraceService.get(terraceId);
        TerraceDetailVO terraceDetailVO = new TerraceDetailVO();
        terraceDetailVO.setTerraceId(terrace.getId());
        terraceDetailVO.setLogo(terrace.getLogo());
        terraceDetailVO.setName(terrace.getName());
        terraceDetailVO.setRate(terrace.getRate());
        terraceDetailVO.setLendType(terrace.getLendType());
        if (terrace.getQuickestTime() < 60) {
            terraceDetailVO.setQuickestTime(60);
        } else {
            terraceDetailVO.setQuickestTime((int) terrace.getQuickestTime());
        }
        terraceDetailVO.setMaxAmount(terrace.getMaxAmount());
        terraceDetailVO.setMinAmount(terrace.getMinAmount());
        terraceDetailVO.setApplyForCondition(terrace.getApplyForCondition());
        terraceDetailVO.setNeedInformation(terrace.getNeedInformation());
        terraceDetailVO.setChoosableRepaymentTimeLimit(terrace.getChoosableRepaymentTimeLimit());
        terraceDetailVO.setUrl(terrace.getUrl());
        terraceDetailVO.setApplyForCount((terrace.getApplyForCount() * 987) + (terrace.getId() * 3) + 11);
        terraceDetailVO.setSuccessCount((terrace.getSuccessCount() * 987) + (terrace.getId() * 3) + 11);
        terraceDetailVO.setSuccessRate(terrace.getSuccessRate());
        if (terrace.getUnitOfQuickestTime() == UnitOfQuickestTime.MINUTE.getValue()) {
            terraceDetailVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() / 60 + "分钟");
        } else {
            terraceDetailVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() + "秒钟");
        }
        if (terrace.getUnitOfRate() == UnitOfRate.RATE_DAY.getValue()) {
            terraceDetailVO.setRateStr(terrace.getRateDay() + "%/天");
        } else if (terrace.getUnitOfRate() == UnitOfRate.RATE_MONTH.getValue()) {
            terraceDetailVO.setRateStr(terrace.getRate() + "%/月");
        }
        terraceDetailVO.setUnitOfRepaymentTime(terrace.getUnitOfRepaymentTime());
        json.setData(terraceDetailVO);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/get_terrace_list", method = RequestMethod.POST)
    public String getTerraceList(@RequestParam int typeId,
                                 @RequestParam(required = false, defaultValue = "0") int showChannel,
                                 @RequestParam(required = false, defaultValue = "0") int maxAmount,
                                 @RequestParam(required = false, defaultValue = "0") int minAmount,
                                 @RequestParam(required = false, defaultValue = "0") int timeLimit,
                                 @RequestParam(required = false, defaultValue = "0") int haveCreditCard,
                                 @RequestParam(required = false, defaultValue = "0") int repaymentWay,
                                 @RequestParam(required = false, defaultValue = "0") int sortWay,
                                 @RequestParam(required = false) String profession,
                                 @RequestParam(required = false) String credit,
                                 @RequestParam(required = false) String lendPurpose,
                                 @RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "12") int pageSize) {

        JsonVO json = new JsonVO();
        if (typeId <= 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，typeId: " + typeId);
            return json.toString();
        }
        if (maxAmount < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，maxAmount: " + maxAmount);
            return json.toString();
        }
        if (minAmount < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，minAmount: " + minAmount);
            return json.toString();
        }
        if (timeLimit < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，timeLimit: " + timeLimit);
            return json.toString();
        }
        if (haveCreditCard < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，haveCreditCard: " + haveCreditCard);
            return json.toString();
        }
        if (repaymentWay < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，repaymentWay: " + repaymentWay);
            return json.toString();
        }
        if (sortWay < 0) {
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，sortWay: " + sortWay);
            return json.toString();
        }

        List<Terrace> terraceList = terraceService.getList(typeId, showChannel, maxAmount, minAmount, timeLimit, haveCreditCard, repaymentWay, sortWay, profession, credit, lendPurpose, page, pageSize);
        List<TerraceListVO> terraceListVOList = new ArrayList<>();
        for (Terrace terrace : terraceList) {
            if (terrace == null) {
                continue;
            }
            TerraceListVO terraceListVO = new TerraceListVO();
            terraceListVO.setTerraceId(terrace.getId());
            terraceListVO.setLogo(terrace.getLogo());
            terraceListVO.setName(terrace.getName());
            terraceListVO.setRate(terrace.getRate());
            terraceListVO.setLendType(terrace.getLendType());
            if (terrace.getQuickestTime() < 60) {
                terraceListVO.setQuickestTime(60);
            } else {
                terraceListVO.setQuickestTime((int) terrace.getQuickestTime());
            }
            terraceListVO.setMaxAmount(terrace.getMaxAmount());
            terraceListVO.setMinAmount(terrace.getMinAmount());
            terraceListVO.setApplyForCount((terrace.getApplyForCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessCount((terrace.getSuccessCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessRate(terrace.getSuccessRate());
            if (terrace.getUnitOfQuickestTime() == UnitOfQuickestTime.MINUTE.getValue()) {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() / 60 + "分钟");
            } else {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() + "秒钟");
            }
            if (terrace.getUnitOfRate() == UnitOfRate.RATE_DAY.getValue()) {
                terraceListVO.setRateStr(terrace.getRateDay() + "%/天");
            } else if (terrace.getUnitOfRate() == UnitOfRate.RATE_MONTH.getValue()) {
                terraceListVO.setRateStr(terrace.getRate() + "%/月");
            }
            terraceListVOList.add(terraceListVO);
        }
        json.setData(terraceListVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/get_hot_terrace_list", method = RequestMethod.GET)
    public String getHotTerraceList(@RequestParam(required = false, defaultValue = "1") int isHot,
                                    @RequestParam(required = false, defaultValue = "0") int showChannel,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "12") int pageSize) {
        JsonVO json = new JsonVO();

        List<Terrace> terraceList = terraceService.getList(isHot, showChannel, 0, 0);
        List<TerraceListVO> terraceListVOList = new ArrayList<>();
        for (Terrace terrace : terraceList) {
            if (terrace == null) {
                continue;
            }
            TerraceListVO terraceListVO = new TerraceListVO();
            terraceListVO.setTerraceId(terrace.getId());
            terraceListVO.setLogo(terrace.getLogo());
            terraceListVO.setName(terrace.getName());
            terraceListVO.setRate(terrace.getRate());
            terraceListVO.setLendType(terrace.getLendType());
            if (terrace.getQuickestTime() < 60) {
                terraceListVO.setQuickestTime(60);
            } else {
                terraceListVO.setQuickestTime((int) terrace.getQuickestTime());
            }
            terraceListVO.setMaxAmount(terrace.getMaxAmount());
            terraceListVO.setMinAmount(terrace.getMinAmount());
            terraceListVO.setApplyForCount((terrace.getApplyForCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessCount((terrace.getSuccessCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessRate(terrace.getSuccessRate());
            if (terrace.getUnitOfQuickestTime() == UnitOfQuickestTime.MINUTE.getValue()) {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() / 60 + "分钟");
            } else {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() + "秒钟");
            }
            if (terrace.getUnitOfRate() == UnitOfRate.RATE_DAY.getValue()) {
                terraceListVO.setRateStr(terrace.getRateDay() + "%/天");
            } else if (terrace.getUnitOfRate() == UnitOfRate.RATE_MONTH.getValue()) {
                terraceListVO.setRateStr(terrace.getRate() + "%/月");
            }
            terraceListVOList.add(terraceListVO);
        }
        json.setData(terraceListVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping("/api/terrace/goto_terrace")
    public String gotoTerrace(@RequestParam int terraceId) {
        JsonVO json = new JsonVO();
        terraceService.addApplyForCount(terraceId);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/terrace_notify", method = RequestMethod.POST)
    public String terraceNotify(@RequestParam int terraceId,
                                @RequestParam double amount,
                                @RequestParam String cellphone,
                                @RequestParam int timeLimit) {

        if (terraceId <= 0) {
            return "";
        }
        if (!VerifyUtil.checkCellphoneNumber(cellphone)) {
            return "";
        }
        if (amount <= 0) {
            return "";
        }
        if (timeLimit < 0) {
            return "";
        }
        Terrace terrace = terraceService.get(terraceId);
        if (terrace == null) {
            return "";
        }
        //广播
        String time = DateTime.now().toString("MM-dd HH:mm");
        String dealCellphone = cellphone.substring(0, 3) + "***" + cellphone.substring(cellphone.length() - 3, cellphone.length());
        String terraceName = terrace.getName();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", time);
        jsonObject.put("dealCellphone", dealCellphone);
        jsonObject.put("terraceName", terraceName);
        jsonObject.put("amount", amount);
        try {
            AppPushUtil.iosAppPush("broadcast", jsonObject.toString());
        } catch (AppPushException e) {
            logger.info("", e);
        }
        terraceNotifyService.dealNotify(terraceId, amount, cellphone, timeLimit);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/terrace_notify_list_by_user", method = RequestMethod.GET)
    public String terraceNotifyListByUser(@RequestParam int userId,
                                          @RequestParam(required = false, defaultValue = "1") int page,
                                          @RequestParam(required = false, defaultValue = "12") int pageSize) {
        JsonVO json = new JsonVO();
        if (userId <= 0) {
            json.setIsSuccess(0);
            json.setMsgForDebug("非法请求，userId: " + userId);
            return json.toString();
        }
        List<TerraceNotify> terraceNotifyList = terraceNotifyService.getByUserId(userId, page, pageSize);
        List<TerraceNotifyVO> terraceNotifyVOList = new ArrayList<>();
        for (TerraceNotify terraceNotify : terraceNotifyList) {
            if (terraceNotify == null) {
                continue;
            }
            TerraceNotifyVO terraceNotifyVO = new TerraceNotifyVO();
            terraceNotifyVO.setAmount(terraceNotify.getAmount());
            terraceNotifyVO.setTerraceId(terraceNotify.getTerraceId());
            terraceNotifyVO.setTerraceNotifyId(terraceNotify.getId());
            terraceNotifyVO.setTimeLimit(terraceNotify.getTimeLimit());
            terraceNotifyVO.setCreateTime(new DateTime(terraceNotify.getGmtCreate().getTime()).toString("yyyy-MM-dd HH:mm:ss"));
            Terrace terrace = terraceService.get(terraceNotify.getTerraceId());
            if (terrace != null) {
                terraceNotifyVO.setTerraceUrl(terrace.getUrl());
                terraceNotifyVO.setTerraceLogo(terrace.getLogo());
            }
            terraceNotifyVOList.add(terraceNotifyVO);
        }
        json.setData(terraceNotifyVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/broadcast_list", method = RequestMethod.GET)
    public String broadcastList(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "12") int pageSize) {
        JsonVO json = new JsonVO();
//        List<TerraceNotify> terraceNotifyList = terraceNotifyService.get(page,pageSize);
//        List<BroadcastVO> broadcastVOList = new ArrayList<>();
//        for (TerraceNotify terraceNotify : terraceNotifyList){
//            if (terraceNotify == null){
//                continue;
//            }
//            BroadcastVO broadcastVO = new BroadcastVO();
//            broadcastVO.setAmount(terraceNotify.getAmount());
//            String cellphone = terraceNotify.getCellphone();
//            broadcastVO.setCellphone(cellphone.substring(0,3) + "***" + cellphone.substring(cellphone.length() - 3,cellphone.length()));
//            broadcastVO.setTime(new DateTime(terraceNotify.getGmtCreate()).toString("MM-dd HH:mm"));
//            Terrace terrace = terraceService.get(terraceNotify.getTerraceId());
//            if (terrace != null){
//                broadcastVO.setTerraceName(terrace.getName());
//            }
//            broadcastVOList.add(broadcastVO);
//        }
        List<Integer> terraceIdList = terraceService.getIdList(Status.UP.getValue());
        int[] cellphoneSecondArray = {3, 5, 8};
        List<BroadcastVO> broadcastVOList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            BroadcastVO broadcastVO = new BroadcastVO();
            Terrace terrace = terraceService.get(terraceIdList.get((int) (Math.random() * (terraceIdList.size()))));
            broadcastVO.setTerraceName(terrace.getName());
            broadcastVO.setAmount((int) (1 + Math.random() * (10)) * 1000);
            StringBuilder cellphone = new StringBuilder();
            cellphone.append("1");
            cellphone.append(cellphoneSecondArray[(int) (Math.random() * (cellphoneSecondArray.length))]);
            cellphone.append((int) (Math.random() * (10)));
            cellphone.append("***");
            cellphone.append((int) (100 + Math.random() * (900)));
//            broadcastVO.setCellphone("" + 1 + cellphoneSecondArray[(int)(Math.random()*(cellphoneSecondArray.length))] + (int)(Math.random()*(9+1)) + "***" + (int)(Math.random()*(999+1)));
            broadcastVO.setCellphone(cellphone.toString());
            broadcastVO.setTime(new DateTime(DateTime.now().getMillis() - (int) (Math.random() * (86400001))).toString("MM-dd HH:mm"));
            broadcastVOList.add(broadcastVO);
        }
        json.setData(broadcastVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/terrace/terrace_notify_list_by_terrace", method = RequestMethod.GET)
    public String terraceNotifyListByTerrace(@RequestParam int terraceId,
                                             @RequestParam(required = false, defaultValue = "1") int page,
                                             @RequestParam(required = false, defaultValue = "3") int pageSize) {
        JsonVO json = new JsonVO();
        List<TerraceNotify> terraceNotifyList = terraceNotifyService.getByTerraceId(terraceId, page, pageSize);
        List<TerraceHistoryLendVO> terraceHistoryLendVOList = new ArrayList<>();
        for (TerraceNotify terraceNotify : terraceNotifyList) {
            if (terraceNotify == null) {
                continue;
            }
            TerraceHistoryLendVO terraceHistoryLendVO = new TerraceHistoryLendVO();
            String cellphone = terraceNotify.getCellphone();
            terraceHistoryLendVO.setCellphone(cellphone.substring(0, 3) + "***" + cellphone.substring(cellphone.length() - 3, cellphone.length()));
            terraceHistoryLendVO.setAmount(terraceNotify.getAmount());
            terraceHistoryLendVO.setCreateTime(new DateTime(terraceNotify.getGmtCreate().getTime()).toString("MM-dd HH:mm"));
            Terrace terrace = terraceService.get(terraceNotify.getTerraceId());
            terraceHistoryLendVO.setTerraceName(terrace.getName());
            terraceHistoryLendVOList.add(terraceHistoryLendVO);
        }
        json.setData(terraceHistoryLendVOList);
        json.setIsSuccess(1);
        return json.toString();
    }
}
