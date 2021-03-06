package org.jeecgframework.web.system.pojo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 部门机构表
 */
@Entity
@Table(name = "t_s_depart")
public class TSDepart implements Serializable {

    private String id;

    private String companyid;//公司

    private TSDepart TSPDepart;//上级部门

    @Excel(name = "部门名称", width = 20)
    private String departname;//部门名称

    @Excel(name = "部门描述", width = 20)
    private String description;//部门描述

    @Excel(name = "机构编码", width = 20)
    private String orgCode;//机构编码

    @Excel(name = "机构类型编码", width = 25)
    private String orgType;//机构编码

    @Excel(name = "电话", width = 20)
    private String mobile;//电话

    @Excel(name = "传真", width = 20)
    private String fax;//传真

    @Excel(name = "地址", width = 20)
    private String address;//地址

    private String departOrder;//排序

    private List<TSDepart> TSDeparts = new ArrayList<TSDepart>();//下属部门

    @Id
    @Column(name = "ID", nullable = true, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "companyid", length = 32)
    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentdepartid")
    public TSDepart getTSPDepart() {
        return this.TSPDepart;
    }

    public void setTSPDepart(TSDepart TSPDepart) {
        this.TSPDepart = TSPDepart;
    }

    @Column(name = "departname", nullable = false, length = 100)
    public String getDepartname() {
        return this.departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TSPDepart")
    public List<TSDepart> getTSDeparts() {
        return TSDeparts;
    }

    public void setTSDeparts(List<TSDepart> tSDeparts) {
        TSDeparts = tSDeparts;
    }

    @Column(name = "org_code", length = 64)
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Column(name = "org_type", length = 1)
    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    @Column(name = "mobile", length = 32)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "fax", length = 32)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "address", length = 100)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "depart_order")
    public String getDepartOrder() {
        return departOrder;
    }

    public void setDepartOrder(String departOrder) {
        this.departOrder = departOrder;
    }

    @Override
    public String toString() {
        return "TSDepart{" +
                "id='" + id + '\'' +
                ", companyid=" + companyid +
                ", departname='" + departname + '\'' +
                ", description='" + description + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgType='" + orgType + '\'' +
                ", mobile='" + mobile + '\'' +
                ", fax='" + fax + '\'' +
                ", address='" + address + '\'' +
                ", departOrder='" + departOrder + '\'' +
                ", TSDeparts=" + TSDeparts +
                '}';
    }
}