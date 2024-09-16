package com.ngb.colorflashscreen.ui.fragment.callscreen;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.datasource.model.ScreenModel;

import java.io.IOException;
import java.util.List;

public class ListScreenAdapter extends RecyclerView.Adapter<ListScreenAdapter.ScreenHolder> {
    private List<ScreenModel> listScreen;
    private Context mContext;
    private OnItemClick callBack;

    public ListScreenAdapter( List<ScreenModel> listScreen, Context mContext ) {
        this.listScreen = listScreen;
        this.mContext = mContext;
    }

    public void setOnItemClick( OnItemClick callBack ) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ScreenHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item, parent, false);
        return new ScreenHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull ScreenHolder holder, int position ) {
        ScreenModel item = listScreen.get(position);
        try {
            Glide.with(mContext).load(BitmapFactory.decodeStream(mContext.getAssets()
                    .open(item.getImage()))).into(holder.ivScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.tvName.setText("Lucy");
        holder.screenModel = item;
    }

    @Override
    public int getItemCount() {
        return listScreen.size();
    }

    public void loadImage( List<ScreenModel> list ) {
        listScreen.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onItemClick( ScreenModel screenModel, boolean b );
    }

    public class ScreenHolder extends RecyclerView.ViewHolder {
        private ImageView ivScreen;
        private ImageButton btCallEnd, btCall;
        private TextView tvName;
        private CardView lnItem;
        private ScreenModel screenModel;

        public ScreenHolder( @NonNull View itemView ) {
            super(itemView);
            ivScreen = itemView.findViewById(R.id.iv_screen);
            tvName = itemView.findViewById(R.id.tv_name);
            btCall = itemView.findViewById(R.id.bt_call);
            btCallEnd = itemView.findViewById(R.id.bt_call_end);
            lnItem = itemView.findViewById(R.id.ln_item);
            itemView.setOnClickListener(v -> {
                if (callBack != null) {
                    if (screenModel.isApply()) {
                        callBack.onItemClick(screenModel, true);
                        notifyDataSetChanged();
                    } else if (!screenModel.isApply()){
                        callBack.onItemClick(screenModel, false);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
