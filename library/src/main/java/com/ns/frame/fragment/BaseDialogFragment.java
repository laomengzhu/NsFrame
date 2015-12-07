package com.ns.frame.fragment;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

import com.ns.frame.activity.BaseActivity;


/**
 * Created by xiaolifan on 2015/11/5.
 */
public class BaseDialogFragment extends DialogFragment {

    protected void showLoadingDialog(String message) {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            ((BaseActivity) activity).showLoadingDialog(message);
        }
    }

    protected void dismissLoadingDialog() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            ((BaseActivity) activity).dismissLoadingDialog();
        }
    }
}
