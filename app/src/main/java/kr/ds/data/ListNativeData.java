package kr.ds.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import kr.ds.asynctask.DsAsyncTask;
import kr.ds.asynctask.DsAsyncTaskCallback;
import kr.ds.handler.ListNativeHandler;
import kr.ds.httpclient.DsHttpClient;
import kr.ds.utils.DsObjectUtils;

/**
 * Created by Administrator on 2016-08-31.
 */
public class ListNativeData extends BaseData {

    private String URL = "";
    private String PARAM = "";

    private ListNativeHandler mListNativeHandler;
    private ArrayList<ListNativeHandler> mData;
    private BaseResultListener mResultListener;

    public ListNativeData(){
    }


    @Override
    public BaseData clear() {
        if (mData != null) {
            mData = null;
        }
        mData = new ArrayList<ListNativeHandler>();
        if (mListNativeHandler != null) {
            mListNativeHandler = null;
        }
        mListNativeHandler = new ListNativeHandler();
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
                        mData.add(new ListNativeHandler());
                        if (mData.size() > 0) {
                            mListNativeHandler = mData.get(mData.size() - 1);

                            mListNativeHandler.setName(jsonObject1.getString("name"));
                            mListNativeHandler.setImage(jsonObject1.getString("image"));
                            mListNativeHandler.setTitle(jsonObject1.getString("title"));
                            mListNativeHandler.setLink(jsonObject1.getString("link"));
                            mListNativeHandler.setStart(jsonObject1.getString("start"));
                            mListNativeHandler.setEnd(jsonObject1.getString("end"));
                            if(jsonObject1.getString("ad").matches("Y")) {
                                mListNativeHandler.setIsLayout(1);
                            }else{
                                mListNativeHandler.setIsLayout(0);
                            }

                            mListNativeHandler.setReservation_link(jsonObject1.getString("reservation_link"));
                            mListNativeHandler.setContent(jsonObject1.getString("content"));
                            mListNativeHandler.setIcon(jsonObject1.getString("icon"));
                            mListNativeHandler.setRegdate(jsonObject1.getString("regdate"));

                            mListNativeHandler.setView(null);
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
