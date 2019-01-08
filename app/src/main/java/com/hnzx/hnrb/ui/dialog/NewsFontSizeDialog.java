package com.hnzx.hnrb.ui.dialog;

import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.htmlTools.TWebView;
import com.hnzx.hnrb.tools.WebUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/11 0011.
 */

public class NewsFontSizeDialog extends BaseDialogFragment implements View.OnClickListener {
    private RadioGroup radioGroup;
    private RadioButton small, middle, big, biger;
    private AppCompatSeekBar seekBar;
    private int fontProgress;
    private TWebView webview;

    public static NewsFontSizeDialog newInstance(TWebView webview) {
        NewsFontSizeDialog dialog = new NewsFontSizeDialog();
        dialog.webview = webview;
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_news_font_size;
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        contentView.findViewById(R.id.layout).setOnClickListener(this);
        radioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup);
        small = (RadioButton) contentView.findViewById(R.id.small);
        middle = (RadioButton) contentView.findViewById(R.id.middle);
        big = (RadioButton) contentView.findViewById(R.id.big);
        biger = (RadioButton) contentView.findViewById(R.id.biger);
        seekBar = (AppCompatSeekBar) contentView.findViewById(R.id.seekBar);
    }

    @Override
    protected void initListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.small:
                        seekBar.setProgress(0);
                        break;
                    case R.id.middle:
                        seekBar.setProgress(20);
                        break;
                    case R.id.big:
                        seekBar.setProgress(40);
                        break;
                    case R.id.biger:
                        seekBar.setProgress(60);
                        break;
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (progress >= 0 && progress < 10) {
                    seekBar.setProgress(0);
                    setWebViewFontSize(0);
                    small.setChecked(true);
                } else if (progress >= 10 && progress < 30) {
                    seekBar.setProgress(20);
                    setWebViewFontSize(1);
                    middle.setChecked(true);
                } else if (progress >= 30 && progress < 50) {
                    seekBar.setProgress(40);
                    setWebViewFontSize(2);
                    big.setChecked(true);
                } else if (progress >= 50 && progress <= 60) {
                    seekBar.setProgress(60);
                    setWebViewFontSize(3);
                    biger.setChecked(true);
                }
            }
        });
    }

    private void setWebViewFontSize(int partInt) {
        switch (partInt) {
            case 0:
                if (fontProgress != 12) {
                    App.getInstance().setWebFontSize(12);
                    fontProgress = 12;
//                    webview.getSettings().setDefaultFontSize(12);
//                    webview.getSettings().setTextSize(WebSettings.TextSize.SMALLEST);
                    webview.getSettings().setTextZoom(100);
                }
                break;
            case 1:
                if (fontProgress != 14) {
                    App.getInstance().setWebFontSize(14);
                    fontProgress = 14;
//                    webview.getSettings().setDefaultFontSize(14);
//                    webview.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                    webview.getSettings().setTextZoom(115);
                }
                break;
            case 2:
                if (fontProgress != 16) {
                    App.getInstance().setWebFontSize(16);
                    fontProgress = 16;
//                    webview.getSettings().setDefaultFontSize(16);
//                    webview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    webview.getSettings().setTextZoom(130);
                }
                break;
            case 3:
                if (fontProgress != 18) {
                    App.getInstance().setWebFontSize(18);
                    fontProgress = 18;
//                    webview.getSettings().setDefaultFontSize(18);
//                    webview.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                    webview.getSettings().setTextZoom(145);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initDatas() {
        fontProgress = App.getInstance().getWebFontSize();
        if (fontProgress == 12) {
            seekBar.setProgress(0);
            small.setChecked(true);
        } else if (fontProgress == 14) {
            seekBar.setProgress(20);
            middle.setChecked(true);
        } else if (fontProgress == 16) {
            seekBar.setProgress(40);
            big.setChecked(true);
        } else if (fontProgress == 18) {
            seekBar.setProgress(60);
            biger.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
    }
}
