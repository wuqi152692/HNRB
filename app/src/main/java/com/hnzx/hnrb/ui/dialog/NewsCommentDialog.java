package com.hnzx.hnrb.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.requestbean.CreateLiveDiscussReq;
import com.hnzx.hnrb.requestbean.CreateTopicCommentReq;
import com.hnzx.hnrb.requestbean.SetCommentCreatReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.KeyBoardUtils;
import com.hnzx.hnrb.ui.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/11 0011.
 */

public class NewsCommentDialog extends BaseDialogFragment implements View.OnClickListener {
    private static final String CONTENT_ID_KEY = "key1";
    private static final String QUOTED_KEY = "key2";
    private static final String FROM_KEY = "key4";
    private static final String ID = "key3";
    private static final String NAME_KEY = "key5";
    public static final int NEWS_COMMENT = 0;
    public static final int LIVE_COMMENT = 1;
    public static final int TOPIC_COMMENT = 3;
    private CheckedTextView send;
    private EditText shuo;
    private String content_id, quoted, id, name;
    private int from;

    /**
     * 一二级评论
     *
     * @param content_id
     * @param quoted
     * @param from
     * @return
     */
    public static NewsCommentDialog newInstance(String content_id, String quoted, int from, String name) {
        NewsCommentDialog dialog = new NewsCommentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_ID_KEY, content_id);
        bundle.putString(QUOTED_KEY, quoted);
        bundle.putInt(FROM_KEY, from);
        bundle.putString(NAME_KEY, name);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 三级级评论
     *
     * @param content_id
     * @param quoted
     * @param from
     * @return
     */
    public static NewsCommentDialog newInstance(String content_id, String quoted, String id, int from, String name) {
        NewsCommentDialog dialog = new NewsCommentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT_ID_KEY, content_id);
        bundle.putString(QUOTED_KEY, quoted);
        bundle.putString(ID, id);
        bundle.putInt(FROM_KEY, from);
        bundle.putString(NAME_KEY, name);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int getContentView() {
        content_id = getArguments().getString(CONTENT_ID_KEY);
        quoted = getArguments().getString(QUOTED_KEY);
        id = getArguments().getString(ID);
        from = getArguments().getInt(FROM_KEY, NEWS_COMMENT);
        name = getArguments().getString(NAME_KEY);
        return R.layout.dialog_news_comment;
    }

    @Override
    protected void initViews(View contentView) {
        send = (CheckedTextView) contentView.findViewById(R.id.send);
        shuo = (EditText) contentView.findViewById(R.id.shuo);
        if (name != null && name.length() > 0)
            shuo.setHint("回复" + name);
        KeyBoardUtils.openKeybord(shuo, mActivity);
    }

    @Override
    protected void initListeners() {
        send.setOnClickListener(this);
        shuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    send.setChecked(true);
                } else
                    send.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                if (send.isChecked()) {
                    if (!App.getInstance().isLogin()) {
                        startActivity(LoginActivity.newIntent(mActivity, false));
                        return;
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("content", shuo.getText().toString());
                    if (quoted != null && quoted.length() > 0)
                        params.put("quoted", quoted);
                    if (id != null && id.length() > 0)
                        params.put("id", id);
                    switch (from) {
                        case NEWS_COMMENT:
                            SetCommentCreatReq req = new SetCommentCreatReq();
                            req.content_id = content_id;
                            params.put("content_id", content_id);
                            App.getInstance().requestJsonDataPost(params, req, new commentListener(), null);
                            break;
                        case LIVE_COMMENT:
                            CreateLiveDiscussReq discussReq = new CreateLiveDiscussReq();
                            discussReq.live_id = content_id;
                            params.put("live_id", content_id);
                            App.getInstance().requestJsonDataPost(params, discussReq, new commentListener(), null);
                            break;
                        case TOPIC_COMMENT:
                            CreateTopicCommentReq topicCommentReqreq = new CreateTopicCommentReq();
                            topicCommentReqreq.topic_id = content_id;
                            params.put("topic_id", content_id);
                            App.getInstance().requestJsonDataPost(params, topicCommentReqreq, new commentListener(), null);
                            break;
                    }
                } else showToast("请输入内容");
                break;
            default:
                dismiss();
                break;
        }
    }

    public static final String ACTION = "Comment";

    private class commentListener implements Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                showToast("评论成功");
                KeyBoardUtils.closeKeybord(shuo, mActivity);
                mActivity.sendBroadcast(new Intent(ACTION));
                dismiss();
            } else showToast(response.Message);
        }
    }
}
