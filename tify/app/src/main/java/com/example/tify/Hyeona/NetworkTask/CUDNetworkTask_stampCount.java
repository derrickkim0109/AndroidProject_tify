package com.example.tify.Hyeona.NetworkTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Hyeona.Bean.Bean_point_history;
import com.example.tify.Hyeona.Bean.Bean_reward_stamphistory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CUDNetworkTask_stampCount extends AsyncTask<Integer, String, Object> {

        /*공용이므로 항상 수정사항 알려주시기 바랍니다.*/

        final static String TAG = "CUDNetworkTask";

        String mAddr = null;
        ProgressDialog progressDialog = null;
        String where = null;
         ArrayList<Bean_reward_stamphistory> stamphistory = null;
        Bean_reward_stamphistory bean_reward_stamphistory = null;

        public CUDNetworkTask_stampCount(String mAddr, String where) {

            this.mAddr = mAddr;
            this.where = where;
            this.bean_reward_stamphistory = new Bean_reward_stamphistory();
            this.stamphistory = new ArrayList<Bean_reward_stamphistory>();

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

            int result = 0;
            int result1 = 0;
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

                    if(where.equals("select_stamp")){
                        //셀렉트로 값 하나만 받아옴
                        result = parserSelect(stringBuffer.toString());
                    }else if (where.equals("select_orderhistory")){
                        parserOrderhistorySelect(stringBuffer.toString());
                    }

                    else{
                        result1 = parserAction(stringBuffer.toString());
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

            if(where.equals("select_stamp")){
                return result;
            }else if (where.equals("select_orderhistory")){
                return stamphistory;
            }


            else{
                //임시
                return result1;
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

        private int parserAction(String s){
            Log.v(TAG,"Parser()");
            int returnValue =0;
            try {
                Log.v(TAG, s);
                JSONObject jsonObject = new JSONObject(s);
                returnValue = jsonObject.getInt("result");
                Log.v("왜","ㅅㅅ"+returnValue);
            }catch (Exception e){
                e.printStackTrace();
            }
            return returnValue;
        }

        private int parserSelect(String s){
            int user_rwStamp = 0;
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("user_rwStamp"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                    user_rwStamp = jsonObject1.getInt("rwStamp");
                    Log.v("네트워크","ㅇㅇ"+user_rwStamp);
                }

            }catch (Exception e){
                e.printStackTrace();
            }return user_rwStamp;
        }


     private void parserOrderhistorySelect(String s){
         try {
             JSONObject jsonObject = new JSONObject(s);
             JSONArray jsonArray = new JSONArray(jsonObject.getString("order_history"));
             for (int i = 0; i < jsonArray.length(); i++) {
                 JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                 int user_uNo = jsonObject1.getInt("user_uNo");
                 String oInsertDate = jsonObject1.getString("oInsertDate");
                 String store_sName = jsonObject1.getString("store_sName");
                 int total = jsonObject1.getInt("total");

                 Log.v("테스","1"+user_uNo);
                 Log.v("테스","1"+oInsertDate);
                 Log.v("테스","1"+store_sName);

                 bean_reward_stamphistory = new Bean_reward_stamphistory(user_uNo,oInsertDate,store_sName,total);
                 stamphistory.add(bean_reward_stamphistory);
             }

         }catch (Exception e){
             e.printStackTrace();
         }
     }


    } // ----------



