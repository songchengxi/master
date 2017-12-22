package com.scx.hr.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 请假信息
 */
@Entity
@Table(name = "hr_leave")
public class HRLeave implements Serializable {

    private String id;
    private String companyId;//公司
    // TODO: 2017/12/21  暂未保存部门信息，存在多个部门的情况
    private String departId;//部门id
    private String departName;//部门名称
    private String userId;//员工id
    // TODO: 2017/12/21     hr_user表，姓名有变动无法更改
    private String userName;//员工姓名
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private String leaveType;//请假类型
    private double duration;//时长
    private double dayNum;//天数
    private String reason;//事由

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

    @Column(name = "depart_id", nullable = true, length = 36)
    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    @Column(name = "user_id", nullable = true, length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "depart_name", nullable = true, length = 50)
    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    @Column(name = "user_name", nullable = true, length = 10)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "start_time", nullable = true)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time", nullable = true)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "leave_type", nullable = true, length = 1)
    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    @Column(name = "duration", nullable = true)
    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Column(name = "day_num", nullable = true)
    public double getDayNum() {
        return dayNum;
    }

    public void setDayNum(double dayNum) {
        this.dayNum = dayNum;
    }

    @Column(name = "reason", nullable = true, length = 200)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "HRLeave{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", departId='" + departId + '\'' +
                ", departName='" + departName + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", leaveType='" + leaveType + '\'' +
                ", duration=" + duration +
                ", dayNum=" + dayNum +
                ", reason='" + reason + '\'' +
                '}';
    }
}
