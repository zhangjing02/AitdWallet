package com.tianqi.baselib.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by zhangjing on 2020/8/31.
 * Describe :联系人的数据库对象
 */

@Entity
public class ContactsInfo {

    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String contactsID;

    private String contactsName;
    private String contactsPhone;
    private String contactsEmail;
    private String remark;
    private String contactsCoinArray;
    @Generated(hash = 153993078)
    public ContactsInfo(Long id, String contactsID, String contactsName,
            String contactsPhone, String contactsEmail, String remark,
            String contactsCoinArray) {
        this.id = id;
        this.contactsID = contactsID;
        this.contactsName = contactsName;
        this.contactsPhone = contactsPhone;
        this.contactsEmail = contactsEmail;
        this.remark = remark;
        this.contactsCoinArray = contactsCoinArray;
    }
    @Generated(hash = 9726432)
    public ContactsInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContactsID() {
        return this.contactsID;
    }
    public void setContactsID(String contactsID) {
        this.contactsID = contactsID;
    }
    public String getContactsName() {
        return this.contactsName;
    }
    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }
    public String getContactsPhone() {
        return this.contactsPhone;
    }
    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
    public String getContactsEmail() {
        return this.contactsEmail;
    }
    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getContactsCoinArray() {
        return this.contactsCoinArray;
    }
    public void setContactsCoinArray(String contactsCoinArray) {
        this.contactsCoinArray = contactsCoinArray;
    }


}
