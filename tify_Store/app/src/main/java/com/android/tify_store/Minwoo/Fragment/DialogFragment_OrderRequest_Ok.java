package com.android.tify_store.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.tify_store.Minwoo.Activity.MainActivity;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.android.tify_store.R;

public class DialogFragment_OrderRequest_Ok extends DialogFragment implements View.OnClickListener {

    // 주문 요청을 접수했을 때 뜨는 다이얼로그!

    String TAG = "DialogFragment_OrderRequest";
    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";
    private String mMainMsg;

    // layout
    Button btn_accept;
    Button btn_cancel;
    Button btn_5;
    Button btn_10;
    Button btn_15;
    Button btn_20;
    EditText et_inputTime;

    // DB Connect
    String macIP;
    String urlAddr = null;
    String where = null;
    int skSeqNo;
    int oNo;

    public static DialogFragment_OrderRequest_Cancel newInstance(String mainMsg) {

        Bundle bundle = new Bundle();

        bundle.putString(ARG_DIALOG_MAIN_MSG, mainMsg);

        DialogFragment_OrderRequest_Cancel fragment = new DialogFragment_OrderRequest_Cancel();

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "구간1--------");
        if (getArguments() != null) {
            mMainMsg = getArguments().getString(ARG_DIALOG_MAIN_MSG);
        }
    }
    private void dismissDialog() {
        this.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ok_cancelBtn:
                dismissDialog();
                break;

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lmw_dialog_ok, container, false);

        et_inputTime = view.findViewById(R.id.dialog_ok_inputTime);
        btn_5 = view.findViewById(R.id.dialog_ok_5);
        btn_10 = view.findViewById(R.id.dialog_ok_10);
        btn_15 = view.findViewById(R.id.dialog_ok_15);
        btn_20 = view.findViewById(R.id.dialog_ok_20);
        btn_cancel = view.findViewById(R.id.dialog_ok_cancelBtn);
        btn_accept = view.findViewById(R.id.dialog_ok_SendBtn);

        // 클릭 리스너
        btn_cancel.setOnClickListener(mClickListener);
        btn_5.setOnClickListener(mClickListener);
        btn_10.setOnClickListener(mClickListener);
        btn_15.setOnClickListener(mClickListener);
        btn_20.setOnClickListener(mClickListener);
        btn_accept.setOnClickListener(mClickListener);

        Bundle bundle = getArguments();
        macIP = bundle.getString("macIP");
        skSeqNo = bundle.getInt("skSeqNo");
        oNo = bundle.getInt("oNo");

        return view;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            switch (v.getId()){
                case R.id.dialog_ok_5: // 5분
                    et_inputTime.setText("5분");
                    break;
                case R.id.dialog_ok_10: // 10분
                    et_inputTime.setText("10분");
                    break;
                case R.id.dialog_ok_15: // 15분
                    et_inputTime.setText("15분");
                    break;
                case R.id.dialog_ok_20: // 20분
                    et_inputTime.setText("20분");
                    break;
                case R.id.dialog_ok_SendBtn: // 전송 버튼 눌렀을 때!

                    String why = et_inputTime.getText().toString(); // 예상 제조시간! 고객에게 알림으로 전달하기!

                    where = "update";
                    urlAddr = "http://" + macIP + ":8080/tify/lmw_order_update_ostatus1.jsp?oNo=" + oNo + "&oStatus=" + 1;
                    String result1 = connectData();

                    if(result1.equals("1")){
                        // 고객에게 제조중이라고 알림 띄우기!
                        Toast.makeText(getActivity(), "해당 주문은 정상적으로 전송되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        // DB Action중 문제 발생!
                        Toast.makeText(getActivity(), "정상적으로 전송되지 않았습니다. \n 관리자에게 문의바랍니다.", Toast.LENGTH_SHORT).show();
                    }

                    intent.putExtra("macIP", macIP);
                    intent.putExtra("skSeqNo", skSeqNo);
                    intent.putExtra("oNo", oNo);

                    startActivity(intent);
                    dismiss();

                    break;
                // --------------------------
                case R.id.dialog_ok_cancelBtn:
                    dismiss();
                    break;
            }

        }
    };

    private String connectData(){ // 오더테이블 oDeleteDate Update & 오더리스트테이블 Delete
        String result = null;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderListNetworkTask networkTask = new LMW_OrderListNetworkTask(getActivity(), urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask.execute().get();
            result = (String) obj;
            ///////////////////////////////////////////////////////////////////////////////////////
            Log.v(TAG, "DB Action Result : " + result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
