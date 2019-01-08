package com.hnzx.hnrb.ui.news;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.GovernmentListAdapter;
import com.hnzx.hnrb.adapter.HighDynamicAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.ui.leftsidebar.LeftMenuFragment;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

public class HighDynamicActivity extends BaseActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private RadioButton rb0, rb1, rb2;
    private Fragment dynamicFragment0, dynamicFragment1, dynamicFragment2;
    private Bundle bundle0, bundle1, bundle2;
    private int tag;
    private ArrayList<String> tags;

    @Override
    protected int getLayoutId() {
        tag = getIntent().getIntExtra("Tag", 1);
        tags = getIntent().getStringArrayListExtra(Constant.BEAN);
        bundle0 = new Bundle();
        bundle0.putInt(Constant.BEAN, 1);
        bundle1 = new Bundle();
        bundle1.putInt(Constant.BEAN, 2);
        bundle2 = new Bundle();
        bundle2.putInt(Constant.BEAN, 3);
        return R.layout.activity_high_dynamic;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("高层动态");

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb0 = (RadioButton) findViewById(R.id.rb0);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        AutoUtils.auto(rb0);
        AutoUtils.auto(rb1);
        AutoUtils.auto(rb2);

        rb0.setText(tags.get(0));
        rb1.setText(tags.get(1));
        rb2.setText(tags.get(2));

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new HighDynamicFragment();
            switch (tag) {
                case 1:
                    fragment.setArguments(bundle0);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, fragment, String.valueOf(rb0.getId())).commit();
                    rb0.setChecked(true);
                    break;
                case 2:
                    fragment.setArguments(bundle1);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, fragment, String.valueOf(rb1.getId())).commit();
                    rb1.setChecked(true);
                    break;
                case 3:
                    fragment.setArguments(bundle2);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content, fragment, String.valueOf(rb2.getId())).commit();
                    rb2.setChecked(true);
                    break;
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();

                dynamicFragment0 = fm.findFragmentByTag(String.valueOf(rb0.getId()));
                dynamicFragment1 = fm.findFragmentByTag(String.valueOf(rb1.getId()));
                dynamicFragment2 = fm.findFragmentByTag(String.valueOf(rb2.getId()));

                if (dynamicFragment0 != null) ft.hide(dynamicFragment0);
                if (dynamicFragment1 != null) ft.hide(dynamicFragment1);
                if (dynamicFragment2 != null) ft.hide(dynamicFragment2);
                System.err.println("489456444444444444444444444444444444444444444444444444");
                switch (checkedId) {
                    case R.id.rb0:
                        if (dynamicFragment0 == null) {
                            dynamicFragment0 = new HighDynamicFragment();
                            dynamicFragment0.setArguments(bundle0);
                            ft.add(R.id.content, dynamicFragment0, String.valueOf(rb0.getId()));
                        } else {
                            ft.show(dynamicFragment0);
                        }
                        break;
                    case R.id.rb1:
                        if (dynamicFragment1 == null) {
                            dynamicFragment1 = new HighDynamicFragment();
                            dynamicFragment1.setArguments(bundle1);
                            ft.add(R.id.content, dynamicFragment1, String.valueOf(rb1.getId()));
                        } else {
                            ft.show(dynamicFragment1);
                        }
                        break;
                    case R.id.rb2:
                        if (dynamicFragment2 == null) {
                            dynamicFragment2 = new HighDynamicFragment();
                            dynamicFragment2.setArguments(bundle2);
                            ft.add(R.id.content, dynamicFragment2, String.valueOf(rb2.getId()));
                        } else {
                            ft.show(dynamicFragment2);
                        }
                        break;
                }
                ft.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }
}
