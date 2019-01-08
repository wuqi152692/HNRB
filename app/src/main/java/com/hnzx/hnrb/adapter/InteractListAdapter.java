package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.SetInteractVoteReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetInteractListRsp;
import com.hnzx.hnrb.responsebean.SetInteractVoteRsp;
import com.hnzx.hnrb.tools.DateUtils;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.ui.interact.ActiveActivity;
import com.hnzx.hnrb.ui.interact.TopicDetailActivity;
import com.hnzx.hnrb.ui.interact.VoteActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Date;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class InteractListAdapter extends BaseAdapter<GetInteractListRsp> {
    private final int TypeTopic = 0;
    private final int TypeActive = 1;
    private final int TypeVoteing = 2;
    private final int TypeVoteType2 = 3;
    private String device_token;

    public InteractListAdapter(Context context) {
        super(context);
        device_token = (String) SharePreferenceTool.get(context, Constant.DEVICE_ID, "henandaily");
    }

    @Override
    public int getItemViewType(int position) {
        GetInteractListRsp rsp = getItem(position);
        switch (rsp.type) {
            case "activity":
                return TypeActive;
            case "topic":
                return TypeTopic;
            case "vote":
                if (rsp.is_link != 1)
                    return TypeVoteing;
                return TypeVoteType2;
            default:
                return TypeActive;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TypeActive:
                return new ActiveViewHolder(mInflater.inflate(R.layout.layout_interact_item_active, parent, false));
            case TypeTopic:
                return new TopicViewHolder(mInflater.inflate(R.layout.layout_interact_item_topic, parent, false));
            case TypeVoteing:
                return new VoteingViewHolder(mInflater.inflate(R.layout.layout_interact_item_voting, parent, false));
            case TypeVoteType2:
                return new VoteType2ViewHolder(mInflater.inflate(R.layout.layout_interact_item_vote_type2, parent, false));
            default:
                return new ActiveViewHolder(mInflater.inflate(R.layout.layout_interact_item_active, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GetInteractListRsp rsp = getItem(position);
        switch (getItemViewType(position)) {
            case TypeActive:
                setActiveHolder((ActiveViewHolder) holder, rsp);
                break;
            case TypeTopic:
                setTopicHolder((TopicViewHolder) holder, rsp);
                break;
            case TypeVoteing:
                setVoteingHolder((VoteingViewHolder) holder, rsp, position);
                break;
            case TypeVoteType2:
                try {
                    setTopicHolder((VoteType2ViewHolder) holder, rsp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                setActiveHolder((ActiveViewHolder) holder, rsp);
                break;
        }
    }

    /**
     * 设置话题
     *
     * @param holder
     * @param rsp
     */
    private void setTopicHolder(TopicViewHolder holder, final GetInteractListRsp rsp) {
        GlideTools.GlideRounded(mContext, rsp.thumb, holder.image, R.drawable.bg_morentu_datumoshi, 8);
        holder.title.setText(rsp.title);
        holder.time.setText(rsp.created);
        holder.lookNum.setText(formatViews(rsp.views));
        holder.num.setText(formatViews(rsp.joined));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TopicDetailActivity.class);
                it.putExtra(TopicDetailActivity.DATA_KEY, rsp.type_id);
                mContext.startActivity(it);
            }
        });
    }

    /**
     * 设置活动
     *
     * @param holder
     * @param rsp
     */
    private void setActiveHolder(ActiveViewHolder holder, final GetInteractListRsp rsp) {
        GlideTools.GlideRounded(mContext, rsp.thumb, holder.image, R.drawable.bg_morentu_datumoshi, 8);
        holder.title.setText(rsp.title);
        holder.time.setText(rsp.created);
        holder.lookNum.setText(formatViews(rsp.views));
        holder.remain_num_value.setText(String.valueOf(rsp.remained));
        holder.state.setText(rsp.type_name);
        holder.end_time.setText(rsp.start_hm);
        holder.end_date.setText(rsp.start_md);
        if (rsp.type_name.equals("进行中")) {
            holder.stateView.setEnabled(true);
            holder.stateView.setClickable(true);
            holder.stateView.setChecked(true);
            holder.state.setChecked(true);
        } else if (rsp.type_name.equals("已结束")) {
            holder.stateView.setChecked(false);
            holder.stateView.setEnabled(true);
            holder.stateView.setClickable(false);
            holder.state.setChecked(false);
        } else {
            holder.stateView.setChecked(false);
            holder.stateView.setEnabled(false);
            holder.stateView.setClickable(true);
            holder.state.setChecked(true);
            holder.state.setTextColor(Color.parseColor("#46a9ec"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ActiveActivity.class);
                it.putExtra(ActiveActivity.DATA_KEY, rsp.type_id);
                mContext.startActivity(it);
            }
        });
    }

    /**
     * 设置二选一投票
     *
     * @param holder
     * @param rsp
     */
    private void setVoteingHolder(VoteingViewHolder holder, final GetInteractListRsp rsp, final int position) {
        holder.title.setText(rsp.title);
        holder.time.setText(rsp.created);
        holder.lookNum.setText(formatViews(rsp.views));
        holder.msg.setText(rsp.brief);
        holder.support.setVisibility(View.GONE);
        holder.supportValue.setVisibility(View.GONE);
        holder.unsupport.setVisibility(View.GONE);
        holder.unsupportValue.setVisibility(View.GONE);
        holder.progress.setVisibility(View.GONE);

        if (rsp.is_voted == 0 && rsp.type_name.equals("进行中")) {
            holder.support.setText(rsp.option1);
            holder.unsupport.setText(rsp.option2);
            holder.support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetInteractVoteReq req = new SetInteractVoteReq();
                    req.vote_id = rsp.type_id;
                    req.vote_option = "option1";
                    req.device_token = device_token;

                    App.getInstance().requestJsonArrayDataGet(req, new voteListener(position, true), new MyErrorListener("投票错误"));
                }
            });
            holder.unsupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetInteractVoteReq req = new SetInteractVoteReq();
                    req.vote_id = rsp.type_id;
                    req.vote_option = "option2";
                    req.device_token = device_token;

                    App.getInstance().requestJsonArrayDataGet(req, new voteListener(position, false), new MyErrorListener("投票错误"));
                }
            });
            holder.support.setVisibility(View.VISIBLE);
            holder.unsupport.setVisibility(View.VISIBLE);
        } else {
            int degress = (int) (rsp.option1_number / (rsp.option1_number + rsp.option2_number + 0.00) * 100);
            holder.supportValue.setText(degress + "%" + rsp.option1);
            holder.unsupportValue.setText((100 - degress) + "%" + rsp.option2);
            holder.progress.setProgress(degress);
            holder.unsupportValue.setVisibility(View.VISIBLE);
            holder.progress.setVisibility(View.VISIBLE);
            holder.supportValue.setVisibility(View.VISIBLE);
            holder.unsupportValue.setVisibility(View.VISIBLE);
            holder.progress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置外链投票
     *
     * @param holder
     * @param rsp
     */
    private void setTopicHolder(VoteType2ViewHolder holder, final GetInteractListRsp rsp) throws Exception {
        GlideTools.GlideRounded(mContext, rsp.thumb, holder.image, R.drawable.bg_morentu_datumoshi, 8);
        holder.title.setText(rsp.title);
        holder.time.setText(rsp.created);
        holder.lookNum.setText(formatViews(rsp.views));

        Date now = new Date();
        if (rsp.start_time.lastIndexOf("-") < 4)
            rsp.start_time = DateUtils.dateToString(now, "yyyy") + "-" + rsp.start_time;
        if (rsp.end_time.lastIndexOf("-") < 4)
            rsp.end_time = DateUtils.dateToString(now, "yyyy") + "-" + rsp.end_time;

        Date start = DateUtils.stringToDate(rsp.start_time, DateUtils.patternLong);
        Date end = DateUtils.stringToDate(rsp.end_time, DateUtils.patternLong);

        holder.end_date.setText(DateUtils.dateToString(start, DateUtils.patternMMdd));
        holder.end_time.setText(DateUtils.dateToString(start, DateUtils.patternHHmm));
        if (now.before(start)) {
            holder.stateTV.setChecked(false);
            holder.stateTV.setEnabled(false);
            holder.stateTV.setClickable(true);
            holder.state.setChecked(true);
            holder.state.setText("未开始");
            holder.state.setTextColor(Color.parseColor("#46a9ec"));
        } else if (now.after(end)) {
            holder.stateTV.setChecked(false);
            holder.stateTV.setEnabled(true);
            holder.stateTV.setClickable(false);
            holder.state.setChecked(false);
            holder.state.setText("已结束");
        } else {
            holder.stateTV.setEnabled(true);
            holder.stateTV.setClickable(true);
            holder.stateTV.setChecked(true);
            holder.state.setChecked(true);
            holder.state.setText("进行中");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, VoteActivity.class);
                it.putExtra(VoteActivity.DATA_KEY, rsp);
                mContext.startActivity(it);
            }
        });
    }

    private class TopicViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, time, lookNum, num;

        public TopicViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            lookNum = (TextView) itemView.findViewById(R.id.lookNum);
            num = (TextView) itemView.findViewById(R.id.num);
        }
    }

    private class ActiveViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private CheckedTextView state, stateView;
        private TextView title, time, lookNum, end_time, end_date, remain_num_value;

        public ActiveViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            lookNum = (TextView) itemView.findViewById(R.id.lookNum);
            end_time = (TextView) itemView.findViewById(R.id.end_time);
            end_date = (TextView) itemView.findViewById(R.id.end_date);
            remain_num_value = (TextView) itemView.findViewById(R.id.remain_num_value);

            stateView = (CheckedTextView) itemView.findViewById(R.id.stateView);
            state = (CheckedTextView) itemView.findViewById(R.id.state);
        }
    }

    private class VoteType2ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, time, lookNum, end_time, end_date;
        private CheckedTextView stateTV, state;

        public VoteType2ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            lookNum = (TextView) itemView.findViewById(R.id.lookNum);
            end_time = (TextView) itemView.findViewById(R.id.end_time);
            end_date = (TextView) itemView.findViewById(R.id.end_date);

            state = (CheckedTextView) itemView.findViewById(R.id.state);
            stateTV = (CheckedTextView) itemView.findViewById(R.id.stateTV);
        }
    }

    private class VoteingViewHolder extends RecyclerView.ViewHolder {
        private TextView msg, title, time, lookNum, support, unsupport, supportValue, unsupportValue;
        private ProgressBar progress;

        public VoteingViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            msg = (TextView) itemView.findViewById(R.id.msg);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            lookNum = (TextView) itemView.findViewById(R.id.lookNum);
            support = (TextView) itemView.findViewById(R.id.support);
            unsupport = (TextView) itemView.findViewById(R.id.unsupport);
            supportValue = (TextView) itemView.findViewById(R.id.supportValue);
            unsupportValue = (TextView) itemView.findViewById(R.id.unsupportValue);

            progress = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }

    private class voteListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<SetInteractVoteRsp>> {
        private int position;
        private boolean isOption1;

        public voteListener(int position, boolean isOption1) {
            this.position = position;
            this.isOption1 = isOption1;
        }

        @Override
        public void onResponse(BaseBeanArrayRsp<SetInteractVoteRsp> response) {
            if (response != null && response.Status == 1) {
                GetInteractListRsp rsp = mList.get(position);
                rsp.is_voted = 1;
                if (isOption1)
                    rsp.option1_number += 1;
                else
                    rsp.option2_number += 1;
                mList.remove(position);
                mList.add(position, rsp);
                notifyDataSetChanged();
            }
        }
    }
}
