package com.ns.frame.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.frame.R;
import com.ns.frame.dialog.FrameDialog;

/**
 * NsFrame 精髓所在，所有Activity都必须集成于BaseActivity<br/>
 * Created by xiaolifan on 15-4-14.
 */
public class BaseActivity extends AppCompatActivity {

    private View mContentView = null;
    private View mLoadView = null;
    private View mEmptyView = null;

    private TextView emptyTextView;
    private Button retryButton;

    private FrameDialog dialogFragment = null;

    protected boolean hasLoadView() {
        return false;
    }

    protected boolean hasEmptyView() {
        return true;
    }

    @Override
    public void setContentView(int layoutResID) {
        //接管"setContentView"函数，用来想视图中添加LoadView，EmptyView
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null, false));
    }

    @Override
    public void setContentView(View view) {
        RelativeLayout rootView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_base, null, false);
        if (!hasToolsBar()) {
            rootView.removeAllViews();
        } else {
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.tb);
            setSupportActionBar(toolbar);
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.tb);
        view.setLayoutParams(lp);
        rootView.addView(view);
        this.mContentView = view;

        //添加没有数据时的空白视图
        if (hasEmptyView()) {
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.tb);
            int[] margins = positionEmptyView();
            if (margins != null && margins.length >= 4) {
                //根据子类给出的外边距设置视图外边距
                lp.leftMargin = margins[0];
                lp.topMargin = margins[1];
                lp.rightMargin = margins[2];
                lp.bottomMargin = margins[3];
            }
            this.mEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, null, false);
            this.mEmptyView.setOnTouchListener(stopTouchListener);
            emptyTextView = (TextView) mEmptyView.findViewById(R.id.tv_empty);
            retryButton = (Button) mEmptyView.findViewById(R.id.bt_retry);
            retryButton.setOnClickListener(retryButtonClickListener);
            rootView.addView(mEmptyView, lp);
        }

        //添加正在加载视图
        if (hasLoadView()) {
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.BELOW, R.id.tb);
            int[] margins = positionLoadView();
            if (margins != null && margins.length >= 4) {
                //根据子类给出的外边距设置视图外边距
                lp.leftMargin = margins[0];
                lp.topMargin = margins[1];
                lp.rightMargin = margins[2];
                lp.bottomMargin = margins[3];
            }
            this.mLoadView = LayoutInflater.from(this).inflate(R.layout.layout_loading, null, false);
            this.mLoadView.setOnTouchListener(stopTouchListener);
            rootView.addView(mLoadView, lp);
        }
        super.setContentView(rootView);
    }

    /**
     * 显示一个加载对话框，用来耗时操作时提示用户等待
     *
     * @param message 显示在对话框上面的文字，可以为空，为空时显示“正在加载…”
     */
    public void showLoadingDialog(@Nullable String message) {
        if (dialogFragment == null) {
            dialogFragment = new FrameDialog();
        }
        dialogFragment.setMessage(message);
        if (!dialogFragment.isShowing()) {
            dialogFragment.show(getSupportFragmentManager(), "loading");
        }
    }

    /**
     * 隐藏正在加载对话框
     */
    public void dismissLoadingDialog() {
        if (dialogFragment != null && dialogFragment.isShowing() && !isFinishing()) {
            dialogFragment.dismiss();
        }
    }

    /**
     * 显示正在加载视图，与"showLoadingDialog"不同，LoadView应该用在初次加载数据时（首次进入，没有数据重新加载时）
     */
    public void showLoadView() {
        if (mContentView == null || mLoadView == null) {
            return;
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }

        mLoadView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏显示正在加载视图
     */
    public void hideLoadView(boolean anim) {
        if (!hasLoadView() || mLoadView.getVisibility() == View.GONE) {
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
     * 显示没有数据空白视图
     *
     * @param emptyTips       文字提示
     * @param showRetryButton 是否显示重新加载按钮
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

    /**
     * 重新加载数据按钮被点击时的回调
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
     * 根据控件id得到控件，相当于findViewByid
     *
     * @param id
     * @return
     */
    public <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
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

    protected boolean hasToolsBar() {
        return true;
    }

}
