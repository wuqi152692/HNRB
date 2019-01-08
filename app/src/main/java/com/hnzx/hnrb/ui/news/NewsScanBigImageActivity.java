package com.hnzx.hnrb.ui.news;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.PictureDetailsListAdapter;
import com.hnzx.hnrb.adapter.ScanPictureListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.audio.ImageActivity;
import com.hnzx.hnrb.view.HackyViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class NewsScanBigImageActivity extends BaseActivity {

    private List<String> img;
    private FrameLayout toplayout;
    private HackyViewPager pager;

    private ScanPictureListAdapter adapter;

    public static final String SHOW_POSITION = "SHOW_POSITION";

    private int position;

    @Override
    protected int getLayoutId() {
        img = getIntent().getStringArrayListExtra(Constant.BEAN);
        position = getIntent().getIntExtra(SHOW_POSITION, 0);
        return R.layout.activity_news_scan_big_image;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        toplayout = (FrameLayout) findViewById(R.id.topLayout);
        pager = (HackyViewPager) findViewById(R.id.pager);

        adapter = new ScanPictureListAdapter(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                NewsScanBigImageActivity.this.finish();
            }
        });
        pager.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        adapter.addPictureData(img);
        pager.setCurrentItem(position);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                showTopToast("图片保存到郑州晚报图集", false);
            }
        } catch (Exception e) {
            showTopToast("保存图片失败", false);
            e.printStackTrace();
        }

    }

    /**
     * 图集下载目录
     */
    File photoLocation() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "河南日报图片");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
