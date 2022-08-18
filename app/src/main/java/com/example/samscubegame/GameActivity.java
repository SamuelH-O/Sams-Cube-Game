package com.example.samscubegame;

import android.annotation.SuppressLint;
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

        // Lock the screen rotation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        // Setup GameSurfaceCallback
        GameSurfaceCallback gameSurfaceCallback = new GameSurfaceCallback(this);
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