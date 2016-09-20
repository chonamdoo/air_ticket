package kr.ds.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.Toast;

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
import kr.ds.utils.Helper;
import kr.ds.utils.ScreenUtils;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            switch (getItemViewType(position)){
                case TYPE_BASIC:
                    convertView = mInflater.inflate(R.layout.fragment_list1_item1,null);
                    holder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);
                    holder.textViewDate = (TextView) convertView.findViewById(R.id.textView_date);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    holder.progressbar = (ProgressBar) convertView.findViewById(R.id.progressBar);
                    holder.textViewName = (TextView) convertView.findViewById(R.id.textView_name);
                    holder.button_link = (Button) convertView.findViewById(R.id.button_link);
                    holder.button_share = (Button) convertView.findViewById(R.id.button_share);

                    break;
                case TYPE_AD:
                    convertView = mInflater.inflate(R.layout.fragment_list1_item2_facebook,null);
                    holder.linearLayoutNative = (LinearLayout) convertView.findViewById(R.id.linearLayout_native);
                    holder.linearLayoutNative2 = (LinearLayout) convertView.findViewById(R.id.linearLayout_native2);
                    holder.linearLayoutNative3 = (LinearLayout) convertView.findViewById(R.id.linearLayout_native3);
                    holder.nativeAdIcon = (ImageView) convertView.findViewById(R.id.native_ad_icon);
                    holder.nativeAdTitle = (TextView) convertView.findViewById(R.id.native_ad_title);
                    holder.nativeAdBody = (TextView) convertView.findViewById(R.id.native_ad_body);
                    holder.nativeAdMedia = (MediaView) convertView.findViewById(R.id.native_ad_media);
                    holder.nativeAdSocialContext = (TextView) convertView.findViewById(R.id.native_ad_social_context);
                    holder.nativeAdCallToAction = (Button) convertView.findViewById(R.id.native_ad_call_to_action);

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
                        holder.textViewDate.setVisibility(View.VISIBLE);
                        String sdate = DsDateUtils.getInstance().getDateDay(mData.get(position).getStart().trim(),"yy-mm-dd");
                        String edate = DsDateUtils.getInstance().getDateDay(mData.get(position).getEnd().trim(),"yy-mm-dd");
                        holder.textViewDate.setText(sdate+" ~ "+edate);
                    } catch (Exception e) {
                        holder.textViewDate.setVisibility(View.GONE);
                        holder.textViewDate.setText("");
                        e.printStackTrace();
                    }
                }else {
                    holder.textViewDate.setVisibility(View.GONE);
                    holder.textViewDate.setText("");

                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getTitle())){
                    holder.textViewTitle.setVisibility(View.VISIBLE);
                    holder.textViewTitle.setText(mData.get(position).getTitle());
                }else {
                    holder.textViewTitle.setVisibility(View.GONE);
                    holder.textViewTitle.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getName())){
                    holder.textViewName.setVisibility(View.VISIBLE);
                    holder.textViewName.setText(mData.get(position).getName());
                }else {
                    holder.textViewName.setVisibility(View.GONE);
                    holder.textViewName.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getImage())){

                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent NextIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getLink()));
                            mContext.startActivity(NextIntent);
                        }
                    });

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

                           int width =  getWidth()- ScreenUtils.getInstacne().getPixelFromDPI(mContext, 14);
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

                holder.button_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent NextIntent = new Intent(Intent.ACTION_SEND);
                            NextIntent.setType("text/plain");
                            NextIntent.putExtra(Intent.EXTRA_SUBJECT, "[" + mData.get(position).getName() + "]" + mData.get(position).getTitle());
                            NextIntent.putExtra(Intent.EXTRA_TEXT, "반갑습니다.^^ 원터치 특가 항공권 입니다.\n\n바로가기:\n" + mData.get(position).getLink() + "\n\n" + "어플다운:\n" + "https://play.google.com/store/apps/details?id=kr.ds.air_ticket");
                            mContext.startActivity(Intent.createChooser(NextIntent, "[" + mData.get(position).getName() + "]" + mData.get(position).getTitle() + " 공유하기"));
                        }catch (Exception e){
                            Toast.makeText(mContext,"오류가 발생되었습니다.계속문제가 발생시 관리자에게 문의 해주시기 바랍니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(!DsObjectUtils.isEmpty(mData.get(position).getReservation_link())){
                    holder.button_link.setVisibility(View.VISIBLE);
                    holder.button_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent NextIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getReservation_link()));
                            mContext.startActivity(NextIntent);
                        }
                    });
                }else {
                    holder.button_link.setVisibility(View.GONE);
                }
                break;
            case TYPE_AD:

                Log.i("TEST","1");
                if(mData.get(position).getNativeAd() != null) {
                    Log.i("TEST","2");
                    if (mData.get(position).getNativeAd().isAdLoaded()) {
                        Log.i("TEST","3");
                        holder.nativeAdMedia.setVisibility(View.VISIBLE);
                        holder.linearLayoutNative.setVisibility(View.VISIBLE);
                        holder.linearLayoutNative2.setVisibility(View.VISIBLE);
                        holder.linearLayoutNative3.setVisibility(View.VISIBLE);
                        // Setting the Text.
                        holder.nativeAdSocialContext.setText(mData.get(position).getNativeAd().getAdSocialContext());
                        holder.nativeAdCallToAction.setText(mData.get(position).getNativeAd().getAdCallToAction());
                        holder.nativeAdTitle.setText(mData.get(position).getNativeAd().getAdTitle());
                        holder.nativeAdBody.setText(mData.get(position).getNativeAd().getAdBody());

                        // Downloading and setting the ad icon.
                        NativeAd.Image adIcon = mData.get(position).getNativeAd().getAdIcon();
                        NativeAd.downloadAndDisplayImage(adIcon, holder.nativeAdIcon);

                        // Download and setting the cover image.
                        NativeAd.Image adCoverImage = mData.get(position).getNativeAd().getAdCoverImage();
                        holder.nativeAdMedia.setNativeAd(mData.get(position).getNativeAd());

                        mData.get(position).getNativeAd().registerViewForInteraction(convertView);
                    } else {
                        Log.i("TEST","4");
                        holder.nativeAdMedia.setVisibility(View.GONE);
                        holder.linearLayoutNative.setVisibility(View.GONE);
                        holder.linearLayoutNative2.setVisibility(View.GONE);
                        holder.linearLayoutNative3.setVisibility(View.GONE);
                    }
                }else{
                    Log.i("TEST","5");
                    holder.nativeAdMedia.setVisibility(View.GONE);
                    holder.linearLayoutNative.setVisibility(View.GONE);
                    holder.linearLayoutNative2.setVisibility(View.GONE);
                    holder.linearLayoutNative3.setVisibility(View.GONE);
                }
                break;
        }
        return convertView;
    }


    class ViewHolder {
        ImageView imageView;
        ProgressBar progressbar;
        TextView textViewTitle, textViewName, textViewDate, textViewText ;
        Button button_link;
        Button button_share
                ;

        LinearLayout linearLayoutNative;
        LinearLayout linearLayoutNative2;
        LinearLayout linearLayoutNative3;
        ImageView nativeAdIcon;
        TextView nativeAdTitle;
        TextView nativeAdBody;
        MediaView nativeAdMedia;
        TextView nativeAdSocialContext;
        Button nativeAdCallToAction;

    }

}