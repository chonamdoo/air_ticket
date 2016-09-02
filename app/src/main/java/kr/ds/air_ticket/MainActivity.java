package kr.ds.air_ticket;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;

import kr.ds.fragment.BaseFragment;
import kr.ds.fragment.List1Fragment;
import kr.ds.store.MainStoreTypeDialog;
import kr.ds.utils.SharedPreference;

public class MainActivity extends BaseActivity {
    private FragmentManager mFm;
    private FragmentTransaction mFt;
    private BaseFragment mFragment = null;
    private Toolbar mToolbar;
    private NativeAd mNativeAd;
    private LinearLayout mAdView;
    private Boolean isNativeCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNative();
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
        }
        mFragment = BaseFragment.newInstance(List1Fragment.class);
        setFragment(mFragment);
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
}
