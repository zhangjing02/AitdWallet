package com.tianqi.baselib.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * Create by zhangjing on 2020/8/31.
 * Describe :币种的数据库对象
 */

@Entity
public class WalletInfo {
    @Id(autoincrement = true)
    private Long id;

    private String userId;
    @Index(unique = true)
    private String wallet_id;
    private String walletName;
    private String walletUrl;
    private double walletBalance;
    private boolean isHide;  //是否隐藏此钱包的余额 0.不隐藏  1.隐藏
    private int walletType; //钱包类型(默认HD多链钱包0，后续有多重签名1，NFC钱包2等)
    private double coin_CNYPrice;
    private double coin_USDPrice;
    private int resource_id;  //本地资源文件的图片的id。
    private String alias_name;//钱包别名，用来显示钱包名称的，如果此字段为空，就用walletName显示。
    private boolean isImportToCreate;//一开始是从导入单链币种，创建的钱包。
    @Generated(hash = 1768808188)
    public WalletInfo(Long id, String userId, String wallet_id, String walletName,
            String walletUrl, double walletBalance, boolean isHide, int walletType,
            double coin_CNYPrice, double coin_USDPrice, int resource_id,
            String alias_name, boolean isImportToCreate) {
        this.id = id;
        this.userId = userId;
        this.wallet_id = wallet_id;
        this.walletName = walletName;
        this.walletUrl = walletUrl;
        this.walletBalance = walletBalance;
        this.isHide = isHide;
        this.walletType = walletType;
        this.coin_CNYPrice = coin_CNYPrice;
        this.coin_USDPrice = coin_USDPrice;
        this.resource_id = resource_id;
        this.alias_name = alias_name;
        this.isImportToCreate = isImportToCreate;
    }
    @Generated(hash = 1144910350)
    public WalletInfo() {
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
    public String getWallet_id() {
        return this.wallet_id;
    }
    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }
    public String getWalletName() {
        return this.walletName;
    }
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
    public String getWalletUrl() {
        return this.walletUrl;
    }
    public void setWalletUrl(String walletUrl) {
        this.walletUrl = walletUrl;
    }
    public double getWalletBalance() {
        return this.walletBalance;
    }
    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }
    public boolean getIsHide() {
        return this.isHide;
    }
    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }
    public int getWalletType() {
        return this.walletType;
    }
    public void setWalletType(int walletType) {
        this.walletType = walletType;
    }
    public double getCoin_CNYPrice() {
        return this.coin_CNYPrice;
    }
    public void setCoin_CNYPrice(double coin_CNYPrice) {
        this.coin_CNYPrice = coin_CNYPrice;
    }
    public double getCoin_USDPrice() {
        return this.coin_USDPrice;
    }
    public void setCoin_USDPrice(double coin_USDPrice) {
        this.coin_USDPrice = coin_USDPrice;
    }
    public int getResource_id() {
        return this.resource_id;
    }
    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }
    public String getAlias_name() {
        return this.alias_name;
    }
    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }
    public boolean getIsImportToCreate() {
        return this.isImportToCreate;
    }
    public void setIsImportToCreate(boolean isImportToCreate) {
        this.isImportToCreate = isImportToCreate;
    }

    @Override
    public String toString() {
        return "WalletInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", wallet_id='" + wallet_id + '\'' +
                ", walletName='" + walletName + '\'' +
                ", walletUrl='" + walletUrl + '\'' +
                ", walletBalance=" + walletBalance +
                ", isHide=" + isHide +
                ", walletType=" + walletType +
                ", coin_CNYPrice=" + coin_CNYPrice +
                ", coin_USDPrice=" + coin_USDPrice +
                ", resource_id=" + resource_id +
                ", alias_name='" + alias_name + '\'' +
                ", isImportToCreate=" + isImportToCreate +
                '}';
    }
}
