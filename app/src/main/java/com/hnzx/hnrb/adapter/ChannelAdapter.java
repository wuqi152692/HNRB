package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.channel.helper.OnItemMoveListener;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.ui.news.NewsFragment;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 16/7/26.
 */
@SuppressWarnings("deprecation")
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {

    public static final int TYPE_MY_HEADER = 0;

    public static final int TYPE_MY = 1;

    public static final int TYPE_OTHER_HEADER = 2;

    public static final int TYPE_OTHER = 3;

    List<GetTopCategoryRsp> listAll;

    public List<GetTopCategoryRsp> listMy = new ArrayList<>();

    private List<GetTopCategoryRsp> listOther = new ArrayList<>();

    private Activity activity;

    private boolean isEdit;

    private ItemTouchHelper itemTouchHelper;

    private OnMyChannelItemCliclListener myChannelItemCliclListener;

    private static final long ANIM_TIME = 360L;

    public ChannelAdapter(Activity activity, ItemTouchHelper itemTouchHelper, List<GetTopCategoryRsp> listAll) {
        this.activity = activity;
        this.itemTouchHelper = itemTouchHelper;
        this.listAll = listAll;

        for (GetTopCategoryRsp rsp : listAll) {
            if (rsp.isdefault == 0) {
                listOther.add(rsp);
            } else {
                listMy.add(rsp);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_MY_HEADER;
        else if (position == listMy.size() + 1)
            return TYPE_OTHER_HEADER;
        else if (position > 0 && position < listMy.size() + 1) {
            return TYPE_MY;
        } else
            return TYPE_OTHER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channel_my_header, parent, false);
                MyHeaderViewHolder myHeaderViewHolder = new MyHeaderViewHolder(view);
                myHeaderViewHolder.sort.setOnClickListener(new myHeaderEditClick(parent, myHeaderViewHolder));
//                myHeaderViewHolder.myHeaderClose.setOnClickListener(new myHeaderCloseClick(parent));
                return myHeaderViewHolder;
            case TYPE_MY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channel_my, parent, false);
                MyViewHolder myViewHolder = new MyViewHolder(view);
                myViewHolder.myText.setOnClickListener(new myTextClick(parent, myViewHolder));
                myViewHolder.myText.setOnLongClickListener(new myTextLongClick(parent, myViewHolder));
                myViewHolder.myText.setOnTouchListener(new myTextTouch(myViewHolder));
                return myViewHolder;
            case TYPE_OTHER_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channel_other_header, parent, false);
                otherHeaderViewHolder otherHeaderViewHolder = new otherHeaderViewHolder(view);
                return otherHeaderViewHolder;
            case TYPE_OTHER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channel_other, parent, false);
                OtherViewHoder otherViewHoder = new OtherViewHoder(view);
                otherViewHoder.otherText.setOnClickListener(new otherTextClick(parent, otherViewHoder));
                return otherViewHoder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHeaderViewHolder) {
            MyHeaderViewHolder myHeaderViewHolder = (MyHeaderViewHolder) holder;
            myHeaderViewHolder.name.setText(isEdit ? "已添加" : "已选择");
            myHeaderViewHolder.tip.setText(isEdit ? "长按拖拽排序" : "点击频道名称移除");
            myHeaderViewHolder.sort.setText(isEdit ? "完成" : "排序");
        } else if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.myText.setText(listMy.get(position - 1).catname);
            myViewHolder.myText.setBackgroundResource(R.drawable.shape_channel_my_bg);
            //noinspection
            myViewHolder.myText.setTag(position - 1);
            myViewHolder.myDelete.setVisibility(isEdit && (position - 1 > 0) ? View.VISIBLE : View.GONE);
        } else if (holder instanceof otherHeaderViewHolder) {
            otherHeaderViewHolder otherHeaderViewHolder = (ChannelAdapter.otherHeaderViewHolder) holder;
            otherHeaderViewHolder.name.setText(isEdit ? "可添加" : "可选择");
        } else if (holder instanceof OtherViewHoder) {
            OtherViewHoder otherViewHoder = (OtherViewHoder) holder;
            otherViewHoder.otherText.setText(listOther.get(position - listMy.size() - 2).catname);
        }
    }


    @Override
    public int getItemCount() {
        return listMy.size() + listOther.size() + 2;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (toPosition == 1)
            toPosition += 1;

        GetTopCategoryRsp rsp = listMy.get(fromPosition - 1);

        listMy.remove(fromPosition - 1);

        listMy.add(toPosition - 1, rsp);

        notifyItemMoved(fromPosition, toPosition);
    }

    class MyHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView name, tip, sort;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.nameMy);
            tip = (TextView) itemView.findViewById(R.id.tipMy);
            sort = (TextView) itemView.findViewById(R.id.sort);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView myText;

        private ImageView myDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            myText = (TextView) itemView.findViewById(R.id.myText);
            myDelete = (ImageView) itemView.findViewById(R.id.myDelete);
        }
    }

    class otherHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public otherHeaderViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    class OtherViewHoder extends RecyclerView.ViewHolder {

        private TextView otherText;

        public OtherViewHoder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            otherText = (TextView) itemView.findViewById(R.id.otherText);
        }
    }

    private class myHeaderEditClick implements View.OnClickListener {

        ViewGroup viewGroup;

        MyHeaderViewHolder myHeaderViewHolder;

        myHeaderEditClick(ViewGroup viewGroup, MyHeaderViewHolder myHeaderViewHolder) {

            this.viewGroup = viewGroup;

            this.myHeaderViewHolder = myHeaderViewHolder;

        }

        @Override
        public void onClick(View v) {
            if (isEdit) {
                cancelEdit((RecyclerView) viewGroup);
            } else {
                startEdit((RecyclerView) viewGroup);
            }
        }
    }

    private class myHeaderCloseClick implements View.OnClickListener {

        ViewGroup viewGroup;

        myHeaderCloseClick(ViewGroup viewGroup) {

            this.viewGroup = viewGroup;

        }

        @Override
        public void onClick(View v) {
            cancelEdit((RecyclerView) viewGroup);
        }
    }

    @SuppressWarnings("UnusedAssignment")
    private class myTextClick implements View.OnClickListener {

        ViewGroup viewGroup;

        MyViewHolder myViewHolder;

        myTextClick(ViewGroup viewGroup, MyViewHolder myViewHolder) {

            this.viewGroup = viewGroup;

            this.myViewHolder = myViewHolder;
        }

        @Override
        public void onClick(View v) {

            int position = myViewHolder.getAdapterPosition();

            if ((int) v.getTag() > 0) {

                RecyclerView recyclerView = (RecyclerView) viewGroup;

                View targetView = recyclerView.getLayoutManager().findViewByPosition(listMy.size() + 2);

                View currentView = recyclerView.getLayoutManager().findViewByPosition(position);

                if (recyclerView.indexOfChild(targetView) >= 0) {

                    int targetX, targetY;

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                    int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();

                    if ((listMy.size() - 1) % spanCount == 0) {

                        View preTargetView = recyclerView.getLayoutManager().findViewByPosition(listMy.size() + 1);

                        targetX = preTargetView.getLeft();

                        targetY = preTargetView.getTop();

                    } else {

                        targetX = targetView.getLeft();

                        targetY = targetView.getTop();

                    }

                    moveToOther(myViewHolder);

//                    startAnimation(recyclerView, currentView, targetX, targetY);


                } else
                    moveToOther(myViewHolder);
                sendDataNotify(false);
            }
        }
    }

    private class myTextLongClick implements View.OnLongClickListener {

        ViewGroup viewGroup;

        MyViewHolder myViewHolder;

        myTextLongClick(ViewGroup viewGroup, MyViewHolder myViewHolder) {

            this.viewGroup = viewGroup;

            this.myViewHolder = myViewHolder;

        }

        @Override
        public boolean onLongClick(View v) {
            if ((int) v.getTag() > 0) {
                if (!isEdit) {
                    RecyclerView recyclerView = (RecyclerView) viewGroup;
                    startEdit(recyclerView);
                }
                itemTouchHelper.startDrag(myViewHolder);
            }
            return true;
        }
    }

    private class myTextTouch implements View.OnTouchListener {

        MyViewHolder myViewHolder;

        long startTime;

        static final long SPACE_TIME = 100;

        myTextTouch(MyViewHolder myViewHolder) {

            this.myViewHolder = myViewHolder;

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (isEdit && (int) v.getTag() > 0) {
                switch (MotionEventCompat.getActionMasked(event)) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                            itemTouchHelper.startDrag(myViewHolder);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        startTime = 0;
                        break;
                }
            }
            return false;
        }
    }

    private class otherTextClick implements View.OnClickListener {

        ViewGroup viewGroup;

        OtherViewHoder otherViewHoder;

        otherTextClick(ViewGroup viewGroup, OtherViewHoder otherViewHoder) {

            this.viewGroup = viewGroup;

            this.otherViewHoder = otherViewHoder;
        }

        @Override
        public void onClick(View v) {

            RecyclerView recyclerView = (RecyclerView) viewGroup;

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            int currentPosition = otherViewHoder.getAdapterPosition();

            View currentView = layoutManager.findViewByPosition(currentPosition);

            View preTargetView = layoutManager.findViewByPosition(listMy.size() + 1);

            if (recyclerView.indexOfChild(preTargetView) >= 0) {

                int targetX = preTargetView.getLeft();

                int targetY = preTargetView.getTop();

                int targetPosition = listMy.size() + 1;

                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

                int spanCount = gridLayoutManager.getSpanCount();

                if ((targetPosition - 1) % spanCount == 0) {
                    View targetView = layoutManager.findViewByPosition(targetPosition);

                    targetX = targetView.getLeft();

                    targetY = targetView.getTop();
                } else {
                    targetX += preTargetView.getWidth();

                    if (gridLayoutManager.findLastVisibleItemPosition() == getItemCount() - 1) {

                        if ((getItemCount() - 1 - listMy.size() - 2) % spanCount == 0) {
                            int firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();

                            if (firstVisiblePosition == 0) {
                                if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                    int offset = (-recyclerView.getChildAt(0).getTop() - recyclerView.getPaddingTop());

                                    targetY += offset;
                                }
                            } else
                                targetY += preTargetView.getHeight();
                        }
                    }

                }

                if (currentPosition == gridLayoutManager.findFirstVisibleItemPosition() && (currentPosition - listMy.size() - 2) % spanCount != 0 && (targetPosition - 1) % spanCount != 0)
                    moveToMyWithDelay(otherViewHoder);

                else
                    moveToMy(otherViewHoder);

//                startAnimation(recyclerView, currentView, targetX, targetY);
            } else
                moveToMy(otherViewHoder);

            sendDataNotify(false);
        }
    }

    /**
     * 开启编辑模式
     */
    private void startEdit(RecyclerView recyclerView) {

        isEdit = true;

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View view = recyclerView.getChildAt(i);
            if (i == 0 && view == recyclerView.getLayoutManager().findViewByPosition(0)) {
                TextView nameMy = (TextView) recyclerView.findViewById(R.id.nameMy);
                TextView tipMy = (TextView) recyclerView.findViewById(R.id.tipMy);
                TextView sort = (TextView) recyclerView.findViewById(R.id.sort);
                nameMy.setText(isEdit ? "已添加" : "已选择");
                tipMy.setText(isEdit ? "长按拖拽排序" : "点击频道名称移除");
                sort.setText(isEdit ? "完成" : "排序");
                TextView name = (TextView) recyclerView.findViewById(R.id.name);
                name.setText(isEdit ? "可添加" : "可选择");
            }
            ImageView myDelete = (ImageView) view.findViewById(R.id.myDelete);
            if (myDelete != null && i - 1 > 0) {
                myDelete.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 完成编辑模式
     */
    private void cancelEdit(RecyclerView recyclerView) {

        isEdit = false;

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View view = recyclerView.getChildAt(i);
            if (i == 0 && view == recyclerView.getLayoutManager().findViewByPosition(0)) {
                TextView nameMy = (TextView) recyclerView.findViewById(R.id.nameMy);
                TextView tipMy = (TextView) recyclerView.findViewById(R.id.tipMy);
                TextView sort = (TextView) recyclerView.findViewById(R.id.sort);
                nameMy.setText(isEdit ? "已添加" : "已选择");
                tipMy.setText(isEdit ? "长按拖拽排序" : "点击频道名称移除");
                sort.setText(isEdit ? "完成" : "排序");
                TextView name = (TextView) recyclerView.findViewById(R.id.name);
                name.setText(isEdit ? "可添加" : "可选择");
            }
            ImageView myDelete = (ImageView) view.findViewById(R.id.myDelete);
            if (myDelete != null) {
                myDelete.setVisibility(View.INVISIBLE);
            }
        }
        sendDataNotify(true);
    }

    /**
     * 发送刷新栏目数据
     */
    private void sendDataNotify(boolean isFinish) {
        ArrayList<GetTopCategoryRsp> list = new ArrayList<>();

        for (GetTopCategoryRsp rsp : listMy) {
            @SuppressWarnings("UnnecessaryLocalVariable") GetTopCategoryRsp item = rsp;
            item.isdefault = 1;
            list.add(item);
        }

        for (GetTopCategoryRsp rsp : listOther) {
            GetTopCategoryRsp item = rsp;
            item.isdefault = 0;
            list.add(item);
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(NewsFragment.COLUMNDATA, list);
        activity.setResult(10, intent);
        // 如为编辑完成模式，则关闭当前Activity
        if (isFinish) activity.finish();
    }

    /**
     * 我的栏目移动到其他栏目
     */

    private void moveToOther(MyViewHolder myViewHolder) {

        int position = myViewHolder.getAdapterPosition();

        int startPosition = position - 1;

        if (startPosition > listMy.size() - 1) {
            return;
        }

        GetTopCategoryRsp rsp = listMy.get(startPosition);

        listMy.remove(startPosition);

        listOther.add(0, rsp);

        notifyDataSetChanged();

    }

    /**
     * 其他栏目移动到我的栏目
     */
    private void moveToMy(OtherViewHoder otherViewHoder) {

        final int position = processItemRemoveAdd(otherViewHoder);

        if (position == -1)
            return;

        notifyItemMoved(position, listMy.size());
    }

    private void moveToMyWithDelay(OtherViewHoder otherViewHoder) {

        final int position = processItemRemoveAdd(otherViewHoder);

        if (position == -1)
            return;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemMoved(position, listMy.size());
            }
        }, ANIM_TIME);
    }

    private int processItemRemoveAdd(OtherViewHoder otherHolder) {
        int position = otherHolder.getAdapterPosition();
        int startPosition = position - listMy.size() - 2;
//        if (startPosition < listMy.size() - 1) {
//            return -1;
//        }
        GetTopCategoryRsp item = listOther.get(startPosition);
        listOther.remove(startPosition);
        listMy.add(item);
        return position;
    }

    /**
     * 增删动画
     */
    private void startAnimation(RecyclerView recyclerView, final View currentView, float targetX, float targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        Animation animation = getTranslateAnimator(targetX - currentView.getLeft(), targetY - currentView.getTop());

        currentView.setVisibility(View.INVISIBLE);

        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);

                if (currentView.getVisibility() == View.INVISIBLE)
                    currentView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 添加需要移动的 镜像View
     */
    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View view) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        recyclerView.getLocationOnScreen(parenLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);

        return mirrorView;
    }

    /**
     * 获取位移动画
     */
    private TranslateAnimation getTranslateAnimator(float targetX, float targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    public interface OnMyChannelItemCliclListener {
        void onitemClick(View view, int position);
    }

    public void setOnMyChannelItemCliclListener(OnMyChannelItemCliclListener myChannelItemCliclListener) {

        this.myChannelItemCliclListener = myChannelItemCliclListener;
    }
}
