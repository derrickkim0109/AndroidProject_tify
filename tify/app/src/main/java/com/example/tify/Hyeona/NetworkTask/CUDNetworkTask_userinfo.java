package com.example.tify.Hyeona.NetworkTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_review_userinfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CUDNetworkTask_userinfo extends AsyncTask<Integer, String, Object> {

    /*공용이므로 항상 수정사항 알려주시기 바랍니다.*/

    final static String TAG = "CUDNetworkTask";
    review_adapter context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    String where = null;
    Bean_review_userinfo bean_review_userinfo = null;

    public CUDNetworkTask_userinfo(String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.where = where;
        this.bean_review_userinfo = new Bean_review_userinfo();

        Log.v(TAG,"Start : " + mAddr);
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Create/Update/Delete");
//        progressDialog.setMessage("Working...");
//        progressDialog.show();
    }

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");

        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String result = null;
        int result_coffee = 0;

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
                }else if(where.equals("cha_main_coffee_count")){
                    result_coffee = parserAction_coffeCount(stringBuffer.toString());
                }
                else{
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

        if(where.equals("select")){
            return bean_review_userinfo;
        }else if(where.equals("cha_main_coffee_count")){
            return result_coffee;
        }
        else{
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
        //progressDialog.dismiss();

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
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                int uNo = jsonObject1.getInt("uNo");
                Log.v("dad","d"+uNo);
                String uEmail = jsonObject1.getString("uEmail");
                Log.v("dad","d"+uEmail);
                String uNickName = jsonObject1.getString("uNickName");
                Log.v("dad","d"+uNickName);
                String uTelNo = jsonObject1.getString("uTelNo");
                Log.v("dad","d"+uTelNo);
                String uImage = jsonObject1.getString("uImage");
                Log.v("dad","d"+uImage);
                String uPayPassword = jsonObject1.getString("uPayPassword");
                Log.v("dad","d"+uPayPassword);

                bean_review_userinfo = new Bean_review_userinfo(uNo, uEmail, uNickName, uTelNo, uImage, uPayPassword);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private int parserAction_coffeCount(String s){
        int user_rwStamp = 0;
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("coffee_count"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                user_rwStamp = jsonObject1.getInt("coffee");
                Log.v("네트워크","ㅇㅇ"+user_rwStamp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }return user_rwStamp;
    }





} // ----------