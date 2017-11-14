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

    @Override
    public String toString() {
        return "CompanyEntity{" +
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
                '}';
    }
}
