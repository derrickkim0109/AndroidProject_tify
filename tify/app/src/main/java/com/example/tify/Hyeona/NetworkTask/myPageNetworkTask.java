//compackage com.example.tify.Hyeona.NetworkTask;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.android.mypeople.Share.Bean.Bean_user;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.sql.Timestamp;
//
//
//public class myPageNetworkTask extends AsyncTask<Integer,String,Object> {
//    final static String TAG = "NetworkTask";
//    Context context = null;
//    String mAddr = null;
//    ProgressDialog progressDialog = null;
//    Bean_user bean_user =null;
//
//    public myPageNetworkTask(Context context, String mAddr) {
//        this.context = context;
//        this.mAddr = mAddr;
//        this.bean_user = new Bean_user();
//    }
//
//    @Override
//    protected void onPreExecute() {
//        Log.v(TAG,"onPreExecute()");
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Data Fetch");
//        progressDialog.setMessage("Get....");
//        progressDialog.show();
//    }
//
//    @Override
//    protected Object doInBackground(Integer... integers) {
//        Log.v(TAG,"doInBackground()");
//        StringBuffer stringBuffer = new StringBuffer();
//        InputStream inputStream =  null;
//        InputStreamReader inputStreamReader = null;
//        BufferedReader bufferedReader = null;
//
//        try {
//            URL url = new URL(mAddr);
//            Log.v(TAG,"1");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setConnectTimeout(10000);
//            httpURLConnection.setReadTimeout(10000);
//
//            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                Log.v(TAG,"4");
//                inputStream = httpURLConnection.getInputStream();
//                Log.v(TAG,"5");
//                inputStreamReader = new InputStreamReader(inputStream);
//                Log.v(TAG,"6");
//                bufferedReader = new BufferedReader(inputStreamReader);
//                Log.v(TAG,"7");
//                while (true){
//                    String strline = bufferedReader.readLine();
//                    Log.v(TAG,"8");
//                    if (strline == null) break;
//                    Log.v(TAG,"9");
//                    stringBuffer.append(strline + "\n");
//                    Log.v(TAG,"10");
//                }
//                parser(stringBuffer.toString());
//                Log.v(TAG,"11");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            try {
//                if (bufferedReader != null) bufferedReader.close();
//                if (inputStreamReader != null) inputStreamReader.close();
//                if (inputStream != null) inputStream.close();
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return bean_user;
//    }
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        Log.v(TAG,"onProgressUpdate()");
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected void onPostExecute(Object o) {
//        Log.v(TAG,"onPostExecute()");
//        super.onPostExecute(o);
//        progressDialog.dismiss();
//    }
//    @Override
//    protected void onCancelled() {
//        Log.v(TAG,"onCancelled()");
//        super.onCancelled();
//    }
//
//    private void parser(String s){
//        Log.v("aaaaaa","parser()");
//        try {
//            JSONObject jsonObject = new JSONObject(s);
//            Log.v("aaaaaa","parser()1111");
//            JSONArray jsonArray = new JSONArray(jsonObject.getString("user_info"));
//            Log.v("aaaaaa","parser()222");
//
//
//            for (int i=0; i<jsonArray.length(); i++){
//                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
//                int uSeqno = jsonObject1.getInt("uSeqno");
//                String uId = jsonObject1.getString("uId");
//                Log.v("aaaaaa","aaaaaa"+uId);
//                String uPw = jsonObject1.getString("uPw");
//                String uName = jsonObject1.getString("uName");
//                String uTel = jsonObject1.getString("uTel");
//                Log.v("aaaaaa","aaaaaa");
//                Timestamp uInsertDate = Timestamp.valueOf(jsonObject1.getString("uInsertDate"));
//                Timestamp uDeleteDate = Timestamp.valueOf(jsonObject1.getString("uDeleteDate"));
//
//                Log.v("aaaaaa","aaaaaa");
//                bean_user = new Bean_user(uSeqno,uId,uPw,uName,uTel,uInsertDate,uDeleteDate);
//                Log.v("aaaaaa","aaaaaa");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
//
