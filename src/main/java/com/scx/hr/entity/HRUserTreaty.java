package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 合同信息
 */
@Entity
@Table(name = "hr_user_treaty")
public class HRUserTreaty implements Serializable {

    private String id;
    private String companyId;
    private String userId;
    // TODO: 2017/12/21
    private String userName;
    private String treatyType;//合同类型    1劳动合同;2劳务合同;3非全日制合同
    private String signType;//签订类型  1初次签订;2续签;3无固定期限签订
    private String treatyNo;//合同编号
    private String term;//期限
    private Date treatyStart;//开始日期
    private Date treatyEnd;//结束日期
    private Date signDate;//签订日期
    private String remark;//备注

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 36)
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

    @Column(name = "user_id", nullable = true, length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "user_name", nullable = true, length = 10)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "treaty_type", nullable = true, length = 2)
    public String getTreatyType() {
        return treatyType;
    }

    public void setTreatyType(String treatyType) {
        this.treatyType = treatyType;
    }

    @Column(name = "sign_type", nullable = true, length = 2)
    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Column(name = "treaty_no", nullable = true, length = 20)
    public String getTreatyNo() {
        return treatyNo;
    }

    public void setTreatyNo(String treatyNo) {
        this.treatyNo = treatyNo;
    }

    @Column(name = "term", nullable = true, length = 3)
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Column(name = "treaty_start", nullable = true)
    public Date getTreatyStart() {
        return treatyStart;
    }

    public void setTreatyStart(Date treatyStart) {
        this.treatyStart = treatyStart;
    }

    @Column(name = "treaty_end", nullable = true)
    public Date getTreatyEnd() {
        return treatyEnd;
    }

    public void setTreatyEnd(Date treatyEnd) {
        this.treatyEnd = treatyEnd;
    }

    @Column(name = "sign_date", nullable = true)
    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    @Column(name = "remark", nullable = true, length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "HRUserTreaty{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", treatyType='" + treatyType + '\'' +
                ", signType='" + signType + '\'' +
                ", treatyNo='" + treatyNo + '\'' +
                ", term='" + term + '\'' +
                ", treatyStart=" + treatyStart +
                ", treatyEnd=" + treatyEnd +
                ", signDate=" + signDate +
                ", remark='" + remark + '\'' +
                '}';
    }
}
