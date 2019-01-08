package com.hnzx.hnrb.tools;

import android.content.Context;

import com.hnzx.hnrb.view.dialog.ProgressHUD;

/**
 * Created by FoAng on 17/5/15 下午2:58;
 */

public class DialogUtil {

    public static ProgressHUD mBaseDialog;

    public static void showBaseDialog(Context mContext) {
        showBaseDialog(mContext, null);
    }

    public static void showBaseDialog(Context mContext, String content) {
        showBaseDialog(mContext, content, false, ProgressHUD.Style.SPIN_INDETERMINATE);
    }

    public static void showBaseDialog(Context mContext, String content, boolean isCanCancel, ProgressHUD.Style mStyle) {
        mBaseDialog = ProgressHUD.create(mContext);
        mBaseDialog.setStyle(mStyle)
                .setLabel(content)
                .setCancellable(isCanCancel)
                .setDimAmount(0.5f);
        if (mBaseDialog != null && !mBaseDialog.isShowing()) mBaseDialog.show();
    }

    public static void dismissDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }

}
