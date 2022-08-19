package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Piece_O extends Piece {

    Piece_O(float squareSize, Resources resources) {
        super(squareSize, resources);
        this.paint.setColor(resources.getColor(R.color.O_color, null));
    }

    private Piece_O(Piece_O pieceToCopy) {
        super(pieceToCopy);
        for (byte i = 0; i < 4; i++) {
            this.squares[i] = new Square(pieceToCopy.squares[0].size, pieceToCopy.paint);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return new Piece_O(this);
    }

    @Override
    void figureOutNextRotation(final GridOfSurfaces grid) {
        byte[] values = {posX, posY, rotation};
        /*
         * O O
         * O O
         * */
        if (rotation >= 3) {
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

        /*
         * O O
         * O O
         * */
        squares[0].setPos((byte) (posX - 1), (byte) (posY - 1));
        squares[1].setPos(posX, (byte) (posY - 1));
        squares[2].setPos((byte) (posX - 1), posY);
        squares[3].setPos(posX, posY);

        leftSide.add(squares[0]);
        leftSide.add(squares[2]);

        rightSide.add(squares[1]);
        rightSide.add(squares[3]);

        bottomSide.add(squares[2]);
        bottomSide.add(squares[3]);
    }

    @Override
    byte getWidth() {
        /*
         * O O
         * O O
         * */
        return 2;
    }

    @Override
    byte getHeight() {
        /*
         * O O
         * O O
         * */
        return 2;
    }

    @NonNull
    @Override
    public String toString() {
        return "O";
    }
}
