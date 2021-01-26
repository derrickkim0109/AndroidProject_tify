package com.example.tify.Jiseok.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Jiseok.Bean.Bean_Login_cjs;
import com.example.tify.Jiseok.Bean.Bean_MainCafeList_cjs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CJS_NetworkTask_CafeList extends AsyncTask<Integer, String, Object> {

    final static String TAG = "CUDNetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    String where = null;

    ArrayList<Bean_MainCafeList_cjs> arrayList = new ArrayList<Bean_MainCafeList_cjs>();
    Bean_MainCafeList_cjs bean_mainCafeList_cjs = null;


    public CJS_NetworkTask_CafeList(Context context, String mAddr, String where) {
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


                cafeListSelect(stringBuffer.toString());
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


        return arrayList;


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




    private void cafeListSelect(String s){
        //Log.v("aaaaaa","parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("cafe_info"));

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String sName = jsonObject1.getString("sName");
                String sTelNo = jsonObject1.getString("sTelNo");
                String sRunningTime = jsonObject1.getString("sRunningTime");
                String sAddress = jsonObject1.getString("sAddress");
                String sImage = jsonObject1.getString("sImage");
                String sPackaging = jsonObject1.getString("sPackaging");
                String sComment = jsonObject1.getString("sComment");
                String storekeeper_skSeqNo = jsonObject1.getString("storekeeper_skSeqNo");
                String likeCount = jsonObject1.getString("likeCount");
                String reviewCount = jsonObject1.getString("reviewCount");
                String skStatus = jsonObject1.getString("skStatus");

               Log.v("카페리스트넷타",""+sName);

                bean_mainCafeList_cjs = new Bean_MainCafeList_cjs(sName,sTelNo,sRunningTime,sAddress,sImage,sPackaging,sComment,storekeeper_skSeqNo,likeCount,reviewCount,skStatus);
                arrayList.add(bean_mainCafeList_cjs);
            }

        }catch (Exception e){
            e.printStackTrace();
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


