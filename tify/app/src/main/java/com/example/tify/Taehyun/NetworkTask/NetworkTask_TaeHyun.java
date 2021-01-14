package com.example.tify.Taehyun.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Taehyun.Bean.Bean_Mypage_userinfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkTask_TaeHyun extends AsyncTask<Integer, String, Object> {

    final static String TAG = "NetworkTask_TaeHyun";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    Bean_Mypage_userinfo userinfo = null;
    Order order = null;
    String where = null;


    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2020.12.25
    //
    // Description:
    //  - NetworkTask를 검색, 입력, 수정, 삭제 구분없이 하나로 사용키 위해 생성자 변수 추가.
    //
    ///////////////////////////////////////////////////////////////////////////////////////

    public NetworkTask_TaeHyun(Context context, String mAddr, String where) {
        this.context = context;
        this.mAddr = mAddr;
        this.userinfo = new Bean_Mypage_userinfo();

        this.where = where;
        Log.v(TAG, "Start : " + mAddr);
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
        ///////////////////////////////////////////////////////////////////////////////////////
        // Date : 2020.12.25
        //
        // Description:
        //  - NetworkTask에서 입력,수정,검색 결과값 관리.
        //
        ///////////////////////////////////////////////////////////////////////////////////////
        String result = null;
        int count = 0;

        ///////////////////////////////////////////////////////////////////////////////////////

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while (true) {
                    String strline = bufferedReader.readLine();
                    if (strline == null) break;
                    stringBuffer.append(strline + "\n");
                }
                ///////////////////////////////////////////////////////////////////////////////////////
                // Date : 2020.12.25
                //
                // Description:
                //  - 검색으로 들어온 Task는 parserSelect()로
                //  - 입력, 수정, 삭제로 들어온 Task는 parserAction()으로 구분
                //
                ///////////////////////////////////////////////////////////////////////////////////////
                if (where.equals("select")) {
                    parserSelect(stringBuffer.toString());
                } else if (where.equals("count")){
                    count = parserCardSelect(stringBuffer.toString());
                } else if (where.equals("selectOrderNumber")){
                    parserOrderNumber(stringBuffer.toString());
                }
                else {
                    result = parserAction(stringBuffer.toString());
                }
                ///////////////////////////////////////////////////////////////////////////////////////

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        // Date : 2020.12.25
        //
        // Description:
        //  - 검색으로 들어온 Task는 members를 return
        //  - 입력, 수정, 삭제로 들어온 Task는 result를 return
        //
        ///////////////////////////////////////////////////////////////////////////////////////
        if (where.equals("select")) {
            Log.v("설마","설");
            Log.v("설마","ㄹㅇㄴㄹㅇㄴ"+userinfo);

            return userinfo;
        } else if (where.equals("count")){
            return count;
        } else if (where.equals("selectOrderNumber")){
            return order;
        }else {
            return result;
        }
        ///////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
        protected void onPostExecute (Object o){
            Log.v(TAG, "onPostExecute()");
            super.onPostExecute(o);
            progressDialog.dismiss();

        }

        @Override
        protected void onCancelled () {
            Log.v(TAG, "onCancelled()");
            super.onCancelled();
        }

        ///////////////////////////////////////////////////////////////////////////////////////
        // Date : 2020.12.25
        //
        // Description:
        //  - 검색후 json parsing
        //
        ///////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////////////////////////

    private void parserSelect (String s){
        Log.v(TAG, "Parser()");

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                int uNo = jsonObject1.getInt("uNo");
                String uEmail = jsonObject1.getString("uEmail");
                String uNickName = jsonObject1.getString("uNickName");
                String uTelNo = jsonObject1.getString("uTelNo");
                String uImage = jsonObject1.getString("uImage");
                String uPayPassword = jsonObject1.getString("uPayPassword");

                Log.v("ddddddd","uNo : "+uNo);
                Log.v("ddddddd","uNo : "+uEmail);
                Log.v("ddddddd","uNo : "+uNickName);
                Log.v("ddddddd","uNo : "+uTelNo);
                Log.v("ddddddd","uNo : "+uImage);
                Log.v("ddddddd","uNo : "+uPayPassword);
                Log.v("ddddddd",where);

                userinfo = new Bean_Mypage_userinfo(uNo, uEmail, uNickName, uTelNo, uImage, uPayPassword);

                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2021.01.11
    //
    // Description:
    //  -//카드 등록한 갯수 파악
    //
    ///////////////////////////////////////////////////////////////////////////////////////
    private int parserCardSelect(String s){

        Log.v(TAG,"Parser()");
        int cardCountResult = 0;

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("cardCount"));

            for (int i = 0; i < jsonArray.length(); i++) {
                Log.v(TAG, s);
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                cardCountResult = jsonObject1.getInt("cardCountResult");
                Log.v(TAG,"cardCountResult" + cardCountResult );

            }


            }
        catch (Exception e){
            e.printStackTrace();
        }
        return cardCountResult;

    }


    ///////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2020.12.25
    //
    // Description:
    //  - 입력, 수정, 삭제후 json parsing
    //
    ///////////////////////////////////////////////////////////////////////////////////////
    private String parserAction (String s){
        Log.v(TAG, "Parser()");
        String returnValue = null;

        try {
            Log.v(TAG, s);

            JSONObject jsonObject = new JSONObject(s);
            returnValue = jsonObject.getString("result");
            Log.v(TAG, returnValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }



    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2021.01.14
    //
    // Description:
    //  - OrderNumber 띄우기
    //
    ///////////////////////////////////////////////////////////////////////////////////////

    private void parserOrderNumber(String s) {
        Log.v(TAG, "Parser()");

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("order_info"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                int oNo = jsonObject1.getInt("oNo");

                int store_sSeqno = jsonObject1.getInt("store_sSeqno");

                String store_sName = jsonObject1.getString("store_sName");
                Log.v(TAG,"order :" + store_sName);


                order = new Order(oNo,store_sSeqno,store_sName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



} // ----------