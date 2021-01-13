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
import com.android.tify_store.Minwoo.Activity.StoreInfoActivity;
import com.android.tify_store.Minwoo.Bean.OrderRequest;
import com.android.tify_store.Minwoo.NetworkTask.LMW_OrderListNetworkTask;
import com.android.tify_store.Minwoo.NetworkTask.LMW_StoreNetworkTask;
import com.android.tify_store.R;

import java.util.ArrayList;

public class DialogFragment_OrderRequest_Cancel extends DialogFragment {

    // 주문 요청을 거절했을 때 뜨는 다이얼로그!

   String TAG = "DialogFragment_OrderRequest";
   private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";
   private String mMainMsg;

   // layout
   Button btn_cancel;
   Button btn_send;
   Button btn_soldout;
   Button btn_close;
   EditText et_why;

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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.dialog_cancel_Btn_Dismiss:
//                dismiss();
//                break;
//
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lmw_dialog_cancel, container, false);
        setCancelable(false);

        et_why = view.findViewById(R.id.dialog_cancel_ET_Why);
        btn_close = view.findViewById(R.id.dialog_cancel_Btn_Close);
        btn_soldout = view.findViewById(R.id.dialog_cancel_Btn_Soldout);
        btn_cancel = view.findViewById(R.id.dialog_cancel_Btn_Dismiss);
        btn_send = view.findViewById(R.id.dialog_cancel_Btn_Send);

        // 클릭 리스너
        btn_cancel.setOnClickListener(mClickListener);
        btn_send.setOnClickListener(mClickListener);
        btn_soldout.setOnClickListener(mClickListener);
        btn_close.setOnClickListener(mClickListener);

        Bundle bundle = getArguments();
        macIP = bundle.getString("macIP");
        skSeqNo = bundle.getInt("skSeqNo");
        oNo = bundle.getInt("oNo");

        return view;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "입력값 : " + et_why);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                switch (v.getId()){
                    case R.id.dialog_cancel_Btn_Dismiss: // 취소 버튼 눌렀을 때!
                        dismiss();
                        break;
                    case R.id.dialog_cancel_Btn_Send: // 전송 버튼 눌렀을 때!

                        if(et_why.getText().toString().length() > 0){
                            String why = et_why.getText().toString(); // 거절 이유! 고객에게 알림으로 전달하기!

                            where = "update";
                            urlAddr = "http://" + macIP + ":8080/tify/lmw_order_cancel.jsp?oNo=" + oNo + "&oStatus=" + 5;
                            int result1 = Integer.parseInt(connectData());

                            if(result1 == 1){
                                // 고객에게 주문 최소됐다고 알림 띄우기 & 거절 사유 전송!
                                Toast.makeText(getActivity(), "고객에게 거절 사유가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                // DB Action중 문제 발생!
                                Toast.makeText(getActivity(), "정상적으로 전송되지 않았습니다. \n 관리자에게 문의바랍니다.", Toast.LENGTH_SHORT).show();
                            }

                            intent.putExtra("macIP", macIP);
                            intent.putExtra("skSeqNo", skSeqNo);
                            intent.putExtra("oNo", oNo);

                            startActivity(intent);
                            dismiss();
                        }else{
                            Toast.makeText(getActivity(), "거절사유를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }



                        break;
                    // --------------------------

                    case R.id.dialog_cancel_Btn_Close:
                        et_why.setText("영업종료");
                        break;
                    case R.id.dialog_cancel_Btn_Soldout:
                        et_why.setText("재료부족");
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

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
