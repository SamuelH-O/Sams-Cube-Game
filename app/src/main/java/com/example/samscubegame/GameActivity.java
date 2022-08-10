package com.example.samscubegame;

import static android.graphics.Color.pack;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
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

    SurfaceHolder surfaceHolder;

    static final String TAG = "SurfaceView";

    float squareSize;

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

        grid = new GridOfGame(squareSize, getResources());
    }

    // TODO: create a start game method and a running game method & find the correct way to do it

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockCanvas();
        // Get the size of the squares by the smaller side of the canvas
        if (canvas.getWidth() < canvas.getHeight()) this.squareSize = (float) canvas.getWidth() / 10;
        else this.squareSize = (float) canvas.getHeight() / 10;
        surfaceHolder.unlockCanvasAndPost(canvas);
        startGame();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    @SuppressLint("NonConstantResourceId") // TODO: Remove once the debugs options are removed
    private void startGame() {
        currentPiece = new Piece(TetrominoTypes.J, squareSize, getResources());

        // Get the radioGroup
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioButton_J);

        // Setup debug grid
        Square s0 = new Square(squareSize, TetrominoTypes.O, getResources());
        s0.setPos((byte) (0), (byte) (15));
        grid.setSquare(s0);
        Square s1 = new Square(squareSize, TetrominoTypes.O, getResources());
        s1.setPos((byte) (1), (byte) (15));
        grid.setSquare(s1);
        Square s2 = new Square(squareSize, TetrominoTypes.I, getResources());
        s2.setPos((byte) (0), (byte) (14));
        grid.setSquare(s2);

        // Draw first frame with background and piece
        currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
        drawFrame();


        // If the radio button of the selected piece change, redraw the piece at the top (debug)
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            RadioButton checkedRadioButton = (RadioButton) radioGroup1.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                switch (checkedRadioButton.getId()) {
                    case R.id.radioButton_I:
                        currentPiece = new Piece(TetrominoTypes.I, squareSize, getResources());
                        break;
                    case R.id.radioButton_J:
                        currentPiece = new Piece(TetrominoTypes.J, squareSize, getResources());
                        break;
                    case R.id.radioButton_L:
                        currentPiece = new Piece(TetrominoTypes.L, squareSize, getResources());
                        break;
                    case R.id.radioButton_O:
                        currentPiece = new Piece(TetrominoTypes.O, squareSize, getResources());
                        break;
                    case R.id.radioButton_S:
                        currentPiece = new Piece(TetrominoTypes.S, squareSize, getResources());
                        break;
                    case R.id.radioButton_T:
                        currentPiece = new Piece(TetrominoTypes.T, squareSize, getResources());
                        break;
                    case R.id.radioButton_Z:
                        currentPiece = new Piece(TetrominoTypes.Z, squareSize, getResources());
                        break;
                }
                currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
                drawFrame();
            }
        });

        // TODO: Add boundary test with the grid of blocks
        // Add OnClickListener to ImageViewMoveLeft (controls left)
        imgViewMoveLeft.setOnClickListener(view -> {
            this.animateImageView(imgViewMoveLeft);

            // Move piece to the left if possible
            if (currentPiece.canMoveLeft(grid)) {
                currentPiece.posX = (byte) (currentPiece.posX - 1);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveRight (controls right)
        imgViewMoveRight.setOnClickListener(view -> {
            this.animateImageView(imgViewMoveRight);

            // Move piece to the right if possible
            if (currentPiece.posX + currentPiece.getWidth() < 10) {
                currentPiece.posX = (byte) (currentPiece.posX + 1);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewRotateRight (controls rotate)
        imgViewRotateRight.setOnClickListener(view -> {
            this.animateImageView(imgViewRotateRight);

            currentPiece.figureOutNextRotation();
            drawFrame();
        });

        // Add OnClickListener to ImageViewSnap (controls snap)
        imgViewSnap.setOnClickListener(view -> {
            this.animateImageView(imgViewSnap);

            currentPiece.posY = currentPiece.getRowToSnapTo(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveBottom (controls bottom)
        imgViewMoveBottom.setOnClickListener(view -> {
            this.animateImageView(imgViewMoveBottom);

            // Move piece to the bottom if possible
            if (currentPiece.posY + currentPiece.getHeight() < 16) {
                currentPiece.posY = (byte) (currentPiece.posY + 1);
            }
            drawFrame();
        });
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