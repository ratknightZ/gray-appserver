package com.arms.appserver.controller.console;

import com.arms.appserver.vo.console.ListTerraceVO;
import com.arms.appserver.vo.console.UpdateTerraceVO;
import com.arms.common.util.PageUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.annotation.Sniff;
import com.arms.service.enums.*;
import com.arms.service.exception.MessageException;
import com.arms.service.exception.TerraceException;
import com.arms.service.model.Message;
import com.arms.service.model.Terrace;
import com.arms.service.model.TerraceShowChannelR;
import com.arms.service.model.TerraceTypeR;
import com.arms.service.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchen
 * @since 2017/12/7
 */
@Controller
public class TerraceController {

    @Resource
    private TerraceService terraceService;

    @Resource
    private TerraceTypeRService terraceTypeRService;

    @Resource
    private MessageService messageService;

    @Resource
    private TerraceShowChannelRService terraceShowChannelRService;

    @Resource
    private LendInfoService lendInfoService;

    private static final Logger logger = LoggerFactory.getLogger(TerraceController.class);

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/terrace/add",method = RequestMethod.POST)
    public String add(Terrace terrace,@RequestParam String terraceTypes, @RequestParam String showChannels, String messageTitle){
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(terrace.getName())){
            json.setMsg("平台名称不能为空");
            json.setMsgForDebug("name不能为空");
            json.setIsSuccess(0);
            return json.toString();
        }
        if (LendType.getLendType(terrace.getLendType()) == null){
            json.setMsg("不存在的借贷类型");
            json.setMsgForDebug("参数错误,lendType: " + terrace.getLendType());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (RepaymentWay.getWay(terrace.getRepaymentWay()) == null){
            json.setMsg("不存在的还款方式");
            json.setMsgForDebug("参数错误,repaymentWay: " + terrace.getRepaymentWay());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (HaveCreditCard.getCard(terrace.getHaveCreditCard()) == null){
            json.setMsg("不存在的信用卡选择");
            json.setMsgForDebug("参数错误,haveCreditCard: " + terrace.getHaveCreditCard());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (StringUtils.isBlank(terraceTypes)){
            json.setMsg("平台类型不能为空");
            json.setMsgForDebug("参数错误,terraceTypes: " + terraceTypes);
            json.setIsSuccess(0);
            return json.toString();
        }
        if (StringUtils.isBlank(showChannels)){
            json.setMsg("显示渠道不能为空");
            json.setMsgForDebug("参数错误，showChannels: " + showChannels);
            json.setIsSuccess(0);
            return json.toString();
        }
        if (Status.getStatus(terrace.getStatus()) == null){
            json.setMsg("选择正确的上线状态");
            json.setMsgForDebug("参数错误,status: " + terrace.getStatus());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (IsHot.getIsHot(terrace.getIsHot()) == null){
            json.setMsg("选择正确的热点状态");
            json.setMsgForDebug("参数错误,isHot: " + terrace.getIsHot());
            json.setIsSuccess(0);
            return json.toString();
        }
        try {
            terraceService.add(terrace,terraceTypes,showChannels);
        } catch (TerraceException e) {
            json.setIsSuccess(0);
            json.setMsg("添加失败");
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        if (StringUtils.isNotBlank(messageTitle)){
            Message message = new Message();
            message.setFromUserId(0);
            message.setToUserId(0);
            message.setTitle(messageTitle);
            message.setContent(terrace.getId() + "");
            try {
                messageService.add(message);
            } catch (MessageException e) {
                logger.info("发送消息失败,messageTitle:" + messageTitle);
            }
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/terrace/update",method = RequestMethod.POST)
    public String update(Terrace terrace,String terraceTypes, @RequestParam String showChannels, String messageTitle){
        JsonVO json = new JsonVO();
        if (terrace.getId() <= 0){
            json.setMsg("平台ID错误");
            json.setMsgForDebug("参数错误，terraceId: " + terrace.getId());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (terrace.getName() != null && StringUtils.isBlank(terrace.getName())){
            json.setMsg("平台名称不能为空");
            json.setMsgForDebug("name不能为空");
            json.setIsSuccess(0);
            return json.toString();
        }
        if (terrace.getLendType() != 0 && LendType.getLendType(terrace.getLendType()) == null){
            json.setMsg("不存在的借贷类型");
            json.setMsgForDebug("参数错误,lendType: " + terrace.getLendType());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (terrace.getRepaymentWay() != 0 && RepaymentWay.getWay(terrace.getRepaymentWay()) == null){
            json.setMsg("不存在的还款方式");
            json.setMsgForDebug("参数错误,repaymentWay: " + terrace.getRepaymentWay());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (terrace.getHaveCreditCard() != 0 && HaveCreditCard.getCard(terrace.getHaveCreditCard()) == null){
            json.setMsg("不存在的信用卡选择");
            json.setMsgForDebug("参数错误,haveCreditCard: " + terrace.getHaveCreditCard());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (Status.getStatus(terrace.getStatus()) == null){
            json.setMsg("选择正确的上线状态");
            json.setMsgForDebug("参数错误,status: " + terrace.getStatus());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (IsHot.getIsHot(terrace.getIsHot()) == null){
            json.setMsg("选择正确的热点状态");
            json.setMsgForDebug("参数错误,isHot: " + terrace.getIsHot());
            json.setIsSuccess(0);
            return json.toString();
        }
        if (StringUtils.isNotBlank(messageTitle)){
            Message message = new Message();
            message.setFromUserId(0);
            message.setToUserId(0);
            message.setTitle(messageTitle);
            message.setContent(terrace.getId() + "");
            try {
                messageService.add(message);
            } catch (MessageException e) {
                logger.info("发送消息失败,messageTitle:" + messageTitle);
            }
        }

        try {
            terraceService.update(terrace,terraceTypes,showChannels);
        } catch (TerraceException e) {
            json.setMsg("更改失败");
            json.setMsgForDebug(e.getMessage());
            json.setIsSuccess(0);
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @RequestMapping(value = "/terrace/list_page",method = RequestMethod.GET)
    public String listPage(Model model, @RequestParam(required = false,defaultValue = "0") int status,
                           @RequestParam(required = false,defaultValue = "") String terraceName,
                           @RequestParam(required = false,defaultValue = "1") int page,
                           @RequestParam(required = false,defaultValue = "12") int pageSize){
        String terraceNameUtf8 = "";
        try {
            terraceNameUtf8 = new String(terraceName.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int totalRoomNum = terraceService.countByStatus(status,terraceNameUtf8);
        int pageCount = PageUtil.calcPageTotal(totalRoomNum,pageSize);
        List<Terrace> terraceList = terraceService.getListByStatus(status,terraceNameUtf8,page,pageSize);
        List<ListTerraceVO> listTerraceVOList = new ArrayList<>();
        for (Terrace terrace : terraceList){
            if (terrace == null){
                continue;
            }
            ListTerraceVO listTerraceVO = new ListTerraceVO();
            listTerraceVO.setLogo(terrace.getLogo());
            listTerraceVO.setTerraceName(terrace.getName());
            listTerraceVO.setLendType(terrace.getLendType());
            listTerraceVO.setMaxAmount(terrace.getMaxAmount());
            listTerraceVO.setMinAmount(terrace.getMinAmount());
            listTerraceVO.setTerraceId(terrace.getId());
            listTerraceVO.setApplyForCount(terrace.getApplyForCount());
            listTerraceVO.setSuccessCount(terrace.getSuccessCount());
            listTerraceVO.setStatus(terrace.getStatus());
            listTerraceVO.setIndependentUserApplyForCount(lendInfoService.getIndependentUserApplyForCount(terrace.getId()));
            listTerraceVO.setYesterdayIndependentUserApplyForCount(lendInfoService.getYesterdayIndependentUserApplyForCount(terrace.getId()));
            listTerraceVOList.add(listTerraceVO);
        }
        model.addAttribute("listTerraceVOList",listTerraceVOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("terraceName",terraceNameUtf8);
        return "page/console/terrace-list";
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/terrace/add_page",method = RequestMethod.GET)
    public String addPage(){
        return "page/console/add-terrace";
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/terrace/update_page",method = RequestMethod.GET)
    public String updatePage(Model model,@RequestParam int terraceId){
        Terrace terrace = terraceService.get(terraceId);
        if (terrace != null){
            UpdateTerraceVO updateTerraceVO = new UpdateTerraceVO();
            updateTerraceVO.setTerraceId(terrace.getId());
            updateTerraceVO.setLogo(terrace.getLogo());
            updateTerraceVO.setName(terrace.getName());
            updateTerraceVO.setUnitOfRate(terrace.getUnitOfRate());
            if (terrace.getUnitOfRate() == UnitOfRate.RATE_DAY.getValue()){
                updateTerraceVO.setRate(terrace.getRateDay());
            }else if (terrace.getUnitOfRate() == UnitOfRate.RATE_MONTH.getValue()){
                updateTerraceVO.setRate(terrace.getRate());
            }
            updateTerraceVO.setLendType(terrace.getLendType());
            updateTerraceVO.setApplyForCondition(terrace.getApplyForCondition());
            updateTerraceVO.setNeedInformation(terrace.getNeedInformation());
            updateTerraceVO.setRepaymentWay(terrace.getRepaymentWay());
            updateTerraceVO.setHaveCreditCard(terrace.getHaveCreditCard());
            updateTerraceVO.setMaxRepaymentTimeLimit(terrace.getMaxRepaymentTimeLimit());
            updateTerraceVO.setMinRepaymentTimeLimit(terrace.getMinRepaymentTimeLimit());
            updateTerraceVO.setChoosableRepaymentTimeLimit(terrace.getChoosableRepaymentTimeLimit());
            updateTerraceVO.setMinAmount(terrace.getMinAmount());
            updateTerraceVO.setMaxAmount(terrace.getMaxAmount());
            updateTerraceVO.setStatus(terrace.getStatus());
            updateTerraceVO.setProfessionArray(terrace.getProfession().split(","));
            updateTerraceVO.setCreditArray(terrace.getCredit().split(","));
            updateTerraceVO.setLendPurposeArray(terrace.getLendPurpose().split(","));
            updateTerraceVO.setUrl(terrace.getUrl());
            updateTerraceVO.setSuccessRate(terrace.getSuccessRate());
            updateTerraceVO.setIsHot(terrace.getIsHot());
            List<TerraceTypeR> terraceTypeRList = terraceTypeRService.getListByTerraceId(terrace.getId());
            updateTerraceVO.setTerraceTypeRList(terraceTypeRList);
            List<TerraceShowChannelR> terraceShowChannelRList = terraceShowChannelRService.getByTerraceId(terraceId);
            updateTerraceVO.setTerraceShowChannelRList(terraceShowChannelRList);
            updateTerraceVO.setUnitOfQuickestTime(terrace.getUnitOfQuickestTime());
            if (terrace.getUnitOfQuickestTime() == UnitOfQuickestTime.MINUTE.getValue()){
                updateTerraceVO.setQuickestTime(terrace.getQuickestTimeSecond() / 60);
            }else {
                updateTerraceVO.setQuickestTime(terrace.getQuickestTimeSecond());
            }
            updateTerraceVO.setUnitOfRepaymentTime(terrace.getUnitOfRepaymentTime());
            updateTerraceVO.setSortCode(terrace.getSortCode());
            model.addAttribute("updateTerraceVO",updateTerraceVO);
        }
        return "page/console/update-terrace";
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/terrace/delete",method = RequestMethod.GET)
    public String delete(@RequestParam int terraceId){
        terraceService.delete(terraceId);
        return "redirect:/terrace/list_page";
    }
}
