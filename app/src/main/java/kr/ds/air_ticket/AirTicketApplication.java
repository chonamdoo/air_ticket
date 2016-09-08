package kr.ds.air_ticket;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import kr.ds.config.Config;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;
import kr.ds.utils.UniqueID;


public class AirTicketApplication extends MultiDexApplication {
	private ImageLoader imageLoader;
	/**
	 * 이미지 로더, 이미지 캐시, 요청 큐를 초기화한다.
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		if(DsObjectUtils.isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.ANDROID_ID))){
			SharedPreference.putSharedPreference(getApplicationContext(), Config.ANDROID_ID, UniqueID.getUniqueID());
			Log.i("TEST",UniqueID.getUniqueID()+"");
		}

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.delayBeforeLoading(100).cacheInMemory().cacheOnDisc().build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(
				defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}
}
