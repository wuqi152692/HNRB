package com.hnzx.hnrb.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.CompoundButton;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;

/**
 * 修改性别对话框
 */
public class ModifySexDialog extends BaseDialogFragment {

    public static final String BUNDLE_KEY_SEX_TYPE = "BUNDLE_KEY_SEX_TYPE";

    public static final int SEX_TYPE_MALE = 0x00;

    public static final int SEX_TYPE_FEMALE = 0x01;

    private AppCompatRadioButton mRadioButtonMale;

    private AppCompatRadioButton mRadioButtonFemale;

    private SexTypeChangeListener sexTypeChangeListener;

    private int sexType = -1;

    public interface SexTypeChangeListener {
        void onSexTypeChange(boolean isMale);
    }

    public void setOnSexTypeChangeListener(SexTypeChangeListener listener) {
        this.sexTypeChangeListener = listener;
    }

    public static ModifySexDialog newInstance(int sexType) {
        ModifySexDialog modifySexDialog = new ModifySexDialog();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(BUNDLE_KEY_SEX_TYPE, sexType);
        modifySexDialog.setArguments(mBundle);
        return modifySexDialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_modify_sex;
    }

    @Override
    protected void initViews(View contentView) {
        mRadioButtonMale = (AppCompatRadioButton) contentView.findViewById(R.id.modify_sex_male);
        mRadioButtonFemale = (AppCompatRadioButton) contentView.findViewById(R.id.modify_sex_female);
    }

    @Override
    protected void initListeners() {
        mRadioButtonMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRadioButtonFemale.setChecked(false);
                    if (sexTypeChangeListener != null) sexTypeChangeListener.onSexTypeChange(true);
                    dismiss();
                }
            }
        });

        mRadioButtonFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRadioButtonMale.setChecked(false);
                    if (sexTypeChangeListener != null) sexTypeChangeListener.onSexTypeChange(false);
                    dismiss();
                }
            }
        });
    }

    @Override
    protected void initDatas() {
        Bundle mBundle = getArguments();
        sexType = mBundle.getInt(BUNDLE_KEY_SEX_TYPE, -1);
        if (sexType == -1) {
            mRadioButtonMale.setChecked(true);
        } else {
            mRadioButtonMale.setChecked(sexType == SEX_TYPE_MALE);
            mRadioButtonFemale.setChecked(sexType == SEX_TYPE_FEMALE);
        }
    }

}
