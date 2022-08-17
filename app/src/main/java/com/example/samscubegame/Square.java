package com.example.samscubegame;

import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

class Square {
    private final Paint paint;

    final float size;

    byte posX, posY;

    private final Paint lightPaint = new Paint();

    private final Paint darkPaint = new Paint();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    Square(float size, Paint paint) {
        this.paint = paint;

        this.paint.setBlendMode(BlendMode.SRC_OVER);

        float[] colorLightHSV = new float[3];
        Color.colorToHSV(paint.getColor(), colorLightHSV);
        colorLightHSV[2] = colorLightHSV[2] + 0.6f;
        this.lightPaint.setColor(Color.HSVToColor(colorLightHSV));
        this.lightPaint.setBlendMode(BlendMode.SRC_OVER);

        float[] colorDarkHSV = new float[3];
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
        int borderSize = 5;
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
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}
