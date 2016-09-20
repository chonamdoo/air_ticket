package kr.ds.air_ticket;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import kr.ds.config.Config;
import kr.ds.fragment.BaseFragment;
import kr.ds.fragment.List1Fragment;
import kr.ds.store.MainStoreTypeDialog;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;

public class MainActivity extends BaseActivity {
    private FragmentManager mFm;
    private FragmentTransaction mFt;
    private BaseFragment mFragment = null;
    private Toolbar mToolbar;
    private NativeAd mNativeAd;
    private LinearLayout mAdView;
    private Boolean isNativeCheck = false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private com.facebook.ads.InterstitialAd interstitialAdFackBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFaceBook();
        setNative();
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
        }
        mFragment = BaseFragment.newInstance(List1Fragment.class);
        setFragment(mFragment);

        if (checkPlayServices() && DsObjectUtils.getInstance(getApplicationContext()).isEmpty(SharedPreference.getSharedPreference(getApplicationContext(), Config.TOKEN))) { //토큰이 없는경우..
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }

        Log.i("TEST", SharedPreference.getSharedPreference(getApplicationContext(), Config.ANDROID_ID));
    }

    private void setFaceBook() {

        interstitialAdFackBook = new com.facebook.ads.InterstitialAd(getApplicationContext(), "294077530963387_300903850280755");
        interstitialAdFackBook.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                super.onError(ad, adError);
                Log.i("TEST","error");
                Log.i("TEST",adError.toString());

            }
            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                interstitialAdFackBook.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                super.onLoggingImpression(ad);
            }
        });
        interstitialAdFackBook.loadAd();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }


    private void setFragment(Fragment fragment) {
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.replace(R.id.content_frame, fragment);
        mFt.commitAllowingStateLoss();
    }

    public void setNative(){
        mNativeAd = new NativeAd(getApplicationContext(), "294077530963387_294077644296709");
        mNativeAd.setAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                super.onError(ad, adError);
                isNativeCheck = false;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                if(ad != mNativeAd){
                    isNativeCheck = false;
                    return;
                }
                isNativeCheck = true;
            }

            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                super.onLoggingImpression(ad);
            }
        });
        mNativeAd.loadAd();
    }
    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (KeyCode == KeyEvent.KEYCODE_BACK) {
                final MainStoreTypeDialog mMainDialog = new MainStoreTypeDialog(mNativeAd, isNativeCheck);// call the static method
                mMainDialog.show(getSupportFragmentManager(), "dialog");
                setNative();
                return true;
            }
        }
        return super.onKeyDown(KeyCode, event);
    }
    @Override
    protected void onDestroy() {
        if (interstitialAdFackBook != null) {
            interstitialAdFackBook.destroy();
        }
        super.onDestroy();
    }
}
