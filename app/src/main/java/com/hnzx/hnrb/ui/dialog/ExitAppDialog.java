package com.hnzx.hnrb.ui.dialog;

import android.view.View;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.ui.LoginActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class ExitAppDialog extends BaseDialogFragment {
    private View cancle, sure;

    @Override
    protected int getContentView() {
        return R.layout.dialog_login_out;
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        cancle = contentView.findViewById(R.id.cancle);
        sure = contentView.findViewById(R.id.sure);
    }

    @Override
    protected void initListeners() {
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mActivity.finish();
            }
        });
    }

    @Override
    protected void initDatas() {

    }
}
