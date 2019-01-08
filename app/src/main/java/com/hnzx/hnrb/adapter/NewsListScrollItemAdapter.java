package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.pili.pldroid.player.widget.PLVideoView;

public class NewsListScrollItemAdapter extends BaseAdapter<GetLatestNewsRsp> {



    public NewsListScrollItemAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.scrollrecycle_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetLatestNewsRsp rsp = getItem(position);




        Log.d("getlates_", "onBindViewHolder: "+rsp);
//        GlideTools.Glide(mContext, rsp.image, mHolder.mImageView, R.drawable.bg_morentu_xiaotumoshi);

        mHolder.mTextView.setText("Grid"+position);


    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.scroll_img);
            mTextView = itemView.findViewById(R.id.scroll_text);

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
