package kr.ds.air_ticket;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import kr.ds.fragment.BaseFragment;
import kr.ds.fragment.List1Fragment;

public class MainActivity extends BaseActivity {
    private FragmentManager mFm;
    private FragmentTransaction mFt;
    private BaseFragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment = BaseFragment.newInstance(List1Fragment.class);
        setFragment(mFragment);
    }


    private void setFragment(Fragment fragment) {
        mFm = getSupportFragmentManager();
        mFt = mFm.beginTransaction();
        mFt.replace(R.id.content_frame, fragment);
        mFt.commitAllowingStateLoss();
    }
}
