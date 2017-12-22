package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.web.system.pojo.base.TSDepart;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 员工--部门
 */
@Entity
@Table(name = "hr_user_org")
public class HRUserOrg implements Serializable {

    private String id;
    private HRUser user;
    private TSDepart tsDepart;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public HRUser getUser() {
        return user;
    }

    public void setUser(HRUser user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id")
    public TSDepart getTsDepart() {
        return tsDepart;
    }

    public void setTsDepart(TSDepart tsDepart) {
        this.tsDepart = tsDepart;
    }

    @Override
    public String toString() {
        return "HRUserOrg{" +
                "id='" + id + '\'' +
                ", user=" + user.getName() +
                ", tsDepart=" + tsDepart +
                '}';
    }
}
