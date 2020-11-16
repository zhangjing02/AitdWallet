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
    private String contactsCoinName;
    private String contactsCoinAddress;
    private String remark;
    private String contactsCoinArray;
    private int    coinResourceId;

    @Generated(hash = 1121287128)
    public ContactsInfo(Long id, String contactsID, String contactsName,
            String contactsCoinName, String contactsCoinAddress, String remark,
            String contactsCoinArray, int coinResourceId) {
        this.id = id;
        this.contactsID = contactsID;
        this.contactsName = contactsName;
        this.contactsCoinName = contactsCoinName;
        this.contactsCoinAddress = contactsCoinAddress;
        this.remark = remark;
        this.contactsCoinArray = contactsCoinArray;
        this.coinResourceId = coinResourceId;
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
    public String getContactsCoinName() {
        return this.contactsCoinName;
    }
    public void setContactsCoinName(String contactsCoinName) {
        this.contactsCoinName = contactsCoinName;
    }
    public String getContactsCoinAddress() {
        return this.contactsCoinAddress;
    }
    public void setContactsCoinAddress(String contactsCoinAddress) {
        this.contactsCoinAddress = contactsCoinAddress;
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
    public int getCoinResourceId() {
        return this.coinResourceId;
    }
    public void setCoinResourceId(int coinResourceId) {
        this.coinResourceId = coinResourceId;
    }
    
}
