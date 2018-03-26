package com.arms.appserver.vo;

/**
 * @author liuchen
 * @since 2017/12/6
 */
public class TerraceDetailVO {

    private int terraceId;

    private String logo;

    private String name;

    private double rate;

    private int lendType;

    private int quickestTime;

    private int maxAmount;

    private int minAmount;

    private String applyForCondition;

    private String needInformation;

    private String choosableRepaymentTimeLimit;

    private String url;

    private int applyForCount;

    private int successCount;

    private int successRate;

    private String quickestTimeStr;

    private String rateStr;

    private int unitOfRepaymentTime;

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

    public int getLendType() {
        return lendType;
    }

    public void setLendType(int lendType) {
        this.lendType = lendType;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
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

    public String getApplyForCondition() {
        return applyForCondition;
    }

    public void setApplyForCondition(String applyForCondition) {
        this.applyForCondition = applyForCondition;
    }

    public String getNeedInformation() {
        return needInformation;
    }

    public void setNeedInformation(String needInformation) {
        this.needInformation = needInformation;
    }

    public String getChoosableRepaymentTimeLimit() {
        return choosableRepaymentTimeLimit;
    }

    public void setChoosableRepaymentTimeLimit(String choosableRepaymentTimeLimit) {
        this.choosableRepaymentTimeLimit = choosableRepaymentTimeLimit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getUnitOfRepaymentTime() {
        return unitOfRepaymentTime;
    }

    public void setUnitOfRepaymentTime(int unitOfRepaymentTime) {
        this.unitOfRepaymentTime = unitOfRepaymentTime;
    }
}
