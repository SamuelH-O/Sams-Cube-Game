package com.example.samscubegame;

import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Tetromino {

    Paint colorOfT = new Paint();

    float squareSize;

    TetrominoTypes piece;

    int posX, posY, rotation;

    Tetromino(TetrominoTypes t, final Canvas canvas) {
        this.piece = t;
        switch(t) {
            case I:
                this.colorOfT.setARGB(255,49, 199, 239);
                break;
            case J:
                this.colorOfT.setARGB(255,90, 101, 173);
                break;
            case L:
                this.colorOfT.setARGB(255,239, 121, 33);
                break;
            case O:
                this.colorOfT.setARGB(255,247, 211, 8);
                break;
            case S:
                this.colorOfT.setARGB(255,66, 182, 66);
                break;
            case T:
                this.colorOfT.setARGB(255,173, 77, 156);
                break;
            case Z:
                this.colorOfT.setARGB(255,239, 32, 41);
                break;
        }
        this.colorOfT.setBlendMode(BlendMode.SRC_OVER);

        // Get the size of the squares by the smaller side of the canvas
        if(canvas.getWidth() < canvas.getHeight()) this.squareSize = (float) canvas.getWidth() / 10;
        else this.squareSize = (float) canvas.getHeight() / 10;
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

    void placeInGrid(int posX, int posY, int rotation, TetrominoTypes[][] grid) {
        switch(piece) {
            case I:
                if(rotation % 2 == 0) {
                    for (int i = posX; i <= posX + 4; i++) {
                        grid[i][posY] = piece;
                    }
                } else {
                    for(int i = posY; i <= posY + 4; i++) {
                        grid[posX][i] = piece;
                    }
                }
                break;
            case J:
                switch(rotation) {
                    case 0:
                        grid[posX][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        grid[posX + 2][posY + 1] = piece;
                        break;
                    case 1:
                        grid[posX][posY] = piece;
                        grid[posX + 1][posY] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        grid[posX + 1][posY + 2] = piece;
                        break;
                    case 2:
                        grid[posX][posY] = piece;
                        grid[posX + 1][posY] = piece;
                        grid[posX + 2][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        break;
                    case 3:
                        grid[posX][posY] = piece;
                        grid[posX + 1][posY] = piece;
                        grid[posX + 2][posY] = piece;
                        grid[posX + 2][posY + 1] = piece;
                        break;
                }
                break;
            case L:
                switch(rotation) {
                    case 0:
                        grid[posX + 2][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        grid[posX + 2][posY + 1] = piece;
                        break;
                    case 1:
                        grid[posX][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX][posY + 2] = piece;
                        grid[posX + 1][posY + 2] = piece;
                        break;
                    case 2:
                        grid[posX][posY] = piece;
                        grid[posX + 1][posY] = piece;
                        grid[posX + 2][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        break;
                    case 3:
                        grid[posX][posY] = piece;
                        grid[posX + 1][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX][posY + 2] = piece;
                        break;
                }
                break;
            case O:
                grid[posX][posY] = piece;
                grid[posX + 1][posY] = piece;
                grid[posX][posY + 1] = piece;
                grid[posX + 1][posY + 1] = piece;
                break;
            case S:
                if(rotation % 2 == 0) {
                    grid[posX + 1][posY] = piece;
                    grid[posX + 2][posY] = piece;
                    grid[posX][posY + 1] = piece;
                    grid[posX + 1][posY + 1] = piece;
                } else {
                    grid[posX][posY] = piece;
                    grid[posX][posY + 1] = piece;
                    grid[posX + 1][posY + 1] = piece;
                    grid[posX + 1][posY + 2] = piece;
                }
                break;
            case T:
                switch(rotation) {
                    case 0:
                        grid[posX + 1][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        grid[posX + 2][posY + 1] = piece;
                        break;
                    case 1:
                        grid[posX][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        grid[posX][posY + 2] = piece;
                        break;
                    case 2:
                        grid[posX][posY] = piece;
                        grid[posX + 1][posY] = piece;
                        grid[posX + 2][posY] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        break;
                    case 3:
                        grid[posX + 1][posY] = piece;
                        grid[posX][posY + 1] = piece;
                        grid[posX + 1][posY + 1] = piece;
                        grid[posX + 1][posY + 2] = piece;
                        break;
                }
                break;
            case Z:
                if(rotation % 2 == 0) {
                    grid[posX][posY] = piece;
                    grid[posX + 1][posY] = piece;
                    grid[posX + 1][posY + 1] = piece;
                    grid[posX + 2][posY + 1] = piece;
                } else {
                    grid[posX + 1][posY] = piece;
                    grid[posX][posY + 1] = piece;
                    grid[posX + 1][posY + 1] = piece;
                    grid[posX][posY + 2] = piece;
                }
                break;
        }
    }

    void draw(int posX, int posY, int rotation, final Canvas canvas) {
        switch(piece) {
            case I:
                if(rotation % 2 == 0) {
                    // Draw a line piece horizontally
                    for (int i = posX; i <= posX + 4; i++) {
                        drawSquare(i, posY, colorOfT, canvas);
                    }
                } else {
                    // Draw a line piece vertically
                    for(int i = posY; i <= posY + 4; i++) {
                        drawSquare(posX, i, colorOfT, canvas);
                    }
                }
                break;
            case J:
                switch(rotation) {
                    case 0:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 2, posY + 1, colorOfT, canvas);
                        break;
                    case 1:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 2, colorOfT, canvas);
                        break;
                    case 2:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX + 2, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        break;
                    case 3:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX + 2, posY, colorOfT, canvas);
                        drawSquare(posX + 2, posY + 1, colorOfT, canvas);
                        break;
                }
                break;
            case L:
                switch(rotation) {
                    case 0:
                        drawSquare(posX + 2, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 2, posY + 1, colorOfT, canvas);
                        break;
                    case 1:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX, posY + 2, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 2, colorOfT, canvas);
                        break;
                    case 2:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX + 2, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        break;
                    case 3:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX, posY + 2, colorOfT, canvas);
                        break;
                }
                break;
            case O:
                drawSquare(posX, posY, colorOfT, canvas);
                drawSquare(posX + 1, posY, colorOfT, canvas);
                drawSquare(posX, posY + 1, colorOfT, canvas);
                drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                break;
            case S:
                if(rotation % 2 == 0) {
                    drawSquare(posX + 1, posY, colorOfT, canvas);
                    drawSquare(posX + 2, posY, colorOfT, canvas);
                    drawSquare(posX, posY + 1, colorOfT, canvas);
                    drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                } else {
                    drawSquare(posX, posY, colorOfT, canvas);
                    drawSquare(posX, posY + 1, colorOfT, canvas);
                    drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                    drawSquare(posX + 1, posY + 2, colorOfT, canvas);
                }
                break;
            case T:
                switch(rotation) {
                    case 0:
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 2, posY + 1, colorOfT, canvas);
                        break;
                    case 1:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        drawSquare(posX, posY + 2, colorOfT, canvas);
                        break;
                    case 2:
                        drawSquare(posX, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX + 2, posY, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        break;
                    case 3:
                        drawSquare(posX + 1, posY, colorOfT, canvas);
                        drawSquare(posX, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                        drawSquare(posX + 1, posY + 2, colorOfT, canvas);
                        break;
                }
                break;
            case Z:
                if(rotation % 2 == 0) {
                    drawSquare(posX, posY, colorOfT, canvas);
                    drawSquare(posX + 1, posY, colorOfT, canvas);
                    drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                    drawSquare(posX + 2, posY + 1, colorOfT, canvas);
                } else {
                    drawSquare(posX + 1, posY, colorOfT, canvas);
                    drawSquare(posX, posY + 1, colorOfT, canvas);
                    drawSquare(posX + 1, posY + 1, colorOfT, canvas);
                    drawSquare(posX, posY + 2, colorOfT, canvas);
                }
                break;
        }
        this.posX = posX;
        this.posY = posY;
        this.rotation = rotation;
    }
}
