package com.tianqi.baselib.dbManager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences��װ
 * 
 * @author Administrator
 * 
 */
public class PrefUtils {

	public static final String PREF_NAME = "config";

	public static final String LANGUAGE = "language";
	public static final String HTTP_TOKEN = "http_token";
	private static final String SP_NAME = "poemTripSpref";
	public static final String FIRST_START_APP = "FIRST_START_APP";
	public static final String APP_UPDATE_TIME = "APP_UPDATE_TIME";

	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);

	}

	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).apply();
	}

	public static void setString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).apply();
	}

	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);

	}

	public static void setInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).apply();
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		return sp.getInt(key, defaultValue);
	}

	public static void setLong(Context context, String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).apply();
	}

	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

		return sp.getLong(key, defaultValue);
	}

	/**
	 * ɾ����������
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.apply();
	}

}
