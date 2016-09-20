package kr.ds.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import kr.ds.air_ticket.R;
import kr.ds.config.Config;

/*
 * 종료 시 팝업 관련 
 * 티스토어 및 마켓 구분 onclick 리스너 수정 
 */
@SuppressLint("ValidFragment")
public class MainStoreTypeDialog extends DialogFragment implements OnClickListener{
	private Button mButton1, mButton2, mButton3, mButton4, mButton5;
	//private CheckBox mCheckBox;
	//private ResultListner mResultListner;//리턴콜백

	private TextView mTextView1, mTextView2, mTextView3, mTextView4;

	private LinearLayout  mNativeAdContainer;
	private LinearLayout mAdView;
	private Boolean isNativeCheck = false;
	private NativeAd mNativeAd;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	public MainStoreTypeDialog(NativeAd nativeAd, Boolean isnativcheck) {
		// TODO Auto-generated constructor stub
		mNativeAd = nativeAd;
		isNativeCheck = isnativcheck;

	}
	
	private DialogResultListner mDialogResultListner;
	public interface DialogResultListner {
		public void onCancel();
	}
	public MainStoreTypeDialog callback (DialogResultListner dialogresultlistner){
		this.mDialogResultListner = dialogresultlistner;
		return this;
	}

	private void setView(){
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		mAdView = (LinearLayout)inflater.inflate(R.layout.dialog_nativie, mNativeAdContainer, false);
		mNativeAdContainer.addView(mAdView);
		// Create native UI using the ad metadata.
		ImageView nativeAdIcon = (ImageView)mAdView.findViewById(R.id.native_ad_icon);
		TextView nativeAdTitle = (TextView)mAdView.findViewById(R.id.native_ad_title);
		TextView nativeAdBody = (TextView)mAdView.findViewById(R.id.native_ad_body);
		MediaView nativeAdMedia = (MediaView)mAdView.findViewById(R.id.native_ad_media);
		TextView nativeAdSocialContext = (TextView)mAdView.findViewById(R.id.native_ad_social_context);
		Button nativeAdCallToAction = (Button)mAdView.findViewById(R.id.native_ad_call_to_action);

		// Setting the Text.
		nativeAdSocialContext.setText(mNativeAd.getAdSocialContext());
		nativeAdCallToAction.setText(mNativeAd.getAdCallToAction());
		nativeAdTitle.setText(mNativeAd.getAdTitle());
		nativeAdBody.setText(mNativeAd.getAdBody());

		// Downloading and setting the ad icon.
		NativeAd.Image adIcon = mNativeAd.getAdIcon();
		NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

		// Download and setting the cover image.
		NativeAd.Image adCoverImage = mNativeAd.getAdCoverImage();
		nativeAdMedia.setNativeAd(mNativeAd);

		// Add adChoices icon
		AdChoicesView adChoicesView = null;
		if (adChoicesView == null) {
			adChoicesView = new AdChoicesView(getActivity(), mNativeAd, true);
			mAdView.addView(adChoicesView, 0);
		}

		mNativeAd.registerViewForInteraction(mAdView);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
		LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
		View view = mLayoutInflater.inflate(R.layout.dialog, null);
		mNativeAdContainer = (LinearLayout)view.findViewById(R.id.linearLayout_native);
		(mTextView1 = (TextView)view.findViewById(R.id.textView1)).setOnClickListener(this);
		(mTextView3 = (TextView)view.findViewById(R.id.textView3)).setOnClickListener(this);
		(mTextView4 = (TextView)view.findViewById(R.id.textView4)).setOnClickListener(this);

		if(isNativeCheck){
			setView();
		}else{
			mNativeAdContainer.setVisibility(View.GONE);
		}

		mBuilder.setView(view);
		return mBuilder.create();
	}

	private void MarketLink(int type){
		switch (type) {
		case 1:
			try {
				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id="+ Config.APP_DOWN_URL_MARKET)));
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getActivity(), R.string.popupview_null,Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textView1: //리뷰 달기
			if(Config.TYPE == Config.MARKET){
				MarketLink(1);
			}
			break;
		case R.id.textView3:
			dismiss();
			break;
		case R.id.textView4:
			getActivity().finish();
			break;
		default:
			break;
		}
	}
}
