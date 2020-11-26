package com.tianqi.aitdwallet.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.Key;
import com.quincysx.crypto.bip32.ExtendedKey;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bip39.MnemonicGenerator;
import com.quincysx.crypto.bip39.RandomSeed;
import com.quincysx.crypto.bip39.SeedCalculator;
import com.quincysx.crypto.bip39.WordCount;
import com.quincysx.crypto.bip39.wordlists.English;
import com.quincysx.crypto.bip44.AddressIndex;
import com.quincysx.crypto.bip44.BIP44;
import com.quincysx.crypto.bip44.CoinPairDerive;
import com.quincysx.crypto.bitcoin.BitCoinECKeyPair;
import com.quincysx.crypto.ethereum.EthECKeyPair;
import com.quincysx.crypto.utils.HexUtils;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.LogUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create by zhangjing on 2020/10/6.
 * Describe :
 */
public class WalletUtils {

    public static ECKeyPair createCoinMaser(CoinTypes coinTypes) {
        UserInformation userInformation = UserInfoManager.getUserInfo();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = gson.fromJson(userInformation.getMnemonicWord(), listType);
        byte[] seed = new SeedCalculator().calculateSeed(list, "");
        ExtendedKey extendedKey = null;
        try {
            extendedKey = ExtendedKey.create(seed);
            Key master1 = extendedKey.getMaster();

            userInformation.setSeedPublicKey(HexUtils.toHex(master1.getRawPublicKey(true)));
            UserInfoManager.insertOrUpdate(userInformation);

            AddressIndex address = BIP44.m().purpose44()
                    .coinType(coinTypes)
                    .account(0)
                    .external()
                    .address(0);
            CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
            ECKeyPair master = coinKeyPair.derive(address);
            return master;
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ECKeyPair createCoinMaserRandom(CoinTypes coinTypes) {
        byte[] random = RandomSeed.random(WordCount.TWELVE);
        List<String> strings = new MnemonicGenerator(English.INSTANCE).createMnemonic(random);
        byte[] seed = new SeedCalculator().calculateSeed(strings, "");
        ExtendedKey extendedKey = null;
        try {
            extendedKey = ExtendedKey.create(seed);
            AddressIndex address = BIP44.m().purpose44()
                    .coinType(coinTypes)
                    .account(0)
                    .external()
                    .address(0);
            CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
            ECKeyPair master = coinKeyPair.derive(address);
            return master;
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ECKeyPair importCoinMaser(CoinTypes coinTypes, String private_key) {
        ECKeyPair master = null;
        if (coinTypes == CoinTypes.Bitcoin) {
            try {
                master = BitCoinECKeyPair.parseWIF(private_key);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        } else if (coinTypes == CoinTypes.Ethereum) {
            try {
                EthECKeyPair ethECKeyPair = new EthECKeyPair(HexUtils.fromHex(private_key));
                master = ethECKeyPair;
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        return master;
    }

    public static ECKeyPair importCoinMaser(CoinTypes coinTypes, List<String> mnemonic_words) {
        byte[] seed = new SeedCalculator().calculateSeed(mnemonic_words, "");
        ExtendedKey extendedKey = null;
        try {
            extendedKey = ExtendedKey.create(seed);
            AddressIndex address = BIP44.m().purpose44()
                    .coinType(coinTypes)
                    .account(0)
                    .external()
                    .address(0);
            CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
            ECKeyPair master = coinKeyPair.derive(address);
            return master;
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
