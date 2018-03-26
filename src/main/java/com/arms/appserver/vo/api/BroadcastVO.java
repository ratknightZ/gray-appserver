package com.arms.appserver.vo.api;

/**
 * @author liuchen
 * @since 2017/12/21
 */
public class BroadcastVO {

    private String terraceName;

    private String cellphone;

    private double amount;

    private String time;

    public String getTerraceName() {
        return terraceName;
    }

    public void setTerraceName(String terraceName) {
        this.terraceName = terraceName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
