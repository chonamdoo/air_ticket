package kr.ds.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;

import kr.ds.air_ticket.R;
import kr.ds.handler.ListHandler;
import kr.ds.utils.DsDateUtils;
import kr.ds.utils.DsObjectUtils;

/**
 * Created by Administrator on 2016-08-31.
 */
public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ListHandler> mData;
    private LayoutInflater mInflater;
    private final ImageLoader imageDownloader = ImageLoader.getInstance();

    private static final int TYPE_BASIC = 0;
    private static final int TYPE_AD = 1;
    private NativeAd mNativeAd;

    public ListAdapter(Context context, ArrayList<ListHandler> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public int getWidth(){
        Point p = new Point();
        p.x = mContext.getResources().getDisplayMetrics().widthPixels;
        return p.x;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getIsLayout();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (getItemViewType(position)){
                case TYPE_BASIC:
                    convertView = mInflater.inflate(R.layout.fragment_list1_item1,null);

                    holder.textViewDate = (TextView) convertView.findViewById(R.id.textView_date);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    holder.progressbar = (ProgressBar) convertView.findViewById(R.id.progressBar);
                    holder.textViewName = (TextView) convertView.findViewById(R.id.textView_name);

                    break;
                case TYPE_AD:
                    convertView = mInflater.inflate(R.layout.fragment_list1_item2_facebook,null);
                    //holder.linearLayoutNative = (LinearLayout)convertView.findViewById(R.id.linearLayout_native);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (getItemViewType(position)) {
            case TYPE_BASIC:

                if(!DsObjectUtils.isEmpty(mData.get(position).getStart()) && !DsObjectUtils.isEmpty(mData.get(position).getEnd())){
                    try {
                        String sdate = DsDateUtils.getInstance().getDateDay(mData.get(position).getStart().trim(),"yy-mm-dd");
                        String edate = DsDateUtils.getInstance().getDateDay(mData.get(position).getEnd().trim(),"yy-mm-dd");
                        holder.textViewDate.setText(sdate+" ~ "+edate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    holder.textViewDate.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getTitle())){
                    holder.textViewName.setText(mData.get(position).getTitle());
                }else {
                    holder.textViewName.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getImage())){
                    imageDownloader.displayImage(mData.get(position).getImage(), holder.imageView, new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String arg0, View arg1) {
                            // TODO Auto-generated method stub
                            holder.imageView.setVisibility(View.INVISIBLE);
                            holder.progressbar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                            // TODO Auto-generated method stub
                            holder.imageView.setVisibility(View.GONE);
                            holder.progressbar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                            // TODO Auto-generated method stub
                            int width =  getWidth();
                            int height = (int) (arg2.getHeight() *(width/(float)arg2.getWidth()));
                            holder.imageView.setLayoutParams(new FrameLayout.LayoutParams(width,height));
                            holder.imageView.setVisibility(View.VISIBLE);
                            holder.progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String arg0, View arg1) {
                            // TODO Auto-generated method stub
                            holder.progressbar.setVisibility(View.GONE);
                        }
                    });
                }else{
                    holder.imageView.setVisibility(View.GONE);
                    holder.progressbar.setVisibility(View.GONE);
                }
                break;
            case TYPE_AD:

                final NativeAd mNativeAd = new NativeAd(mContext, "294077530963387_294077644296709");
                final View ConvertView = convertView;
                mNativeAd.setAdListener(new AbstractAdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        super.onError(ad, adError);
                        //holder.linearLayoutNative.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        super.onAdLoaded(ad);
                        if(ad != mNativeAd){
                            //holder.linearLayoutNative.setVisibility(View.GONE);
                            return;
                        }
                        //holder.linearLayoutNative.setVisibility(View.VISIBLE);


                        //holder.linearLayoutNative.addView(mAdView);

                        // Create native UI using the ad metadata.
                        ImageView nativeAdIcon = (ImageView)ConvertView.findViewById(R.id.native_ad_icon);
                        TextView nativeAdTitle = (TextView)ConvertView.findViewById(R.id.native_ad_title);
                        TextView nativeAdBody = (TextView)ConvertView.findViewById(R.id.native_ad_body);
                        MediaView nativeAdMedia = (MediaView)ConvertView.findViewById(R.id.native_ad_media);
                        TextView nativeAdSocialContext = (TextView)ConvertView.findViewById(R.id.native_ad_social_context);
                        Button nativeAdCallToAction = (Button)ConvertView.findViewById(R.id.native_ad_call_to_action);

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
//                        AdChoicesView adChoicesView = null;
//                        if (adChoicesView == null) {
//                            adChoicesView = new AdChoicesView(mContext, mNativeAd, true);
//                            ConvertView.addView(adChoicesView, 0);
//                        }
                        mNativeAd.registerViewForInteraction(ConvertView);
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

                break;
        }
        return convertView;
    }


    class ViewHolder {
        ImageView imageView;
        ProgressBar progressbar;
        TextView textViewName, textViewDate, textViewText ;

        LinearLayout linearLayoutNative;
    }

}