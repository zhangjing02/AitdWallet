package com.tianqi.aitdwallet.utils.usdt;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.spongycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhangjing on 2020/10/10.
 * Describe :
 */
public class OmniUsdtUtils {
    /**
     *	usdt 离线签名
     *  @param privateKey：私钥
     *  @param toAddress：接收地址
     *  @param amount:转账金额
     *  @return
     *      
     */
    public static String signTransaction(String fromAddress, String toAddress, String changeAddress, Long amount, boolean isMainNet, int propertyId, String privateKey) throws Exception {
        List<UTXO> utxos = getUnspent(fromAddress, isMainNet);
        // 获取手续费
        Long fee = getFee(546L, utxos);
        // 判断是主链试试测试链
        NetworkParameters networkParameters = isMainNet ? MainNetParams.get() : TestNet3Params.get();
        Transaction tran = new Transaction(networkParameters);
        if (utxos == null || utxos.size() == 0) {
            throw new Exception("utxo为空");
        }
        //这是比特币的限制最小转账金额，所以很多usdt转账会收到一笔0.00000546的btc
        Long miniBtc = 546L;
        tran.addOutput(Coin.valueOf(miniBtc), Address.fromBase58(networkParameters, toAddress));

        //构建usdt的输出脚本 注意这里的金额是要乘10的8次方
        String usdtHex = "6a146f6d6e69" + String.format("%016x", propertyId) + String.format("%016x", amount);
        tran.addOutput(Coin.valueOf(0L), new Script(Utils.HEX.decode(usdtHex)));

        Long changeAmount = 0L;
        Long utxoAmount = 0L;
        List<UTXO> needUtxo = new ArrayList<>();
        //过滤掉多的uxto
        for (UTXO utxo : utxos) {
            if (utxoAmount > (fee + miniBtc)) {
                break;
            } else {
                needUtxo.add(utxo);
                utxoAmount += utxo.getValue().value;
            }
        }
        changeAmount = utxoAmount - (fee + miniBtc);
        //余额判断
        if (changeAmount < 0) {
            throw new Exception("utxo余额不足");
        }
        if (changeAmount > 0) {
            tran.addOutput(Coin.valueOf(changeAmount), Address.fromBase58(networkParameters, changeAddress));
        }

        //先添加未签名的输入，也就是utxo
        for (UTXO utxo : needUtxo) {
            tran.addInput(utxo.getHash(), utxo.getIndex(), utxo.getScript()).setSequenceNumber(TransactionInput.NO_SEQUENCE - 2);
        }

        //下面就是签名
        for (int i = 0; i < needUtxo.size(); i++) {
            //这里获取地址
            String addr = needUtxo.get(i).getAddress();

            ECKey ecKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey).getKey();
            TransactionInput transactionInput = tran.getInput(i);
            Script scriptPubKey = ScriptBuilder.createOutputScript(Address.fromBase58(networkParameters, addr));
            Sha256Hash hash = tran.hashForSignature(i, scriptPubKey, Transaction.SigHash.ALL, false);
            ECKey.ECDSASignature ecSig = ecKey.sign(hash);
            TransactionSignature txSig = new TransactionSignature(ecSig, Transaction.SigHash.ALL, false);
            transactionInput.setScriptSig(ScriptBuilder.createInputScript(txSig, ecKey));
        }

        //这是签名之后的原始交易，直接去广播就行了
        String signedHex = Hex.toHexString(tran.bitcoinSerialize());
        //这是交易的hash
        String txHash = Hex.toHexString(Utils.reverseBytes(Sha256Hash.hash(Sha256Hash.hash(tran.bitcoinSerialize()))));
        return signedHex;
    }

    private static Long getFee(long l, List<UTXO> utxos) {
        // TODO: 2020/10/12  此处应该写一个计算费用的方法。
        return 546*3L;
    }

    private static List<UTXO> getUnspent(String fromAddress, boolean isMainNet) {
        // TODO: 2020/10/12 这里可以写一个接口来调用未花费，然后获取此集合。 
        List<UTXO> utxoList=new ArrayList<>();
        
        return utxoList;

    }

}
