package kr.ds.config;

import android.content.Context;

public class Config {
	public Context mContext;
	
	public static String URL = "http://pctu1213.cafe24.com/app/";
	public static String URL_XML = "air_ticket/";
	public static String URL_LIST = "list.php";

	//gcm
	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
	public static final String TOKEN = "token";
	//gcmend
	
	public final static int MARKET = 1;
	public final static int TSTORE = 2;
	public final static int STORE_TYPE = MARKET;


	public static final String GCM_INTENT_FILLTER = "kr.inaweb.GCM_INTENT_FILLTER";




}
