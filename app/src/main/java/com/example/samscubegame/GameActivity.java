package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences(String.valueOf(R.string.pref_file), Context.MODE_PRIVATE);
        boolean showGrid = sharedPref.getBoolean(getResources().getString(R.string.show_grid_key), false);


        // Lock the screen rotation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        // Setup PreviewSurfaceCallback
        PreviewSurfaceCallback previewSurfaceCallback = new PreviewSurfaceCallback(this, showGrid);
        SurfaceView previewSurfaceView = findViewById(R.id.surfaceViewPreview);
        SurfaceHolder previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(previewSurfaceCallback);

        // Setup GameSurfaceCallback
        GameSurfaceCallback gameSurfaceCallback = new GameSurfaceCallback(this, showGrid, previewSurfaceCallback);
        SurfaceView gameSurfaceView = findViewById(R.id.surfaceViewGame);
        SurfaceHolder gameSurfaceHolder = gameSurfaceView.getHolder();
        gameSurfaceHolder.addCallback(gameSurfaceCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}