package com.prabhat.wallpaperspro.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.activities.WallpapersActivity;
import com.prabhat.wallpaperspro.misc.GlideApp;
import com.prabhat.wallpaperspro.models.WallpaperCategory;

import java.util.ArrayList;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<WallpaperCategory> mWallpaperCategories;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private WallpaperCategory mClickedWallpaperCategory;
    private int mClickedCategoryPosition;

    public RecyclerCategoryAdapter(Context context, ArrayList<WallpaperCategory> wallpaperCategories, RecyclerView recyclerView) {
        mWallpaperCategories = wallpaperCategories;
        mContext = context;
        mRecyclerView = recyclerView;
    }

    public RecyclerCategoryAdapter() {
    }

    @NonNull
    @Override
    public RecyclerCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCategoryAdapter.ViewHolder holder, int position) {
        WallpaperCategory wallpaperCategory = mWallpaperCategories.get(position);
        GlideApp.with(mContext)
                .load(wallpaperCategory.getCategoryImageUrl())
                .into(holder.mCategoryImage);

        holder.mCategoryName.setText(wallpaperCategory.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mWallpaperCategories.size();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(mContext, WallpapersActivity.class);
        mClickedCategoryPosition = mRecyclerView.getChildLayoutPosition(v);
        mClickedWallpaperCategory = mWallpaperCategories.get(mClickedCategoryPosition);
        intent.putExtra("wallpaperCategory", mClickedWallpaperCategory);
        intent.putExtra("position", mClickedCategoryPosition);
        mContext.startActivity(intent);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mCategoryImage;
        private TextView mCategoryName;
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mCategoryImage = (ImageView)itemView.findViewById(R.id.category_image);
            mCategoryName = (TextView)itemView.findViewById(R.id.category_name);
            mView = itemView;
        }
    }
}
