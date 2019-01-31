package com.hnzx.hnrb.ui.live;

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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PublishActivity extends BaseActivity implements View.OnClickListener {
    private EditText editText;

    private RecyclerView recyclerView;

    private PublishImageAdapter adapter;

    private String live_id;

    public static Intent newIntent(Context context, String live_id) {
        Intent intent = new Intent(context, PublishActivity.class);
        intent.putExtra(Constant.BEAN, live_id);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        live_id = getIntent().getStringExtra(Constant.BEAN);
        return R.layout.activity_publish;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("发布");

        editText = (EditText) findViewById(R.id.mEditText);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new PublishImageAdapter(this, false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        List<LocalMedia> data = new ArrayList<>();
        data.add(new LocalMedia());
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
                for (LocalMedia media : adapter.getList()) {
                    if (media != null)
                        files.add(new File(media.getPath()));
                }
                if (TextUtils.isEmpty(editText.getText()) && files.size() <= 0) {
                    showTopToast("发布内容为空", true);
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private List<File> files;

    private void upLoadPublishMessage(String content) {
        DialogUtil.showBaseDialog(this, "正在上传");
        try {
            StringBuilder mStringBuilder = new StringBuilder();

            mStringBuilder.append("device_type=android&");
            mStringBuilder.append("live_id=" + live_id + "&");
            mStringBuilder.append("user_id=" + App.getInstance().getLoginInfo().user_id + "&");
            String path = GetData.url
                    + GetData.CreateHostStatement
                    + mStringBuilder
                    + "token="
                    + Algorithm
                    .Md5Encrypt(
                            mStringBuilder.append(
                                    App.getInstance().getLoginInfo().auth_key)
                                    .toString(), GetData.encode).toLowerCase();

            App.getInstance().doPostPublishMsg(path, null, null, null, content, files, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    DialogUtil.dismissDialog();
                    showTopToast("发布失败", true);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody mResponseBody = response.body();
                    BaseBeanRsp<JSONObject> mResult = JSONObject.parseObject(mResponseBody.string(), BaseBeanRsp.class);
                    DialogUtil.dismissDialog();
                    if (mResult != null && mResult.Status == 1) {
                        showTopToast("发布成功", false);
                        finish();
                    } else showTopToast(mResult.Message, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(this);
    }
}
