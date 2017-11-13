package com.scx.system.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.web.system.pojo.base.TSUser;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 公司角色--用户
 */
@Entity
@Table(name = "sys_company_role_user")
public class CompanyRoleUser implements Serializable {

    private String id;

    private CompanyRole TSRole;

    private TSUser TSUser;

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleid")
    public CompanyRole getTSRole() {
        return TSRole;
    }

    public void setTSRole(CompanyRole Role) {
        this.TSRole = Role;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    public TSUser getTSUser() {
        return TSUser;
    }

    public void setTSUser(TSUser TSUser) {
        this.TSUser = TSUser;
    }

    @Override
    public String toString() {
        return "CompanyRoleUser{" +
                "id='" + id + '\'' +
                ", TSRole=" + TSRole +
                ", TSUser=" + TSUser +
                '}';
    }
}
