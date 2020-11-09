package com.tianqi.baselib.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by zhangjing on 2020/8/31.
 * Describe :币种的数据库对象
 */

@Entity
public class CoinInfo {

    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String coin_id;

    private String coin_name;
    private String coin_fullName;
    private String alias_name;//币种别名，用来显示钱包名称的，如果此字段为空，就用walletName显示。
    private int coin_type;   //币种类型(0,60)

    private String coin_address;

    private boolean isCollect;   //是否增加币种的钱包,资产管理的开关

    private double coin_totalAmount;  //币种总资产
    private String coin_blockHeight;
    private int coin_ComeType;  //币种的来源（0代表创建，1代表导入）
    private String coin_url;
    private String wallet_id;
    private int walletLimit;  //各种钱包的限制条件（默认是0；1例如xrp需要激活；2例如EOS有个加号。等等）
    private String keystoreStr;
    private String privateKey;
    private String publicKey;
    private boolean isUpChain;  //是否上链(比特币是通过接口把地址导入到节点上，就叫上链)
    private int resourceId;  //本地资源文件的图片的id。
    private boolean isMerge;  //用来标记是否是合并的币种（此币种下是否有多个币种，只是为了页面显示，）
    private double math_amount;//只用于临时计算的总额。
    private boolean isHidden;  //可能用户管理币种的隐藏与否
    @Generated(hash = 1220044928)
    public CoinInfo(Long id, String coin_id, String coin_name, String coin_fullName,
            String alias_name, int coin_type, String coin_address,
            boolean isCollect, double coin_totalAmount, String coin_blockHeight,
            int coin_ComeType, String coin_url, String wallet_id, int walletLimit,
            String keystoreStr, String privateKey, String publicKey,
            boolean isUpChain, int resourceId, boolean isMerge, double math_amount,
            boolean isHidden) {
        this.id = id;
        this.coin_id = coin_id;
        this.coin_name = coin_name;
        this.coin_fullName = coin_fullName;
        this.alias_name = alias_name;
        this.coin_type = coin_type;
        this.coin_address = coin_address;
        this.isCollect = isCollect;
        this.coin_totalAmount = coin_totalAmount;
        this.coin_blockHeight = coin_blockHeight;
        this.coin_ComeType = coin_ComeType;
        this.coin_url = coin_url;
        this.wallet_id = wallet_id;
        this.walletLimit = walletLimit;
        this.keystoreStr = keystoreStr;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.isUpChain = isUpChain;
        this.resourceId = resourceId;
        this.isMerge = isMerge;
        this.math_amount = math_amount;
        this.isHidden = isHidden;
    }
    @Generated(hash = 197728820)
    public CoinInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCoin_id() {
        return this.coin_id;
    }
    public void setCoin_id(String coin_id) {
        this.coin_id = coin_id;
    }
    public String getCoin_name() {
        return this.coin_name;
    }
    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }
    public String getCoin_fullName() {
        return this.coin_fullName;
    }
    public void setCoin_fullName(String coin_fullName) {
        this.coin_fullName = coin_fullName;
    }
    public String getAlias_name() {
        return this.alias_name;
    }
    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }
    public int getCoin_type() {
        return this.coin_type;
    }
    public void setCoin_type(int coin_type) {
        this.coin_type = coin_type;
    }
    public String getCoin_address() {
        return this.coin_address;
    }
    public void setCoin_address(String coin_address) {
        this.coin_address = coin_address;
    }
    public boolean getIsCollect() {
        return this.isCollect;
    }
    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }
    public double getCoin_totalAmount() {
        return this.coin_totalAmount;
    }
    public void setCoin_totalAmount(double coin_totalAmount) {
        this.coin_totalAmount = coin_totalAmount;
    }
    public String getCoin_blockHeight() {
        return this.coin_blockHeight;
    }
    public void setCoin_blockHeight(String coin_blockHeight) {
        this.coin_blockHeight = coin_blockHeight;
    }
    public int getCoin_ComeType() {
        return this.coin_ComeType;
    }
    public void setCoin_ComeType(int coin_ComeType) {
        this.coin_ComeType = coin_ComeType;
    }
    public String getCoin_url() {
        return this.coin_url;
    }
    public void setCoin_url(String coin_url) {
        this.coin_url = coin_url;
    }
    public String getWallet_id() {
        return this.wallet_id;
    }
    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }
    public int getWalletLimit() {
        return this.walletLimit;
    }
    public void setWalletLimit(int walletLimit) {
        this.walletLimit = walletLimit;
    }
    public String getKeystoreStr() {
        return this.keystoreStr;
    }
    public void setKeystoreStr(String keystoreStr) {
        this.keystoreStr = keystoreStr;
    }
    public String getPrivateKey() {
        return this.privateKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    public String getPublicKey() {
        return this.publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public boolean getIsUpChain() {
        return this.isUpChain;
    }
    public void setIsUpChain(boolean isUpChain) {
        this.isUpChain = isUpChain;
    }
    public int getResourceId() {
        return this.resourceId;
    }
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    public boolean getIsMerge() {
        return this.isMerge;
    }
    public void setIsMerge(boolean isMerge) {
        this.isMerge = isMerge;
    }
    public double getMath_amount() {
        return this.math_amount;
    }
    public void setMath_amount(double math_amount) {
        this.math_amount = math_amount;
    }
    public boolean getIsHidden() {
        return this.isHidden;
    }
    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }


    @Override
    public String toString() {
        return "CoinInfo{" +
                "id=" + id +
                ", coin_id='" + coin_id + '\'' +
                ", coin_name='" + coin_name + '\'' +
                ", coin_fullName='" + coin_fullName + '\'' +
                ", alias_name='" + alias_name + '\'' +
                ", coin_type=" + coin_type +
                ", coin_address='" + coin_address + '\'' +
                ", isCollect=" + isCollect +
                ", coin_totalAmount=" + coin_totalAmount +
                ", coin_blockHeight='" + coin_blockHeight + '\'' +
                ", coin_ComeType=" + coin_ComeType +
                ", coin_url='" + coin_url + '\'' +
                ", wallet_id='" + wallet_id + '\'' +
                ", walletLimit=" + walletLimit +
                ", keystoreStr='" + keystoreStr + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", isUpChain=" + isUpChain +
                ", resourceId=" + resourceId +
                ", isMerge=" + isMerge +
                ", math_amount=" + math_amount +
                ", isHidden=" + isHidden +
                '}';
    }
}
