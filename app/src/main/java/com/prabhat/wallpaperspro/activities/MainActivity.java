package com.prabhat.wallpaperspro.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.prabhat.wallpaperspro.PrivacyPolicyActivity;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.adapters.WallpaperSectionPagerAdapter;


public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "FavoriteWallpapers";
    private static final String TAG = "MainActivity";

    private WallpaperSectionPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    SQLiteDatabase mSQLiteDatabase;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-7221944186866843~8988946191");
        //setting the toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSQLiteDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTable();

        //setting the adapter
        mSectionPagerAdapter = new WallpaperSectionPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPager.setAdapter(mSectionPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.settings_menu:
                item.expandActionView();
                return true;
            case R.id.privacy_menu:
                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
            default:
                return true;
        }

    }

    private void createTable() {
        String sql =
                "CREATE TABLE IF NOT EXISTS FavoriteWallpapersModel(\n" +
                        "WallpaperID INTEGER NOT NULL CONSTRAINT fovorite_wallpapers_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "WallpaperURL varchar(200) NOT NULL);";
        mSQLiteDatabase.execSQL(sql);
        Log.d(TAG, "createTable: Table is created.");
    }
}
