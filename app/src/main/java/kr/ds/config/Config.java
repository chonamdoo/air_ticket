package kr.ds.config;

import android.content.Context;

public class Config {
	public Context mContext;
	
	public static String URL = "http://pctu1213.cafe24.com/app/";
	public static String URL_XML = "air_ticket/";
	public static String URL_LIST = "list_v2.php";

	//gcm
	public static final String ANDROID_ID = "android_id";
	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
	public static final String TOKEN = "token";
	//gcmend
	
	public final static int MARKET = 1;
	public final static int TSTORE = 2;
	public final static int TYPE = MARKET;

	public static String APP_DOWN_URL_MARKET = "kr.ds.air_ticket";
	public static String APP_DOWN_TITLE = "원터치 특가 항공권";

	public static final String GCM_INTENT_FILLTER = "kr.ds.GCM_INTENT_FILLTER";

}
