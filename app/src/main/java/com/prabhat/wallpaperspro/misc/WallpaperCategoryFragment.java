package com.prabhat.wallpaperspro.misc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prabhat.wallpaperspro.R;

public class WallpaperCategoryFragment extends Fragment {

    public static final String  SECTION_NUMBER = "section_number";

    public WallpaperCategoryFragment() {
    }

    public static WallpaperCategoryFragment newinstance(int sectionNumber) {
        WallpaperCategoryFragment wallpaperCategoryFragment = new WallpaperCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_NUMBER, sectionNumber);
        wallpaperCategoryFragment.setArguments(args);
        return wallpaperCategoryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_images, container, false);

        return rootView;

    }
}
