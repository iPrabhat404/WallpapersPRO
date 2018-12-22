package com.prabhat.wallpaperspro.activities;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.prabhat.wallpaperspro.R;
import com.prabhat.wallpaperspro.misc.GlideApp;
import com.prabhat.wallpaperspro.models.WallpaperImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission;

public class SetWallpaperActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SetWallpaperActivity";
    private static final int STORAGE_REQUEST_CODE = 123;
    private boolean mIsPermissionGranted;

    private int mPermissionAskingMethod;
    private static final int DOWNLOAD_IMAGE = 1;
    private static final int SET_IMAGE_AS_WALLPAPER = 2;

    ArrayList<WallpaperImage> mWallpaperImages;
    ArrayList<String> mWallpaperUrlList = new ArrayList<String>();
    ImageView mImage;
    ImageButton mSetWallpaperBtn, mDownloadImageBtn, mFavoriteBtn, mCloseBtn;
    ProgressBar mProgressBar;
    String mUrl;
    SQLiteDatabase mSQLiteDatabase;
    AdView mAdView;
    int mIndexInFavorites;
    boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);
        mAdView = (AdView) findViewById(R.id.adView_setWallpaper);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mSQLiteDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        Bundle bundle = getIntent().getExtras();

        mImage = (ImageView) findViewById(R.id.image);
        mSetWallpaperBtn = (ImageButton) findViewById(R.id.set_wallpaper_button);
        mDownloadImageBtn = (ImageButton) findViewById(R.id.download_button);
        mFavoriteBtn = (ImageButton) findViewById(R.id.favorite_button);
        mCloseBtn = (ImageButton) findViewById(R.id.close_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_setting_wallpaper);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.setElevation(10.0f);
        }

//        mWallpaperImages = (ArrayList<WallpaperImage>) bundle.getSerializable("wallpaperArrayList");
//        int position = bundle.getInt("position");
//        mUrl = mWallpaperImages.get(position).getImageUrl();
        mUrl = bundle.getString("url");

        checkIfFavorite();

        GlideApp.with(getApplicationContext())
                .load(mUrl)
                .placeholder(R.drawable.wallpaper_placeholder)
                .fitCenter()
                .into(mImage);

        mSetWallpaperBtn.setOnClickListener(this);
        mCloseBtn.setOnClickListener(this);
        mDownloadImageBtn.setOnClickListener(this);
        mFavoriteBtn.setOnClickListener(this);
    }

    private void checkIfFavorite() {
        String sql = "SELECT * FROM FavoriteWallpapersModel";
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            Log.d(TAG, "onCreate: SQL DATA- has children.");
            do {
                mWallpaperUrlList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "onCreate: SQL DATA has NO children.");
        }
        if (mWallpaperUrlList.size() > 0) {
            if (mWallpaperUrlList.contains(mUrl)) {
                mIndexInFavorites = mWallpaperUrlList.indexOf(mUrl);
                mFavoriteBtn.setImageResource(R.drawable.ic_favorite);
                mIsFavorite = true;
            } else mIsFavorite = false;
        } else mIsFavorite = false;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_wallpaper_button:
                setImageAsWallpaper(mUrl);
                break;
            case R.id.close_button:
                onBackPressed();
                break;
            case R.id.download_button:
                downloadImage(mUrl);
                break;
            case R.id.favorite_button:
                toggleFavorite();
                break;

        }
    }

    private void toggleFavorite() {

        if (mIsFavorite) {
            String deleteSql = "DELETE FROM FavoriteWallpapersModel WHERE WallpaperURL = ?";
            mSQLiteDatabase.execSQL(deleteSql, new String[]{mUrl});
            mFavoriteBtn.setImageResource(R.drawable.ic_add_favorite);
            mIsFavorite = false;
            Toast.makeText(this, "Removed from favorites.", Toast.LENGTH_SHORT).show();
        } else {
            String addSql = "INSERT INTO FavoriteWallpapersModel(WallpaperURL) VALUES(?)";
            mSQLiteDatabase.execSQL(addSql, new String[]{mUrl});
            mFavoriteBtn.setImageResource(R.drawable.ic_favorite);
            mIsFavorite = true;
            Toast.makeText(this, "Added to favorites.", Toast.LENGTH_SHORT).show();
        }


    }

    private void downloadImage(String url) {

        if (!isWriteToStorageAllowed()) {
            mPermissionAskingMethod = this.DOWNLOAD_IMAGE;
            requestStoragePermission();
        } else {

            Log.d(TAG, "downloadImage: Download button clicked.");
            mProgressBar.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Log.d(TAG, "onResourceReady: Resource is Ready.");
                            saveImage(resource);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.WRITE_EXTERNAL_STORAGE)) {
            //to do code if user has already denied the permission one time
        }

        ActivityCompat.requestPermissions(this, new String[]{permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //checking the permission request code
        if (requestCode == STORAGE_REQUEST_CODE) {

            //checking if permission granted or not
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO: code when permission is granted

                mIsPermissionGranted = true;

                if (mPermissionAskingMethod == this.DOWNLOAD_IMAGE) {
                    downloadImage(mUrl);
                } else if (mPermissionAskingMethod == this.SET_IMAGE_AS_WALLPAPER) {
                    setImageAsWallpaper(mUrl);
                }
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Storage permission is required to download wallpaper.", Toast.LENGTH_SHORT).show();
                mIsPermissionGranted = false;
            }
        }

    }

    private boolean isWriteToStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) return true;
        return false;
    }

    private String saveImage(Bitmap image) {
        Log.d(TAG, "saveImage: Image saving has started.");
        String savedImagePath = null;
        Calendar calendar = Calendar.getInstance();
        String currentTime = String.valueOf(calendar.getTimeInMillis());
        Log.d(TAG, "saveImage: Got current time successfully.");
        String imageFileName = "WallpapersPRO_" + currentTime + ".jpg";

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/WallpapersPRO");

        boolean success = true;

        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
            Log.d(TAG, "saveImage: storage directory created.");
        }

        if (success) {
            Log.d(TAG, "saveImage: storage directory exists.");
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();

            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                Log.d(TAG, "saveImage: Image saved.");
            } catch (FileNotFoundException e) {
                Log.d(TAG, "saveImage: file not found exception.");
                e.printStackTrace();

            } catch (IOException e) {
                Log.d(TAG, "saveImage: IO exception.");
                e.printStackTrace();
            }
            // adding picture to gallery

            addImageToGallery(savedImagePath);
            Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG).show();

        }

        return savedImagePath;
    }

    private void addImageToGallery(String savedImagePath) {
        Log.d(TAG, "addImageToGallery: Adding picture to gallery.");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(savedImagePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Log.d(TAG, "addImageToGallery: picture added to gallery.");
    }

    private void setImageAsWallpaper(String url) {

        if (!isWriteToStorageAllowed()) {
            mPermissionAskingMethod = this.SET_IMAGE_AS_WALLPAPER;
            requestStoragePermission();
//            if (!mIsPermissionGranted) return;
        } else {

            mProgressBar.setVisibility(View.VISIBLE);

            final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

            GlideApp.with(this)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>(1080, 1920) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            Uri imageUri = getImageUri(resource);
                            try {
//                                Intent intent = new Intent(wallpaperManager.getCropAndSetWallpaperIntent(imageUri));
//                                startActivityForResult(intent, SET_IMAGE_AS_WALLPAPER);
                                wallpaperManager.setBitmap(resource);
                                Toast.makeText(SetWallpaperActivity.this, "Wallpaper Changed", Toast.LENGTH_SHORT).show();

                                mProgressBar.setVisibility(View.GONE);


                            } catch (IllegalArgumentException e) {
                                try {
                                    wallpaperManager.setBitmap(resource);
                                    mProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(SetWallpaperActivity.this, "Wallpaper Changed", Toast.LENGTH_SHORT).show();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                        Toast.makeText(SetWallpaperActivity.this, "Wallpaper changed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

//    private Uri getImageUri(Bitmap image) {
//
//        fixMediaDir();
//        Uri imageUri = null;
//
//        Log.d(TAG, "getImageUri: imageSize " + image.getByteCount());
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        String path = Images.Media.insertImage(this.getContentResolver(), image, "Title", null);
//        Log.d(TAG, "getImageUri: " + String.valueOf(path));
//        Log.d(TAG, "getImageUri: Returning Image Uri.");
//
//        return Uri.parse(path);
//    }

//    void fixMediaDir() {
//        File sdcard = Environment.getExternalStorageDirectory();
//        if (sdcard != null) {
//            Log.d(TAG, "fixMediaDir: SDCard is not null");
//            File mediaDir = new File(sdcard, "DCIM/Camera");
//            if (!mediaDir.exists()) {
//                Log.d(TAG, "fixMediaDir: mediaDirectory doesn't exist, creating it...");
//                mediaDir.mkdirs();
//            }
//        } else {
//            Log.d(TAG, "fixMediaDir: SDCard is null");
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == this.SET_IMAGE_AS_WALLPAPER) {
                Toast.makeText(this, "Wallpaper Changed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (requestCode == this.SET_IMAGE_AS_WALLPAPER) {
                Toast.makeText(this, "Wallpaper changing cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
