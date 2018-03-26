package com.arms.appserver.controller.console;

import com.arms.appserver.vo.console.ListBannerVO;
import com.arms.appserver.vo.console.UpdateBannerVO;
import com.arms.common.util.PageUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.annotation.Sniff;
import com.arms.service.enums.BannerRedirectType;
import com.arms.service.exception.BannerException;
import com.arms.service.model.Banner;
import com.arms.service.service.BannerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchen
 * @since 2017/12/20
 */
@Controller
public class BannerController {

    @Resource
    private BannerService bannerService;

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/banner/add_page",method = RequestMethod.GET)
    public String addPage(){
        return "page/console/add-banner";
    }

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/banner/add",method = RequestMethod.POST)
    public String add(@RequestParam String bannerName,
                      @RequestParam int redirectType,
                      @RequestParam(required = false,defaultValue = "") String hoverTip,
                      @RequestParam String picUrl,
                      @RequestParam String linkUrl,
                      @RequestParam(required = false,defaultValue = "") String groupName,
                      @RequestParam(required = false,defaultValue = "") String description){
        JsonVO json = new JsonVO();
        if (StringUtils.isBlank(bannerName)){
            json.setIsSuccess(0);
            json.setMsg("banner名称不能为空");
            return json.toString();
        }
        if (BannerRedirectType.getType(redirectType) == null){
            json.setIsSuccess(0);
            json.setMsg("请选择正确的跳转类型");
            json.setMsgForDebug("非法参数，redirectType: " + redirectType);
            return json.toString();
        }
        if (StringUtils.isBlank(picUrl)){
            json.setIsSuccess(0);
            json.setMsg("图片不能为空");
            return json.toString();
        }
        if (StringUtils.isBlank(linkUrl)){
            json.setIsSuccess(0);
            json.setMsg("跳转地址不能为空");
            return json.toString();
        }
        Banner banner = new Banner();
        banner.setBannerName(bannerName);
        banner.setRedirectType(redirectType);
        banner.setHoverTip(hoverTip);
        banner.setPicUrl(picUrl);
        banner.setLinkUrl(linkUrl);
        banner.setGroupName(groupName);
        banner.setDescription(description);
        try {
            bannerService.add(banner);
        } catch (BannerException e) {
            json.setIsSuccess(0);
            json.setMsg(e.getMessage());
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/banner/list_page",method = RequestMethod.GET)
    public String listPage(Model model,
                           @RequestParam(required = false,defaultValue = "1") int page,
                           @RequestParam(required = false,defaultValue = "12") int pageSize){
        int totalRoomNum = bannerService.countAll();
        int pageCount = PageUtil.calcPageTotal(totalRoomNum,pageSize);
        List<Banner> bannerList = bannerService.getList(page,pageSize);
        List<ListBannerVO> listBannerVOList = new ArrayList<>();
        for (Banner banner : bannerList){
            if (banner == null){
                continue;
            }
            ListBannerVO listBannerVO = new ListBannerVO();
            listBannerVO.setBannerId(banner.getId());
            listBannerVO.setGroupName(banner.getGroupName());
            listBannerVO.setBannerName(banner.getBannerName());
            listBannerVO.setPicUrl(banner.getPicUrl());
            listBannerVOList.add(listBannerVO);
        }
        model.addAttribute("listBannerVOList",listBannerVOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageCount);
        return "page/console/banner-list";
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/banner/update_page",method = RequestMethod.GET)
    public String updatePage(Model model, @RequestParam int bannerId){
        Banner banner = bannerService.get(bannerId);
        if (banner != null){
            UpdateBannerVO updateBannerVO = new UpdateBannerVO();
            updateBannerVO.setBannerId(banner.getId());
            updateBannerVO.setBannerName(banner.getBannerName());
            updateBannerVO.setGroupName(banner.getGroupName());
            updateBannerVO.setPicUrl(banner.getPicUrl());
            updateBannerVO.setLinkUrl(banner.getLinkUrl());
            updateBannerVO.setHoverTip(banner.getHoverTip());
            updateBannerVO.setRedirectType(banner.getRedirectType());
            updateBannerVO.setDescription(banner.getDescription());
            model.addAttribute("updateBannerVO",updateBannerVO);
        }
        return "page/console/update-banner";
    }

    @ResponseBody
    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/banner/update",method = RequestMethod.POST)
    public String update(@RequestParam int bannerId,
                         @RequestParam String bannerName,
                         @RequestParam int redirectType,
                         @RequestParam(required = false,defaultValue = "") String hoverTip,
                         @RequestParam String picUrl,
                         @RequestParam String linkUrl,
                         @RequestParam(required = false,defaultValue = "") String groupName,
                         @RequestParam(required = false,defaultValue = "") String description){
        JsonVO json = new JsonVO();
        Banner banner = bannerService.get(bannerId);
        if(banner == null){
            json.setIsSuccess(0);
            json.setMsg("房间不存在");
            return json.toString();
        }
        if (StringUtils.isNotBlank(bannerName)){
            banner.setBannerName(bannerName);
        }
        if (BannerRedirectType.getType(redirectType) == null){
            banner.setRedirectType(redirectType);
        }
        if (StringUtils.isNotBlank(hoverTip)){
            banner.setHoverTip(hoverTip);
        }
        if (StringUtils.isNotBlank(picUrl)){
            banner.setPicUrl(picUrl);
        }
        if (StringUtils.isNotBlank(linkUrl)){
            banner.setLinkUrl(linkUrl);
        }
        if (StringUtils.isNotBlank(groupName)){
            banner.setGroupName(groupName);
        }
        if (StringUtils.isNotBlank(description)){
            banner.setDescription(description);
        }
        try {
            bannerService.update(banner);
        } catch (BannerException e) {
            json.setIsSuccess(1);
            json.setMsg(e.getMessage());
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }

    @Sniff("ROLE_PRODUCT")
    @RequestMapping(value = "/banner/delete",method = RequestMethod.GET)
    public String delete(@RequestParam int bannerId) throws BannerException {
        bannerService.delete(bannerId);
        return "redirect:/banner/list_page";
    }
}
