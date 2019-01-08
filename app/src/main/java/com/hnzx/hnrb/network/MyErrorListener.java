package com.hnzx.hnrb.network;

import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.view.MultiStateView;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class MyErrorListener implements com.android.volley.Response.ErrorListener {
    private MultiStateView view;
    private String msgToast;

    public MyErrorListener(String msgToast) {
        this.msgToast = msgToast;
    }

    public MyErrorListener(MultiStateView view) {
        this.view = view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (view != null)
            view.setViewState(MultiStateView.VIEW_STATE_ERROR);
        else
            Toast.makeText(App.getInstance().getApplicationContext(), msgToast, Toast.LENGTH_SHORT).show();
    }
}
