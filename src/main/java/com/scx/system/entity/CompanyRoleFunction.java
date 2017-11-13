package com.scx.system.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.web.system.pojo.base.TSFunction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色--权限
 */
@Entity
@Table(name = "sys_company_role_function")
public class CompanyRoleFunction implements Serializable {

    private String id;

    private String operation;

    private String dataRule;

    private CompanyRole TSRole;

    private TSFunction TSFunction;

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "operation", length = 100)
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Column(name = "datarule", length = 100)
    public String getDataRule() {
        return dataRule;
    }

    public void setDataRule(String dataRule) {
        this.dataRule = dataRule;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleid")
    public CompanyRole getTSRole() {
        return TSRole;
    }

    public void setTSRole(CompanyRole CompanyRole) {
        this.TSRole = CompanyRole;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "functionid")
    public TSFunction getTSFunction() {
        return TSFunction;
    }

    public void setTSFunction(TSFunction TSFunction) {
        this.TSFunction = TSFunction;
    }

    @Override
    public String toString() {
        return "CompanyRoleFunction{" +
                "id='" + id + '\'' +
                ", operation='" + operation + '\'' +
                ", dataRule='" + dataRule + '\'' +
                ", CompanyRole=" + TSRole +
                ", TSFunction=" + TSFunction +
                '}';
    }
}
