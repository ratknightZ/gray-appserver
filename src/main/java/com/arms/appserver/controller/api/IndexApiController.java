package com.arms.appserver.controller.api;

import com.arms.appserver.vo.BannerVO;
import com.arms.common.vo.JsonVO;
import com.arms.service.model.Banner;
import com.arms.service.service.BannerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinxuan
 * @since 2017/11/22
 */
@Controller
public class IndexApiController {

    @Resource
    private BannerService bannerService;

    private static final String TOP_BANNER_GROUP_NAME = "index_top_banner";

    @ResponseBody
    @RequestMapping(value = "/api/index/get_top_banner_list", method = RequestMethod.GET)
    public String getTopBannerList() {
        JsonVO json = new JsonVO();
        List<BannerVO> bannerVOList = new ArrayList<>();
        List<Banner> topBannerList = bannerService.getByGroup(TOP_BANNER_GROUP_NAME);
        if (topBannerList != null && topBannerList.size() > 0) {
            for (Banner banner : topBannerList) {
                if (banner == null) {
                    continue;
                }
                BannerVO bannerVO = new BannerVO();
                bannerVO.setId(banner.getId());
                bannerVO.setBannerName(banner.getBannerName());
                bannerVO.setRedirectType(banner.getRedirectType());
                bannerVO.setHoverTip(banner.getHoverTip());
                bannerVO.setPicUrl(banner.getPicUrl());
                bannerVO.setLinkUrl(banner.getLinkUrl());
                bannerVO.setGroupName(banner.getGroupName());
                bannerVO.setDescription(banner.getDescription());
                bannerVOList.add(bannerVO);
            }
        }
        json.setData(bannerVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

}
