package com.hnzx.hnrb.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * @author: mingancai
 * @Time: 2017/4/11 0011.
 */

public class InvitedCodeShareDialog extends BaseDialogFragment implements View.OnClickListener {
    private ShareAction action;
    private String title, msg, imageUrl, shareUrl;

    public static InvitedCodeShareDialog newInstance(String title, String msg, String imageUrl, String shareUrl) {
        InvitedCodeShareDialog dialog = new InvitedCodeShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        bundle.putString("imageUrl", imageUrl);
        bundle.putString("shareUrl", shareUrl);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int getContentView() {
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        msg = bundle.getString("msg");
        imageUrl = bundle.getString("imageUrl");
        shareUrl = bundle.getString("shareUrl");
        return R.layout.dialog_invited_code_share;
    }

    @Override
    protected void initViews(View contentView) {
        contentView.findViewById(R.id.layout).setOnClickListener(this);
        contentView.findViewById(R.id.wechatFriend).setOnClickListener(this);
        contentView.findViewById(R.id.qqZone).setOnClickListener(this);
        contentView.findViewById(R.id.qq).setOnClickListener(this);
        contentView.findViewById(R.id.wechat).setOnClickListener(this);
        contentView.findViewById(R.id.sina).setOnClickListener(this);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        UMWeb web;
        web = new UMWeb(shareUrl);
        if (imageUrl != null && imageUrl.startsWith("http")) {
            web.setThumb(new UMImage(mActivity, imageUrl));
        } else {
            web.setThumb(new UMImage(mActivity, R.mipmap.icon_square));
        }
        web.setDescription(msg);
        web.setTitle(title);
        action = new ShareAction(mActivity).withMedia(web);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechatFriend:
                action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.wechat:
                action.setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.qq:
                action.setPlatform(SHARE_MEDIA.QQ);
                break;
            case R.id.qqZone:
                action.setPlatform(SHARE_MEDIA.QZONE);
                break;
            case R.id.sina:
                action.setPlatform(SHARE_MEDIA.SINA);
                break;
        }
        action.setCallback(listener).share();
    }

    private UMShareListener listener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            showToast("分享成功啦");
            dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            showToast("分享失败");
            dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            showToast("取消分享");
            dismiss();
        }
    };
}
