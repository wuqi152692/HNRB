package com.hnzx.hnrb.ui.government.governmentbox;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.responsebean.GetResumeListRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.zhy.autolayout.utils.AutoUtils;

public class LeaderAboutActivity extends BaseActivity implements View.OnClickListener {
    public static final String DATA_KEY = "datakey";
    private ImageView image;
    private TextView name, msg, data;
    private CheckBox showOrHideMsg;
    private RadioButton personalRB, aboutRB;
    private RadioGroup radioGroup;
    private Fragment fragmentPersonal, fragmentAbout;
    private Bundle bundle;
    private GetResumeListRsp resume;

    @Override
    protected int getLayoutId() {
        resume = (GetResumeListRsp) getIntent().getSerializableExtra(DATA_KEY);
        bundle = new Bundle();
        bundle.putString(DATA_KEY, resume.resume_id);
        return R.layout.activity_leader_about;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        image = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        msg = (TextView) findViewById(R.id.msg);
        data = (TextView) findViewById(R.id.data);
        showOrHideMsg = (CheckBox) findViewById(R.id.showOrHideMsg);
        personalRB = (RadioButton) findViewById(R.id.personal);
        aboutRB = (RadioButton) findViewById(R.id.about);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        AutoUtils.auto(name);
        AutoUtils.auto(msg);
        AutoUtils.auto(data);
        AutoUtils.auto(showOrHideMsg);
        AutoUtils.auto(personalRB);
        AutoUtils.auto(aboutRB);
        AutoUtils.auto(radioGroup);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentPersonal = new PersonalResumeFragment();
            fragmentPersonal.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragmentPersonal, String.valueOf(personalRB.getId())).commit();
        }
    }

    @Override
    protected void initData() {
        ((TextView) findViewById(R.id.title)).setText("相关信息");

        if (resume != null) {
            GlideTools.GlideRounded(this, resume.thumb, image, R.drawable.bg_morentu_datumoshi, 8);
            name.setText(resume.name);
            msg.setText(resume.brief);
            data.setText(resume.info);
        }


    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        showOrHideMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();

                fragmentPersonal = fm.findFragmentByTag(String.valueOf(personalRB.getId()));
                fragmentAbout = fm.findFragmentByTag(String.valueOf(aboutRB.getId()));

                if (fragmentPersonal != null) ft.hide(fragmentPersonal);
                if (fragmentAbout != null) ft.hide(fragmentAbout);
                switch (checkedId) {
                    case R.id.personal:
                        if (fragmentPersonal == null) {
                            fragmentPersonal = new PersonalResumeFragment();
                            fragmentPersonal.setArguments(bundle);
                            ft.add(R.id.content, fragmentPersonal, String.valueOf(personalRB.getId()));
                        } else
                            ft.show(fragmentPersonal);
                        break;
                    case R.id.about:
                        if (fragmentAbout == null) {
                            fragmentAbout = new AboutNewsFragment();
                            fragmentAbout.setArguments(bundle);
                            ft.add(R.id.content, fragmentAbout, String.valueOf(aboutRB.getId()));
                        } else
                            ft.show(fragmentAbout);
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
            default:
                break;
        }
    }
}