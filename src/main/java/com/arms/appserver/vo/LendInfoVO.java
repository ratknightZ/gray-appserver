package com.arms.appserver.vo;

/**
 * @author liuchen
 * @since 2017/12/7
 */
public class LendInfoVO {

    private int userId;

    private String name;

    private String idCard;

    private String lendPurpose;

    private double applyForAmount;

    private int timeLimit;

    private String profession;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLendPurpose() {
        return lendPurpose;
    }

    public void setLendPurpose(String lendPurpose) {
        this.lendPurpose = lendPurpose;
    }

    public double getApplyForAmount() {
        return applyForAmount;
    }

    public void setApplyForAmount(double applyForAmount) {
        this.applyForAmount = applyForAmount;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
