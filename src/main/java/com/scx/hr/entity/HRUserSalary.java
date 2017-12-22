package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 员工薪酬信息
 */
@Entity
@Table(name = "hr_user_salary")
public class HRUserSalary implements Serializable {

    private String id;
    private String userId;
    private String salaryId;
    private Double value;

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

    @Column(name = "user_id", nullable = true, length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "salary_id", nullable = true, length = 36)
    public String getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(String salaryId) {
        this.salaryId = salaryId;
    }

    @Column(name = "value", nullable = true)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HRUserSalary{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", salaryId='" + salaryId + '\'' +
                ", value=" + value +
                '}';
    }
}
