package com.example.tify.Jiseok.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.Bean.Bean_review_store;
import com.example.tify.Jiseok.Bean.Bean_Login_cjs;
import com.example.tify.Minwoo.Bean.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CJS_NetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "CUDNetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    String where = null;

    int userTelCount = 0;
    int userEmailCount=0;
    int uNo=0;
    Bean_Login_cjs bean_login_cjs = null;


    public CJS_NetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.where = where;

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
            httpURLConnection.setConnectTimeout(100000);

            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true){
                    String strline = bufferedReader.readLine();
                    if(strline == null) break;
                    stringBuffer.append(strline + "\n");
                }

//                switch (where){
//                    case "userTelCount":
//                        userTelCount(stringBuffer.toString());
//                        break;
//                    case "userEmailCount":
//                        userEmailCount(stringBuffer.toString());
//                        break;
//                    case "uNoSelect":
//                        uNoSelect(stringBuffer.toString());
//                        break;
//                    case "insertUserInfo":
//                        parserAction(stringBuffer.toString());
//                        break;
//                    default:
//
//                        break;
//                }
                if(where.equals("userTelCount")) userTelCount(stringBuffer.toString());
                if(where.equals("userEmailCount")) userEmailCount(stringBuffer.toString());
                if(where.equals("uNoSelect")) uNoSelect(stringBuffer.toString());
                if(where.equals("insertUserInfo")) parserAction(stringBuffer.toString());
                if(where.equals("emailLogin")) emailLogin(stringBuffer.toString());
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

        if(where.equals("userTelCount")) return userTelCount;
        if(where.equals("userEmailCount")) return userEmailCount;
        if(where.equals("uNoSelect")) return uNo;
        if(where.equals("emailLogin")) return bean_login_cjs;


       return null;
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




    private void userTelCount(String s){
        //Log.v("aaaaaa","parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int storekeeper_skSeqNo = jsonObject1.getInt("userTelCount");

                userTelCount = storekeeper_skSeqNo;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void userEmailCount(String s){
        //Log.v("aaaaaa","parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int storekeeper_skSeqNo = jsonObject1.getInt("EmailCount");

                userEmailCount = storekeeper_skSeqNo;
                Log.v("여기","userEmailCount_parser : "+userEmailCount);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void uNoSelect(String s){
        try {
            Log.v("왜안떠","1");
            JSONObject jsonObject = new JSONObject(s);
            Log.v("왜안떠","2");
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));
            Log.v("왜안떠","3");
            Log.v("왜안떠",""+jsonArray.length());
            for (int i=0; i<jsonArray.length(); i++){
                Log.v("왜안떠","4");
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                Log.v("왜안떠","5");
                int storekeeper_skSeqNo = jsonObject1.getInt("uNo");

                uNo = storekeeper_skSeqNo;
                Log.v("왜안떠","no : "+uNo);
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.v("왜안떠",""+e);
        }

    }

    private void emailLogin(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));
            Log.v("왜안떠",""+jsonArray.length());
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int count = jsonObject1.getInt("count");
                int uNo = jsonObject1.getInt("uNo");
                String uEmail = jsonObject1.getString("uEmail");
                String uNickName = jsonObject1.getString("uNickName");

                bean_login_cjs = new Bean_Login_cjs(count,uNo,uEmail,uNickName);

            }

        }catch (Exception e){
            e.printStackTrace();
            Log.v("왜안떠",""+e);
        }

    }



    private String parserAction(String s){
        Log.v(TAG,"Parser()");
        String returnValue = null;

        try {
            Log.v(TAG, s);

            JSONObject jsonObject = new JSONObject(s);
            returnValue = jsonObject.getString("result");
            Log.v(TAG, returnValue);
            Log.v("여기","returnValue : "+returnValue);

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }





    } // ------


