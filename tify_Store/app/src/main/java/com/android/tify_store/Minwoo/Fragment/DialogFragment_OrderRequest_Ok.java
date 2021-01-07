package com.android.tify_store.Minwoo.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.tify_store.R;

public class DialogFragment_OrderRequest_Ok extends DialogFragment implements View.OnClickListener {

    String TAG = "DialogFragment_OrderRequest_Ok";
    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";
    private String mMainMsg;

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


        return view;
    }
}
