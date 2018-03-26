package com.arms.appserver.vo;

/**
 * Created by Administrator on 2017/11/27.
 */
public class GoldAccountVO {

    private long goldAccountId;

    private int userId;

    private int value;

    private int type;

    private String typeReferencePrimaryKey;

    private String sketch;

    private String createTime;

    public long getGoldAccountId() {
        return goldAccountId;
    }

    public void setGoldAccountId(long goldAccountId) {
        this.goldAccountId = goldAccountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeReferencePrimaryKey() {
        return typeReferencePrimaryKey;
    }

    public void setTypeReferencePrimaryKey(String typeReferencePrimaryKey) {
        this.typeReferencePrimaryKey = typeReferencePrimaryKey;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
