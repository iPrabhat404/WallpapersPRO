package com.prabhat.wallpaperspro.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.util.CrashUtils;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.activities.MainActivity;
import com.prabhat.wallpaperspro.adapters.FavoriteImageAdapter;
import com.prabhat.wallpaperspro.adapters.RecyclerImageAdapter;
import com.prabhat.wallpaperspro.models.WallpaperImage;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FavoriteWallpapersFragment extends Fragment {
    private static final String TAG = "FavoriteWallpapersFragm";
    private ProgressBar mLoadingProgressBar;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ImageView mNoWallpapersImage;

    private ArrayList<String> mWallpaperImagesList = new ArrayList<>();
    private SQLiteDatabase mSQLiteDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_images_recycler, container, false);

        mNoWallpapersImage = (ImageView) rootView.findViewById(R.id.no_favorites_imageview);
        mNoWallpapersImage.setVisibility(View.VISIBLE);

        mLoadingProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        mWallpaperImagesList.clear();

        mSQLiteDatabase = getActivity().openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_image_container);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.load_more_progressbar);
        mProgressBar.setVisibility(View.GONE);
        final GridLayoutManager myGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(myGridLayoutManager);
        final FavoriteImageAdapter adapter = new FavoriteImageAdapter(getActivity(), mWallpaperImagesList, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

        String sql = "SELECT * FROM FavoriteWallpapersModel";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToLast()){
            do {
                mWallpaperImagesList.add(cursor.getString(1));
            }while (cursor.moveToPrevious());

            adapter.notifyDataSetChanged();
            mNoWallpapersImage.setVisibility(View.GONE);

        } else {
            Log.d(TAG, "onCreateView: No Favorite Wallpapers");
            Toast.makeText(getActivity(), "No Favorite Wallpapers", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }



}
