package com.example.samscubegame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
class BlockMono extends Block {

    private float innerSize , halfInnerSize;

    BlockMono(float size, Paint paint) {
        super(size, paint);
        this.paint.setARGB(255, 41, 41, 41);

        this.innerSize = size / 3f;
        this.halfInnerSize = innerSize / 2;
    }

    @Override
    void setSize(float size) {
        this.size = size;
        this.innerSize = size / 3f;
        this.halfInnerSize = innerSize / 2;
    }

    @Override
    void draw(final Canvas canvas) {
        /*
         * # # # # # #
         * #         #
         * #   # #   #
         * #   # #   #
         * #         #
         * # # # # # #
         * */
        final float tmpPosX = posX * size, tmpPosY = posY * size;
        // Draw the left side of the box
        canvas.drawRect(tmpPosX, tmpPosY, tmpPosX + halfInnerSize, tmpPosY + size, paint);
        
        // Draw the top side of the box
        canvas.drawRect(tmpPosX, tmpPosY, tmpPosX + size, tmpPosY + halfInnerSize, paint);

        // Draw the right side of the box
        canvas.drawRect(tmpPosX + size - halfInnerSize, tmpPosY, tmpPosX + size, tmpPosY + size, paint);

        // Draw the bottom side of the box
        canvas.drawRect(tmpPosX, tmpPosY + size - halfInnerSize, tmpPosX + size, tmpPosY + size, paint);

        // Draw the inner block
        canvas.drawRect(tmpPosX + innerSize, tmpPosY + innerSize, tmpPosX + innerSize + innerSize, tmpPosY + innerSize + innerSize, paint);
    }
}
