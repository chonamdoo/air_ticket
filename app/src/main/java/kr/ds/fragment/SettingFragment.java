package kr.ds.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


import kr.ds.air_ticket.R;
import kr.ds.config.Config;
import kr.ds.handler.VersionCheckHandler;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;
import kr.ds.utils.VersionUtils;
import kr.ds.utils.gcmHandler;


public class SettingFragment extends BaseFragment implements OnClickListener{

	private View mView;
	private CheckBox mCheckBoxPush;
	private ArrayList<VersionCheckHandler> DATA = new ArrayList<VersionCheckHandler>();
    
	private Context mContext;
	private String regId;
	private String androidId;
	private String url = "http://pctu1213.cafe24.com/app/air_ticket/gcm/gcm_send_check.php";
	private String updateurl = "http://pctu1213.cafe24.com/app/air_ticket/gcm/gcm_send_update.php";
	private TextView mTextViewVersion;
	private SharedPreferences sharedPreferences;
	private LinearLayout mLinearLayoutPush;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = getActivity();	
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mView = inflater.inflate(R.layout.setting, container, false);
		mLinearLayoutPush = (LinearLayout)mView.findViewById(R.id.linearLayout_push);
		(mCheckBoxPush = (CheckBox)mView.findViewById(R.id.checkBox_push)).setOnClickListener(this);
		mTextViewVersion = (TextView)mView.findViewById(R.id.textView_version);
		return mView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mTextViewVersion.setText(new VersionUtils().VersionName(mContext));

		if(!DsObjectUtils.getInstance(getActivity()).isEmpty(SharedPreference.getSharedPreference(getActivity(), Config.TOKEN))){
			mLinearLayoutPush.setVisibility(View.VISIBLE);
			regId = sharedPreferences.getString(Config.TOKEN , "");
			androidId = sharedPreferences.getString(Config.ANDROID_ID , "");
			if(!DsObjectUtils.getInstance(mContext).isEmpty(regId) && !DsObjectUtils.getInstance(mContext).isEmpty(androidId)) {
				new regSendCheckTask().execute(androidId, regId, "", url);
			}
		}else{
			mLinearLayoutPush.setVisibility(View.GONE);
		}
	}
	
	private class regSendCheckTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			return new gcmHandler().HttpPostData(params[0], params[1],params[2],params[3]);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.trim().matches("Y")){//gcm id값이 없을경우
				setBackgroundChecked(mCheckBoxPush, true);
			}else{
				setBackgroundChecked(mCheckBoxPush, false);
			}
		}
	}
	
	public void setBackgroundChecked(CheckBox toggleButton, boolean ischeck){
		if(ischeck == false){
			toggleButton.setChecked(false);
		}else{
			toggleButton.setChecked(true);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			
		case R.id.checkBox_push:
			if(mCheckBoxPush.isChecked() == true){
				new regSendCheckTask().execute(androidId, regId, "Y",updateurl);
				setBackgroundChecked(mCheckBoxPush, true);
			}else{	
				new regSendCheckTask().execute(androidId, regId, "N",updateurl);
				setBackgroundChecked(mCheckBoxPush, false);
			}
			break;
		}
	} 
	
}
