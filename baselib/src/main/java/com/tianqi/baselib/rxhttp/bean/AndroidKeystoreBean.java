package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/11/20.
 * Describe :
 */
public class AndroidKeystoreBean {

    /**
     * address : 740B402E31408f03717d2b9377EE8bCc65c3993E
     * crypto : {"cipher":"aes-128-ctr","cipherparams":{"iv":"660c71be75b62270d75962dc79a4e851"},"ciphertext":"1f22922f7d29b9c1bea8ae241193dade9ae749e7b011274ed5145e219f39e86a","kdf":"scrypt","kdfparams":{"dklen":32,"n":4096,"p":6,"r":8,"salt":"d80d1bc9d09c7ecbbf67277a29983aadbe5f329972696bbc714ba112d0a32952"},"mac":"aefe822ff134bfbdda2b7948577fa15ffbf822aabca748141700c57c92b7c664"}
     * id : 245fcd3c-77a9-41e4-91d5-dd0d65cc768b
     * version : 3
     */

    private String address;
    private CryptoBean crypto;
    private String id;
    private int version;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CryptoBean getCrypto() {
        return crypto;
    }

    public void setCrypto(CryptoBean crypto) {
        this.crypto = crypto;
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

    public static class CryptoBean {
        /**
         * cipher : aes-128-ctr
         * cipherparams : {"iv":"660c71be75b62270d75962dc79a4e851"}
         * ciphertext : 1f22922f7d29b9c1bea8ae241193dade9ae749e7b011274ed5145e219f39e86a
         * kdf : scrypt
         * kdfparams : {"dklen":32,"n":4096,"p":6,"r":8,"salt":"d80d1bc9d09c7ecbbf67277a29983aadbe5f329972696bbc714ba112d0a32952"}
         * mac : aefe822ff134bfbdda2b7948577fa15ffbf822aabca748141700c57c92b7c664
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
             * iv : 660c71be75b62270d75962dc79a4e851
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
             * n : 4096
             * p : 6
             * r : 8
             * salt : d80d1bc9d09c7ecbbf67277a29983aadbe5f329972696bbc714ba112d0a32952
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
}
