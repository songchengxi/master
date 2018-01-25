package com.scx.hr.entity;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @version V1.0
 * @Description: 员工信息
 * @date 2017-10-28 18:36:49
 */
@Entity
@Table(name = "hr_user", schema = "")
public class HRUser implements Serializable {

    //主键
    private String id;

    private String companyId;//公司

    //工号
    @Excel(name = "工号", width = 15)
    private String jobNumber;

    //姓名
    @Excel(name = "姓名", width = 15)
    private String name;

    private List<HRUserOrg> userOrgList = new ArrayList<HRUserOrg>();//部门信息

    //性别
    @Excel(name = "性别", width = 15, dicCode = "sex")
    private String sex;

    //出生日期
    @Excel(name = "出生日期", width = 15, format = "yyyy-MM-dd")
    private Date birthday;

    @Excel(name = "年龄", width = 15)
    private Integer age;

    @Excel(name = "证件类型",width = 15)
    private String idType;

    //证件号码
    @Excel(name = "证件号码", width = 15)
    private String idNumber;

    //婚姻状况
    @Excel(name = "婚姻状况", width = 15)
    private String marriageStatus;

    //民族
    @Excel(name = "民族", width = 15)
    private String nation;

    //籍贯
    @Excel(name = "籍贯", width = 15)
    private String nativePlace;

    //政治面貌
    @Excel(name = "政治面貌", width = 15)
    private String politics;

    //电子邮箱
    @Excel(name = "电子邮箱", width = 15)
    private String email;

    //联系电话
    @Excel(name = "联系电话", width = 15)
    private String phone;

    //地址
    @Excel(name = "地址", width = 15)
    private String address;

    //照片
    @Excel(name = "照片", width = 15)
    private String photo;

    //岗位
    @Excel(name = "岗位", width = 15)
    private String post;

    //职称
    @Excel(name = "职称", width = 15)
    private String title;

    //试用期工资（比例）
    @Excel(name = "试用期工资（比例）", width = 15)
    private String probationSalary;

    //固定工资
    @Excel(name = "固定工资", width = 15)
    private Double fixSalary;

    //奖励工资
    @Excel(name = "奖励工资", width = 15)
    private Double rewardSalary;

    //员工性质
    @Excel(name = "员工性质", width = 15)
    private String quality;

    //学历
    @Excel(name = "学历", width = 15)
    private String education;

    //专业
    @Excel(name = "专业", width = 15)
    private String major;

    //毕业院校
    @Excel(name = "毕业院校", width = 15)
    private String school;

    //入职日期
    @Excel(name = "入职日期", width = 15, format = "yyyy-MM-dd")
    private Date joinTime;

    //试用期
    @Excel(name = "试用期", width = 15)
    private String period;

    @Excel(name = "转正时间", width = 15)
    private Date formalDate;

    //在职状态
    @Excel(name = "在职状态", width = 15)
    private String jobStatus;// 3试用期；6正式；9离职

    //工龄
    @Excel(name = "工龄", width = 15)
    private Double workAge;

    //简历
    @Excel(name = "简历", width = 15)
    private String resume;

    //备注
    @Excel(name = "备注", width = 15)
    private String remark;

    //删除标记
    private Short deleteFlag;// 状态: 0：存在  1：已删除

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name = "ID", nullable = false, length = 36)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "age", nullable = true, length = 3)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name = "period", nullable = true, length = 2)
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Column(name = "formal_date", nullable = true)
    public Date getFormalDate() {
        return formalDate;
    }

    public void setFormalDate(Date formalDate) {
        this.formalDate = formalDate;
    }

    @Column(name = "COMPANY_ID", nullable = true, length = 36)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "NAME", nullable = true, length = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SEX", nullable = true, length = 1)
    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "BIRTHDAY", nullable = true)
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "ID_TYPE", nullable = true, length = 2)
    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    @Column(name = "ID_NUMBER", nullable = true, length = 20)
    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Column(name = "MARRIAGE_STATUS", nullable = true, length = 1)
    public String getMarriageStatus() {
        return this.marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    @Column(name = "NATION", nullable = true, length = 20)
    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @Column(name = "JOB_NUMBER", nullable = true, length = 10)
    public String getJobNumber() {
        return this.jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Column(name = "NATIVE_PLACE", nullable = true, length = 50)
    public String getNativePlace() {
        return this.nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    @Column(name = "POLITICS", nullable = true, length = 10)
    public String getPolitics() {
        return this.politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    @Column(name = "EMAIL", nullable = true, length = 32)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "PHONE", nullable = true, length = 20)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "ADDRESS", nullable = true, length = 50)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "PHOTO", nullable = true, length = 32)
    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Column(name = "post", nullable = true, length = 36)
    public String getPost() {
        return this.post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Column(name = "TITLE", nullable = true, length = 32)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "probation_salary", nullable = true, length = 2)
    public String getProbationSalary() {
        return probationSalary;
    }

    public void setProbationSalary(String probationSalary) {
        this.probationSalary = probationSalary;
    }

    @Column(name = "fix_salary", nullable = true, scale = 2, length = 32)
    public Double getFixSalary() {
        return fixSalary;
    }

    public void setFixSalary(Double fixSalary) {
        this.fixSalary = fixSalary;
    }

    @Column(name = "reward_salary", nullable = true, scale = 2, length = 32)
    public Double getRewardSalary() {
        return rewardSalary;
    }

    public void setRewardSalary(Double rewardSalary) {
        this.rewardSalary = rewardSalary;
    }

    @Column(name = "quality", nullable = true, length = 10)
    public String getQuality() {
        return this.quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Column(name = "EDUCATION", nullable = true, length = 10)
    public String getEducation() {
        return this.education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Column(name = "MAJOR", nullable = true, length = 32)
    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Column(name = "SCHOOL", nullable = true, length = 32)
    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Column(name = "JOIN_TIME", nullable = true, length = 32)
    public java.util.Date getJoinTime() {
        return this.joinTime;
    }

    public void setJoinTime(java.util.Date joinTime) {
        this.joinTime = joinTime;
    }

    @Column(name = "JOB_STATUS", nullable = true, length = 10)
    public String getJobStatus() {
        return this.jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    @Column(name = "WORK_AGE", nullable = true, scale = 1, length = 32)
    public Double getWorkAge() {
        return this.workAge;
    }

    public void setWorkAge(Double workAge) {
        this.workAge = workAge;
    }

    @Column(name = "RESUME", nullable = true, length = 50)
    public String getResume() {
        return this.resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    @Column(name = "REMARK", nullable = true, length = 50)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "delete_flag")
    public Short getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    public List<HRUserOrg> getUserOrgList() {
        return userOrgList;
    }

    public void setUserOrgList(List<HRUserOrg> userOrgList) {
        this.userOrgList = userOrgList;
    }

    @Override
    public String toString() {
        return "HRUser{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", name='" + name + '\'' +
                ", userOrgList=" + userOrgList +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", idType='" + idType + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", marriageStatus='" + marriageStatus + '\'' +
                ", nation='" + nation + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", politics='" + politics + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", photo='" + photo + '\'' +
                ", post='" + post + '\'' +
                ", title='" + title + '\'' +
                ", probationSalary=" + probationSalary +
                ", fixSalary=" + fixSalary +
                ", rewardSalary=" + rewardSalary +
                ", quality='" + quality + '\'' +
                ", education='" + education + '\'' +
                ", major='" + major + '\'' +
                ", school='" + school + '\'' +
                ", joinTime=" + joinTime +
                ", period='" + period + '\'' +
                ", formalDate=" + formalDate +
                ", jobStatus='" + jobStatus + '\'' +
                ", workAge=" + workAge +
                ", resume='" + resume + '\'' +
                ", remark='" + remark + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}