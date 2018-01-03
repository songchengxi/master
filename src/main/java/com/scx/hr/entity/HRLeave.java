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
    private String userId;//员工id
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private String leaveType;//请假类型
    private double duration;//时长
    private double dayNum;//天数
    private String reason;//事由
    private Short deleteFlag;//删除标记   0：存在；1：已删除

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

    @Column(name = "user_id", nullable = true, length = 36)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @Column(name = "delete_flag")
    public Short getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "HRLeave{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", userId='" + userId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", leaveType='" + leaveType + '\'' +
                ", duration=" + duration +
                ", dayNum=" + dayNum +
                ", reason='" + reason + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                '}';
    }
}
