package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

@RequiresApi(api = Build.VERSION_CODES.S)
public class MenuActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu);
    }

    /** Called when the user taps the start button */
    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the settings button */
    public void openSettings(View view) {
        ModalBottomSheetFragment modalBottomSheetFragment = new ModalBottomSheetFragment();
        modalBottomSheetFragment.show(getSupportFragmentManager(), "Modal BottomSheet");
    }
}