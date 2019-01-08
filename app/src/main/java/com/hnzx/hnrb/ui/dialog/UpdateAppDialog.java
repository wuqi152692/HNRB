package com.hnzx.hnrb.ui.dialog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.UpdateMessageAdapter;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.responsebean.GetCheckUpdateRsp;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class UpdateAppDialog extends BaseDialogFragment {
    private RecyclerView recyclerView;
    private View update, close;
    private static GetCheckUpdateRsp _data;

    public static UpdateAppDialog newInstance(GetCheckUpdateRsp data) {
        UpdateAppDialog dialog = new UpdateAppDialog();
        _data = data;
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_update_app;
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        update = contentView.findViewById(R.id.update);
        close = contentView.findViewById(R.id.close);

        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initListeners() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(_data.url));
                    mActivity.startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    showToast("链接有误");
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initDatas() {
        UpdateMessageAdapter adapter = new UpdateMessageAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        List<String> messages = new ArrayList<>();
        if (_data.log.contains("\r\n")) {
            String[] datas = _data.log.split("\r\n");
            for (String msg : datas)
                messages.add(msg);
        } else messages.add(_data.log);
        adapter.setList(messages);
    }
}
