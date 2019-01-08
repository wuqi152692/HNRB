package com.hnzx.hnrb.tools;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by FoAng on 16/10/18 下午5:26；
 */
public class IEMUtil {

    /**
     * 关闭输入法键盘
     * @param mContext
     */
    public static void hideKeyBoard(Context mContext) {
        InputMethodManager inputMgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }


    /**
     * 显示输入法键盘
     */
    public static void showKeyBoard(Context mContext){
        InputMethodManager inputMgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 显示输入法键盘
     * @param mContext
     * @param mView
     */
    public static void showKeyBoard(Context mContext,View mView){
        InputMethodManager inputMgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.showSoftInput(mView, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 关闭输入法
     * @param mContext
     * @param mView
     */
    public static void hideKeyBoard(Context mContext,View mView){
        InputMethodManager inputMgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }
}
