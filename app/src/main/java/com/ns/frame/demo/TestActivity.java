package com.ns.frame.demo;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.ns.frame.activity.BaseActivity;

/**
 * Created by xiaolifan on 2015/11/5.
 */
public class TestActivity extends BaseActivity {

    @Override
    protected boolean hasLoadView() {
        return false;
    }

    @Override
    protected boolean hasEmptyView() {
        return false;
    }

    @Override
    protected boolean hasToolsBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, 0, 0, "菜单");
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showLoadingDialog("文字提示文字提示文字提示文字提示文字提示文字提示文字提示文字提示文字提示文字提示文字提示文字提示文字提示");
        return true;
    }

}
