package com.hnzx.hnrb.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hnzx.hnrb.R;

/**
 * @author: mingancai
 * @Time: 2017/3/29 0029.
 */

public class SoundTrackView extends View {
    private int color;
    private byte[] mBytes;// 波形数组
    private Paint mPaint = new Paint();// 主画笔
    private int soundWidth;//单波宽度
    private int soundMaxHeight;//单波最大高度
    private int soundNum = 4;//显示波的数量

    public SoundTrackView(Context context) {
        this(context, null);
    }

    public SoundTrackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SoundTrackView);
        color = ta.getColor(R.styleable.SoundTrackView_track_color, getResources().getColor(R.color.colorAccent));
        soundNum = ta.getInteger(R.styleable.SoundTrackView_track_num, soundNum);
        mPaint.setColor(color);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        soundMaxHeight = getMeasuredHeight();
        soundWidth = getMeasuredWidth() / (2 * soundNum - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null)
            return;
        for (int i = 0; i < (mBytes.length > soundNum ? soundNum : mBytes.length); i++) {
            int degree = Math.abs(soundMaxHeight - Math.abs(mBytes[i]));
            canvas.drawRect(2 * i * soundWidth, degree > 0 ? (degree > soundMaxHeight ? soundMaxHeight : degree) :
                    0, (2 * i + 1) * soundWidth, soundMaxHeight, mPaint);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //开始波动
        start();
    }

    private void start() {

    }

    /**
     * 更新波形状态
     */
    public void updateState(byte[] fft) {
        byte[] model = new byte[fft.length / 2 + 1];

        model[0] = (byte) Math.abs(fft[0]);
        for (int i = 2, j = 1; j < soundNum; ) {
            model[j] = (byte) Math.hypot(Math.abs(fft[i]) + 1, Math.abs(fft[i + 1]) + 1);
            i += 2;
            j++;
        }
        mBytes = model;
        invalidate();
    }
}
