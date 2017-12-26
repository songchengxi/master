package com.scx.hr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 员工社保设置
 */
@Entity
@Table(name = "hr_user_social")
public class HRUserSocial implements Serializable {

    private String userId;
    private String socialStart;//起缴月
    private Double socialBase;//社保缴费基数
    private Double socialComVal;//社保公司缴费数额
    private Double socialUserVal;//社保个人缴纳数额
    private Double fundBase;//公积金缴费基数
    private Double fundComVal;//公积金公司缴纳数额
    private Double fundUserVal;//公积金个人缴纳数额
    private Short deleteFlag;//删除标记   0：存在；1：已删除

    @Id
    @Column(name = "USER_ID", nullable = true, length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "SOCIAL_START", nullable = true, length = 10)
    public String getSocialStart() {
        return socialStart;
    }

    public void setSocialStart(String socialStart) {
        this.socialStart = socialStart;
    }

    @Column(name = "SOCIAL_BASE", nullable = true)
    public Double getSocialBase() {
        return socialBase;
    }

    public void setSocialBase(Double socialBase) {
        this.socialBase = socialBase;
    }

    @Column(name = "SOCIAL_COM_VAL", nullable = true)
    public Double getSocialComVal() {
        return socialComVal;
    }

    public void setSocialComVal(Double socialComVal) {
        this.socialComVal = socialComVal;
    }

    @Column(name = "SOCIAL_USER_VAL", nullable = true)
    public Double getSocialUserVal() {
        return socialUserVal;
    }

    public void setSocialUserVal(Double socialUserVal) {
        this.socialUserVal = socialUserVal;
    }

    @Column(name = "FUND_BASE", nullable = true)
    public Double getFundBase() {
        return fundBase;
    }

    public void setFundBase(Double fundBase) {
        this.fundBase = fundBase;
    }

    @Column(name = "FUND_COM_VAL", nullable = true)
    public Double getFundComVal() {
        return fundComVal;
    }

    public void setFundComVal(Double fundComVal) {
        this.fundComVal = fundComVal;
    }

    @Column(name = "FUND_USER_VAL", nullable = true)
    public Double getFundUserVal() {
        return fundUserVal;
    }

    public void setFundUserVal(Double fundUserVal) {
        this.fundUserVal = fundUserVal;
    }

    @Column(name = "delete_flag")
    public Short getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "HRUserSocial{" +
                "userId='" + userId + '\'' +
                ", socialStart=" + socialStart +
                ", socialBase=" + socialBase +
                ", socialComVal=" + socialComVal +
                ", socialUserVal=" + socialUserVal +
                ", fundBase=" + fundBase +
                ", fundComVal=" + fundComVal +
                ", fundUserVal=" + fundUserVal +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
