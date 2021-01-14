package com.example.tify.Hyeona.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.Bean.Bean_review_store;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CUDNetworkTask_review extends AsyncTask<Integer, String, Object> {

    /*공용이므로 항상 수정사항 알려주시기 바랍니다.*/

    final static String TAG = "CUDNetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    String where = null;
    Bean_review_review bean_review_review = null;
    ArrayList<Bean_review_review> reviews = null;
    Bean_review_store bean_review_store = null;

    int oNo;

    public CUDNetworkTask_review(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.where = where;
        this.reviews = new ArrayList<Bean_review_review>();
        this.bean_review_store = new Bean_review_store();
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Create/Update/Delete");
        progressDialog.setMessage("Working...");
        progressDialog.show();
    }

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String result = null;

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true){
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }
                if(where.equals("select")){
                    parserSelect(stringBuffer.toString());
                }else if(where.equals("select_review_storeinfo")){
                    parserSelect_review_storeinfo(stringBuffer.toString());
                }else if(where.equals("oNo")){
                    parserSelect_review_oNo(stringBuffer.toString());
                } else{
                    result = parserAction(stringBuffer.toString());
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(bufferedReader != null) bufferedReader.close();
                if(inputStreamReader != null) inputStreamReader.close();
                if(inputStream != null) inputStream.close();

            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        if(where.equals("oNo")){
            return oNo;
        }

        if(where.equals("select")){
            return reviews;
        }if(where.equals("select_review_storeinfo")){
            return bean_review_store;
        }else{
            return result;
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "onPostExecute()");
        super.onPostExecute(o);
        progressDialog.dismiss();

    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled");
        super.onCancelled();
    }

    private String parserAction(String s){
        Log.v(TAG,"Parser()");
        String returnValue = null;

        try {
            Log.v(TAG, s);

            JSONObject jsonObject = new JSONObject(s);
            returnValue = jsonObject.getString("result");
            Log.v(TAG, returnValue);

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }

    private void parserSelect(String s){
        //Log.v("aaaaaa","parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("review"));
            reviews.clear();

            for (int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                int rNo = jsonObject1.getInt("rNo");
                String rContent = jsonObject1.getString("rContent");
                Log.v("값확인","ㅇ"+rNo);
                Log.v("값확인","ㅇ"+rContent);
                String rImage = jsonObject1.getString("rImage");
                Log.v("값확인","ㅇ"+rImage);
                String rOwnerComment = jsonObject1.getString("rOwnerComment");
                Log.v("값확인","ㅇ"+rOwnerComment);
                String rDeletedate = jsonObject1.getString("rDeletedate");
                Log.v("값확인","ㅇ"+rDeletedate);
                String rInsertDate = jsonObject1.getString("rInsertDate");
                Log.v("값확인","ㅇ"+rInsertDate);

                int user_uNo = jsonObject1.getInt("user_uNo");
                Log.v("값확인","ㅇ"+user_uNo);
                int storekeeper_skSeqNo = jsonObject1.getInt("storekeeper_skSeqNo");

                Log.v("값확인","ㅇ"+storekeeper_skSeqNo);

                bean_review_review = new Bean_review_review(rNo, user_uNo, storekeeper_skSeqNo, rContent, rImage, rOwnerComment, rDeletedate, rInsertDate);
                Log.v("값확인","ㅇ"+ bean_review_review);
                reviews.add(bean_review_review);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void parserSelect_review_storeinfo(String s){
        //Log.v("aaaaaa","parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("review_store"));

            for (int i=0; i<jsonArray.length(); i++){

                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    int storekeeper_skSeqNo = jsonObject1.getInt("storekeeper_skSeqNo");
                    String sName = jsonObject1.getString("sName");
                    String sTelNo = jsonObject1.getString("sTelNo");
                    String sAddress = jsonObject1.getString("sAddress");
                    String sImage = jsonObject1.getString("sImage");
                    Log.v("스토어",sName);
                    Log.v("스토어",sImage);

                    bean_review_store = new Bean_review_store(storekeeper_skSeqNo, sName, sTelNo, sAddress, sImage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void  parserSelect_review_oNo(String s){
        //Log.v("aaaaaa","parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("oNo"));

            for (int i=0; i<jsonArray.length(); i++){

                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                oNo = jsonObject1.getInt("oNo");


            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


} // —————