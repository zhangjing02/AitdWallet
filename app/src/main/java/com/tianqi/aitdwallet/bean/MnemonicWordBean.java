package com.tianqi.aitdwallet.bean;

/**
 * Create by zhangjing on 2020/10/26.
 * Describe :
 */
public class MnemonicWordBean {
    private String word;
    private boolean isHidden;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }
}
