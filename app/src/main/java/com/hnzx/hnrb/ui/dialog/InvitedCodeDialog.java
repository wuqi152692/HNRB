package com.hnzx.hnrb.ui.dialog;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.network.Algorithm;
import com.hnzx.hnrb.requestbean.SetWirteInviteReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.IEMUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class InvitedCodeDialog extends BaseDialogFragment {
    private EditText codeET;
    private TextView sure, cancle;

    @Override
    protected int getContentView() {
        return R.layout.dialog_invited_code;
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        codeET = (EditText) contentView.findViewById(R.id.codeET);
        sure = (TextView) contentView.findViewById(R.id.sure);
        cancle = (TextView) contentView.findViewById(R.id.cancle);
    }

    @Override
    protected void initListeners() {
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(codeET.getText())) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("icode", codeET.getText().toString());
                    if (App.getInstance().isLogin())
                        params.put("uid", App.getInstance().getLoginInfo().user_id);
                    params.put("uinfo", getOnlyCode());
                    params.put("usystem", "2");
                    App.getInstance().requestJsonDataPost(params, new SetWirteInviteReq(), new InvitedListener(), null);
                    // 关闭当前输入法
                    IEMUtil.hideKeyBoard(mActivity);
                } else {
                    showToast("请输入邀请码");
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initDatas() {
    }

    private String getOnlyCode() {
        TelephonyManager TelephonyMgr = (TelephonyManager) mActivity
                .getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();

        WifiManager wm = (WifiManager) mActivity
                .getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        try {
            return Algorithm.Md5Encrypt(szImei + m_szWLANMAC, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private class InvitedListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                showToast("添加成功");
                dismiss();
            } else {
                showToast("添加失败");
            }
        }
    }
}
