package com.example.samscubegame;

import static android.graphics.Color.pack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
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

    SurfaceView gameSurfaceView;

    SurfaceHolder surfaceHolder;

    float squareSize;

    GridOfGame grid;

    ImageView imgViewMoveLeft;
    ImageView imgViewMoveRight;
    ImageView imgViewRotateRight;
    ImageView imgViewSnap;
    ImageView imgViewMoveBottom;

    Piece currentPiece;
    Piece nextPiece;
    ArrayList<Piece> rndmBag;

    private float[] gridPoints;

    SharedPreferences sharedPref;
    boolean showGrid;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        grid = new GridOfGame(squareSize, getResources());

        sharedPref = this.getSharedPreferences(String.valueOf(R.string.pref_file), Context.MODE_PRIVATE);
        showGrid = sharedPref.getBoolean(getString(R.string.show_grid_key), false);
    }

    // TODO: create a start game method and a running game method & find the correct way to do it

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockCanvas();

        // Get the size of the squares by the smaller side of the canvas
        if (canvas.getWidth() < canvas.getHeight()) this.squareSize = (float) canvas.getWidth() / 10;
        else this.squareSize = (float) canvas.getHeight() / 10;

        // Measure debug grid points
        gridPoints = new float[64 + 40];
        float linesYOffset = 0f, columnsXOffset = 0f;
        boolean isFirstPoint = true;
        for (int i = 0; i < 64 + 40; i++) {
            if (i < 64) {// Is points for lines
                if (isFirstPoint) {// Is first point
                    if (i % 2 == 0) {// Is x
                        gridPoints[i] = 0f;
                    } else {// Is y
                        gridPoints[i] = linesYOffset + (canvas.getHeight() / 16f);
                        linesYOffset = linesYOffset + (canvas.getHeight() / 16f);
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
                        gridPoints[i] = columnsXOffset + (canvas.getWidth() / 10f);
                        columnsXOffset = columnsXOffset + (canvas.getWidth() / 10f);
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
        surfaceHolder.unlockCanvasAndPost(canvas);

        rndmBag = new ArrayList<>();
        for (PieceTypes i : PieceTypes.values()) {
            if (i != PieceTypes.NULL) {
                rndmBag.add(new Piece(i, squareSize, getResources()));
            }
        }

        startGame();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    @SuppressLint("NonConstantResourceId") // TODO: Remove once the debugs options are removed
    private void startGame() {
        currentPiece = getRandomPiece();
        nextPiece = getRandomPiece();

        // Get the radioGroup
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton_J);

        // Setup debug grid
        Square s0 = new Square(squareSize, PieceTypes.O, getResources());
        s0.setPos((byte) (9), (byte) (15));
        grid.setSquare(s0);
        Square s1 = new Square(squareSize, PieceTypes.O, getResources());
        s1.setPos((byte) (8), (byte) (15));
        grid.setSquare(s1);
        Square s2 = new Square(squareSize, PieceTypes.I, getResources());
        s2.setPos((byte) (9), (byte) (14));
        grid.setSquare(s2);

        // Draw first frame with background and piece
        currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
        drawFrame();


        // If the radio button of the selected piece change, redraw the piece at the top (debug)
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            RadioButton checkedRadioButton = radioGroup1.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                switch (checkedRadioButton.getId()) {
                    case R.id.radioButton_I:
                        currentPiece = new Piece(PieceTypes.I, squareSize, getResources());
                        break;
                    case R.id.radioButton_J:
                        currentPiece = new Piece(PieceTypes.J, squareSize, getResources());
                        break;
                    case R.id.radioButton_L:
                        currentPiece = new Piece(PieceTypes.L, squareSize, getResources());
                        break;
                    case R.id.radioButton_O:
                        currentPiece = new Piece(PieceTypes.O, squareSize, getResources());
                        break;
                    case R.id.radioButton_S:
                        currentPiece = new Piece(PieceTypes.S, squareSize, getResources());
                        break;
                    case R.id.radioButton_T:
                        currentPiece = new Piece(PieceTypes.T, squareSize, getResources());
                        break;
                    case R.id.radioButton_Z:
                        currentPiece = new Piece(PieceTypes.Z, squareSize, getResources());
                        break;
                }
                currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
                drawFrame();
            }
        });

        // Add OnClickListener to ImageViewMoveLeft (controls left)
        imgViewMoveLeft.setOnClickListener(view -> {
            this.animateImageView(imgViewMoveLeft);

            // Move piece to the left if possible
            if (currentPiece.canMoveLeft(grid)) {
                currentPiece.setPosAndRot((byte) (currentPiece.posX - 1), currentPiece.posY, currentPiece.rotation);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveRight (controls right)
        imgViewMoveRight.setOnClickListener(view -> {
            this.animateImageView(imgViewMoveRight);

            // Move piece to the right if possible
            if (currentPiece.canMoveRight(grid)) {
                currentPiece.setPosAndRot((byte) (currentPiece.posX + 1), currentPiece.posY, currentPiece.rotation);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewRotateRight (controls rotate)
        imgViewRotateRight.setOnClickListener(view -> {
            this.animateImageView(imgViewRotateRight);

            currentPiece.figureOutNextRotation(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewSnap (controls snap)
        imgViewSnap.setOnClickListener(view -> {
            this.animateImageView(imgViewSnap);

            currentPiece.drop(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveBottom (controls bottom)
        imgViewMoveBottom.setOnClickListener(view -> {
            this.animateImageView(imgViewMoveBottom);

            // Move piece to the bottom if possible
            if (currentPiece.canMoveBottom(grid)) {
                currentPiece.setPosAndRot(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation);
            }
            drawFrame();
        });
        gameLoop();
    }

    private void gameLoop() {
        double level = 1d;
        double timeBtwnStep = Math.pow((0.8d-((level-1d)*0.007d)), (level-1d)) * 1000d;
        Handler gameLoopHandler = new Handler();
        Runnable r = new Runnable() {
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
                drawFrame();
                i = i + 1;
                gameLoopHandler.postDelayed(this, (long) timeBtwnStep);
            }
        };
        gameLoopHandler.post(r);
    }

    private Piece getRandomPiece() {
        Instant start = Instant.now();// TODO: Optimize this
        // Refill the bag if empty
        if (rndmBag.isEmpty()) {
            for (PieceTypes i : PieceTypes.values()) {
                if (i != PieceTypes.NULL) {
                    rndmBag.add(new Piece(i, squareSize, getResources()));
                }
            }
        }

        Random rndm = new Random();
        int rndmInt = rndm.nextInt(rndmBag.size());
        Piece ret = rndmBag.get(rndmInt);
        rndmBag.remove(rndmInt);
        Instant finish = Instant.now();
        Log.d("Time Elapsed", "" + Duration.between(start, finish).toNanos());
        return ret;
    }

    private void drawFrame() {
        Canvas canvas = surfaceHolder.lockCanvas();

        setBackground(canvas);
        grid.draw(canvas);
        currentPiece.draw(canvas);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

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

        // Add debug grid & it's text
        if (showGrid) {
            Paint whitePaint = new Paint();
            whitePaint.setARGB(255, 255, 255, 255);
            whitePaint.setBlendMode(BlendMode.DIFFERENCE);
            whitePaint.setTextSize(canvas.getHeight() / 16f / 3f);
            whitePaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawLines(gridPoints, whitePaint);
            String str;
            float xOffset = canvas.getWidth() / 10f, yOffset = canvas.getHeight() / 16f;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 16; j++) {
                    str = ("" + i + " " + j);
                    // ((whitePaint.descent() - whitePaint.ascent()) / 2) is the distance from the baseline to the center.
                    canvas.drawText(str, (xOffset * i) + (xOffset / 2f), (yOffset * j) + (yOffset / 2f) + ((whitePaint.descent() - whitePaint.ascent()) / 2), whitePaint);
                }
            }
        }
    }

    private void animateImageView(ImageView imageView) {
        // Create matrix to un-zoom inside the image
        Matrix matrixZoom = new Matrix();
        matrixZoom.setScale(0.9f, 0.9f);

        // Apply the zoom
        imageView.animateTransform(matrixZoom);

        // After 100 milliseconds un-zoom
        imageView.postDelayed(() -> {
            matrixZoom.setScale(1f, 1f);
            imageView.animateTransform(matrixZoom);
        }, 100);
    }
}