package com.android.tify_store.Minwoo.NetworkTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageNetworkTask_TaeHyun extends AsyncTask<Integer,String,Integer> {

    final static String TAG = "ImageNetworkTask_TaeHyun";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    String devicePath;
    ImageView imageView;

    public ImageNetworkTask_TaeHyun(Context context, ImageView imageView, String devicePath, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.devicePath = devicePath;
        this.imageView = imageView;
        Log.v("찾는중", "3 mAddr :" + mAddr);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Status");
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(Integer integer) {
        Log.v(TAG, "onPostExecute()");
        progressDialog.dismiss();

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onCancelled() {
        Log.v(TAG,"onCancelled()");
        super.onCancelled();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //              File Upload 작업이 Background에서 동작함
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("LongLogTag")
    @Override
    protected Integer doInBackground(Integer... integers) {
        File file = new File(devicePath);
        Log.v("찾는중", "4 devicePath :" + devicePath);
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //
                //              RequestBody.create의 parameter의 순서가 바뀜
                //
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")))
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                .build();

        Request request = new Request.Builder()
                .url(mAddr)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.v(TAG, "Success");
            return 1;

        }catch (Exception e){
            Log.v(TAG, "Error");
            e.printStackTrace();
            return 0;
        }
    }

}////----END
