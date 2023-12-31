package com.tianqi.baselib.utils.digital;

import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * author : zhangjing
 * date : 2020/8/5 14:55
 * description :数据整理工具类
 */
public class DataReshape {
    public static DecimalFormat df3  = new DecimalFormat("#0.00######");
    public static DecimalFormat df4  = new DecimalFormat("#0.0000####");
    public static DecimalFormat df6  = new DecimalFormat("#0.000000##");
    public static DecimalFormat df8  = new DecimalFormat("#0.00000000");
    public static DecimalFormat df8_int  = new DecimalFormat("#0.########");
    public static DecimalFormat df_int  = new DecimalFormat("#0.##");
//    public static DecimalFormat df3  = new DecimalFormat("#0.00000000");
    //对于大数的显示，不要显示科学计数法，全显示就好
    public static String doubleBig(double d,int decimal) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));

        // 四舍五入,保留decimal位小数
        return df3.format(d1.divide(d2,decimal,BigDecimal.ROUND_HALF_UP));
    }


    public static String doubleIntBig(double d,int decimal) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));

        // 四舍五入,保留decimal位小数
        return df8_int.format(d1.divide(d2,decimal,BigDecimal.ROUND_HALF_UP));
    }


    public static String doubleAll(double d,int decimal) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        DecimalFormat decimalFormat;
        switch (decimal){
            case 0:
                decimalFormat=df_int;
                break;
            case 2:
                decimalFormat=df3;
                break;
            case 4:
                decimalFormat=df4;
                break;
            case 6:
                decimalFormat=df6;
                break;
            case 8:
                decimalFormat=df8;
                break;
            default:
                decimalFormat=df3;
                break;
        }
        // 四舍五入,保留decimal位小数
        return decimalFormat.format(d1.divide(d2,decimal,BigDecimal.ROUND_HALF_UP));
    }

    public static String doubleBig(double d,int decimal,int show_zero_count) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留decimal位小数
        DecimalFormat decimalFormat;
        if (d>0){
            switch (show_zero_count){
                case 0:
                    decimalFormat=df_int;
                    break;
                case 2:
                    decimalFormat=df3;
                    break;
                case 4:
                    decimalFormat=df4;
                    break;
                case 6:
                    decimalFormat=df6;
                    break;
                case 8:
                    decimalFormat=df8;
                    break;
                default:
                    decimalFormat=df3;
                    break;
            }
        }else {
            decimalFormat=df3;
        }

        return decimalFormat.format(d1.divide(d2,decimal,BigDecimal.ROUND_HALF_UP));
    }

    public static String double2int(double d,int decimal) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));

        // 四舍五入,保留decimal位小数
        return df_int.format(d1.divide(d2,decimal,BigDecimal.ROUND_HALF_UP));
    }
}
