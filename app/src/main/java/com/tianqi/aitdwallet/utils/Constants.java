package com.tianqi.aitdwallet.utils;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;

public class Constants {
    public static final boolean TEST = true;
    public static final NetworkParameters NETWORK_PARAMETERS = TEST ? TestNet3Params.get() : MainNetParams.get();

    public static final String CHECKPOINTS = "checkpoints.txt";
    public static final String CHECKPOINTS_TESTNET = "checkpoints-testnet.txt";

    public static final String BITCOIN_WALLET_NAME = "wallet-protobuf";

    //  public static final String BITCOIN_MOMENT_WALLET_NAME="wallet-protobuf-for_moment";


    public static final String TRANSACTION_TYPE = "content";
    public static final String TRANSACTION_COIN_ID = "tx_coin_id";
    public static final String TRANSACTION_ALL = "全部";
    public static final String TRANSACTION_SEND = "转出";
    public static final String TRANSACTION_RECEIVE = "转入";
    public static final String TRANSACTION_FIALE = "失败";


    public static final String TRANSACTION_COIN_NAME = "transaction_coin_name";
    public static final String INTENT_PUT_COIN_ID = "intent_coin_id";
    public static final String INTENT_PUT_COIN_PASSWORD = "intent_coin_psd";
    public static final String INTENT_PUT_COIN_TYPE = "intent_coin_type";
    public static final String TRANSACTION_COIN_ADDRESS = "transaction_coin_address";


    public static final String INTENT_PUT_TAG= "intent_put_tag";
    public static final String INTENT_PUT_IMPORT_WALLET= "导入钱包";
    public static final String INTENT_PUT_EXPORT_PRIVATE_KEY= "导出私钥";
    public static final String INTENT_PUT_DELETE_COIN= "删除该币种（钱包）";
    public static final String INTENT_PUT_BACK_UP_MNEMONIC= "备份助记词";
    public static final String INTENT_PUT_EXPORT_KEYSTORE= "导出keystore";
    public static final String INTENT_PUT_CREATE_WALLET= "创建钱包";
    public static final String INTENT_PUT_TRANSACTION_ID= "交易id";
    public static final String INTENT_PUT_TRANSACTION_TYPE= "交易类型";
    public static final String INTENT_PUT_DELETE_CREATE_WALLET= "删除创建的钱包";
    public static final String INTENT_PUT_DELETE_IMPORT_WALLET= "删除导入的钱包";
    public static final String INTENT_PUT_ALREADY_MNEMONIC= "备份已有的助记词";
    public static final String INTENT_PUT_MNEMONIC= "INTENT_PUT_MNEMONIC";
    public static final String INTENT_PUT_CHANGE_PSD= "修改密码";
    public static final String INTENT_PUT_RESOURCE= "intent_put_resource";

    public static final String INTENT_PUT_CREATE_ADDRESS= "新建地址";

   // public static final String INTENT_PUT_TRANSACTION_TYPE= "INTENT_PUT_TRANSACTION_TYPE";


    public static final String SINGEL_HOME_FRESH = "SINGEL_HOME_FRESH";
    public static final String COIN_NULL= "COIN_NULL";


    public static final String FIAT_CNY = "CNY";
    public static final String FIAT_USD = "USD";

    public static final int LANGUAGE_CHINA= 0;
    public static final int LANGUAGE_ENGLISH= 1;

    public static final String HEX_PREFIX = "0x";


}
