package com.example.samscubegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

abstract class Block {
    final Paint paint;

    float size;

    byte posX, posY;

    @RequiresApi(api = Build.VERSION_CODES.S)
    Block(float size, Paint paint) {
        this.size = size;
        this.paint = paint;
    }

    abstract void draw(final Canvas canvas);

    void setPos(byte posX, byte posY) {
        this.posX = posX;
        this.posY = posY;
    }

    abstract void setSize(float size);

    @NonNull
    @Override
    public String toString() {
        return "block{" +
                "paint=" + paint +
                ", size=" + size +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}
