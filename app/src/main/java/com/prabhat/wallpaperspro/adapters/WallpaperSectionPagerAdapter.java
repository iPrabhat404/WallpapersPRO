package com.prabhat.wallpaperspro.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.prabhat.wallpaperspro.fragments.FavoriteWallpapersFragment;
import com.prabhat.wallpaperspro.fragments.WallpapersFragment;
import com.prabhat.wallpaperspro.fragments.WallpaperCategoriesFragment;

public class WallpaperSectionPagerAdapter extends FragmentPagerAdapter {

    public WallpaperSectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle args = new Bundle();
                args.putString("category", "Latest");
                Fragment latestWallpapersFragment = new WallpapersFragment();
                latestWallpapersFragment.setArguments(args);
                return latestWallpapersFragment;
            case 1:
                return new WallpaperCategoriesFragment();
            case 2:
                return new FavoriteWallpapersFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
