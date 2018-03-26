package com.arms.appserver.vo.console;

/**
 * @author liuchen
 * @since 2017/12/21
 */
public class ListTerraceVO {

    private int terraceId;

    private String logo;

    private String terraceName;

    private int lendType;

    private int maxAmount;

    private int minAmount;

    private int applyForCount;

    private int successCount;

    private int status;

    private int independentUserApplyForCount;

    private int yesterdayIndependentUserApplyForCount;

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

    public String getTerraceName() {
        return terraceName;
    }

    public void setTerraceName(String terraceName) {
        this.terraceName = terraceName;
    }

    public int getLendType() {
        return lendType;
    }

    public void setLendType(int lendType) {
        this.lendType = lendType;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIndependentUserApplyForCount() {
        return independentUserApplyForCount;
    }

    public void setIndependentUserApplyForCount(int independentUserApplyForCount) {
        this.independentUserApplyForCount = independentUserApplyForCount;
    }

    public int getYesterdayIndependentUserApplyForCount() {
        return yesterdayIndependentUserApplyForCount;
    }

    public void setYesterdayIndependentUserApplyForCount(int yesterdayIndependentUserApplyForCount) {
        this.yesterdayIndependentUserApplyForCount = yesterdayIndependentUserApplyForCount;
    }
}
