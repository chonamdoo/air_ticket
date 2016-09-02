package kr.ds.data;

import android.app.Activity;
import android.content.Context;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAdsManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import kr.ds.asynctask.DsAsyncTask;
import kr.ds.asynctask.DsAsyncTaskCallback;
import kr.ds.handler.ListHandler;
import kr.ds.httpclient.DsHttpClient;
import kr.ds.utils.DsObjectUtils;

/**
 * Created by Administrator on 2016-08-31.
 */
public class ListData extends BaseData {

    private String URL = "";
    private String PARAM = "";

    private ListHandler mListHandler;
    private ArrayList<ListHandler> mData;
    private BaseResultListener mResultListener;

    public ListData(){
    }


    @Override
    public BaseData clear() {
        if (mData != null) {
            mData = null;
        }
        mData = new ArrayList<ListHandler>();
        if (mListHandler != null) {
            mListHandler = null;
        }
        mListHandler = new ListHandler();
        return this;
    }

    @Override
    public BaseData setUrl(String url) {
        if(DsObjectUtils.isEmpty(URL)){
            URL = url;
        }
        return this;
    }
    @Override
    public BaseData setParam(String param) {
        PARAM = param;
        return this;
    }

    @Override
    public BaseData getView() {
        new DsAsyncTask<String[]>().setCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {

                String content = new DsHttpClient().HttpGet(URL + PARAM, "utf-8");
                JSONObject jsonObject = new JSONObject(content);
                JSONObject summeryjsonObject = jsonObject.getJSONObject("summery");
                String result = summeryjsonObject.getString("result");
                String[] summery = new String[2];
                summery[0] = summeryjsonObject.getString("result");
                summery[1] = summeryjsonObject.getString("msg");
                if (result.matches("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mData.add(new ListHandler());
                        if (mData.size() > 0) {
                            mListHandler = mData.get(mData.size() - 1);

                            mListHandler.setName(jsonObject1.getString("name"));
                            mListHandler.setImage(jsonObject1.getString("image"));
                            mListHandler.setTitle(jsonObject1.getString("title"));
                            mListHandler.setLink(jsonObject1.getString("link"));
                            mListHandler.setStart(jsonObject1.getString("start"));
                            mListHandler.setEnd(jsonObject1.getString("end"));
                            if(jsonObject1.getString("ad").matches("Y")) {
                                mListHandler.setIsLayout(1);
                            }else{
                                mListHandler.setIsLayout(0);
                            }
                            mListHandler.setNativeAd(null);
                        }
                    }
                }



                return summery;

            }
        }).setCallback(new DsAsyncTaskCallback<String[]>() {
            @Override
            public void onPreExecute() {
            }
            @Override
            public void onPostExecute(String[] result) {
                if (result[0].matches("success")) {
                    if (mResultListener != null) {
                        mResultListener.OnComplete(mData);
                        mResultListener.OnMessage(result[1]);
                    }
                } else {
                    if (mResultListener != null) {
                        mResultListener.OnMessage(result[1]);
                    }
                }
            }
            @Override
            public void onCancelled() {
            }
            @Override
            public void Exception(Exception e) {
                if (mResultListener != null) {
                    mResultListener.OnMessage(e.getMessage() + "");
                }
            }
        }).execute();
        return this;
    }

    @Override
    public <T> BaseData getViewPost(T post) {
        return this;
    }

    @Override
    public <T> BaseData setCallBack(T resultListener) {
        mResultListener = (BaseResultListener) resultListener;
        return this;
    }


}
