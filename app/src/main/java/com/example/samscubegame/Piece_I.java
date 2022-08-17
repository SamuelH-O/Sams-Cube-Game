package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Piece_I extends Piece{

    Piece_I(float squareSize, Resources resources) {
        super(squareSize, resources);
        this.paint.setColor(resources.getColor(R.color.I_color, null));
    }

    Piece_I(Piece_I pieceToCopy) {
        super(pieceToCopy);
        for (byte i = 0; i < 4; i++) {
            this.squares[i] = new Square(pieceToCopy.squares[0].size, pieceToCopy.paint);
        }
    }

    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        super.clone();
        return new Piece_I(this);
    }

    @Override
    void figureOutNextRotation(final GridOfGame grid) {
        byte[] values = {posX, posY, rotation};

        if (rotation % 2 == 0) {
            // I I I I [0] | Next -> [1]
            if (posY + 3 >= 16) {
                values[1] = 16 - 4;
            }
            values[2] = 1;
        } else {
            /*
             * I [1] | Next -> [0]
             * I
             * I
             * I
             * */
            if (posX + 3 >= 10) {
                values[0] = 10 - 4;
            }
            values[2] = 0;
        }

        super.applyRotation(grid, values[0], values[1], values[2]);
    }

    @Override
    void setPosAndRot(byte posX, byte posY, byte rotation) {
        this.posX = posX;
        this.posY = posY;
        this.rotation = rotation;

        leftSide.clear();
        rightSide.clear();
        bottomSide.clear();

        if (rotation % 2 == 0) {
            // I I I I
            for (byte i = 0; i < 4; i++) {
                squares[i].setPos((byte) (posX + i), posY);
            }

            leftSide.add(squares[0]);

            rightSide.add(squares[3]);

            bottomSide.addAll(Arrays.asList(squares));
        } else {
            /*
             * I
             * I
             * I
             * I
             * */
            for (byte i = 0; i < 4; i++) {
                squares[i].setPos(posX, (byte) (posY + i));
            }

            leftSide.addAll(Arrays.asList(squares));

            rightSide.addAll(Arrays.asList(squares));

            bottomSide.add(squares[3]);
        }
    }

    @Override
    byte getWidth() {
        if (rotation % 2 == 0) {
            // I I I I
            return 4;
        } else {
            /*
             * I
             * I
             * I
             * I
             * */
            return 1;
        }
    }

    @Override
    byte getHeight() {
        if (rotation % 2 == 0) {
            // I I I I
            return 1;
        } else {
            /*
             * I
             * I
             * I
             * I
             * */
            return 4;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "I";
    }
}
