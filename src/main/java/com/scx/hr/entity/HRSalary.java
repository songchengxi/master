package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 薪酬信息
 */
@Entity
@Table(name = "hr_salary")
public class HRSalary implements Serializable {

    private String id;
    private String name;
    private String code;
    private String isSys;//是否系统 N：不是  Y：是
    private String companyId;//公司
    private String status;//是否启用  N：关闭；Y：开启

    private HRSalary parent;
    private List<HRSalary> childs;

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

    @Column(name = "NAME", nullable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CODE", nullable = true, length = 20)
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

    @Column(name = "company_id", nullable = true, length = 36)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "status", nullable = true, length = 1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public HRSalary getParent() {
        return parent;
    }

    public void setParent(HRSalary parent) {
        this.parent = parent;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
    public List<HRSalary> getChilds() {
        return childs;
    }

    public void setChilds(List<HRSalary> childs) {
        this.childs = childs;
    }

    @Override
    public String toString() {
        return "HRSalary{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isSys='" + isSys + '\'' +
                ", companyId='" + companyId + '\'' +
                ", status='" + status + '\'' +
                ", childs=" + childs +
                '}';
    }

    private Double value;

    private Double probation;//试用期工资

    @Transient
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Transient
    public Double getProbation() {
        return probation;
    }

    public void setProbation(Double probation) {
        this.probation = probation;
    }
}
