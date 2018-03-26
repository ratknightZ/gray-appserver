package com.arms.appserver.vo;

/**
 * @author liuchen
 * @since 2017/12/14
 */
public class TerraceNotifyVO {

    private int terraceNotifyId;

    private int terraceId;

    private String terraceUrl;

    private String terraceLogo;

    private double amount;

    private int timeLimit;

    private String createTime;

    public int getTerraceNotifyId() {
        return terraceNotifyId;
    }

    public void setTerraceNotifyId(int terraceNotifyId) {
        this.terraceNotifyId = terraceNotifyId;
    }

    public int getTerraceId() {
        return terraceId;
    }

    public void setTerraceId(int terraceId) {
        this.terraceId = terraceId;
    }

    public String getTerraceUrl() {
        return terraceUrl;
    }

    public void setTerraceUrl(String terraceUrl) {
        this.terraceUrl = terraceUrl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTerraceLogo() {
        return terraceLogo;
    }

    public void setTerraceLogo(String terraceLogo) {
        this.terraceLogo = terraceLogo;
    }
}
