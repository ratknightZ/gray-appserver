package com.arms.appserver.vo.api;

/**
 * @author liuchen
 * @since 2017/12/25
 */
public class TerraceHistoryLendVO {

    private double amount;

    private String cellphone;

    private String terraceName;

    private String createTime;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTerraceName() {
        return terraceName;
    }

    public void setTerraceName(String terraceName) {
        this.terraceName = terraceName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
