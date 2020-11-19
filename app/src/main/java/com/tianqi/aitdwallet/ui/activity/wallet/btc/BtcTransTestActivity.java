package com.tianqi.aitdwallet.ui.activity.wallet.btc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bitcoin.BTCTransaction;
import com.quincysx.crypto.bitcoin.BitCoinECKeyPair;
import com.quincysx.crypto.bitcoin.BitcoinException;
import com.quincysx.crypto.utils.HexUtils;
import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.utils.digital.DataReshape;

import java.math.BigInteger;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BtcTransTestActivity extends BaseActivity {

    @BindView(R.id.tv_tx_send1)
    TextView tvTxSend1;
    @BindView(R.id.tv_tx_send2)
    TextView tvTxSend2;
    @BindView(R.id.et_send_value)
    EditText etSendValue;
    @BindView(R.id.tv_tx_send3)
    TextView tvTxSend3;

    private ECKeyPair master = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_btc_trans_test;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        try {
            master = BitCoinECKeyPair.parseWIF("cQwsQHtfr1U8WbhogD3EpctisPCexprShGiiUjKXLswYauazaUVY");
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_tx_send1, R.id.tv_tx_send2,R.id.tv_tx_send3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tx_send1:
                try {
                    BTCTransaction unsignedTransaction002 = new BTCTransaction(HexUtils.fromHex("02000000019a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff010000000000000000166a146f6d6e690000000000000001000000001b6b0b0000000000"));

                    BTCTransaction.Script script = BTCTransaction.Script.buildOutput("mjnJUDMqPd1jEktdvdAnGrLd2bvC4epzGi");  //对方的地址
                    BTCTransaction.Output output = new BTCTransaction.Output(846, script);
                    BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex("c2dae3b67b9711ddcd012ad15b47fb795ac1a52584a5acfd14f366ef2a63909a"), 0);
                    BTCTransaction.Script script002 = BTCTransaction.Script.buildOutput(master.getAddress());  //自己的地址
                    BTCTransaction.Input input = new BTCTransaction.Input(outPoint, script002, 0);

                    BTCTransaction unsignedTransaction = new BTCTransaction(new BTCTransaction.Input[]{input}, new BTCTransaction.Output[]{output, unsignedTransaction002.outputs[0]}, 0);
                    byte[] sign = unsignedTransaction.sign(master);
                    String toHex = HexUtils.toHex(sign);
                  //  Log.i("tttttttttttttttttttt", master.getAddress() + "------onViewClicked: 我们看签名后的数据是？" + toHex);
                } catch (BitcoinException e) {
                    e.printStackTrace();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_tx_send2:
                BTCTransaction.Script script002 = null;  //自己的地址
                try {
                    BTCTransaction.Output output = new BTCTransaction.Output(0, null);
                    BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex("745c7692a70712f621b4d5a6095e5bd3fe3399f868b2fa6d7a2c08f59f7e5f40"), 0);
                    BTCTransaction.Input input = new BTCTransaction.Input(outPoint, null, 0);
                    BTCTransaction unsignedTransaction = new BTCTransaction(new BTCTransaction.Input[]{input}, new BTCTransaction.Output[]{output}, 0);
                    byte[] sign = unsignedTransaction.sign(master);
                    String toHex = HexUtils.toHex(sign);

                    String tx_cut_str = "0000000000ffffffff0000000000";
                    String connet_tx_str = "010000000000000000166a146f6d6e6900000000";
                    String omni_origal_data = "00000000000000010000000000000000";

                    //创建裸交易
                    String create_load_tx = toHex.substring(0, 74) + tx_cut_str;
                    //创建omni交易
                    String strHex = null;
                    Scanner cin = new Scanner("460000000");     //金额：单位聪。
                    while (cin.hasNext()) {
                        String str = cin.next();
                        strHex = new BigInteger(str, 10).toString(16);//10进制转换2进制
                    }
                    cin.close();

                    String omni_str = omni_origal_data.substring(8, omni_origal_data.length() - strHex.length()) + strHex + "00000000";
                 //   Log.i("ttttttttttttt", strHex + "---------onViewClicked: 002我们看这个" + omni_str);

                    //把负载和omni转账，拼接成一个hex，然后形成out。
                    String total_tx_str = create_load_tx.substring(0, create_load_tx.indexOf(tx_cut_str) + 18) + connet_tx_str + omni_str;
                  //  Log.i("ttttttttttttt", "onViewClicked: 003我们看这个" + total_tx_str);
                    BTCTransaction unsignedTransaction002 = new BTCTransaction(HexUtils.fromHex(total_tx_str));

                } catch (BitcoinException e) {
                    e.printStackTrace();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_tx_send3:
                String send_value = etSendValue.getText().toString();
                double bigInteger=Double.valueOf(send_value)*100000000l;


                Scanner cin = new Scanner(DataReshape.double2int(bigInteger,0));     //金额：单位聪。
                while (cin.hasNext()) {
                    String str = cin.next();
                   String strHex = new BigInteger(str, 10).toString(16);//10进制转换2进制
                //    Log.i("ttttttttttttt", "onViewClicked: 我们看这里交易情况"+strHex);
                }
                cin.close();
                //02000000029a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff1b83b1cfd5f7fec08c5bbfd5edfa74f7cf5de9eaeb3f8ef41931049d6e6028680000000000ffffffff0000000000
                //02000000029a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff1b83b1cfd5f7fec08c5bbfd5edfa74f7cf5de9eaeb3f8ef41931049d6e6028680000000000ffffffff010000000000000000166a146f6d6e69000000000000001f000000000bebc20000000000



                //02000000019a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff0000000000
                //02000000019a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff010000000000000000166a146f6d6e69000000000000001f000000000bebc20000000000

                //02000000039a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff9a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff9a90632aef66f314fdaca58425a5c15a79fb475bd12a01cddd11977bb6e3dac20000000000ffffffff0000000000
                //02000000031658e8fc846b557107a3b7618956e926b0b2236a146439c61909ea5f9da9f87b0000000000ffffffff1b83b1cfd5f7fec08c5bbfd5edfa74f7cf5de9eaeb3f8ef41931049d6e6028680000000000ffffffff1cdb3e3dfe5e25edcd704149ce4165d11186890509a3c7b636edcb1089c599ae0000000000ffffffff010000000000000000166a146f6d6e69000000000000001f000000000bebc20000000000


                break;
        }
    }

}