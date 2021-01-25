package com.android.tify_store.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.tify_store.Minwoo.Activity.MainActivity;
import com.android.tify_store.R;

public class DialogFragment_Progressing_Accept extends DialogFragment {

    // 주문 요청을 접수했을 때 뜨는 다이얼로그!

    String TAG = "DialogFragment_OrderRequest";
    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";
    private String mMainMsg;

    // layout
    Button btn_accept;
    Button btn_cancel;

    // DB Connect
    String macIP;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lmw_dialog_accept, container, false);

        btn_cancel = view.findViewById(R.id.dialog_accept_dismiss);
        btn_accept = view.findViewById(R.id.dialog_accept_ok);

        // 클릭 리스너
        btn_cancel.setOnClickListener(mClickListener);
        btn_accept.setOnClickListener(mClickListener);

        Bundle bundle = getArguments();
        macIP = bundle.getString("macIP");
        skSeqNo = bundle.getInt("skSeqNo");

        return view;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.dialog_accept_ok: //

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("macIP", macIP);
                    intent.putExtra("skSeqNo", skSeqNo);
                    intent.putExtra("refrash", "Aceept");
                    startActivity(intent);
                    break;
                case R.id.dialog_accept_dismiss: // 취소 버튼
                    dismiss();
                    break;

            }

        }
    };
}
