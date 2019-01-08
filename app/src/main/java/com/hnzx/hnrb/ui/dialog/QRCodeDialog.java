package com.hnzx.hnrb.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.twocode.decoding.BitmapDecoder;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.ref.WeakReference;

/**
 * @author: mingancai
 * @Time: 2017/6/5 0005.
 */

public class QRCodeDialog extends BaseDialogFragment {
    private View qr_code;
    private static String _data;
    private ProgressDialog bar;

    private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;

    public static QRCodeDialog newInstance(String data) {
        QRCodeDialog dialog = new QRCodeDialog();
        _data = data;
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_qr_code;
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        qr_code = contentView.findViewById(R.id.qr_code);
    }

    @Override
    protected void initListeners() {
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bar.isShowing())
                    dismiss();
            }
        });
        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (bar == null) {
                    bar = new ProgressDialog(mActivity);
                    bar.setTitle("识别中...");
                }
                mHandler.sendEmptyMessageDelayed(100, 500);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        qrCode();
                    }
                }).start();
            }
        });
    }

    private void qrCode() {
        Bitmap img = getCompressedBitmap(_data);

        BitmapDecoder decoder = new BitmapDecoder(
                mActivity);

        if (img != null) {
            Result result = decoder.getRawResult(img);
            if (result != null) {
                Message m = mHandler.obtainMessage();
                m.what = PARSE_BARCODE_SUC;
                String resultStr = ResultParser.parseResult(result)
                        .toString();
                m.obj = resultStr;
                setCaptureResult(resultStr);
                mHandler.sendMessage(m);
            } else {
                Message m = mHandler.obtainMessage();
                m.what = PARSE_BARCODE_FAIL;
                mHandler.sendMessage(m);
            }
        } else {
            Message m = mHandler.obtainMessage();
            m.what = PARSE_BARCODE_FAIL;
            mHandler.sendMessage(m);
        }
    }

    private void setCaptureResult(String result) {
        if (doHandlerResult(result)) {
            Intent intent = WebActivity.newIntent(mActivity, result, false);
            mActivity.startActivity(intent);
        } else {
            showTopToast(result, false);
        }
    }

    private boolean doHandlerResult(String decode) {
        return decode != null && (decode.startsWith("http://") || decode.startsWith("https://"));
    }

    private Handler mHandler;

    class MyHandler extends Handler {

        private WeakReference<Activity> activityReference;

        public MyHandler(Activity activity) {
            activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    bar.show();
                    break;
                case PARSE_BARCODE_SUC: // 解析图片成功
                    QRCodeDialog.this.showTopToast("识别成功", true);
                    bar.dismiss();
                    break;
                case PARSE_BARCODE_FAIL:// 解析图片失败
                    QRCodeDialog.this.showTopToast("未识别出相关信息", false);
                    bar.dismiss();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }

    @Override
    protected void initDatas() {
        mHandler = new MyHandler(mActivity);
    }

    private Bitmap getCompressedBitmap(String path) {
        int width = mActivity.getWindow().getDecorView().getRootView().getWidth();
        int height = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的根布局
        View view = mActivity.getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//        options.inSampleSize = calculateInSampleSize(options, 480, 800);
//        options.inJustDecodeBounds = false;

//        try {
//            return Glide.with(mActivity).load(path).asBitmap().fitCenter().into(500, 500).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return temBitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
