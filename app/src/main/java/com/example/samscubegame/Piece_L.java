package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Piece_L extends Piece {

    Piece_L(float squareSize, Resources resources) {
        super(squareSize, resources);
        this.paint.setColor(resources.getColor(R.color.L_color, null));
    }

    private Piece_L(Piece_L pieceToCopy) {
        super(pieceToCopy);
        for (byte i = 0; i < 4; i++) {
            this.squares[i] = new Square(pieceToCopy.squares[0].size, pieceToCopy.paint);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return new Piece_L(this);
    }

    @Override
    void figureOutNextRotation(final GridOfGame grid) {
        byte[] values = {posX, posY, rotation};

        switch (rotation) {
            case 0:
            case 2:
                /*
                 * L     [0] | Next -> [1]
                 * L L L
                 or
                 * L L L [2] | Next -> [3]
                 * L
                 * */
                if (posY + 2 >= 16) {
                    values[1] = 16 - 3;
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 1:
                /*
                 * L   [1] | Next -> [2]
                 * L
                 * L L
                 * */
                if (posX + 2 >= 10) {
                    values[0] = 10 - 3;
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 3:
                /*
                 * L L [3] | Next -> [0]
                 *   L
                 *   L
                 * */
                if (posX + 2 >= 10) {
                    values[0] = 10 - 3;
                }
                values[2] = 0;
                break;
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

        switch (rotation) {
            case 0:
                /*
                 *     L
                 * L L L
                 * */
                squares[0].setPos((byte) (posX + 2), posY);
                squares[1].setPos(posX, (byte) (posY + 1));
                squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[1]);

                rightSide.add(squares[0]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[1]);
                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                break;
            case 1:
                /*
                 * L
                 * L
                 * L L
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos(posX, (byte) (posY + 1));
                squares[2].setPos(posX, (byte) (posY + 2));
                squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));

                leftSide.add(squares[0]);
                leftSide.add(squares[1]);
                leftSide.add(squares[2]);

                rightSide.add(squares[0]);
                rightSide.add(squares[1]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                break;
            case 2:
                /*
                 * L L L
                 * L
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos((byte) (posX + 1), posY);
                squares[2].setPos((byte) (posX + 2), posY);
                squares[3].setPos(posX, (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[3]);

                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[3]);
                bottomSide.add(squares[1]);
                bottomSide.add(squares[2]);
                break;
            case 3:
                /*
                 * L L
                 *   L
                 *   L
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos((byte) (posX + 1), posY);
                squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));

                leftSide.add(squares[0]);
                leftSide.add(squares[2]);
                leftSide.add(squares[3]);

                rightSide.add(squares[1]);
                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[0]);
                bottomSide.add(squares[3]);
                break;
        }
    }

    @Override
    byte getWidth() {
        if (rotation % 2 == 0) {
            /*
             * L
             * L L L
             or
             * L L L
             * L
             * */
            return 3;
        } else {
            /*
             * L
             * L
             * L L
             or
             * L L
             *   L
             *   L
             * */
            return 2;
        }
    }

    @Override
    byte getHeight() {
        if (rotation % 2 == 0) {
            /*
             * L
             * L L L
             or
             * L L L
             * L
             * */
            return 2;
        } else {
            /*
             * L
             * L
             * L L
             or
             * L L
             *   L
             *   L
             * */
            return 3;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "L";
    }
}
