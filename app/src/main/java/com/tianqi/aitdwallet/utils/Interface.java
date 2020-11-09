package com.tianqi.aitdwallet.utils;

import org.bitcoinj.core.NetworkParameters;

public interface Interface {
    
    long creationtime = 1409478661L;

   // String keypath = "44H/0H/0H/0/0";
    String keypath = "44H/1H/0H/0/0";

    String net = "testnet";
   // String net = "production";

    NetworkParameters getNetParams(String net);
}
