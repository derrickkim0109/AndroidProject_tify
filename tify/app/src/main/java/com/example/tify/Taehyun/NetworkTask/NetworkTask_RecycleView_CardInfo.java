package com.example.tify.Taehyun.NetworkTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.tify.Taehyun.Bean.Bean_Mypage_CardInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkTask_RecycleView_CardInfo extends AsyncTask<Integer, String, Object> {

       final static String TAG = "NetworkTask_RecycleView_Taehyun";
        Context context = null;
        String mAddr = null;
//        ProgressDialog progressDialog = null;
        Bean_Mypage_CardInfo cardInfo = null;
        ArrayList<Bean_Mypage_CardInfo> cardInfos = null;
        String where = null;


        public NetworkTask_RecycleView_CardInfo(String mAddr, String where) {

                this.mAddr = mAddr;
                this.cardInfo = new Bean_Mypage_CardInfo();
                this.cardInfos = new ArrayList<Bean_Mypage_CardInfo>();
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
                if (where.equals("select_cardInfo")) {
                parserSelect(stringBuffer.toString());
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
                if (where.equals("select_cardInfo")) {
                return cardInfos;
                } else {
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

    private void parserSelect (String s){

            try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("card_info"));

            for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

            String cImage = jsonObject1.getString("cCardCompany");

            String cCardNo = jsonObject1.getString("cCardNo");

            int cNo = jsonObject1.getInt("cNo");

            String cInfo = jsonObject1.getString("cInfo");

            cardInfo = new Bean_Mypage_CardInfo(cImage, cCardNo, cNo, cInfo);
            cardInfos.add(cardInfo);

            }

            } catch (Exception e) {
            e.printStackTrace();
            }
            }

}////END

