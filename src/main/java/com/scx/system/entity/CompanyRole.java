package com.scx.system.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 公司--角色表
 */
@Entity
@Table(name = "sys_company_role")
public class CompanyRole implements Serializable {

    private String id;

    private String companyId;//公司id

    private String roleName;//角色名称

    private String roleCode;//角色编码

    private CompanyRole parentRole;//上级角色

    private List<CompanyRole> childRoles;//下级角色

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

    @Column(name = "COMPANY_ID", nullable = true, length = 36)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "role_name", nullable = false, length = 20)
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(name = "role_code", length = 10)
    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "P_ID")
    public CompanyRole getParentRole() {
        return parentRole;
    }

    public void setParentRole(CompanyRole parentRole) {
        this.parentRole = parentRole;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentRole")
    public List<CompanyRole> getChildRoles() {
        return childRoles;
    }

    public void setChildRoles(List<CompanyRole> childRoles) {
        this.childRoles = childRoles;
    }

    @Override
    public String toString() {
        return "CompanyRole{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", childRoles=" + childRoles +
                '}';
    }
}