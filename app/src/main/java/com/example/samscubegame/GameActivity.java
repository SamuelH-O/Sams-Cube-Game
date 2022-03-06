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

import java.util.EnumMap;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView gameSurfaceView;

    private static final String TAG = "Svetlin SurfaceView";

    Paint testPaint = new Paint();

    float squareSize;

    enum Tetromino {
        I, J, L, O, S, T, Z,
    }

    EnumMap<Tetromino, Paint> paintMap = new EnumMap<>(Tetromino.class);

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

        // Add paints to the EnumMap
        paintMap.put(Tetromino.I, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.I)).setARGB(255,49, 199, 239);
        Objects.requireNonNull(paintMap.get(Tetromino.I)).setBlendMode(BlendMode.SRC_OVER);

        paintMap.put(Tetromino.J, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.J)).setARGB(255,90, 101, 173);
        Objects.requireNonNull(paintMap.get(Tetromino.J)).setBlendMode(BlendMode.SRC_OVER);

        paintMap.put(Tetromino.L, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.L)).setARGB(255,239, 121, 33);
        Objects.requireNonNull(paintMap.get(Tetromino.L)).setBlendMode(BlendMode.SRC_OVER);

        paintMap.put(Tetromino.O, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.O)).setARGB(255,247, 211, 8);
        Objects.requireNonNull(paintMap.get(Tetromino.O)).setBlendMode(BlendMode.SRC_OVER);

        paintMap.put(Tetromino.S, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.S)).setARGB(255,66, 182, 66);
        Objects.requireNonNull(paintMap.get(Tetromino.S)).setBlendMode(BlendMode.SRC_OVER);

        paintMap.put(Tetromino.T, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.T)).setARGB(255,173, 77, 156);
        Objects.requireNonNull(paintMap.get(Tetromino.T)).setBlendMode(BlendMode.SRC_OVER);

        paintMap.put(Tetromino.Z, new Paint());
        Objects.requireNonNull(paintMap.get(Tetromino.Z)).setARGB(255,239, 32, 41);
        Objects.requireNonNull(paintMap.get(Tetromino.Z)).setBlendMode(BlendMode.SRC_OVER);

        testPaint.setColor(Color.rgb(255, 20, 255));
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(5.0f);
    }

    // TODO: create a start game method and a running game method & find the correct way to do it

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

    private void drawMyStuff(final Canvas canvas) {
        Log.i(TAG, "Drawing...");

        // Get the size of the squares by the smaller side of the canvas
        if(canvas.getWidth() < canvas.getHeight()) squareSize = (float) canvas.getWidth() / 10;
        else squareSize = (float) canvas.getHeight() / 10;

        //drawTetromino(2, 0, 2, canvas, Tetromino.Z);
    }

    // Draw from the top-left-most position
    void drawSquare(int posX, int posY, Paint paint, final Canvas canvas) {
        // Draw the square
        canvas.drawRect(posX * squareSize, posY * squareSize, squareSize + posX * squareSize, squareSize + posY * squareSize, paint);

        int borderSize = 5;

        // Draw the light border
        float[] colorLightHSV = new float[3];
        Color.colorToHSV(paint.getColor(), colorLightHSV);
        colorLightHSV[2] = colorLightHSV[2] + 0.6f;
        Paint lightPaint = new Paint();
        lightPaint.setColor(Color.HSVToColor(colorLightHSV));
        lightPaint.setBlendMode(BlendMode.SRC_OVER);
        canvas.drawRect(posX * squareSize, posY * squareSize, borderSize + posX * squareSize, squareSize + posY * squareSize, lightPaint);
        canvas.drawRect(posX * squareSize, posY * squareSize, squareSize + posX * squareSize, borderSize + posY * squareSize, lightPaint);

        // Draw the dark border
        float[] colorDarkHSV = new float[3];
        Color.colorToHSV(paint.getColor(), colorDarkHSV);
        colorDarkHSV[2] = colorDarkHSV[2] - 0.6f;
        Paint darkPaint = new Paint();
        darkPaint.setColor(Color.HSVToColor(colorDarkHSV));
        darkPaint.setBlendMode(BlendMode.SRC_OVER);
        canvas.drawRect(squareSize + posX * squareSize, posY * squareSize, squareSize + posX * squareSize - borderSize, squareSize + posY * squareSize, darkPaint);
        canvas.drawRect(posX * squareSize, squareSize + posY * squareSize, squareSize + posX * squareSize, squareSize + posY * squareSize - borderSize, darkPaint);
    }

    void drawTetromino(int posX, int posY, int rotation, final Canvas canvas, Tetromino piece) {
        switch(piece) {
            case I:
                if(rotation % 2 == 0) {
                    // Draw a line piece horizontally
                    for (int i = posX; i < 8; i++) {
                        drawSquare(i, posY, paintMap.get(Tetromino.I), canvas);
                    }
                } else {
                    // Draw a line piece vertically
                    for(int i = posY; i < 8; i++) {
                        drawSquare(posX, i, paintMap.get(Tetromino.I), canvas);
                    }
                }
                break;
            case J:
                switch(rotation) {
                    case 0:
                        drawSquare(posX, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 2, posY + 1, paintMap.get(Tetromino.J), canvas);
                        break;
                    case 1:
                        drawSquare(posX, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 1, posY + 2, paintMap.get(Tetromino.J), canvas);
                        break;
                    case 2:
                        drawSquare(posX, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 2, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.J), canvas);
                        break;
                    case 3:
                        drawSquare(posX, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 2, posY, paintMap.get(Tetromino.J), canvas);
                        drawSquare(posX + 2, posY + 1, paintMap.get(Tetromino.J), canvas);
                        break;
                }
                break;
            case L:
                switch(rotation) {
                    case 0:
                        drawSquare(posX + 2, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX + 2, posY + 1, paintMap.get(Tetromino.L), canvas);
                        break;
                    case 1:
                        drawSquare(posX, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX, posY + 2, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX + 1, posY + 2, paintMap.get(Tetromino.L), canvas);
                        break;
                    case 2:
                        drawSquare(posX, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX + 2, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.L), canvas);
                        break;
                    case 3:
                        drawSquare(posX, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.L), canvas);
                        drawSquare(posX, posY + 2, paintMap.get(Tetromino.L), canvas);
                        break;
                }
                break;
            case O:
                drawSquare(posX, posY, paintMap.get(Tetromino.O), canvas);
                drawSquare(posX + 1, posY, paintMap.get(Tetromino.O), canvas);
                drawSquare(posX, posY + 1, paintMap.get(Tetromino.O), canvas);
                drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.O), canvas);
                break;
            case S:
                if(rotation % 2 == 0) {
                    drawSquare(posX + 1, posY, paintMap.get(Tetromino.S), canvas);
                    drawSquare(posX + 2, posY, paintMap.get(Tetromino.S), canvas);
                    drawSquare(posX, posY + 1, paintMap.get(Tetromino.S), canvas);
                    drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.S), canvas);
                } else {
                    drawSquare(posX, posY, paintMap.get(Tetromino.S), canvas);
                    drawSquare(posX, posY + 1, paintMap.get(Tetromino.S), canvas);
                    drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.S), canvas);
                    drawSquare(posX + 1, posY + 2, paintMap.get(Tetromino.S), canvas);
                }
                break;
            case T:
                switch(rotation) {
                    case 0:
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 2, posY + 1, paintMap.get(Tetromino.T), canvas);
                        break;
                    case 1:
                        drawSquare(posX, posY, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX, posY + 2, paintMap.get(Tetromino.T), canvas);
                        break;
                    case 2:
                        drawSquare(posX, posY, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 2, posY, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.T), canvas);
                        break;
                    case 3:
                        drawSquare(posX + 1, posY, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX, posY + 1, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.T), canvas);
                        drawSquare(posX + 1, posY + 2, paintMap.get(Tetromino.T), canvas);
                        break;
                }
                break;
            case Z:
                if(rotation % 2 == 0) {
                    drawSquare(posX, posY, paintMap.get(Tetromino.Z), canvas);
                    drawSquare(posX + 1, posY, paintMap.get(Tetromino.Z), canvas);
                    drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.Z), canvas);
                    drawSquare(posX + 2, posY + 1, paintMap.get(Tetromino.Z), canvas);
                } else {
                    drawSquare(posX + 1, posY, paintMap.get(Tetromino.Z), canvas);
                    drawSquare(posX, posY + 1, paintMap.get(Tetromino.Z), canvas);
                    drawSquare(posX + 1, posY + 1, paintMap.get(Tetromino.Z), canvas);
                    drawSquare(posX, posY + 2, paintMap.get(Tetromino.Z), canvas);
                }
                break;
        }
    }
}