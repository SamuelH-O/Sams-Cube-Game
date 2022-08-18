package com.example.samscubegame;

import static android.graphics.Color.pack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private SurfaceHolder gameSurfaceHolder;

    static final byte NB_ROWS = 16, NB_COLUMNS = 10;

    private float squareSize;

    private GridOfGame grid;

    private Button buttonMoveLeft;
    private Button buttonMoveRight;
    private Button buttonRotateRight;
    private Button buttonSnap;
    private Button buttonMoveBottom;

    private final long[] bgColors = new long[3];
    private final Paint gradientPaint = new Paint();
    private final Paint greyPaint = new Paint();

    private Runnable gameLoopRunnable;

    private Piece currentPiece = null;
    private Piece nextPiece = null;

    private ArrayList<Piece> fullBag = null;
    private ArrayList<Piece> rndmBag = null;
    private final Random rndm = new Random();

    private float[] gridPoints;

    private boolean showGrid;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the random seed to the current time
        rndm.setSeed(Instant.now().toEpochMilli());

        // Lock the screen rotation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_game);

        // Get the SurfaceView
        SurfaceView gameSurfaceView = findViewById(R.id.surfaceView);

        gameSurfaceView.getHolder().addCallback(this);

        // Initialize ImageViews (controls) with method findViewById()
        buttonMoveLeft = findViewById(R.id.buttonMoveLeft);
        buttonMoveRight = findViewById(R.id.buttonMoveRight);
        buttonRotateRight = findViewById(R.id.buttonRotateRight);
        buttonSnap = findViewById(R.id.buttonSnap);
        buttonMoveBottom = findViewById(R.id.buttonMoveBottom);

        grid = new GridOfGame();

        SharedPreferences sharedPref = this.getSharedPreferences(String.valueOf(R.string.pref_file), Context.MODE_PRIVATE);
        showGrid = sharedPref.getBoolean(getString(R.string.show_grid_key), false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.gameSurfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

        // Get the size of the squares by the smaller side of the canvas
        if (canvas.getWidth() < canvas.getHeight()) this.squareSize = (float) canvas.getWidth() / NB_COLUMNS;
        else this.squareSize = (float) canvas.getHeight() / NB_COLUMNS;

        // Create the colors for the gradient (TODO: add the option to modify them)
        bgColors[0] = pack(0.847f, 0.035f, 0.494f, 1.0f);
        bgColors[1] = pack(0.549f, 0.341f, 0.611f,1.0f);
        bgColors[2] =  pack(0.141f, 0.274f, 0.556f,1.0f);

        // Create the gradient from the top left to the bottom right (TODO: add the option to modify the direction)
        LinearGradient bgGradient = new LinearGradient(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), bgColors, null, Shader.TileMode.CLAMP);

        // Set the gradient to a paint with the right parameters
        gradientPaint.setDither(true);
        gradientPaint.setShader(bgGradient);

        // Add a semi-transparent grey paint to help focus (TODO: add the ability to modify this)
        greyPaint.setARGB(63, 61, 61, 61);
        greyPaint.setBlendMode(BlendMode.DARKEN);

        // Measure debug grid points
        if (showGrid) {
            gridPoints = new float[64 + 40];
            float linesYOffset = 0f, columnsXOffset = 0f;
            boolean isFirstPoint = true;
            for (int i = 0; i < 64 + 40; i++) {
                if (i < 64) {// Is points for lines
                    if (isFirstPoint) {// Is first point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = 0f;
                        } else {// Is y
                            gridPoints[i] = linesYOffset + (canvas.getHeight() / (float) NB_ROWS);
                            linesYOffset = linesYOffset + (canvas.getHeight() / (float) NB_ROWS);
                            isFirstPoint = false;
                        }
                    } else {// Is second point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = canvas.getWidth();
                        } else {// Is y
                            gridPoints[i] = linesYOffset;
                            isFirstPoint = true;
                        }
                    }
                } else {// Is points for columns
                    if (isFirstPoint) {// Is first point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = columnsXOffset + (canvas.getWidth() / (float) NB_COLUMNS);
                            columnsXOffset = columnsXOffset + (canvas.getWidth() / (float) NB_COLUMNS);
                        } else {// Is y
                            gridPoints[i] = 0f;
                            isFirstPoint = false;
                        }
                    } else {// Is second point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = columnsXOffset;
                        } else {// Is y
                            gridPoints[i] = canvas.getHeight();
                            isFirstPoint = true;
                        }
                    }
                }
            }
        }
        surfaceHolder.unlockCanvasAndPost(canvas);

        if (fullBag == null) {
            fullBag = new ArrayList<>();
            fullBag.add(new Piece_I(squareSize, getResources()));
            fullBag.add(new Piece_J(squareSize, getResources()));
            fullBag.add(new Piece_L(squareSize, getResources()));
            fullBag.add(new Piece_O(squareSize, getResources()));
            fullBag.add(new Piece_S(squareSize, getResources()));
            fullBag.add(new Piece_T(squareSize, getResources()));
            fullBag.add(new Piece_Z(squareSize, getResources()));
        }

        if (rndmBag == null) {
            rndmBag = new ArrayList<>();
            for (Piece i : fullBag) {
                try {
                    rndmBag.add(i.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentPiece == null) {
            currentPiece = getRandomPiece();
            nextPiece = getRandomPiece();
            // Place the first piece
            currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
        }

        startGame();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    @SuppressLint("NonConstantResourceId") // TODO: Remove once the debugs options are removed
    private void startGame() {
        // Get the radioGroup
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton_J);

        // If the radio button of the selected piece change, redraw the piece at the top (debug)
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            RadioButton checkedRadioButton = radioGroup1.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                switch (checkedRadioButton.getId()) {
                    case R.id.radioButton_I:
                        currentPiece = new Piece_I(squareSize, getResources());
                        break;
                    case R.id.radioButton_J:
                        currentPiece = new Piece_J(squareSize, getResources());
                        break;
                    case R.id.radioButton_L:
                        currentPiece = new Piece_L(squareSize, getResources());
                        break;
                    case R.id.radioButton_O:
                        currentPiece = new Piece_O(squareSize, getResources());
                        break;
                    case R.id.radioButton_S:
                        currentPiece = new Piece_S(squareSize, getResources());
                        break;
                    case R.id.radioButton_T:
                        currentPiece = new Piece_T(squareSize, getResources());
                        break;
                    case R.id.radioButton_Z:
                        currentPiece = new Piece_Z(squareSize, getResources());
                        break;
                }
                currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
                drawFrame();
            }
        });

        // Add OnClickListener to ImageViewMoveLeft (controls left)
        buttonMoveLeft.setOnClickListener(view -> {
            // Move piece to the left if possible
            if (currentPiece.canMoveLeft(grid)) {
                currentPiece.setPosAndRot((byte) (currentPiece.posX - 1), currentPiece.posY, currentPiece.rotation);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveRight (controls right)
        buttonMoveRight.setOnClickListener(view -> {
            // Move piece to the right if possible
            if (currentPiece.canMoveRight(grid)) {
                currentPiece.setPosAndRot((byte) (currentPiece.posX + 1), currentPiece.posY, currentPiece.rotation);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewRotateRight (controls rotate)
        buttonRotateRight.setOnClickListener(view -> {
            currentPiece.figureOutNextRotation(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewSnap (controls snap)
        buttonSnap.setOnClickListener(view -> {
            currentPiece.drop(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveBottom (controls bottom)
        buttonMoveBottom.setOnClickListener(view -> {
            // Move piece to the bottom if possible
            if (currentPiece.canMoveBottom(grid)) {
                currentPiece.setPosAndRot(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation);
            }
            drawFrame();
        });

        // Draw the first frame
        drawFrame();

        // start the game loop
        gameLoop();
    }

    private void gameLoop() {
        double level = 1d;
        double timeBtwnStep = Math.pow((0.8d-((level-1d)*0.007d)), (level-1d)) * 1000d;
        Handler gameLoopHandler = new Handler();
        gameLoopRunnable = new Runnable() {
            int i = 0;
            @Override
            public void run() {
                // Move piece one step to the bottom if possible
                if (currentPiece.canMoveBottom(grid) && i > 0) {
                    currentPiece.setPosAndRot(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation);
                } else if (i > 0) {
                    currentPiece.placeInGrid(grid);
                    currentPiece = nextPiece;

                    nextPiece = getRandomPiece();

                    currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
                }
                grid.checkForLines();
                drawFrame();
                i = i + 1;
                gameLoopHandler.postDelayed(this, (long) timeBtwnStep);
            }
        };
        gameLoopHandler.post(gameLoopRunnable);
    }

    private Piece getRandomPiece() {
        Instant start = Instant.now();// TODO: Optimize this
        // Refill the bag if empty
        if (rndmBag.isEmpty()) {
            for (Piece i : fullBag) {
                try {
                    rndmBag.add(i.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        int rndmInt = rndm.nextInt(rndmBag.size());
        Piece ret = rndmBag.get(rndmInt);
        rndmBag.remove(rndmInt);
        Instant finish = Instant.now();
        Log.d("Time Elapsed", "" + Duration.between(start, finish).toNanos());
        return ret;
    }

    private void drawFrame() {
        Canvas canvas = gameSurfaceHolder.lockHardwareCanvas();

        setBackground(canvas);
        grid.draw(canvas);
        currentPiece.draw(canvas);

        gameSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void setBackground(final Canvas canvas) {
        // Add a fancy background
        canvas.drawPaint(gradientPaint);

        canvas.drawPaint(greyPaint);

        // Add debug grid & it's text
        if (showGrid) {
            Paint whitePaint = new Paint();
            whitePaint.setARGB(255, 255, 255, 255);
            whitePaint.setBlendMode(BlendMode.DIFFERENCE);
            whitePaint.setTextSize(canvas.getHeight() / (float) NB_ROWS / 3f);
            whitePaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawLines(gridPoints, whitePaint);
            String str;
            float xOffset = canvas.getWidth() / (float) NB_COLUMNS, yOffset = canvas.getHeight() / (float) NB_ROWS;
            for (int i = 0; i < NB_COLUMNS; i++) {
                for (int j = 0; j < NB_ROWS; j++) {
                    str = ("" + i + " " + j);
                    canvas.drawText(str, (xOffset * i) + (xOffset / 2f), (yOffset * j) + (yOffset / 2f) + ((whitePaint.descent() - whitePaint.ascent()) / 2), whitePaint);
                }
            }
        }
    }
}