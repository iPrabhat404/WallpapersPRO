package com.prabhat.wallpaperspro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.adapters.RecyclerCategoryAdapter;
import com.prabhat.wallpaperspro.models.WallpaperCategory;

import java.util.ArrayList;


public class WallpaperCategoriesFragment extends Fragment {

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Categories");
    ArrayList<WallpaperCategory> mWallpaperCategories = new ArrayList<>();
    RecyclerView mRecyclerView;

    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wallpaper_categories_fragment, container, false);

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        mWallpaperCategories.clear();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.wallpaper_categories_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final RecyclerCategoryAdapter adapter = new RecyclerCategoryAdapter(getActivity(), mWallpaperCategories, mRecyclerView);
        mRecyclerView.setAdapter(adapter);


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    mWallpaperCategories.add(child.getValue(WallpaperCategory.class));
                }
                adapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }
}
