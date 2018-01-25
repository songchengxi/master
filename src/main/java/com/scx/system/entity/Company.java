package com.scx.system.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 公司信息
 */
@Entity
@Table(name = "sys_company")
public class Company implements Serializable{

    private String id;

    private String name;//公司名称

    private String industry;//行业

    private String address;//办公地址

    private String invoiceTitle;//发票抬头

    private String taxNum;//税号

    private String bank;//开户行

    private String bankAccount;//银行账号

    private String phone;//电话

    private String registerAddress;//注册地址

    private String remark;//备注说明

    private String dayNumType;//月计薪天数方式 1自定义；2标准计薪21.75天；3当月应出勤天数
    private String dayNum;//月计薪天数

    private String hour;//日计薪小时

    private String sick;//病假工资发放比例
    private String maternity;//产假工资发放比例
    private String paternity;//陪产假工资发放比例

    private String weekdayType;//工作日加班计薪方式  1固定薪资；2时薪百分比
    private String weekday;//工作日加班

    private String playdayType;//休息日加班计薪方式  1固定薪资；2时薪百分比
    private String playday;//休息日加班

    private String statutoryType;//法定节假日加班计薪方式  1固定薪资；2时薪百分比
    private String statutory;//法定节假日加班

    @Id
    @Column(name = "ID", nullable = false, length = 32)
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

    @Column(name = "INDUSTRY", nullable = true, length = 20)
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Column(name = "ADDRESS", nullable = true, length = 50)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "INVOICE_TITLE", nullable = true, length = 50)
    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    @Column(name = "TAX_NUM", nullable = true, length = 20)
    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    @Column(name = "BANK", nullable = true, length = 20)
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(name = "BANK_ACCOUNT", nullable = true, length = 20)
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column(name = "PHONE", nullable = true, length = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "REGISTER_ADDRESS", nullable = true, length = 50)
    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    @Column(name = "REMARK", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "day_num_type", nullable = true, length = 2)
    public String getDayNumType() {
        return dayNumType;
    }

    public void setDayNumType(String dayNumType) {
        this.dayNumType = dayNumType;
    }

    @Column(name = "day_num", nullable = true, length = 10)
    public String getDayNum() {
        return dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    @Column(name = "hour", nullable = true, length = 2)
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Column(name = "sick", nullable = true, length = 5)
    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    @Column(name = "maternity", nullable = true, length = 5)
    public String getMaternity() {
        return maternity;
    }

    public void setMaternity(String maternity) {
        this.maternity = maternity;
    }

    @Column(name = "paternity", nullable = true, length = 5)
    public String getPaternity() {
        return paternity;
    }

    public void setPaternity(String paternity) {
        this.paternity = paternity;
    }

    @Column(name = "weekday_type", nullable = true, length = 2)
    public String getWeekdayType() {
        return weekdayType;
    }

    public void setWeekdayType(String weekdayType) {
        this.weekdayType = weekdayType;
    }

    @Column(name = "weekday", nullable = true, length = 5)
    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    @Column(name = "playday_type", nullable = true, length = 2)
    public String getPlaydayType() {
        return playdayType;
    }

    public void setPlaydayType(String playdayType) {
        this.playdayType = playdayType;
    }

    @Column(name = "playday", nullable = true, length = 5)
    public String getPlayday() {
        return playday;
    }

    public void setPlayday(String playday) {
        this.playday = playday;
    }

    @Column(name = "statutory_type", nullable = true, length = 2)
    public String getStatutoryType() {
        return statutoryType;
    }

    public void setStatutoryType(String statutoryType) {
        this.statutoryType = statutoryType;
    }

    @Column(name = "statutory", nullable = true, length = 5)
    public String getStatutory() {
        return statutory;
    }

    public void setStatutory(String statutory) {
        this.statutory = statutory;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", industry='" + industry + '\'' +
                ", address='" + address + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", taxNum='" + taxNum + '\'' +
                ", bank='" + bank + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", phone='" + phone + '\'' +
                ", registerAddress='" + registerAddress + '\'' +
                ", remark='" + remark + '\'' +
                ", dayNumType='" + dayNumType + '\'' +
                ", dayNum='" + dayNum + '\'' +
                ", hour='" + hour + '\'' +
                ", sick='" + sick + '\'' +
                ", maternity='" + maternity + '\'' +
                ", paternity='" + paternity + '\'' +
                ", weekdayType='" + weekdayType + '\'' +
                ", weekday='" + weekday + '\'' +
                ", playdayType='" + playdayType + '\'' +
                ", playday='" + playday + '\'' +
                ", statutoryType='" + statutoryType + '\'' +
                ", statutory='" + statutory + '\'' +
                '}';
    }
}
