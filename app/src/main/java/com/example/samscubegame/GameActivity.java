package com.example.samscubegame;

import static android.graphics.Color.pack;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView gameSurfaceView;

    private static final String TAG = "Svetlin SurfaceView";

    Paint testPaint = new Paint();

    TetrominoTypes[][] grid = new TetrominoTypes[10][16];

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Lock the screen rotation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        // Get the SurfaceView
        gameSurfaceView = findViewById(R.id.surfaceView);

        gameSurfaceView.getHolder().addCallback(this);

        // Fill the grid with NULL
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 16; j++) {
                grid[i][j] = TetrominoTypes.NULL;
            }
        }

        testPaint.setColor(Color.rgb(255, 20, 255));
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(5.0f);
    }

    // TODO: create a start game method and a running game method & find the correct way to do it
    // TODO: find a way to erase squares & decide if I should redraw the whole grid every change or just update the changes

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        tryDrawing(surfaceHolder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        tryDrawing(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    private void tryDrawing(SurfaceHolder holder) {
        Log.i(TAG, "Trying to draw...");

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
        } else {
            addBackground(canvas);
            drawMyStuff(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void addBackground(final Canvas canvas) {
        // Create the colors for the gradient (TODO: add the option to modify them)
        long[] bgColors = new long[3];
        bgColors[0] = pack(0.847f, 0.035f, 0.494f, 1.0f);
        bgColors[1] = pack(0.549f, 0.341f, 0.611f,1.0f);
        bgColors[2] =  pack(0.141f, 0.274f, 0.556f,1.0f);

        // Create the gradient from the top left to the bottom right (TODO: add the option to modify the direction)
        LinearGradient bgGradient = new LinearGradient(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), bgColors, null, Shader.TileMode.CLAMP);

        // Add the gradient to a paint with the right parameters
        Paint gradientPaint = new Paint();
        gradientPaint.setDither(true);
        gradientPaint.setShader(bgGradient);

        // Add a fancy background
        canvas.drawPaint(gradientPaint);

        // Add a semi-transparent grey paint to help focus (TODO: add the ability to modify this)
        Paint greyPaint = new Paint();
        greyPaint.setARGB(63, 61, 61, 61);
        greyPaint.setBlendMode(BlendMode.DARKEN);
        canvas.drawPaint(greyPaint);
    }

    private char tetrominoTypeToString(TetrominoTypes t) {
        switch(t) {
            case I:
                return 'I';
            case J:
                return 'J';
            case L:
                return 'L';
            case O:
                return 'O';
            case S:
                return 'S';
            case T:
                return 'T';
            case Z:
                return 'Z';
            case NULL:
                return ' ';
        }
        return ' ';
    }

    private void printGridState() {
        String str = "";
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 10; j++) {
                str = str + "|" + tetrominoTypeToString(grid[j][i]);
            }
            str = str + "|\n" + "#####################\n";
        }
        Log.i(TAG, "State of the grid :\n" + str);
    }

    private void drawMyStuff(final Canvas canvas) {
        Log.i(TAG, "Drawing...");

        Tetromino t = new Tetromino(TetrominoTypes.I, canvas);
        t.draw(4, 0, 1, canvas);
        t.placeInGrid(4, 0, 1, grid);
        printGridState();
    }
}