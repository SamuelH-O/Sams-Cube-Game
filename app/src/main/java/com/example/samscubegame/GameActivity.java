package com.example.samscubegame;

import static android.graphics.Color.pack;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView gameSurfaceView;

    static final String TAG = "SurfaceView";

    GridOfGame grid;

    ImageView imgViewMoveLeft;
    ImageView imgViewMoveRight;
    ImageView imgViewRotateRight;
    ImageView imgViewSnap;
    ImageView imgViewMoveBottom;

    Piece currentPiece;

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

        grid = new GridOfGame();
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

    @SuppressLint("NonConstantResourceId") // TODO: Remove once the debugs options are removed
    private void startGame(SurfaceHolder holder) {
        Log.i(TAG, "Trying to draw test piece");
        Canvas canvas = holder.lockCanvas();

        currentPiece = new Piece(TetrominoTypes.J, canvas, getResources());

        // Get the radioGroup
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton_J);

        setBackground(canvas);
        currentPiece.draw((byte) (4), (byte) (0), (byte) (0), canvas);
        holder.unlockCanvasAndPost(canvas);

        // If the radio button of the selected piece change, redraw the piece at the top (debug)
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            RadioButton checkedRadioButton = (RadioButton) radioGroup1.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                Canvas canvas1 = holder.lockCanvas();
                switch (checkedRadioButton.getId()) {
                    case R.id.radioButton_I:
                        currentPiece = new Piece(TetrominoTypes.I, canvas1, getResources());
                        break;
                    case R.id.radioButton_J:
                        currentPiece = new Piece(TetrominoTypes.J, canvas1, getResources());
                        break;
                    case R.id.radioButton_L:
                        currentPiece = new Piece(TetrominoTypes.L, canvas1, getResources());
                        break;
                    case R.id.radioButton_O:
                        currentPiece = new Piece(TetrominoTypes.O, canvas1, getResources());
                        break;
                    case R.id.radioButton_S:
                        currentPiece = new Piece(TetrominoTypes.S, canvas1, getResources());
                        break;
                    case R.id.radioButton_T:
                        currentPiece = new Piece(TetrominoTypes.T, canvas1, getResources());
                        break;
                    case R.id.radioButton_Z:
                        currentPiece = new Piece(TetrominoTypes.Z, canvas1, getResources());
                        break;
                }
                if (canvas1 == null) {
                    Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
                } else {
                    setBackground(canvas1);
                    currentPiece.draw((byte) (4), (byte) (0), (byte) (0), canvas1);
                    holder.unlockCanvasAndPost(canvas1);
                }
            }
        });

        // TODO: Add boundary test with the grid of blocks
        // Add OnClickListener to ImageViewMoveLeft (controls left)
        imgViewMoveLeft.setOnClickListener(view -> {
            Log.i(TAG, "Trying to move current piece to the left");
            Canvas canvas1 = holder.lockCanvas();
            if (canvas1 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas1);
                if (currentPiece.posX - 1 >= 0) {
                    currentPiece.draw((byte) (currentPiece.posX - 1), currentPiece.posY, currentPiece.rotation, canvas1);
                } else {
                    currentPiece.draw(currentPiece.posX, currentPiece.posY, currentPiece.rotation, canvas1);
                }
                holder.unlockCanvasAndPost(canvas1);
            }
        });

        // Add OnClickListener to ImageViewMoveRight (controls right)
        imgViewMoveRight.setOnClickListener(view -> {
            Log.i(TAG, "Trying to move current piece to the right");
            Canvas canvas1 = holder.lockCanvas();
            if (canvas1 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas1);
                if (currentPiece.posX + currentPiece.getWidth() < 10) {
                    currentPiece.draw((byte) (currentPiece.posX + 1), currentPiece.posY, currentPiece.rotation, canvas1);
                } else {
                    currentPiece.draw(currentPiece.posX, currentPiece.posY, currentPiece.rotation, canvas1);
                }
                holder.unlockCanvasAndPost(canvas1);
            }
        });

        // Add OnClickListener to ImageViewRotateRight (controls rotate)
        imgViewRotateRight.setOnClickListener(view -> {
            Log.i(TAG, "Trying to rotate current piece to the right");
            Canvas canvas1 = holder.lockCanvas();
            if (canvas1 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas1);
                currentPiece.drawNextRotation(canvas1);
                holder.unlockCanvasAndPost(canvas1);
            }
        });

        // Add OnClickListener to ImageViewSnap (controls snap)
        imgViewSnap.setOnClickListener(view -> {
            Log.i(TAG, "Trying to snap current piece to the bottom");
            Canvas canvas1 = holder.lockCanvas();
            if (canvas1 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas1);
                currentPiece.draw(currentPiece.posX, currentPiece.getRowToSnapTo(grid), currentPiece.rotation, canvas1);
                holder.unlockCanvasAndPost(canvas1);
            }
        });

        // Add OnClickListener to ImageViewMoveBottom (controls bottom)
        imgViewMoveBottom.setOnClickListener(view -> {
            Log.i(TAG, "Trying to move current piece to the bottom");
            Canvas canvas1 = holder.lockCanvas();
            if (canvas1 == null) {
                Log.e(TAG, "Cannot draw onto the canvas as it's null (being drawn on)");
            } else {
                setBackground(canvas1);
                if (currentPiece.posY + currentPiece.getHeight() < 16) {
                    currentPiece.draw(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation, canvas1);
                } else {
                    currentPiece.draw(currentPiece.posX, currentPiece.posY, currentPiece.rotation, canvas1);
                }
                holder.unlockCanvasAndPost(canvas1);
            }
        });
    }

    private void drawMyStuff(final Canvas canvas) {
        Log.i(TAG, "Drawing...");

        Piece t = new Piece(TetrominoTypes.I, canvas, getResources());
        t.draw((byte) (4), (byte) (0), (byte) (1), canvas);
        t.placeInGrid(grid);
        grid.printGridState();
    }
}