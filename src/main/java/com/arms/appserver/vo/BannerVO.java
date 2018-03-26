package com.arms.appserver.vo;

/**
 * @author jinxuan
 * @since 2017/11/22
 */
public class BannerVO {

    private int    id;

    private String bannerName;

    private int    redirectType;

    private String hoverTip;

    private String picUrl;

    private String linkUrl;

    private String groupName;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public String getHoverTip() {
        return hoverTip;
    }

    public void setHoverTip(String hoverTip) {
        this.hoverTip = hoverTip;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
