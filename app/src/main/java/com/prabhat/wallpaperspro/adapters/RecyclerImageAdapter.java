package com.prabhat.wallpaperspro.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.prabhat.wallpaperspro.activities.SetWallpaperActivity;
import com.prabhat.wallpaperspro.misc.GlideApp;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.models.WallpaperImage;

import java.util.ArrayList;

public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<WallpaperImage> mWallpaperImages;
    private RecyclerView mRecyclerView;
    private DataSnapshot mDataSnapshot;

    public RecyclerImageAdapter(Context context, ArrayList<WallpaperImage> wallpaperImages, RecyclerView recyclerView) {
        mContext = context;
        mWallpaperImages = wallpaperImages;
        mRecyclerView = recyclerView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WallpaperImage wallpaperImage = mWallpaperImages.get(position);
        GlideApp.with(mContext)
                .load(wallpaperImage.getImageUrl())
                .thumbnail(GlideApp.with(mContext).load(R.drawable.placeholder_thumbnail))
//                .placeholder(R.drawable.placeholder)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mWallpaperImages.size();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, SetWallpaperActivity.class);
//        intent.putExtra("wallpaperArrayList", mWallpaperImages);
//        intent.putExtra("position", mRecyclerView.getChildLayoutPosition(v));
        intent.putExtra("url", mWallpaperImages.get(mRecyclerView.getChildLayoutPosition(v)).getImageUrl());
        mContext.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.wallpaper_image);
            mView = itemView;
        }


    }

}

