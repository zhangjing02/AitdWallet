package com.tianqi.baselib.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by zhangjing on 2020/10/12.
 * Describe :
 */
@Entity
public class CoinRateInfo {

    @Id(autoincrement = true)
    private Long ids;

    private double available_supply;
    @Index(unique = true)
    private String id;
    private int last_updated;
    private String logo;
    private String logo_png;
    private double market_cap_cny;
    private double market_cap_usd;
    private double max_supply;
    private String name;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;
    private double price_btc;
    private double price_cny;
    private double price_usd;
    private int rank;
    private String symbol;
    private double total_supply;
    private double volume_24h_cny;
    private double volume_24h_usd;
    @Generated(hash = 371954761)
    public CoinRateInfo(Long ids, double available_supply, String id,
            int last_updated, String logo, String logo_png, double market_cap_cny,
            double market_cap_usd, double max_supply, String name,
            double percent_change_1h, double percent_change_24h,
            double percent_change_7d, double price_btc, double price_cny,
            double price_usd, int rank, String symbol, double total_supply,
            double volume_24h_cny, double volume_24h_usd) {
        this.ids = ids;
        this.available_supply = available_supply;
        this.id = id;
        this.last_updated = last_updated;
        this.logo = logo;
        this.logo_png = logo_png;
        this.market_cap_cny = market_cap_cny;
        this.market_cap_usd = market_cap_usd;
        this.max_supply = max_supply;
        this.name = name;
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        this.percent_change_7d = percent_change_7d;
        this.price_btc = price_btc;
        this.price_cny = price_cny;
        this.price_usd = price_usd;
        this.rank = rank;
        this.symbol = symbol;
        this.total_supply = total_supply;
        this.volume_24h_cny = volume_24h_cny;
        this.volume_24h_usd = volume_24h_usd;
    }
    @Generated(hash = 1061932321)
    public CoinRateInfo() {
    }
    public Long getIds() {
        return this.ids;
    }
    public void setIds(Long ids) {
        this.ids = ids;
    }
    public double getAvailable_supply() {
        return this.available_supply;
    }
    public void setAvailable_supply(double available_supply) {
        this.available_supply = available_supply;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getLast_updated() {
        return this.last_updated;
    }
    public void setLast_updated(int last_updated) {
        this.last_updated = last_updated;
    }
    public String getLogo() {
        return this.logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getLogo_png() {
        return this.logo_png;
    }
    public void setLogo_png(String logo_png) {
        this.logo_png = logo_png;
    }
    public double getMarket_cap_cny() {
        return this.market_cap_cny;
    }
    public void setMarket_cap_cny(double market_cap_cny) {
        this.market_cap_cny = market_cap_cny;
    }
    public double getMarket_cap_usd() {
        return this.market_cap_usd;
    }
    public void setMarket_cap_usd(double market_cap_usd) {
        this.market_cap_usd = market_cap_usd;
    }
    public double getMax_supply() {
        return this.max_supply;
    }
    public void setMax_supply(double max_supply) {
        this.max_supply = max_supply;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPercent_change_1h() {
        return this.percent_change_1h;
    }
    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }
    public double getPercent_change_24h() {
        return this.percent_change_24h;
    }
    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }
    public double getPercent_change_7d() {
        return this.percent_change_7d;
    }
    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }
    public double getPrice_btc() {
        return this.price_btc;
    }
    public void setPrice_btc(double price_btc) {
        this.price_btc = price_btc;
    }
    public double getPrice_cny() {
        return this.price_cny;
    }
    public void setPrice_cny(double price_cny) {
        this.price_cny = price_cny;
    }
    public double getPrice_usd() {
        return this.price_usd;
    }
    public void setPrice_usd(double price_usd) {
        this.price_usd = price_usd;
    }
    public int getRank() {
        return this.rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getSymbol() {
        return this.symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public double getTotal_supply() {
        return this.total_supply;
    }
    public void setTotal_supply(double total_supply) {
        this.total_supply = total_supply;
    }
    public double getVolume_24h_cny() {
        return this.volume_24h_cny;
    }
    public void setVolume_24h_cny(double volume_24h_cny) {
        this.volume_24h_cny = volume_24h_cny;
    }
    public double getVolume_24h_usd() {
        return this.volume_24h_usd;
    }
    public void setVolume_24h_usd(double volume_24h_usd) {
        this.volume_24h_usd = volume_24h_usd;
    }

    @Override
    public String toString() {
        return "CoinRateInfo{" +
                "ids=" + ids +
                ", available_supply=" + available_supply +
                ", id='" + id + '\'' +
                ", last_updated=" + last_updated +
                ", logo='" + logo + '\'' +
                ", logo_png='" + logo_png + '\'' +
                ", market_cap_cny=" + market_cap_cny +
                ", market_cap_usd=" + market_cap_usd +
                ", max_supply=" + max_supply +
                ", name='" + name + '\'' +
                ", percent_change_1h=" + percent_change_1h +
                ", percent_change_24h=" + percent_change_24h +
                ", percent_change_7d=" + percent_change_7d +
                ", price_btc=" + price_btc +
                ", price_cny=" + price_cny +
                ", price_usd=" + price_usd +
                ", rank=" + rank +
                ", symbol='" + symbol + '\'' +
                ", total_supply=" + total_supply +
                ", volume_24h_cny=" + volume_24h_cny +
                ", volume_24h_usd=" + volume_24h_usd +
                '}';
    }
}
