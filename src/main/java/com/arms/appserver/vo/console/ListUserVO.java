package com.arms.appserver.vo.console;

/**
 * @author liuchen
 * @since 2017/12/29
 */
public class ListUserVO {

    private int userId;

    private String cellphone;

    private String createTime;

    private String source;

    private String ip;

    private int countIp;

    private int countLendInfo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getCountIp() {
        return countIp;
    }

    public void setCountIp(int countIp) {
        this.countIp = countIp;
    }

    public int getCountLendInfo() {
        return countLendInfo;
    }

    public void setCountLendInfo(int countLendInfo) {
        this.countLendInfo = countLendInfo;
    }
}
