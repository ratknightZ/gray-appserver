package com.arms.appserver.vo.console;

import com.arms.service.model.TerraceShowChannelR;
import com.arms.service.model.TerraceTypeR;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author liuchen
 * @since 2017/12/5
 */
public class UpdateTerraceVO {

    private int terraceId;

    private String logo;

    private String name;

    private double rate;

    private int lendType;

    private double quickestTime;

    private String applyForCondition;

    private String needInformation;

    private int repaymentWay;

    private int haveCreditCard;

    private int minRepaymentTimeLimit;

    private int maxRepaymentTimeLimit;

    private String choosableRepaymentTimeLimit;

    private int maxAmount;

    private int minAmount;

    private int status;

    private String[] professionArray;

    private String[] creditArray;

    private String[] lendPurposeArray;

    private String url;

    private int applyForCount;

    private int successCount;

    private int successRate;

    private int isHot;

    private List<TerraceTypeR> terraceTypeRList;

    private List<TerraceShowChannelR> terraceShowChannelRList;

    private int unitOfQuickestTime;

    private int unitOfRepaymentTime;

    private int unitOfRate;

    private int sortCode;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getTerraceId() {
        return terraceId;
    }

    public void setTerraceId(int terraceId) {
        this.terraceId = terraceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getLendType() {
        return lendType;
    }

    public void setLendType(int lendType) {
        this.lendType = lendType;
    }

    public double getQuickestTime() {
        return quickestTime;
    }

    public void setQuickestTime(double quickestTime) {
        this.quickestTime = quickestTime;
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

    public int getRepaymentWay() {
        return repaymentWay;
    }

    public void setRepaymentWay(int repaymentWay) {
        this.repaymentWay = repaymentWay;
    }

    public int getHaveCreditCard() {
        return haveCreditCard;
    }

    public void setHaveCreditCard(int haveCreditCard) {
        this.haveCreditCard = haveCreditCard;
    }

    public int getMinRepaymentTimeLimit() {
        return minRepaymentTimeLimit;
    }

    public void setMinRepaymentTimeLimit(int minRepaymentTimeLimit) {
        this.minRepaymentTimeLimit = minRepaymentTimeLimit;
    }

    public int getMaxRepaymentTimeLimit() {
        return maxRepaymentTimeLimit;
    }

    public void setMaxRepaymentTimeLimit(int maxRepaymentTimeLimit) {
        this.maxRepaymentTimeLimit = maxRepaymentTimeLimit;
    }

    public String getChoosableRepaymentTimeLimit() {
        return choosableRepaymentTimeLimit;
    }

    public void setChoosableRepaymentTimeLimit(String choosableRepaymentTimeLimit) {
        this.choosableRepaymentTimeLimit = choosableRepaymentTimeLimit;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getProfessionArray() {
        return professionArray;
    }

    public void setProfessionArray(String[] professionArray) {
        this.professionArray = professionArray;
    }

    public String[] getCreditArray() {
        return creditArray;
    }

    public void setCreditArray(String[] creditArray) {
        this.creditArray = creditArray;
    }

    public String[] getLendPurposeArray() {
        return lendPurposeArray;
    }

    public void setLendPurposeArray(String[] lendPurposeArray) {
        this.lendPurposeArray = lendPurposeArray;
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

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public List<TerraceTypeR> getTerraceTypeRList() {
        return terraceTypeRList;
    }

    public void setTerraceTypeRList(List<TerraceTypeR> terraceTypeRList) {
        this.terraceTypeRList = terraceTypeRList;
    }

    public List<TerraceShowChannelR> getTerraceShowChannelRList() {
        return terraceShowChannelRList;
    }

    public void setTerraceShowChannelRList(List<TerraceShowChannelR> terraceShowChannelRList) {
        this.terraceShowChannelRList = terraceShowChannelRList;
    }

    public int getUnitOfQuickestTime() {
        return unitOfQuickestTime;
    }

    public void setUnitOfQuickestTime(int unitOfQuickestTime) {
        this.unitOfQuickestTime = unitOfQuickestTime;
    }

    public int getUnitOfRepaymentTime() {
        return unitOfRepaymentTime;
    }

    public void setUnitOfRepaymentTime(int unitOfRepaymentTime) {
        this.unitOfRepaymentTime = unitOfRepaymentTime;
    }

    public int getUnitOfRate() {
        return unitOfRate;
    }

    public void setUnitOfRate(int unitOfRate) {
        this.unitOfRate = unitOfRate;
    }

    public int getSortCode() {
        return sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }
}
