package com.example.samscubegame;

import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Square {
    Paint paint = new Paint();

    float size;

    TetrominoTypes type;

    byte posX, posY;

    Square(float size, TetrominoTypes t, final Resources resources) {
        this.type = t;
        switch (t) {
            case I:
                this.paint.setColor(resources.getColor(R.color.I_color, null));
                break;
            case J:
                this.paint.setColor(resources.getColor(R.color.J_color, null));
                break;
            case L:
                this.paint.setColor(resources.getColor(R.color.L_color, null));
                break;
            case O:
                this.paint.setColor(resources.getColor(R.color.O_color, null));
                break;
            case S:
                this.paint.setColor(resources.getColor(R.color.S_color, null));
                break;
            case T:
                this.paint.setColor(resources.getColor(R.color.T_color, null));
                break;
            case Z:
                this.paint.setColor(resources.getColor(R.color.Z_color, null));
                break;
        }
        this.size = size;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void draw(byte posX, byte posY, final Canvas canvas) {
        this.posX = posX;
        this.posY = posY;
        this.paint.setBlendMode(BlendMode.SRC_OVER);
        // Draw the square
        canvas.drawRect(posX * size, posY * size, size + posX * size, size + posY * size, paint);

        int borderSize = 5;

        // Draw the light border
        float[] colorLightHSV = new float[3];
        Color.colorToHSV(paint.getColor(), colorLightHSV);
        colorLightHSV[2] = colorLightHSV[2] + 0.6f;
        Paint lightPaint = new Paint();
        lightPaint.setColor(Color.HSVToColor(colorLightHSV));
        lightPaint.setBlendMode(BlendMode.SRC_OVER);
        canvas.drawRect(posX * size, posY * size, borderSize + posX * size, size + posY * size, lightPaint);
        canvas.drawRect(posX * size, posY * size, size + posX * size, borderSize + posY * size, lightPaint);

        // Draw the dark border
        float[] colorDarkHSV = new float[3];
        Color.colorToHSV(paint.getColor(), colorDarkHSV);
        colorDarkHSV[2] = colorDarkHSV[2] - 0.6f;
        Paint darkPaint = new Paint();
        darkPaint.setColor(Color.HSVToColor(colorDarkHSV));
        darkPaint.setBlendMode(BlendMode.SRC_OVER);
        canvas.drawRect(size + posX * size, posY * size, size + posX * size - borderSize, size + posY * size, darkPaint);
        canvas.drawRect(posX * size, size + posY * size, size + posX * size, size + posY * size - borderSize, darkPaint);
    }

    void setInGrid(GridOfGame grid) {
        grid.setSquare(this.posX, this.posY, this.type);
    }
}
