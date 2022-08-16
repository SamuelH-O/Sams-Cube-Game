package com.example.samscubegame;

import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class Square {
    Paint paint = new Paint();

    float size;

    PieceTypes type;

    byte posX, posY;

    int borderSize = 5;

    float[] colorLightHSV = new float[3];
    Paint lightPaint = new Paint();

    float[] colorDarkHSV = new float[3];
    Paint darkPaint = new Paint();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    Square(float size, PieceTypes t, final Resources resources) {
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

        this.paint.setBlendMode(BlendMode.SRC_OVER);

        Color.colorToHSV(paint.getColor(), colorLightHSV);
        colorLightHSV[2] = colorLightHSV[2] + 0.6f;
        this.lightPaint.setColor(Color.HSVToColor(colorLightHSV));
        this.lightPaint.setBlendMode(BlendMode.SRC_OVER);

        Color.colorToHSV(paint.getColor(), colorDarkHSV);
        colorDarkHSV[2] = colorDarkHSV[2] - 0.6f;
        darkPaint.setColor(Color.HSVToColor(colorDarkHSV));
        darkPaint.setBlendMode(BlendMode.SRC_OVER);

        this.size = size;
    }
    void draw(final Canvas canvas) {
        // Draw the square
        canvas.drawRect(posX * size, posY * size, size + posX * size, size + posY * size, paint);

        // Draw the light border
        canvas.drawRect(posX * size, posY * size, borderSize + posX * size, size + posY * size, lightPaint);
        canvas.drawRect(posX * size, posY * size, size + posX * size, borderSize + posY * size, lightPaint);

        // Draw the dark border
        canvas.drawRect(size + posX * size, posY * size, size + posX * size - borderSize, size + posY * size, darkPaint);
        canvas.drawRect(posX * size, size + posY * size, size + posX * size, size + posY * size - borderSize, darkPaint);
    }

    void setPos(byte posX, byte posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @NonNull
    @Override
    public String toString() {
        return "Square{" +
                "paint=" + paint +
                ", size=" + size +
                ", type=" + type +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}
