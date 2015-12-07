package com.ns.frame.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.frame.R;
import com.ns.frame.activity.BaseActivity;


/**
 * Created by xiaolifan on 2015/11/5.
 */
public class BaseFragment extends Fragment {

    private View mContentView = null;
    private View mLoadView = null;
    private View mEmptyView = null;

    private TextView emptyTextView;
    private Button retryButton;

    /**
     * You can invoke the method to add a load view and a empty view on your view, then, you can show/dismiss them when load something
     *
     * @param contentView your view
     */
    public View bindLoadView(LayoutInflater inflater, ViewGroup container, View contentView) {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_base, container, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(lp);
        rootView.addView(contentView, 0);
        this.mContentView = contentView;

        if (hasEmptyView()) {
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            int[] margins = positionEmptyView();
            if (margins != null && margins.length >= 4) {
                lp.leftMargin = margins[0];
                lp.topMargin = margins[1];
                lp.rightMargin = margins[2];
                lp.bottomMargin = margins[3];
            }
            this.mEmptyView = inflater.inflate(R.layout.layout_empty, container, false);
            this.mEmptyView.setOnTouchListener(stopTouchListener);
            emptyTextView = (TextView) mEmptyView.findViewById(R.id.tv_empty);
            retryButton = (Button) mEmptyView.findViewById(R.id.bt_retry);
            retryButton.setOnClickListener(retryButtonClickListener);
            rootView.addView(mEmptyView, lp);
        }

        if (hasLoadView()) {
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            int[] margins = positionLoadView();
            if (margins != null && margins.length >= 4) {
                lp.leftMargin = margins[0];
                lp.topMargin = margins[1];
                lp.rightMargin = margins[2];
                lp.bottomMargin = margins[3];
            }
            this.mLoadView = inflater.inflate(R.layout.layout_loading, container, false);
            this.mLoadView.setOnTouchListener(stopTouchListener);
            rootView.addView(mLoadView, lp);
        }

        return rootView;
    }

    /**
     * 显示加载视图
     */
    public void showLoadView() {
        if (mContentView == null || mLoadView == null) {
            return;
        }
        mEmptyView.setVisibility(View.GONE);
        mLoadView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载视图
     */
    public void hideLoadView(boolean anim) {
        if (mContentView == null || mLoadView == null || mLoadView.getVisibility() == View.GONE) {
            return;
        }

        if (!anim) {
            mLoadView.setVisibility(View.GONE);
            return;
        }

        AlphaAnimation alphaAnimation1 = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLoadView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation1.setDuration(300);
        mLoadView.startAnimation(alphaAnimation1);
    }

    /**
     * 显示空白视图
     *
     * @param emptyTips 显示给用户的提示语
     */
    public void showEmptyView(String emptyTips, boolean showRetryButton) {
        if (emptyTextView != null) {
            emptyTextView.setText(emptyTips);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }

        if (showRetryButton) {
            retryButton.setVisibility(View.VISIBLE);
        } else {
            retryButton.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏空白视图
     */
    public void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    protected boolean hasLoadView() {
        return true;
    }

    protected boolean hasEmptyView() {
        return true;
    }

    /**
     * Show the fragment
     */
    public void show(FragmentManager fm) {
        fm.beginTransaction().show(this).commit();
    }

    /**
     * Hide the fragment
     */
    public void hide(FragmentManager fm) {
        fm.beginTransaction().hide(this).commit();
    }

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

    /**
     * 用户点击重新加载按钮回调
     */
    protected void retryLoad() {

    }

    private View.OnClickListener retryButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            retryLoad();
        }
    };

    /**
     * 返回包含emptyView四个方向边距数组，用来控制emptyView的位置
     */
    protected int[] positionEmptyView() {
        return new int[]{0, 0, 0, 0};
    }

    /**
     * 返回包含loadView四个方向边距数组，用来控制loadView的位置
     */
    protected int[] positionLoadView() {
        return new int[]{0, 0, 0, 0};
    }

    /**
     * 根据控件id得到view中的控件，相当于findViewByid
     *
     * @param id
     * @return
     */
    public <T extends View> T getViewById(int id, View view) {
        return (T) view.findViewById(id);
    }

    /**
     * 拦截EmptyView和LoadView的触摸事件，防止误操作
     */
    private View.OnTouchListener stopTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

}
