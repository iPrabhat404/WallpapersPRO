package com.prabhat.wallpaperspro.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.fragments.WallpapersFragment;
import com.prabhat.wallpaperspro.models.WallpaperCategory;

public class WallpapersActivity extends AppCompatActivity {

    Toolbar mToolbar;
    FrameLayout mFrameLayout;

    AdView mAdView;
    String mReferenceText;
    int position;
    private Bundle mBundle;
    private WallpaperCategory mWallpaperCategory;
    private int mPosition;
    private String mCategoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers);

        mBundle = getIntent().getExtras();
        mWallpaperCategory = (WallpaperCategory) mBundle.getSerializable("wallpaperCategory");
        mPosition = mBundle.getInt("position");
        mCategoryReference = mWallpaperCategory.getCategoryName();

        mToolbar = (Toolbar) findViewById(R.id.wallpapers_toolbar);
        mToolbar.setTitle(mWallpaperCategory.getCategoryName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mFrameLayout = (FrameLayout) findViewById(R.id.fragment_container_frame);

        mAdView = (AdView) findViewById(R.id.adView_wallpapers);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        this.loadWallpaperFragment();


    }

    private void loadWallpaperFragment() {
        Bundle args = new Bundle();
        args.putString("category", mCategoryReference);
        switch (mPosition) {
            case 0:
                Fragment abstractFragment = new WallpapersFragment();
                abstractFragment.setArguments(args);
                FragmentTransaction abstractTransaction = getSupportFragmentManager().beginTransaction();
                abstractTransaction.add(R.id.fragment_container_frame, abstractFragment).commit();
                break;
            case 1:
                Fragment amoledFragment = new WallpapersFragment();
                amoledFragment.setArguments(args);
                FragmentTransaction amoledTransaction = getSupportFragmentManager().beginTransaction();
                amoledTransaction.add(R.id.fragment_container_frame, amoledFragment).commit();
                break;
            case 2:
                Fragment animalFragment = new WallpapersFragment();
                animalFragment.setArguments(args);
                FragmentTransaction animalTransaction = getSupportFragmentManager().beginTransaction();
                animalTransaction.add(R.id.fragment_container_frame, animalFragment).commit();
                break;
            case 3:
                Fragment minimalFragment = new WallpapersFragment();
                minimalFragment.setArguments(args);
                FragmentTransaction minimalTransaction = getSupportFragmentManager().beginTransaction();
                minimalTransaction.add(R.id.fragment_container_frame, minimalFragment).commit();
                break;
            case 4:
                Fragment natureFragment = new WallpapersFragment();
                natureFragment.setArguments(args);
                FragmentTransaction natureTransaction = getSupportFragmentManager().beginTransaction();
                natureTransaction.add(R.id.fragment_container_frame, natureFragment).commit();
                break;
            default:
                Toast.makeText(this, "Pending", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public String getCategoryReference() {
        return mCategoryReference;
    }
}
