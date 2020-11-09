package com.tianqi.baselib.utils.digital;

import com.tianqi.baselib.utils.Constant;

import java.security.MessageDigest;

public class MD5 {
	// MD5変換
	public static String Md5(String source) {
		StringBuffer sbonce = new StringBuffer(32);
		StringBuffer sbtwice = new StringBuffer(32);

		try {
			//第一次加密
			if (true){
				source=source+ Constant.PASSWORD_SALT_ONE;
				MessageDigest md 	= MessageDigest.getInstance("MD5");
				byte[] array 		= md.digest(source.getBytes("utf-8"));

				for (int i = 0; i < array.length; i++) {
					sbonce.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
				}
			}
			//第二次加密
			if (true){
				String 	source2=Constant.PASSWORD_SALT_TWO+sbonce.toString();
				MessageDigest md 	= MessageDigest.getInstance("MD5");
				byte[] array 		= md.digest(source2.getBytes("utf-8"));

				for (int i = 0; i < array.length; i++) {
					sbtwice.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
				}
			}
		} catch (Exception e) {
			//			logger.error("Can not encode the string '" + source + "' to MD5!", e);
			return null;
		}
		return sbtwice.toString();
	}


}
