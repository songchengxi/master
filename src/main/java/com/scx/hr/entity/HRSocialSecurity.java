package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 社保信息
 */
@Entity
@Table(name = "hr_social_security")
public class HRSocialSecurity implements Serializable {

    private String id;
    private String name;
    private String code;
    private String isSys;//是否系统 N：不是  Y：是
    private String companyId;//公司
    private String status;//是否启用  N：关闭；Y：开启

    private HRSocialSecurity parent;
    private List<HRSocialSecurity> childs;

    private Double base;//缴纳基数
    private Double companyProportion;//公司缴纳比例
    private Double userProportion;//个人缴纳比例
    private Double companyVal;//公司缴纳数额
    private Double userVal;//个人缴纳数额

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

    @Column(name = "NAME", nullable = true, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CODE", nullable = true, length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "IS_SYS", nullable = true, length = 1)
    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    @Column(name = "COMPANY_ID", nullable = true, length = 36)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "STATUS", nullable = true, length = 1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public HRSocialSecurity getParent() {
        return parent;
    }

    public void setParent(HRSocialSecurity parent) {
        this.parent = parent;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
    public List<HRSocialSecurity> getChilds() {
        return childs;
    }

    public void setChilds(List<HRSocialSecurity> childs) {
        this.childs = childs;
    }

    @Column(name = "BASE", nullable = true)
    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    @Column(name = "COMPANY_PRO", nullable = true)
    public Double getCompanyProportion() {
        return companyProportion;
    }

    public void setCompanyProportion(Double companyProportion) {
        this.companyProportion = companyProportion;
    }

    @Column(name = "USER_PRO", nullable = true)
    public Double getUserProportion() {
        return userProportion;
    }

    public void setUserProportion(Double userProportion) {
        this.userProportion = userProportion;
    }

    @Column(name = "COMPANY_VAL", nullable = true)
    public Double getCompanyVal() {
        return companyVal;
    }

    public void setCompanyVal(Double companyVal) {
        this.companyVal = companyVal;
    }

    @Column(name = "USER_VAL", nullable = true)
    public Double getUserVal() {
        return userVal;
    }

    public void setUserVal(Double userVal) {
        this.userVal = userVal;
    }

    @Override
    public String toString() {
        return "HRSocialSecurity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isSys='" + isSys + '\'' +
                ", companyId='" + companyId + '\'' +
                ", status='" + status + '\'' +
                ", childs=" + childs +
                ", base=" + base +
                ", companyProportion=" + companyProportion +
                ", userProportion=" + userProportion +
                ", companyVal=" + companyVal +
                ", userVal=" + userVal +
                '}';
    }
}
