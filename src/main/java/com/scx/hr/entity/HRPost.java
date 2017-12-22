package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 岗位信息
 */
@Entity
@Table(name = "hr_post")
public class HRPost implements Serializable {

    private String id;
    private String postName;
    private String companyId;

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

    @Column(name = "name", nullable = false, length = 20)
    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @Column(name = "company_id", nullable = false, length = 36)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "HRPost{" +
                "id='" + id + '\'' +
                ", postName='" + postName + '\'' +
                ", companyId='" + companyId + '\'' +
                '}';
    }
}
