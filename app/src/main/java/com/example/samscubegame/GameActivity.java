package com.example.samscubegame;

import static android.graphics.Color.pack;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView gameSurfaceView;

    private static final String TAG = "SurfaceView";

    TetrominoTypes[][] grid = new TetrominoTypes[10][16];

    ImageView imgViewMoveLeft;
    ImageView imgViewMoveRight;
    ImageView imgViewRotateRight;
    ImageView imgViewSnap;
    ImageView imgViewMoveBottom;

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

        // Initialize ImageViews (controls) with method findViewById()
        imgViewMoveLeft = findViewById((R.id.imageViewMoveLeft));
        imgViewMoveRight = findViewById(R.id.imageViewMoveRight);
        imgViewRotateRight = findViewById(R.id.imageViewRotateRight);
        imgViewSnap = findViewById(R.id.imageViewSnap);
        imgViewMoveBottom = findViewById(R.id.imageViewMoveBottom);

        // Fill the grid with NULL
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 16; j++) {
                grid[i][j] = TetrominoTypes.NULL;
            }
        }
    }

    // TODO: create a start game method and a running game method & find the correct way to do it

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startGame(surfaceHolder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //startGame(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    private void setBackground(final Canvas canvas) {
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
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 10; j++) {
                str.append("|").append(tetrominoTypeToString(grid[j][i]));
            }
            str.append("|\n").append("#####################\n");
        }
        Log.i(TAG, "State of the grid :\n" + str);
    }

    private void startGame(SurfaceHolder holder) {
        Log.i(TAG, "Trying to draw test piece");
        Canvas canvas = holder.lockCanvas();
        Tetromino t = new Tetromino(TetrominoTypes.I, canvas);
        if (canvas == null) {
            Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
        } else {
            setBackground(canvas);
            t.draw(4, 0, 1, canvas);
            holder.unlockCanvasAndPost(canvas);
        }

        // Add OnClickListener to ImageViews (controls)
        imgViewMoveLeft.setOnClickListener(view -> {
            Log.i(TAG, "Trying to move test piece to the left");
            Canvas canvas1 = holder.lockCanvas();
            if (canvas1 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas1);
                if (t.posX - 1 >= 0) {
                    t.draw(t.posX - 1, 0, 1, canvas1);
                } else {
                    t.draw(t.posX, 0, 1, canvas1);
                }
                holder.unlockCanvasAndPost(canvas1);
            }
        });
        imgViewMoveRight.setOnClickListener(view -> {
            Log.i(TAG, "Trying to move test piece to the right");
            Canvas canvas12 = holder.lockCanvas();
            if (canvas12 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas12);
                if (t.posX + 1 <= 9) {
                    t.draw(t.posX + 1, 0, 1, canvas12);
                } else {
                    t.draw(t.posX, 0, 1, canvas12);
                }
                holder.unlockCanvasAndPost(canvas12);
            }
        });
        imgViewRotateRight.setOnClickListener(view -> {

        });
        imgViewSnap.setOnClickListener(view -> {

        });
        imgViewMoveBottom.setOnClickListener(view -> {

        });
    }

    private void drawMyStuff(final Canvas canvas) {
        Log.i(TAG, "Drawing...");

        Tetromino t = new Tetromino(TetrominoTypes.I, canvas);
        t.draw(4, 0, 1, canvas);
        t.placeInGrid(4, 0, 1, grid);
        printGridState();
    }
}