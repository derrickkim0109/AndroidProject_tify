package com.example.tify.Taehyun.NetworkTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tify.Taehyun.Bean.Bean_Mypage_cardlist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkTask_RecycleView_CardView extends AsyncTask<Integer, String, Object> {


       final static String TAG = "NetworkTask_RecycleView_CardView";
        Context context = null;
        String mAddr = null;
//        ProgressDialog progressDialog = null;
        Bean_Mypage_cardlist cardlist = null;
        ArrayList<Bean_Mypage_cardlist> cardlists = null;
        String where = null;


        public NetworkTask_RecycleView_CardView(String mAddr, String where) {
                this.mAddr = mAddr;
                this.cardlist = new Bean_Mypage_cardlist();
                this.cardlists = new ArrayList<Bean_Mypage_cardlist>();
                this.where = where;

                }


        @Override
        protected void onPreExecute() {
//                progressDialog = new ProgressDialog(context);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                progressDialog.setTitle("Dialogue");
//                progressDialog.setMessage("Get ....");
//                progressDialog.show();

                }

        @Override
        protected Object doInBackground(Integer... integers) {

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
                if (where.equals("select_cardList")) {
                         parserSelect(stringBuffer.toString());
                }else if (where.equals("CardList_Bean")) {
                        parserCardList(stringBuffer.toString());
                }else {
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
                if (where.equals("select_cardList")) {

                return cardlists;

                }else if (where.equals("CardList_Bean")){
                        return cardlist;

                }
                else {
                return result;
                }
                ///////////////////////////////////////////////////////////////////////////////////////
                }


            @Override
        protected void onPostExecute (Object o){
                super.onPostExecute(o);
//                progressDialog.dismiss();

                }

        @Override
        protected void onCancelled () {
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

    @SuppressLint("LongLogTag")
    private void parserSelect (String s){

            try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("card_info"));

            for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

            String cCardNo = jsonObject1.getString("cCardNo");
            String cCardCompany = jsonObject1.getString("cCardCompany");
            String cYear = jsonObject1.getString("cYear");
            String cMM = jsonObject1.getString("cMM");
            int cNo = jsonObject1.getInt("cNo");

            cardlist = new Bean_Mypage_cardlist(cCardCompany, cCardNo, cYear, cMM, cNo);
            cardlists.add(cardlist);
            Log.v(TAG,"cardlist" + cardlist);
            Log.v(TAG,"cCardNo" + cCardNo);
            Log.v(TAG,"cCardNo" + cCardCompany);
            Log.v(TAG,"cCardNo" + cYear);
            Log.v(TAG,"cCardNo" + cMM);
            Log.v(TAG,"cCardNo" + cNo);
            }

            } catch (Exception e) {
            e.printStackTrace();
            }

        }
        private void parserCardList (String s){

            try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("card_info"));

            for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

            String cCardNo = jsonObject1.getString("cCardNo");
            String cCardCompany = jsonObject1.getString("cCardCompany");
            int cNo = jsonObject1.getInt("cNo");

            Log.v("tag","cCardNo"+cCardCompany);


            cardlist = new Bean_Mypage_cardlist(cCardCompany, cCardNo, cNo);


            }

            } catch (Exception e) {
            e.printStackTrace();
            }

        }

}////END

