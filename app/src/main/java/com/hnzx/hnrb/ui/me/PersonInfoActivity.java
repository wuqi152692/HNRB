package com.hnzx.hnrb.ui.me;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.ModifySexReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.tools.DialogUtil;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.PermissionCheckUtil;
import com.hnzx.hnrb.ui.dialog.InvitedCodeShareDialog;
import com.hnzx.hnrb.ui.dialog.ModifySexDialog;
import com.hnzx.hnrb.view.TopHeadView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by FoAng on 17/4/24 下午9:20;
 * 个人信息页面
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final String ACTION_STRING_CHANGE_AVATAR = "ACTION_STRING_CHANGE_AVATAR";

    private static final int REQUEST_CODE_MODIFY_NICK_NAME = 0x01 << 1;

    private TopHeadView mTopHeadView;

    private ImageView mImageViewHead;

    private TextView mTextViewNick;

    private TextView mTextViewSex;

    private TextView mTextViewMobile;

    private TextView mTextViewInvitedCode;

    private LinearLayout mLinearLayoutNick;

    private LinearLayout mLinearLayoutSex;

    private LinearLayout mLinearLayoutMobile;

    private LinearLayout mLinearLayoutPwd;

    private TextView mTextViewLoginOut;

    private InvitedCodeShareDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person_info;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTopHeadView = getViewById(R.id.person_info_headView);
        mImageViewHead = getViewById(R.id.person_info_imageHead);
        mTextViewNick = getViewById(R.id.person_info_nickName);
        mTextViewMobile = getViewById(R.id.person_info_mobile);
        mTextViewSex = getViewById(R.id.person_info_sex);
        mTextViewLoginOut = getViewById(R.id.person_info_loginOut);
        mLinearLayoutPwd = getViewById(R.id.person_info_modifyPwd);
        mTextViewInvitedCode = getViewById(R.id.person_info_invitedCode);

        mLinearLayoutMobile = getViewById(R.id.person_info_modifyMobile);
        mLinearLayoutSex = getViewById(R.id.person_info_sexContent);
        mLinearLayoutNick = getViewById(R.id.person_info_nickNameContent);
    }

    @Override
    protected void initData() {
        mTopHeadView.setHeadTitle("个人信息");
        final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
        if (mUserInfoRsp != null) {
            mTextViewSex.setText(TextUtils.isEmpty(mUserInfoRsp.sex) ? "男" : Integer.parseInt(mUserInfoRsp.sex) == 1 ? "男" : "女");
            mTextViewNick.setText(TextUtils.isEmpty(mUserInfoRsp.nickname) ? "" : mUserInfoRsp.nickname);
            mTextViewMobile.setText(TextUtils.isEmpty(mUserInfoRsp.username) ? "" : getSubMobileString(mUserInfoRsp.username));
            GlideTools.Glide(PersonInfoActivity.this, mUserInfoRsp.avatar, mImageViewHead, R.drawable.icon_default_head);
            mTextViewInvitedCode.setText(mUserInfoRsp.invite_num);
        }
    }

    private String getSubMobileString(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        } else {
            return mobile.substring(0, 3) + "*****" + mobile.substring(mobile.length() - 3, mobile.length());
        }
    }

    @Override
    protected void initListeners() {
        mLinearLayoutPwd.setOnClickListener(this);
        mLinearLayoutSex.setOnClickListener(this);
        mLinearLayoutMobile.setOnClickListener(this);
        mLinearLayoutNick.setOnClickListener(this);

        mImageViewHead.setOnClickListener(this);
        mTextViewLoginOut.setOnClickListener(this);
        findViewById(R.id.person_info_invitedShare).setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MODIFY_NICK_NAME && resultCode == Activity.RESULT_OK) {
            updateUserInfoContent();
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    System.err.println(new File(selectList.get(0).getCutPath()).length());
                    uploadUserAvatar(new File(selectList.get(0).getCutPath()));
                    break;
            }
        }
    }

    /**
     * 用户上传头像
     *
     * @param myFile
     */
    private void uploadUserAvatar(File myFile) {
        DialogUtil.showBaseDialog(this, "正在上传");
        App.getInstance().doUpLoadUserAvatar(myFile, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showTopToast("上传失败，请重试", false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody mResponseBody = response.body();
                BaseBeanRsp<JSONObject> mResult = JSONObject.parseObject(mResponseBody.string(), BaseBeanRsp.class);
                if (mResult.Status == 1) {
                    showTopToast("头像上传成功", true);
                    final String avatarUrl = mResult.Info.getString("avatar");
                    PersonInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.dismissDialog();
                            sendUserInfoChangeBroadCast(avatarUrl);
                            updateUserInfoContent();
                        }
                    });
                } else {
                    showTopToast("上传失败：" + mResult.Message, false);
                }
            }
        });
    }

    private void sendUserInfoChangeBroadCast(String userAvatar) {
        final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
        if (!TextUtils.isEmpty(userAvatar) && mUserInfoRsp != null) {
            mUserInfoRsp.avatar = userAvatar;
            App.getInstance().saveLoginUserInfo(mUserInfoRsp);
            Intent mIntent = new Intent();
            mIntent.setAction(ACTION_STRING_CHANGE_AVATAR);
            sendBroadcast(mIntent);
        }
    }

    private void updateUserInfoContent() {
        final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
        if (mUserInfoRsp != null && App.getInstance().isLogin()) {
            mTextViewNick.setText(mUserInfoRsp.nickname);
            GlideTools.Glide(PersonInfoActivity.this, mUserInfoRsp.avatar, mImageViewHead, R.drawable.icon_default_head);
            mTextViewSex.setText(TextUtils.isEmpty(mUserInfoRsp.sex) ? "男" : Integer.parseInt(mUserInfoRsp.sex) == 1 ? "男" : "女");
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.person_info_imageHead:
                PermissionCheckUtil.getInstance(this).checkPermission(this, new PermissionCheckUtil.CheckListener() {
                    @Override
                    public void isPermissionOn() {
                        PictureSelector.create(PersonInfoActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .imageSpanCount(4)// 每行显示个数 int
                                .selectionMode(PictureConfig.SINGLE)
                                .previewImage(true)
                                .isCamera(true)
                                .imageFormat(PictureMimeType.PNG)
                                .isZoomAnim(true)
                                .sizeMultiplier(0.5f)
                                .setOutputCameraPath("/CustomPath")
                                .enableCrop(true)
                                .withAspectRatio(1, 1)
                                .freeStyleCropEnabled(true)
                                .showCropFrame(true)
                                .showCropGrid(true)
                                .rotateEnabled(true)
                                .scaleEnabled(true)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                    }

                    @Override
                    public void isPermissionNo() {
                        showTopToast("请开启权限", false);
                    }
                }, PermissionCheckUtil.PERMISSION_SD_STORAGE);
                break;
            case R.id.person_info_modifyMobile:
                startActivity(ModifyMobileActivity.newIntent(this, ModifyMobileActivity.class));
                break;
            case R.id.person_info_nickNameContent:
                final UserInfoRsp userInfoRsp = App.getInstance().getLoginInfo();
                startActivityForResult(ModifyNickActivity.newIntent(this, userInfoRsp.nickname), REQUEST_CODE_MODIFY_NICK_NAME);
                break;
            case R.id.person_info_modifyPwd:
                startActivity(ModifyPwdActivity.newIntent(this, false, null, null));
                break;
            case R.id.person_info_sexContent:
                showSexTypeChangeDialog();
                break;
            case R.id.person_info_loginOut:
                if (builder == null) {
                    builder = new AlertDialog.Builder(this);
                    builder.setMessage("您确定退出当前账号？");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getInstance().cleanLoginUserInfo();
                            startActivity(MainActivity.newIntent(PersonInfoActivity.this, true));
                        }
                    });
                    builder.setPositiveButton("取消", null);
                    builder.create();
                }
                builder.show();
                break;
            case R.id.person_info_invitedShare:
                share();
                break;
        }
    }

    private void share() {
        if (dialog == null || dialog.isAdded()) {
            final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
            dialog = InvitedCodeShareDialog.newInstance("河南日报，“河”你在一起", "快来“河”好友一起畅读河南日报吧", "", mUserInfoRsp.invite_url + "?code=" + mUserInfoRsp.invite_num);
        }
        dialog.show(PersonInfoActivity.this.getFragmentManager(), getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null)
            dialog.dismiss();
    }

    private AlertDialog.Builder builder;

    private ModifySexDialog modifySexDialog;

    private void showSexTypeChangeDialog() {
        final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
        modifySexDialog = ModifySexDialog.newInstance(mUserInfoRsp.sex.equals("1") ? ModifySexDialog.SEX_TYPE_MALE :
                ModifySexDialog.SEX_TYPE_FEMALE);

        modifySexDialog.setOnSexTypeChangeListener(new ModifySexDialog.SexTypeChangeListener() {
            @Override
            public void onSexTypeChange(boolean isMale) {
                DialogUtil.showBaseDialog(PersonInfoActivity.this);
                modifySexType(isMale);
            }
        });
        modifySexDialog.show(getFragmentManager(), "modify_sex_type");
    }

    private void modifySexType(final boolean isMale) {
        ModifySexReq modifySexReq = new ModifySexReq();
        Map<String, String> mParams = new HashMap<String, String>() {{
            put("sex", isMale ? "1" : "0");
        }};

        App.getInstance().requestJsonDataPost(mParams,
                modifySexReq, new com.android.volley.Response.Listener<BaseBeanRsp<String>>() {
                    @Override
                    public void onResponse(BaseBeanRsp<String> response) {
                        DialogUtil.dismissDialog();
                        if (response != null && response.Status == 1) {
                            showTopToast("修改成功", true);
                            final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
                            mUserInfoRsp.sex = isMale ? "1" : "0";
                            App.getInstance().saveLoginUserInfo(mUserInfoRsp);
                            updateUserInfoContent();
                        } else {
                            showTopToast("修改失败" + ((response != null && !TextUtils.isEmpty(response.Message)) ? ":" + response.Message :
                                    ""), false);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DialogUtil.dismissDialog();
                        showTopToast("修改失败：" + error.getMessage(), false);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(this);
    }
}
