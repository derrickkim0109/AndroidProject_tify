package com.example.tify.Minwoo.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Menu;
import com.example.tify.Minwoo.Bean.OrderList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LMW_NetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "NetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<Cart> carts;
    ArrayList<Menu> menus;
    ArrayList<OrderList> orderLists;

    String where = null; // 어떤 bean을 이용하고 어떤 jsp를 선택할지를 결정
    int number = -1; // select == 0, insert, update, delete == 1

    public LMW_NetworkTask(Context context, String mAddr, String where, int number) {
        this.context = context;
        this.mAddr = mAddr;
        switch (where) {
            case "MenuFragment":
                this.menus = new ArrayList<Menu>();
                break;
            case "CartActivity":
                this.carts = new ArrayList<Cart>();
                break;
            case "OrderListActivity":
                this.orderLists = new ArrayList<OrderList>();
                break;
            case "OrderDetailActivity":
                this.orderLists = new ArrayList<OrderList>();
                break;

        }
        this.where = where;
        this.number = number;
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

        String result = null;

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
                if (where.equals("MenuFragment")) {
                    parserMenu(stringBuffer.toString()); // 전체 친구 리스트 가져오기
                    Log.v(TAG, "where : " + where);
                } else if (where.equals("CartActivity")) { // 장바구니는 CRUD 모두 가능해야한다.
                    switch (number) { // select == 0, insert, update, delete == 1, 메뉴 이름 불러오기 == 2
                        case 0:
                            parserCart_Select(stringBuffer.toString());
                            Log.v(TAG, "where : " + where + " parserCart_Select");
                            break;
                        case 1:
                            result = parserAction(stringBuffer.toString());
                            Log.v(TAG, "where : " + where + " parserCart_CUD");
                            break;
                    }

                } else if (where.equals("OrderListActivity")) { // 주문내역은 CRU 가능
                    switch (number) { // select == 0, insert, update== 1
                        case 0:
                            parserOrder_Select(stringBuffer.toString());
                            Log.v(TAG, "where : " + where + " parserOrder_Select");
                            break;
                        case 1:
                            result = parserAction(stringBuffer.toString());
                            Log.v(TAG, "where : " + where + " parserOrder_CU");
                            break;
                    }

                } else if (where.equals("OrderDetailActivity")) { // 주문내역은 CRU 가능
                    parserOrderList_Select(stringBuffer.toString());
                    Log.v(TAG, "where : " + where + " parserOrderList_Select");
                } else if (where.equals("OrderSummaryActivity")) {
                    result = parserAction(stringBuffer.toString());
                    Log.v(TAG, "where : " + where + " parserAction");
                } else {
                    result = parserAction(stringBuffer.toString());
                    Log.v(TAG, "where : else");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "연결 실패");
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (where.equals("MenuFragment")) {
            return menus;
        } else if (number == 2) { // 메뉴 이름
            return menus;
        } else if (where.equals("CartActivity") && number != 2) {
            return carts;
        } else if (where.equals("OrderListActivity")) {
            return orderLists;
        } else if (where.equals("OrderDetailActivity")) {
            return orderLists;
        } else {
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
        Log.v(TAG, "onCancelled()");
        super.onCancelled();
    }

    /////////////////////////////////////////////////////////////////////
    ////////////////////////////parser 부분//////////////////////////////
    ////////////////////////////////////////////////////////////////////

    private void parserMenu(String s) { // MenuFragment
        Log.v(TAG, "parserMenu()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("menuList"));
            menus.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int mNo = jsonObject1.getInt("mNo");
                String mName = jsonObject1.getString("mName");
                int mPrice = jsonObject1.getInt("mPrice");
                int mShut = jsonObject1.getInt("mShut");
                int mSizeUp = jsonObject1.getInt("mSizeUp");
                int mType = jsonObject1.getInt("mType");
                String mImage = jsonObject1.getString("mImage");
                String mComment = jsonObject1.getString("mComment");

                Log.v(TAG, "mName : " + mName);


                Menu menu = new Menu(mNo, mName, mPrice, mSizeUp, mShut, mImage, mType, mComment);
                menus.add(menu);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parserCart_Select(String s) { // CartActivity
        Log.v(TAG, "parserCart_Select()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("cartList"));
            carts.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int cLNo = jsonObject1.getInt("cLNo");
                int store_sSeqNo = jsonObject1.getInt("store_sSeqNo");
                String menu_mName = jsonObject1.getString("menu_mName");
                int cLPrice = jsonObject1.getInt("cLPrice");
                int cLQuantity = jsonObject1.getInt("cLQuantity");
                String cLImage = jsonObject1.getString("cLImage");
                int cLSizeUp = jsonObject1.getInt("cLSizeUp");
                int cLAddShot = jsonObject1.getInt("cLAddShot");
                String cLRequest = jsonObject1.getString("cLRequest");

                Log.v(TAG, "cLNo : " + cLNo);


                Cart cart = new Cart(cLNo, store_sSeqNo, menu_mName, cLPrice, cLQuantity, cLImage, cLSizeUp, cLAddShot, cLRequest);
                carts.add(cart);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parserOrder_Select(String s) { // OrderListActivity
        Log.v(TAG, "parserOrderList()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("order"));
            orderLists.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                int oNo = jsonObject1.getInt("oNo");
                int store_sSeqNo = jsonObject1.getInt("store_sSeqNo");
                int cartlist_cLNo = jsonObject1.getInt("cartlist_cLNo");
                String oInsertDate = jsonObject1.getString("oInsertDate");
                String oDeleteDate = jsonObject1.getString("oDeleteDate");
                int oSum = jsonObject1.getInt("oSum");
                String oCardName = jsonObject1.getString("oCardName");
                int oCardNo = jsonObject1.getInt("oCardNo");
                String sName = jsonObject1.getString("sName");
                int oReview = jsonObject1.getInt("oReview");

                Log.v(TAG, "oNo : " + oNo);


                OrderList orderList = new OrderList(oNo, store_sSeqNo, cartlist_cLNo, oInsertDate, oDeleteDate, oSum, oCardName, oCardNo, oReview);
                orderLists.add(orderList);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parserOrderList_Select(String s) { // OrderDetailActivity
        Log.v(TAG, "parserOrderList_Select()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("orderList"));
            orderLists.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                String oInsertDate = jsonObject1.getString("oInsertDate");
                String sName = jsonObject1.getString("sName");
                String mName = jsonObject1.getString("mName");
                int olSeqNo = jsonObject1.getInt("olSeqNo");
                int olAddOrder = jsonObject1.getInt("olAddOrder");
                String olRequest = jsonObject1.getString("olRequest");
                int olPrice = jsonObject1.getInt("olPrice");
                int olQuantity = jsonObject1.getInt("olQuantity");

                Log.v(TAG, "olSeqNo : " + olSeqNo);

                OrderList orderList = new OrderList(oInsertDate, sName, olSeqNo, olAddOrder, olRequest, olPrice, olQuantity, mName);
                orderLists.add(orderList);
                // Log.v(TAG, member.toString());
                Log.v(TAG, "----------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String parserAction(String s){ // CUD일 경우 결과값만 받는다!
        Log.v(TAG,"parserAction()");
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
}

