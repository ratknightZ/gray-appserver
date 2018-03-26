package com.arms.appserver.vo.console;

/**
 * @author liuchen
 * @since 2018/3/16
 */
public class ListOpinionVO {

    private int opinionId;

    private String theme;

    private String detail;

    private int userId;

    private String email;

    private int status;

    private String cellphone;

    private String createTime;

    public int getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(int opinionId) {
        this.opinionId = opinionId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
