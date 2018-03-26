package com.arms.appserver.vo;

/**
 * @author liuchen
 * @since 2017/12/6
 */
public class TerraceListVO {

    private int terraceId;

    private String logo;

    private String name;

    private double rate;

    private int lendType;

    private int quickestTime;

    private int maxAmount;

    private int minAmount;

    private int applyForCount;

    private int successCount;

    private int successRate;

    private String url;

    private String quickestTimeStr;

    private String rateStr;

    public int getTerraceId() {
        return terraceId;
    }

    public void setTerraceId(int terraceId) {
        this.terraceId = terraceId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLendType() {
        return lendType;
    }

    public void setLendType(int lendType) {
        this.lendType = lendType;
    }

    public int getQuickestTime() {
        return quickestTime;
    }

    public void setQuickestTime(int quickestTime) {
        this.quickestTime = quickestTime;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getApplyForCount() {
        return applyForCount;
    }

    public void setApplyForCount(int applyForCount) {
        this.applyForCount = applyForCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuickestTimeStr() {
        return quickestTimeStr;
    }

    public void setQuickestTimeStr(String quickestTimeStr) {
        this.quickestTimeStr = quickestTimeStr;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }
}
