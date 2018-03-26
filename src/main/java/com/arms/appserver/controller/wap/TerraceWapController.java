package com.arms.appserver.controller.wap;

import com.arms.appserver.vo.TerraceListVO;
import com.arms.service.enums.ShowChannel;
import com.arms.service.enums.UnitOfQuickestTime;
import com.arms.service.enums.UnitOfRate;
import com.arms.service.model.Terrace;
import com.arms.service.service.TerraceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchen
 * @since 2018/3/1
 */
@Controller
public class TerraceWapController {

    @Resource
    private TerraceService terraceService;

    @RequestMapping(value = "/wap/terrace/list_page")
    public String listPage(Model model){
        List<Terrace> terraceList = terraceService.getList(0, ShowChannel.H5.getValue(),0,0,0,0,0,0,null,null,null,0,0);
        List<TerraceListVO> terraceListVOList = new ArrayList<>();
        for(Terrace terrace : terraceList){
            if (terrace == null){
                continue;
            }
            TerraceListVO terraceListVO = new TerraceListVO();
            terraceListVO.setTerraceId(terrace.getId());
            terraceListVO.setLogo(terrace.getLogo());
            terraceListVO.setName(terrace.getName());
            terraceListVO.setRate(terrace.getRate());
            terraceListVO.setLendType(terrace.getLendType());
            if (terrace.getQuickestTime() < 60){
                terraceListVO.setQuickestTime(1);
            }else {
                terraceListVO.setQuickestTime((int)terrace.getQuickestTime() / 60);
            }
            terraceListVO.setMaxAmount(terrace.getMaxAmount());
            terraceListVO.setMinAmount(terrace.getMinAmount());
            terraceListVO.setApplyForCount((terrace.getApplyForCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessCount((terrace.getSuccessCount() * 987) + (terrace.getId() * 3) + 11);
            terraceListVO.setSuccessRate(terrace.getSuccessRate());
            terraceListVO.setUrl(terrace.getUrl());
            if (terrace.getUnitOfQuickestTime() == UnitOfQuickestTime.MINUTE.getValue()){
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() / 60 + "分钟放款");
            }else {
                terraceListVO.setQuickestTimeStr(terrace.getQuickestTimeSecond() + "秒钟放款");
            }
            if (terrace.getUnitOfRate() == UnitOfRate.RATE_DAY.getValue()){
                terraceListVO.setRateStr(terrace.getRateDay() + "%/天");
            }else if (terrace.getUnitOfRate() == UnitOfRate.RATE_MONTH.getValue()){
                terraceListVO.setRateStr(terrace.getRate() + "%/月");
            }
            terraceListVOList.add(terraceListVO);
        }
        model.addAttribute("terraceListVOList",terraceListVOList);
        return "/page/app/hot";
    }
}
