package com.hnzx.hnrb.ui.interact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.PublishImageAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.Algorithm;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.DialogUtil;
import com.hnzx.hnrb.view.photopicker.PhotoPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author: mingancai
 * @Time: 2017/5/28 0028.
 */

public class TopicCommentActivity extends BaseActivity implements View.OnClickListener {
    public static final String ACTION = "TopicCommentActivity";
    private EditText editText;

    private RecyclerView recyclerView;

    private PublishImageAdapter adapter;

    private String topic_id;
    private String quoted;
    private String id;

    /**
     * 添加一级评论
     *
     * @param context
     * @param topic_id
     * @return
     */
    public static Intent newIntent(Context context, String topic_id) {
        Intent intent = new Intent(context, TopicCommentActivity.class);
        intent.putExtra(Constant.BEAN, topic_id);
        return intent;
    }

    /**
     * 添加二级评论
     *
     * @param context
     * @param topic_id
     * @return
     */
    public static Intent newIntent(Context context, String topic_id, String quoted) {
        Intent intent = new Intent(context, TopicCommentActivity.class);
        intent.putExtra(Constant.BEAN, topic_id);
        intent.putExtra("quoted", quoted);
        return intent;
    }

    /**
     * 添加三级评论
     *
     * @param context
     * @param topic_id
     * @return
     */
    public static Intent newIntent(Context context, String topic_id, String quoted, String id) {
        Intent intent = new Intent(context, TopicCommentActivity.class);
        intent.putExtra(Constant.BEAN, topic_id);
        intent.putExtra("quoted", quoted);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        topic_id = getIntent().getStringExtra(Constant.BEAN);
        quoted = getIntent().getStringExtra("quoted");
        id = getIntent().getStringExtra("id");
        return R.layout.activity_publish;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("写评论");

        editText = (EditText) findViewById(R.id.mEditText);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new PublishImageAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        List<String> data = new ArrayList<>();
        data.add("");
        adapter.setList(data);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.publish).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish:
                files = new ArrayList<>();
                for (String url : adapter.getList()) {
                    if (url != null && url.length() > 1)
                        files.add(new File(url));
                }
                if (TextUtils.isEmpty(editText.getText()) && files.size() <= 0) {
                    showTopToast("评论内容为空", true);
                    return;
                } else upLoadPublishMessage(editText.getText().toString());
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoPicker.REQUEST_CODE && data != null) {
            ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            List<String> adapterDatas = adapter.getList();
            if (adapterDatas.size() == 1) {
                photos.addAll(adapterDatas);
            } else {
                adapterDatas.remove(adapterDatas.size() - 1);
                photos.addAll(0, adapterDatas);
                photos.add("");
            }
            adapter.setList(photos);
        }
    }

    private List<File> files;

    private void upLoadPublishMessage(String content) {
        DialogUtil.showBaseDialog(this, "评论提交中");
        try {
            StringBuilder mStringBuilder = new StringBuilder();

            if (id != null && id.length() > 0)
                mStringBuilder.append("id=" + id + "&");
            if (quoted != null && quoted.length() > 0)
                mStringBuilder.append("quoted=" + quoted + "&");
            mStringBuilder.append("topic_id=" + topic_id + "&");
            mStringBuilder.append("user_id=" + App.getInstance().getLoginInfo().user_id + "&");

            String path = GetData.url
                    + GetData.CreateTopicComment
                    + mStringBuilder
                    + "token="
                    + Algorithm
                    .Md5Encrypt(
                            mStringBuilder.append(
                                    App.getInstance().getLoginInfo().auth_key)
                                    .toString(), GetData.encode).toLowerCase();

            App.getInstance().doPostPublishMsg(path, topic_id, quoted, id, content, files, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    DialogUtil.dismissDialog();
                    showTopToast("评论失败", true);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody mResponseBody = response.body();
                    BaseBeanRsp<JSONObject> mResult = JSONObject.parseObject(mResponseBody.string(), BaseBeanRsp.class);
                    DialogUtil.dismissDialog();
                    if (mResult != null && mResult.Status == 1) {
                        showTopToast("评论成功", false);
                        TopicCommentActivity.this.sendBroadcast(new Intent(ACTION));
                        finish();
                    } else showTopToast(mResult.Message, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
