package com.example.tify.Hyeona.NetworkTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Hyeona.Bean.Bean_main_search;
import com.example.tify.Hyeona.Bean.Bean_point_history;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

    public class CUDNetworkTask_mainsearch  extends AsyncTask<Integer, String, Object> {

        /*공용이므로 항상 수정사항 알려주시기 바랍니다.*/

        final static String TAG = "CUDNetworkTask";
        String mAddr = null;
        //ProgressDialog progressDialog = null;
        String where = null;
        Bean_main_search bean_main_search = null;
        ArrayList<Bean_main_search> searchs = null;
        public CUDNetworkTask_mainsearch (String mAddr, String where) {
            this.mAddr = mAddr;
            this.where = where;
            this.bean_main_search = new Bean_main_search();
            this.searchs = new ArrayList<Bean_main_search>();

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

            String result1 = null;
            int result2 = 0;

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
                    if(where.equals("search_select")){
                        parserSelect(stringBuffer.toString());
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

            if(where.equals("search_select")){
                return searchs;
            }else{
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
                JSONArray jsonArray = new JSONArray(jsonObject.getString("cafe_info"));
                for (int i = 0; i < jsonArray.length(); i++) {
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

                    bean_main_search = new Bean_main_search(sName,sTelNo,sRunningTime,sAddress,sImage,sPackaging,sComment,storekeeper_skSeqNo,likeCount,reviewCount,skStatus);
                    searchs.add(bean_main_search);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }


//        private void parserpPointHistory_select(String s){
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray = new JSONArray(jsonObject.getString("rewardhistory"));
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//
//                    int rhNo = jsonObject1.getInt("rhNo");
//                    Log.v("히스토리","ㅇ"+rhNo);
//                    String rhDay = jsonObject1.getString("rhDay");
//                    Log.v("히스토리","ㅇ"+rhDay);
//                    String rhContent = jsonObject1.getString("rhContent");
//                    Log.v("히스토리","ㅇ"+rhContent);
//                    int rhChoice = jsonObject1.getInt("rhChoice");
//                    Log.v("히스토리","ㅇ"+rhChoice);
//                    String rhPointHow = jsonObject1.getString("rhPointHow");
//                    Log.v("히스토리","ㅇ"+rhPointHow);
//                    bean_point_history = new Bean_point_history(rhNo,rhDay,rhContent,rhChoice,rhPointHow);
//                    point_history.add(bean_point_history);
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }

//        private int parserPointSelect(String s){
//            int user_rwStamp = 0;
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray = new JSONArray(jsonObject.getString("rwPoint_select"));
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//
//                    user_rwStamp = jsonObject1.getInt("rwPoint");
//                    Log.v("네트워크","ㅇㅇ"+user_rwStamp);
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }return user_rwStamp;
//        }



    } // ----------

