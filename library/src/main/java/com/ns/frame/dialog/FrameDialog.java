package com.ns.frame.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.frame.R;

public class FrameDialog extends DialogFragment {

    private TextView tvMessage;
    private String message;

    public boolean isShowing() {
        return showing;
    }

    private boolean showing = false;

    public void setMessage(String message) {
        this.message = message;
        if (TextUtils.isEmpty(this.message)) {
            this.message = getString(R.string.is_loading);
        }
        if (tvMessage != null) {
            tvMessage.setText(this.message);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tvMessage != null) {
            tvMessage.setText(message);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.dialog_frame, null);
        if (TextUtils.isEmpty(message)) {
            message = getString(R.string.is_loading);
        }
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (showing) {
            return;
        }
        showing = true;
        super.show(manager, tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        showing = false;
    }

}
