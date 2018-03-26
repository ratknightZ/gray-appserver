package com.arms.appserver.vo.console;

/**
 * @author liuchen
 * @since 2017/12/25
 */
public class ListLendInfoVO {

    private long lendInfoId;

    private String source;

    private String terraceName;

    private int terraceId;

    private int userId;

    private String name;

    private String idCard;

    private String lendPurpose;

    private double applyForAmount;

    private int timeLimit;

    private String profession;

    private String cellphone;

    private String time;

    public long getLendInfoId() {
        return lendInfoId;
    }

    public void setLendInfoId(long lendInfoId) {
        this.lendInfoId = lendInfoId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTerraceName() {
        return terraceName;
    }

    public void setTerraceName(String terraceName) {
        this.terraceName = terraceName;
    }

    public int getTerraceId() {
        return terraceId;
    }

    public void setTerraceId(int terraceId) {
        this.terraceId = terraceId;
    }

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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
