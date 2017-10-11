package kr.ds.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ds.adapter.ListRecyclerAdapter;
import kr.ds.air_ticket.R;
import kr.ds.config.Config;
import kr.ds.data.BaseResultListener;
import kr.ds.data.ListData;
import kr.ds.data.ListNativeData;
import kr.ds.handler.ListNativeHandler;
import kr.ds.widget.AdAdmobNativeView;
import kr.ds.widget.AdFaceBookNativeView;

/**
 * Created by Administrator on 2017-08-04.
 */

public class List1RecyclerFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {

    private ArrayList<ListNativeHandler> mData;
    private ArrayList<ListNativeHandler> mMainData;
    private int mNumber = 6;
    private int mPage = 1;
    private int startPage = 0;
    private int endPage = 0;

    private View mView;
    private ProgressBar mProgressBar;
    private ListData mListData;
    private Boolean mIsTheLoding = false;
    private SwipeRefreshLayout mSwipeLayout;
    private Context mContext;


    private final static int LIST = 0;
    private final static int ONLOAD = 1;
    private final static int REFRESH = 2;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private ListRecyclerAdapter mListRecyclerAdapter;
    private AdAdmobNativeView mAdAdmobNativeView;
    private AdFaceBookNativeView mAdFaceBookNativeView;

    private Object mNativeAd;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list1_recycler, null);

        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);
        mProgressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = (RecyclerView)mView.findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                int topRowVerticalPosition = (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeLayout.setEnabled(lastVisibleItemPosition == 0 && topRowVerticalPosition >= 0);
                if (!mIsTheLoding && isMaxScrollReached(recyclerView)) {
                    mIsTheLoding = true;
                    onLoadMore();
                }
            }
        });

        return mView;
    }

    private boolean isMaxScrollReached(RecyclerView recyclerView) {
        int maxScroll = recyclerView.computeVerticalScrollRange();
        int currentScroll = recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent();
        return currentScroll >= maxScroll;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mProgressBar.setVisibility(View.VISIBLE);
        setNative(LIST);

    }

    public void setList(){
        new ListNativeData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                mProgressBar.setVisibility(View.GONE);
                if(data != null){
                    mPage = 1;
                    mMainData = (ArrayList<ListNativeHandler>) data;
                    if(mMainData.size() - ((mPage-1)*mNumber) > 0){
                        if(mMainData.size() >= mPage * mNumber){
                            startPage = (mPage-1) * mNumber;
                            endPage = mPage * mNumber;
                        }else{
                            startPage = (mPage-1) * mNumber;
                            endPage = mMainData.size();
                        }
                        mData  = new ArrayList<>();
                        for(int i=startPage; i< endPage; i++){
                            if(mMainData.get(i).getIsLayout() == 1){
                                mMainData.get(i).setView(mNativeAd);
                            }
                            mData.add(mMainData.get(i));
                        }
                        mListRecyclerAdapter = new ListRecyclerAdapter(mContext, mData);
                        mRecyclerView.setAdapter(mListRecyclerAdapter);
                    }
                }else{
                    mRecyclerView.setAdapter(null);
                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_XML+ Config.URL_LIST).getView();
    }
    public void setListOnLoad(){
        mPage++;
        if(mMainData.size() - ((mPage-1)*mNumber) < 0){
            mIsTheLoding = true;
        }else{
            if(mMainData.size() >= mPage * mNumber){
                startPage = (mPage-1) * mNumber;
                endPage = mPage * mNumber;
            }else{
                startPage = (mPage-1) * mNumber;
                endPage = mMainData.size();
            }
            for(int i=startPage; i< endPage; i++){
                if(mMainData.get(i).getIsLayout() == 1){
                    mMainData.get(i).setView(mNativeAd);
                }
                mData.add(mMainData.get(i));
            }
            mListRecyclerAdapter.notifyItemRangeInserted(mPage*mNumber, mNumber);
            mIsTheLoding = false;
        }
        mProgressBar.setVisibility(View.GONE);
    }

    public void setListRefresh(){
        new ListNativeData().clear().setCallBack(new BaseResultListener() {
            @Override
            public <T> void OnComplete() {

            }
            @Override
            public <T> void OnComplete(Object data) {
                mSwipeLayout.setRefreshing(false);
                mIsTheLoding = false;
                if(data != null){
                    mPage = 1;
                    mData  = new ArrayList<>();
                    mMainData = (ArrayList<ListNativeHandler>) data;
                    if(mMainData.size() - ((mPage-1)*mNumber) > 0){
                        if(mMainData.size() >= mPage * mNumber){
                            startPage = (mPage-1) * mNumber;
                            endPage = mPage * mNumber;
                        }else{
                            startPage = (mPage-1) * mNumber;
                            endPage = mMainData.size();
                        }
                        mData  = new ArrayList<>();
                        for(int i=startPage; i< endPage; i++){
                            if(mMainData.get(i).getIsLayout() == 1){
                                mMainData.get(i).setView(mNativeAd);
                            }
                            mData.add(mMainData.get(i));
                        }
                        mListRecyclerAdapter = new ListRecyclerAdapter(mContext, mData);
                        mRecyclerView.setAdapter(mListRecyclerAdapter);
                    }
                }else{
                    mRecyclerView.setAdapter(null);
                }
            }

            @Override
            public void OnMessage(String str) {

            }
        }).setUrl(Config.URL+ Config.URL_XML+ Config.URL_LIST).getView();
    }

    public void setNative(final int type){
        mAdFaceBookNativeView = new AdFaceBookNativeView(mContext);
        mAdFaceBookNativeView.setLayout(R.layout.native_facebook).setCallBack(new AdFaceBookNativeView.ResultListener() {
            @Override
            public <T> void OnLoad(T data) {
                Log.i("TEST", "OnLoad");
                mNativeAd = (Object) data;
                if(type == LIST){
                    setList();
                }else if(type == ONLOAD){
                    setListOnLoad();
                }else if(type == REFRESH){
                    setListRefresh();
                }

            }
            @Override
            public <T> void OnFail() {
                Log.i("TEST", "OnFail");
                mAdAdmobNativeView = new AdAdmobNativeView(mContext);
                mAdAdmobNativeView.setLayout().setCallBack(new AdAdmobNativeView.ResultListener() {
                    @Override
                    public <T> void OnLoad(T data) {
                        mNativeAd = (Object) data;
                        if(type == LIST){
                            setList();
                        }else if(type == ONLOAD){
                            setListOnLoad();
                        }else if(type == REFRESH){
                            setListRefresh();
                        }
                    }

                    @Override
                    public <T> void OnFail() {
                        if(type == LIST){
                            setList();
                        }else if(type == ONLOAD){
                            setListOnLoad();
                        }else if(type == REFRESH){
                            setListRefresh();
                        }
                    }
                });
            }
        });
    }


    public void onLoadMore(){
        mProgressBar.setVisibility(View.VISIBLE);
        setNative(ONLOAD);
    }

    @Override
    public void onRefresh() {
        setNative(REFRESH);
    }

}
