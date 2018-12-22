package com.prabhat.wallpaperspro.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.activities.SetWallpaperActivity;
import com.prabhat.wallpaperspro.misc.GlideApp;

import java.util.ArrayList;

public class FavoriteImageAdapter extends RecyclerView.Adapter<FavoriteImageAdapter.ViewHolder> implements View.OnClickListener{

    Context mContext;
    ArrayList<String> mFavoriteWallpapersList;
    RecyclerView mRecyclerView;

    public FavoriteImageAdapter(Context context, ArrayList<String> favoriteWallpapersList, RecyclerView recyclerView) {
        mContext = context;
        mFavoriteWallpapersList = favoriteWallpapersList;
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public FavoriteImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteImageAdapter.ViewHolder holder, int position) {
        String url = mFavoriteWallpapersList.get(position);

        GlideApp.with(mContext)
                .load(url)
                .thumbnail(GlideApp.with(mContext).load(R.drawable.placeholder_thumbnail))
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mFavoriteWallpapersList.size();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, SetWallpaperActivity.class);
        intent.putExtra("url", mFavoriteWallpapersList.get(mRecyclerView.getChildLayoutPosition(v)));
        mContext.startActivity(intent);
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.wallpaper_image);
            mView = itemView;
        }
    }
}
