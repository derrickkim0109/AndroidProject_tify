package com.example.tify.Hyeona.NetworkTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Hyeona.Bean.Bean_payment_paylist;
import com.example.tify.Hyeona.Bean.Bean_payment_select;
import com.example.tify.Hyeona.Bean.Bean_point_history;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;


public class CUDNetworkTask_payment extends AsyncTask<Integer, String, Object> {

        /*공용이므로 항상 수정사항 알려주시기 바랍니다.*/

        final static String TAG = "CUDNetworkTask";

        String mAddr = null;
        ProgressDialog progressDialog = null;
        String where = null;
        Bean_payment_select bean_payment_select = null;
        Bean_payment_paylist bean_payment_paylist = null;
        ArrayList<Bean_payment_paylist> payment_paylists = null;

        public CUDNetworkTask_payment(String mAddr, String where) {

            this.mAddr = mAddr;
            this.where = where;
            this.bean_payment_select = new Bean_payment_select();
            this.bean_payment_paylist = new Bean_payment_paylist();
            this.payment_paylists = new ArrayList<Bean_payment_paylist>();
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
                    if(where.equals("select")){
                        //셀렉트로 값 하나만 받아옴
                        parserSelect(stringBuffer.toString());
                    }else if (where.equals("update")){
                        result = parserUpdate(stringBuffer.toString());
                    }
                    else if(where.equals("stamp_count")){
                        result1 = parserStampCount(stringBuffer.toString());
                    }else if(where.equals("stamp_update")){
                        result1 = parserUpdate(stringBuffer.toString());
                    }else if(where.equals("paylist")) {
                        parserpaySelect(stringBuffer.toString());

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
                return bean_payment_select;
            }else if (where.equals("update")){
                return result;
            }
            else if(where.equals("stamp_count")){
                return result1;
            }else if(where.equals("stamp_update")){
                return result1;
            }else if(where.equals("paylist")){
                return payment_paylists;
            }else{
                return null;
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

        private int parserUpdate(String s){
            Log.v(TAG,"Parser()");
            int returnValue =0;
            try {
                Log.v(TAG, s);
                JSONObject jsonObject = new JSONObject(s);
                returnValue = jsonObject.getInt("result");
                Log.v("왜","2"+returnValue);
            }catch (Exception e){
                e.printStackTrace();
            }
            return returnValue;
        }

        private int parserStampCount(String s){
            Log.v(TAG,"Parser()");
            int returnValue =0;
            try {
                Log.v(TAG, s);

                JSONObject jsonObject = new JSONObject(s);
                returnValue = jsonObject.getInt("stamp_count");
                Log.v("왜","2"+returnValue);


            }catch (Exception e){
                e.printStackTrace();
            }
            return returnValue;
        }


        private void parserSelect(String s){
            Log.v("aaaaaa","parser()");
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.v("aaaaaa","parser()1111");
                JSONArray jsonArray = new JSONArray(jsonObject.getString("payment_select"));
                Log.v("aaaaaa","parser()222");


                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                        int user_uNo = jsonObject1.getInt("user_uNo");
                        int oNo = jsonObject1.getInt("oNo");
                        int storekeeper_skSeqNo = jsonObject1.getInt("storekeeper_skSeqNo");
                        String store_sName = jsonObject1.getString("store_sName");
                        String oInsertDate = jsonObject1.getString("oInsertDate");
                        String oDeleteDate = jsonObject1.getString("oDeleteDate");
                        String oSum = jsonObject1.getString("oSum");
                        String oCardName = jsonObject1.getString("oCardName");
                        String oCardNo = jsonObject1.getString("oCardNo");

                        bean_payment_select = new Bean_payment_select(user_uNo, oNo, storekeeper_skSeqNo,store_sName, oInsertDate, oDeleteDate, oSum, oCardName, oCardNo);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void parserpaySelect(String s){
            Log.v("aaaaaa","parser()");
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("pay_history"));


                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                    String store_sName = jsonObject1.getString("store_sName");
                    String oInsertDate = jsonObject1.getString("oInsertDate");
                    int oSum = jsonObject1.getInt("oSum");

                    Log.v("ddd","테스트"+store_sName);
                    Log.v("ddd","테스트"+oInsertDate);
                    Log.v("ddd","테스트"+oSum);

                   bean_payment_paylist = new Bean_payment_paylist(store_sName, oInsertDate, oSum);
                   payment_paylists.add(bean_payment_paylist);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


