package kr.ds.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import kr.ds.air_ticket.R;
import kr.ds.handler.ListNativeHandler;
import kr.ds.utils.DsDateUtils;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.ScreenUtils;

/**
 * Created by Administrator on 2016-08-31.
 */
public class ListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ListNativeHandler> mData;
    private LayoutInflater mInflater;
    private static final int TYPE_BASIC = 0;
    private static final int TYPE_AD = 1;

    public ListRecyclerAdapter(Context context, ArrayList<ListNativeHandler> data) {
        mContext = context;
        mData = data;
    }

    public int getWidth(){
        Point p = new Point();
        p.x = mContext.getResources().getDisplayMetrics().widthPixels;
        return p.x;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case TYPE_BASIC:
                View basicItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.fragment_list1_item1, parent, false);
                return new BasicViewHolder(basicItemLayoutView);
            case TYPE_AD:
            default:
                View adItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.fragment_list1_item3, parent, false);
                return new AdViewHolder(adItemLayoutView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_BASIC:
                final BasicViewHolder basicItemHolder = (BasicViewHolder) holder;

                if(!DsObjectUtils.isEmpty(mData.get(position).getStart()) && !DsObjectUtils.isEmpty(mData.get(position).getEnd())){
                    try {
                        basicItemHolder.textViewDate.setVisibility(View.VISIBLE);
                        String sdate = DsDateUtils.getInstance().getDateDay(mData.get(position).getStart().trim(),"yy-mm-dd");
                        String edate = DsDateUtils.getInstance().getDateDay(mData.get(position).getEnd().trim(),"yy-mm-dd");
                        basicItemHolder.textViewDate.setText(sdate+" ~ "+edate);
                    } catch (Exception e) {
                        basicItemHolder.textViewDate.setVisibility(View.GONE);
                        basicItemHolder.textViewDate.setText("");
                        e.printStackTrace();
                    }
                }else {
                    basicItemHolder.textViewDate.setVisibility(View.GONE);
                    basicItemHolder.textViewDate.setText("");

                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getTitle())){
                    basicItemHolder.textViewTitle.setVisibility(View.VISIBLE);
                    basicItemHolder.textViewTitle.setText(mData.get(position).getTitle());
                }else {
                    basicItemHolder.textViewTitle.setVisibility(View.GONE);
                    basicItemHolder.textViewTitle.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getName())){
                    basicItemHolder.textViewName.setVisibility(View.VISIBLE);
                    basicItemHolder.textViewName.setText(mData.get(position).getName());
                }else {
                    basicItemHolder.textViewName.setVisibility(View.GONE);
                    basicItemHolder.textViewName.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getImage())){
                    basicItemHolder.imageView.setVisibility(View.VISIBLE);
                    basicItemHolder.progressBar.setVisibility(View.VISIBLE);

                    basicItemHolder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent NextIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getLink()));
                            mContext.startActivity(NextIntent);
                        }
                    });

                    Glide.with(mContext)
                            .load(mData.get(position).getImage())
                            .thumbnail(0.5f)
                            .override(500,500)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    basicItemHolder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    basicItemHolder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(new ImageViewTarget<GlideDrawable>(basicItemHolder.imageView) {
                                @Override
                                protected void setResource(GlideDrawable resource) {
                                    basicItemHolder.imageView.setVisibility(View.VISIBLE);
                                    int width =  getWidth();
                                    int height = (int) (resource.getIntrinsicHeight() *(width/(float)resource.getIntrinsicWidth()));
                                    basicItemHolder.imageView.setLayoutParams(new FrameLayout.LayoutParams(width,height));
                                    basicItemHolder.imageView.setImageDrawable(resource);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                    basicItemHolder.progressBar.setVisibility(View.GONE);
                                    Glide.with(mContext)
                                            .load("http://pctu1213.cafe24.com/app/air_ticket/images/no_img.jpg")
                                            .override(500,500)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(new ImageViewTarget<GlideDrawable>(basicItemHolder.imageView) {
                                                @Override
                                                protected void setResource(GlideDrawable resource) {
                                                    basicItemHolder.imageView.setVisibility(View.VISIBLE);
                                                    int width = getWidth() - ScreenUtils.getInstacne().getPixelFromDPI(mContext, 14);
                                                    int height = (int) (resource.getIntrinsicHeight() * (width / (float) resource.getIntrinsicWidth()));
                                                    basicItemHolder.imageView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
                                                    basicItemHolder.imageView.setImageDrawable(resource);
                                                }
                                            });
                                }
                            });
                }else{
                    basicItemHolder.imageView.setVisibility(View.GONE);
                    basicItemHolder.progressBar.setVisibility(View.GONE);
                }

                basicItemHolder.button_share.setOnClickListener(new View.OnClickListener() {
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
                    basicItemHolder.button_link.setVisibility(View.VISIBLE);
                    basicItemHolder.button_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent NextIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getReservation_link()));
                            mContext.startActivity(NextIntent);
                        }
                    });
                }else {
                    basicItemHolder.button_link.setVisibility(View.GONE);
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getContent())){
                    basicItemHolder.textViewContent.setVisibility(View.VISIBLE);
                    basicItemHolder.textViewContent.setText(mData.get(position).getContent());
                }else {
                    basicItemHolder.textViewContent.setVisibility(View.GONE);
                    basicItemHolder.textViewContent.setText("");
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getIcon())){
                    basicItemHolder.imageViewIcon.setVisibility(View.VISIBLE);

                    Glide.with(mContext)
                            .load(mData.get(position).getIcon())
                            .thumbnail(0.5f)
                            .override(50,50)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new ImageViewTarget<GlideDrawable>(basicItemHolder.imageViewIcon) {
                                @Override
                                protected void setResource(GlideDrawable resource) {
                                    basicItemHolder.imageViewIcon.setImageDrawable(resource);
                                }
                            });
                }else {
                    basicItemHolder.imageViewIcon.setVisibility(View.GONE);
                }

                if(!DsObjectUtils.isEmpty(mData.get(position).getRegdate())){
                    basicItemHolder.textViewRegdate.setVisibility(View.VISIBLE);
                    basicItemHolder.textViewRegdate.setText(mData.get(position).getRegdate());
                }else {
                    basicItemHolder.textViewRegdate.setVisibility(View.GONE);
                    basicItemHolder.textViewRegdate.setText("");
                }

                basicItemHolder.setClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        String url = mData.get(position).getLink();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(intent);
                    }
                });

                break;
            case TYPE_AD:
                final AdViewHolder adItemHolder = (AdViewHolder) holder;
                if (adItemHolder.frameLayoutNative.getChildCount() > 0) {
                    adItemHolder.frameLayoutNative.removeAllViews();
                }
                if((View) mData.get(position).getView() != null) {
                    adItemHolder.frameLayoutNative.setVisibility(View.VISIBLE);
                    if (((View) mData.get(position).getView()).getParent() != null) {
                        ((ViewGroup) ((View) mData.get(position).getView()).getParent()).removeView((View) mData.get(position).getView());
                    }
                    adItemHolder.frameLayoutNative.addView((View) mData.get(position).getView());
                }else{
                    adItemHolder.frameLayoutNative.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getIsLayout();
    }


    public static class BasicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {//view 홀더

        ImageView imageView;
        TextView textViewTitle, textViewName, textViewDate, textViewText, textViewContent, textViewRegdate ;
        Button button_link;
        Button button_share;
        ImageView imageViewIcon;


        ProgressBar progressBar;
        ItemClickListener clickListener;

        public BasicViewHolder(View v) {
            super(v);
            textViewTitle = (TextView) v.findViewById(R.id.textView_title);
            textViewDate = (TextView) v.findViewById(R.id.textView_date);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            textViewName = (TextView) v.findViewById(R.id.textView_name);
            button_link = (Button) v.findViewById(R.id.button_link);
            button_share = (Button) v.findViewById(R.id.button_share);
            textViewContent = (TextView) v.findViewById(R.id.textView_content);

            imageViewIcon = (ImageView) v.findViewById(R.id.imageView_icon);
            textViewRegdate = (TextView) v.findViewById(R.id.textView_regdate);

            v.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(v, getLayoutPosition());
            }
        }
    }
    public interface ItemClickListener {
        void onClick(View view, int position);
    }


    public static class AdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {//view 홀더
        public LinearLayout linearLayoutNative;
        public FrameLayout frameLayoutNative;
        public View viewNative;
        ItemClickListener clickListener;

        public AdViewHolder(View v) {
            super(v);
            linearLayoutNative = (LinearLayout)v.findViewById(R.id.lineLayout_native);
            viewNative = (View)v.findViewById(R.id.view_native);
            frameLayoutNative = (FrameLayout) v.findViewById(R.id.frameLayout_native);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(v, getLayoutPosition());
            }
        }
    }
}