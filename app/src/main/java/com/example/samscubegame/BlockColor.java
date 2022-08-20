package com.example.samscubegame;

import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
class BlockColor extends Block {
    private final Paint lightPaint = new Paint();

    private final Paint darkPaint = new Paint();

    BlockColor(float size, Paint paint) {
        super(size, paint);

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
    }

    @Override
    void setSize(float size) {
        this.size = size;
    }

    @Override
    void draw(final Canvas canvas) {
        // Draw the block
        canvas.drawRect(posX * size, posY * size, size + posX * size, size + posY * size, paint);

        // Draw the light border
        int borderSize = 5;
        canvas.drawRect(posX * size, posY * size, borderSize + posX * size, size + posY * size, lightPaint);
        canvas.drawRect(posX * size, posY * size, size + posX * size, borderSize + posY * size, lightPaint);

        // Draw the dark border
        canvas.drawRect(size + posX * size, posY * size, size + posX * size - borderSize, size + posY * size, darkPaint);
        canvas.drawRect(posX * size, size + posY * size, size + posX * size, size + posY * size - borderSize, darkPaint);
    }
}
