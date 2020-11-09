package com.tianqi.aitdwallet.bean;

import java.io.Serializable;

public class CurrencyCardBean implements Serializable {

    private String coin_name;
    private String coin_alias_name;
    private String coin_icon_id;
    private String coin_address;
    private String currency_to_fiat_num;
    private double currency_to_froze_num;
    private int currency_bg_id;
    private int coin_icon_white_id;

    public int getCoin_icon_white_id() {
        return coin_icon_white_id;
    }

    public void setCoin_icon_white_id(int coin_icon_white_id) {
        this.coin_icon_white_id = coin_icon_white_id;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }

    public String getCoin_icon_id() {
        return coin_icon_id;
    }

    public void setCoin_icon_id(String coin_icon_id) {
        this.coin_icon_id = coin_icon_id;
    }

    public String getCoin_address() {
        return coin_address;
    }

    public void setCoin_address(String coin_address) {
        this.coin_address = coin_address;
    }

    public double getCurrency_to_froze_num() {
        return currency_to_froze_num;
    }

    public void setCurrency_to_froze_num(double currency_to_froze_num) {
        this.currency_to_froze_num = currency_to_froze_num;
    }

    public String getCurrency_to_fiat_num() {
        return currency_to_fiat_num;
    }

    public void setCurrency_to_fiat_num(String currency_to_fiat_num) {
        this.currency_to_fiat_num = currency_to_fiat_num;
    }

    public int getCurrency_bg_id() {
        return currency_bg_id;
    }

    public void setCurrency_bg_id(int currency_bg_id) {
        this.currency_bg_id = currency_bg_id;
    }


    public String getCoin_alias_name() {
        return coin_alias_name;
    }

    public void setCoin_alias_name(String coin_alias_name) {
        this.coin_alias_name = coin_alias_name;
    }
}
