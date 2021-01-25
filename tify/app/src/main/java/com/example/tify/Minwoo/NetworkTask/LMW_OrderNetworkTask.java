package com.example.tify.Minwoo.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.Bean.OrderList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LMW_OrderNetworkTask extends AsyncTask<Integer, String, Object> {
    final static String TAG = "LMW_OrderNetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<Order> orders;

    String where = null;


    public LMW_OrderNetworkTask(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.orders = new ArrayList<Order>();
        this.where = where;
        Log.v(TAG, "Start : " + mAddr);
        Log.v(TAG, "where : " + where);
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialogue");
        progressDialog.setMessage("Get ....");
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
                    parserOrder_Select(stringBuffer.toString());
                }else if(where.equals("oNo")) {
                    parserOrdermNo_Select(stringBuffer.toString());
                }else{
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

        if(where.equals("select") || where.equals("oNo")){
            return orders;
        }else{
            return result;
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "onPostExecute()");
        super.onPostExecute(o);
        progressDialog.dismiss();

    }

    @Override
    protected void onCancelled() {
        Log.v(TAG,"onCancelled()");
        super.onCancelled();
    }

    private void parserOrder_Select(String s) { // BeforePayActivity
        Log.v(TAG, "parserOrder_Select()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("order"));
            orders.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int user_uNo = jsonObject1.getInt("user_uNo");
                int oNo = jsonObject1.getInt("oNo");
                int store_sSeqNo = jsonObject1.getInt("store_sSeqNo");
                String store_sName = jsonObject1.getString("store_sName");
                String oInsertDate = jsonObject1.getString("oInsertDate");
                String oDeleteDate = jsonObject1.getString("oDeleteDate");
                int oSum = jsonObject1.getInt("oSum");
                String oCardName = jsonObject1.getString("oCardName");
                String oCardNo = null;
                int oReview = jsonObject1.getInt("oReview");
                int oStatus = jsonObject1.getInt("oStatus");

                Log.v(TAG, "oReview : " + oReview);
                Log.v(TAG, "oStatus : " + oStatus);

                Order order = new Order(user_uNo, oNo, store_sSeqNo, store_sName, oInsertDate, oDeleteDate, oSum, oCardName, oCardNo, oReview, oStatus);
                orders.add(order);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parserOrdermNo_Select(String s) { // BeforePayActivity
        Log.v(TAG, "parserOrdermNo_Select()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("order"));
            orders.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int max = jsonObject1.getInt("max");

                Log.v(TAG, "max : " + max);

                Order order = new Order(max);
                orders.add(order);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
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

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }



} // ------
