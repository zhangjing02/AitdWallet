package com.tianqi.baselib.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserInformation {
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String userId;

    private String mnemonicWord;
    private String passwordStr;
    private boolean noCenter;
    private String passwordTip;
    private String fiatUnit;
    private int  languageId;
    private String seedPublicKey;
    @Generated(hash = 636160721)
    public UserInformation(Long id, String userId, String mnemonicWord,
            String passwordStr, boolean noCenter, String passwordTip,
            String fiatUnit, int languageId, String seedPublicKey) {
        this.id = id;
        this.userId = userId;
        this.mnemonicWord = mnemonicWord;
        this.passwordStr = passwordStr;
        this.noCenter = noCenter;
        this.passwordTip = passwordTip;
        this.fiatUnit = fiatUnit;
        this.languageId = languageId;
        this.seedPublicKey = seedPublicKey;
    }
    @Generated(hash = 1920987651)
    public UserInformation() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getMnemonicWord() {
        return this.mnemonicWord;
    }
    public void setMnemonicWord(String mnemonicWord) {
        this.mnemonicWord = mnemonicWord;
    }
    public String getPasswordStr() {
        return this.passwordStr;
    }
    public void setPasswordStr(String passwordStr) {
        this.passwordStr = passwordStr;
    }
    public boolean getNoCenter() {
        return this.noCenter;
    }
    public void setNoCenter(boolean noCenter) {
        this.noCenter = noCenter;
    }
    public String getPasswordTip() {
        return this.passwordTip;
    }
    public void setPasswordTip(String passwordTip) {
        this.passwordTip = passwordTip;
    }
    public String getFiatUnit() {
        return this.fiatUnit;
    }
    public void setFiatUnit(String fiatUnit) {
        this.fiatUnit = fiatUnit;
    }
    public int getLanguageId() {
        return this.languageId;
    }
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }
    public String getSeedPublicKey() {
        return this.seedPublicKey;
    }
    public void setSeedPublicKey(String seedPublicKey) {
        this.seedPublicKey = seedPublicKey;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", mnemonicWord='" + mnemonicWord + '\'' +
                ", passwordStr='" + passwordStr + '\'' +
                ", noCenter=" + noCenter +
                ", passwordTip='" + passwordTip + '\'' +
                ", fiatUnit='" + fiatUnit + '\'' +
                ", languageId=" + languageId +
                ", seedPublicKey='" + seedPublicKey + '\'' +
                '}';
    }
}
