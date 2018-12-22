package com.prabhat.wallpaperspro.fragments;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.models.WallpaperImage;
import com.prabhat.wallpaperspro.adapters.RecyclerImageAdapter;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class WallpapersFragment extends Fragment {

    String[] texts = {"1Nature", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"
    };

    ArrayList<WallpaperImage> mWallpaperImagesList = new ArrayList<>();
    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    ProgressBar mLoadingProgressBar;
    ImageView mNoWallpapersImageView;

    String mTotalWallpapers;
    Boolean mNoMoreWallpapers = false;
    int totalWallpaperCounter = 0;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReference = mDatabase.getReference();
    DatabaseReference mDatabaseReference;
    long mLastVisibleWallpaper;
    private static final String TAG = "WallpapersFragment";
    private boolean mIsFirstIteration;
    private boolean mIsLoading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_images_recycler, container, false);

        mNoWallpapersImageView = (ImageView) rootView.findViewById(R.id.no_favorites_imageview);
        mNoWallpapersImageView.setVisibility(View.GONE);

        mLoadingProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        mWallpaperImagesList.clear();

        if (getArguments() != null) {
            Log.d(TAG, "onCreateView: Fragment loaded from arguments: " + getArguments().getString("category"));
            mDatabaseReference = mDatabase.getReference(getArguments().getString("category"));
        } else {
            mDatabaseReference = mDatabase.getReference("Minimal");
        }
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_image_container);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.load_more_progressbar);
        mProgressBar.setVisibility(View.GONE);
        final GridLayoutManager myGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(myGridLayoutManager);
        final RecyclerImageAdapter adapter = new RecyclerImageAdapter(getActivity(), mWallpaperImagesList, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

        mDatabaseReference
                .child("wallpapers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mTotalWallpapers = String.valueOf(dataSnapshot.getChildrenCount() - 1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        mIsLoading = true;

        mDatabaseReference
                .child("wallpapers")
                .orderByChild("index")
                .limitToFirst(13)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int snapshotCounter = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshotCounter == 12) {
                                mLastVisibleWallpaper = (long) snapshot.child("index").getValue();
                                break;
                            }
                            mWallpaperImagesList.add(snapshot.getValue(WallpaperImage.class));
//                    mLastVisibleWallpaper = (Long) snapshot.child("index").getValue();
                            snapshotCounter++;
                            Log.d(TAG, "onDataChange: Last Wallpaper Key- " + mLastVisibleWallpaper);

                        }
//                Collections.reverse(mWallpaperImagesList);
                        adapter.notifyDataSetChanged();
                        mIsLoading = false;
                        mLoadingProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int currentLastVisibleItem;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((currentLastVisibleItem + 1) == totalItem && newState == SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: Reached to last");
                    Log.d(TAG, "onDataChange: Last Visible Wallpaper- " + mLastVisibleWallpaper);

                    if (mNoMoreWallpapers) {
                        Log.d(TAG, "onScrollStateChanged: No More Wallpapers.");
                        return;
                    }
                    if (mIsLoading) {
                        return;
                    }
                    mProgressBar.setVisibility(View.VISIBLE);
                    mIsLoading = true;

                    mDatabaseReference
                            .child("wallpapers")
                            .orderByChild("index")
                            .startAt(mLastVisibleWallpaper)
                            .limitToFirst(13)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Log.d(TAG, "onDataChange: Getting Wallpapers from- " + (mLastVisibleWallpaper + 1));
                                    Log.d(TAG, "onDataChange: Datasnapshot taken- " + dataSnapshot.getChildrenCount());

                                    int snapshotCounter = 0;

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        if (snapshotCounter == 12) {
                                            mLastVisibleWallpaper = (long) child.child("index").getValue();
                                            break;
                                        }
                                        mWallpaperImagesList.add(child.getValue(WallpaperImage.class));
//                    mLastVisibleWallpaper = (Long) snapshot.child("index").getValue();
                                        snapshotCounter++;
                                        if (mRecyclerView.getAdapter().getItemCount() == Integer.valueOf(mTotalWallpapers)) {
                                            Log.d(TAG, "onDataChange: Total Wallpapers- " + mTotalWallpapers);
                                            Log.d(TAG, "onDataChange: Total RecyclerView Items- " + mRecyclerView.getAdapter().getItemCount());
                                            mNoMoreWallpapers = true;
                                            Log.d(TAG, "onDataChange: Has more wallpapers value: " + mNoMoreWallpapers);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    mProgressBar.setVisibility(View.GONE);
                                    mIsLoading = false;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else {
                    Log.d(TAG, "onScrollStateChanged: Its not last");
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentLastVisibleItem = myGridLayoutManager.findLastCompletelyVisibleItemPosition();
                totalItem = myGridLayoutManager.getItemCount();

            }
        });


        return rootView;
    }

}