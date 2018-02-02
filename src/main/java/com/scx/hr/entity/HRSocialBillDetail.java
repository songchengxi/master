package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */
@Entity
@Table(name = "hr_social_bill_detail")
public class HRSocialBillDetail implements Serializable {

    private String id;
    private String companyId;//公司
    private String userId;
    private String month;
    private Double socialComVal;//社保公司缴费数额
    private Double socialUserVal;//社保个人缴纳数额
    private Double fundComVal;//公积金公司缴纳数额
    private Double fundUserVal;//公积金个人缴纳数额

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "id", nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "company_id", nullable = true, length = 36)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "user_id", nullable = false, length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "month", nullable = false, length = 10)
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Column(name = "social_com_val", nullable = false)
    public Double getSocialComVal() {
        return socialComVal;
    }

    public void setSocialComVal(Double socialComVal) {
        this.socialComVal = socialComVal;
    }

    @Column(name = "social_user_val", nullable = false)
    public Double getSocialUserVal() {
        return socialUserVal;
    }

    public void setSocialUserVal(Double socialUserVal) {
        this.socialUserVal = socialUserVal;
    }

    @Column(name = "fund_com_val", nullable = false)
    public Double getFundComVal() {
        return fundComVal;
    }

    public void setFundComVal(Double fundComVal) {
        this.fundComVal = fundComVal;
    }

    @Column(name = "fund_user_val", nullable = false)
    public Double getFundUserVal() {
        return fundUserVal;
    }

    public void setFundUserVal(Double fundUserVal) {
        this.fundUserVal = fundUserVal;
    }

    @Override
    public String toString() {
        return "HRSocialBillDetail{" +
                "id='" + id +
                ", companyId='" + companyId +
                ", userId='" + userId +
                ", month='" + month +
                ", socialComVal=" + socialComVal +
                ", socialUserVal=" + socialUserVal +
                ", fundComVal=" + fundComVal +
                ", fundUserVal=" + fundUserVal +
                '}';
    }
}
