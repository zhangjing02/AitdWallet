package com.tianqi.baselib.rxhttp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Create by zhangjing on 2020/11/20.
 * Describe :
 */
public class IosKeystoreBean {

    /**
     * Crypto : {"cipher":"aes-128-ctr","cipherparams":{"iv":"0b5d08d27c0cf426d0d033df1b5f97c6"},"ciphertext":"7c46a25e18bf374db65a9db24c655321f3566b3c8354f553e471687899b43ccd","kdf":"scrypt","kdfparams":{"dklen":32,"n":262144,"p":1,"r":8,"salt":"ca922dbcff95d8b8eebb854e070a6e2de57ba7052b353c971bfa9e9bd10ee90b"},"mac":"6fd41cc587b011584754cd0708048403fc82b483c9b240898b6a7708b0667e14"}
     * address : f590b990ed3f0d6e9a4eb7518e9f2705d6813506
     * id : 9EFAAD9A-3673-4D46-B559-22CACA5811A0
     * version : 3
     * x-ethers : {"client":"ethers/iOS","gethFilename":"UTC--2020-11-20T11-09-41.0Z--f590b990ed3f0d6e9a4eb7518e9f2705d6813506","mnemonicCiphertext":"544cb234133d5843dc2ee21056ad61b9","mnemonicCounter":"3fcac88c8aa0657d1bdae5621d78dc0b","version":"0.1"}
     */

    private CryptoBean Crypto;
    private String address;
    private String id;
    private int version;
  //  @SerializedName("x-ethers")
    private XethersBean xethers;

    public CryptoBean getCrypto() {
        return Crypto;
    }

    public void setCrypto(CryptoBean Crypto) {
        this.Crypto = Crypto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public XethersBean getXethers() {
        return xethers;
    }

    public void setXethers(XethersBean xethers) {
        this.xethers = xethers;
    }

    public static class CryptoBean {
        /**
         * cipher : aes-128-ctr
         * cipherparams : {"iv":"0b5d08d27c0cf426d0d033df1b5f97c6"}
         * ciphertext : 7c46a25e18bf374db65a9db24c655321f3566b3c8354f553e471687899b43ccd
         * kdf : scrypt
         * kdfparams : {"dklen":32,"n":262144,"p":1,"r":8,"salt":"ca922dbcff95d8b8eebb854e070a6e2de57ba7052b353c971bfa9e9bd10ee90b"}
         * mac : 6fd41cc587b011584754cd0708048403fc82b483c9b240898b6a7708b0667e14
         */

        private String cipher;
        private CipherparamsBean cipherparams;
        private String ciphertext;
        private String kdf;
        private KdfparamsBean kdfparams;
        private String mac;

        public String getCipher() {
            return cipher;
        }

        public void setCipher(String cipher) {
            this.cipher = cipher;
        }

        public CipherparamsBean getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(CipherparamsBean cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public KdfparamsBean getKdfparams() {
            return kdfparams;
        }

        public void setKdfparams(KdfparamsBean kdfparams) {
            this.kdfparams = kdfparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public static class CipherparamsBean {
            /**
             * iv : 0b5d08d27c0cf426d0d033df1b5f97c6
             */

            private String iv;

            public String getIv() {
                return iv;
            }

            public void setIv(String iv) {
                this.iv = iv;
            }
        }

        public static class KdfparamsBean {
            /**
             * dklen : 32
             * n : 262144
             * p : 1
             * r : 8
             * salt : ca922dbcff95d8b8eebb854e070a6e2de57ba7052b353c971bfa9e9bd10ee90b
             */

            private int dklen;
            private int n;
            private int p;
            private int r;
            private String salt;

            public int getDklen() {
                return dklen;
            }

            public void setDklen(int dklen) {
                this.dklen = dklen;
            }

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public int getP() {
                return p;
            }

            public void setP(int p) {
                this.p = p;
            }

            public int getR() {
                return r;
            }

            public void setR(int r) {
                this.r = r;
            }

            public String getSalt() {
                return salt;
            }

            public void setSalt(String salt) {
                this.salt = salt;
            }
        }
    }

    public static class XethersBean {
        /**
         * client : ethers/iOS
         * gethFilename : UTC--2020-11-20T11-09-41.0Z--f590b990ed3f0d6e9a4eb7518e9f2705d6813506
         * mnemonicCiphertext : 544cb234133d5843dc2ee21056ad61b9
         * mnemonicCounter : 3fcac88c8aa0657d1bdae5621d78dc0b
         * version : 0.1
         */

        private String client;
        private String gethFilename;
        private String mnemonicCiphertext;
        private String mnemonicCounter;
        private String version;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getGethFilename() {
            return gethFilename;
        }

        public void setGethFilename(String gethFilename) {
            this.gethFilename = gethFilename;
        }

        public String getMnemonicCiphertext() {
            return mnemonicCiphertext;
        }

        public void setMnemonicCiphertext(String mnemonicCiphertext) {
            this.mnemonicCiphertext = mnemonicCiphertext;
        }

        public String getMnemonicCounter() {
            return mnemonicCounter;
        }

        public void setMnemonicCounter(String mnemonicCounter) {
            this.mnemonicCounter = mnemonicCounter;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
