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

import java.io.File;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock the screen rotation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        SharedPreferences sharedPref = getSharedPreferences(String.valueOf(R.string.pref_file), Context.MODE_PRIVATE);
        File highScoreFile = new File(getApplicationContext().getFilesDir(), "highScore.csv");
        try {
            //noinspection ResultOfMethodCallIgnored
            highScoreFile.createNewFile();// TODO: create button to view top 10
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup PreviewSurfaceCallback
        PreviewSurfaceCallback previewSurfaceCallback = new PreviewSurfaceCallback(this, sharedPref);
        SurfaceView previewSurfaceView = findViewById(R.id.surfaceView_preview);
        SurfaceHolder previewSurfaceHolder = previewSurfaceView.getHolder();
        previewSurfaceHolder.addCallback(previewSurfaceCallback);

        // Setup GameSurfaceCallback
        GameSurfaceCallback gameSurfaceCallback = new GameSurfaceCallback(this, sharedPref, previewSurfaceCallback, highScoreFile);
        SurfaceView gameSurfaceView = findViewById(R.id.surfaceView_game);
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