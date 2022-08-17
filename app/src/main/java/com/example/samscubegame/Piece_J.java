package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Piece_J extends Piece{

    Piece_J(float squareSize, Resources resources) {
        super(squareSize, resources);
        this.paint.setColor(resources.getColor(R.color.J_color, null));
    }

    private Piece_J(Piece_J pieceToCopy) {
        super(pieceToCopy);
        for (byte i = 0; i < 4; i++) {
            this.squares[i] = new Square(pieceToCopy.squares[0].size, pieceToCopy.paint);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return new Piece_J(this);
    }

    @Override
    void figureOutNextRotation(final GridOfGame grid) {
        byte[] values = {posX, posY, rotation};

        switch (rotation) {
            case 0:
            case 2:
                /*
                 * J J J [0] | Next -> [1]
                 *     J
                 or
                 * J     [2] | Next -> [3]
                 * J J J
                 * */
                if (posY + 2 >= 16) {
                    values[1] = 16 - 3;
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 1:
                /*
                 *   J [1] | Next -> [2]
                 *   J
                 * J J
                 * */
                if (posX + 2 >= 10) {
                    values[0] = 10 - 3;
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 3:
                /*
                 * J J [3] | Next -> [0]
                 * J
                 * J
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
                 * J J J
                 *     J
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos((byte) (posX + 1), posY);
                squares[2].setPos((byte) (posX + 2), posY);
                squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[3]);

                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[0]);
                bottomSide.add(squares[1]);
                bottomSide.add(squares[3]);
                break;
            case 1:
                /*
                 *   J
                 *   J
                 * J J
                 * */
                squares[0].setPos((byte) (posX + 1), posY);
                squares[1].setPos((byte) (posX + 1), (byte) (posY + 1));
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
                 * J
                 * J J J
                 * */
                squares[0].setPos(posX, posY);
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
            case 3:
                /*
                 * J J
                 * J
                 * J
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos((byte) (posX + 1), posY);
                squares[2].setPos(posX, (byte) (posY + 1));
                squares[3].setPos(posX, (byte) (posY + 2));

                leftSide.add(squares[0]);
                leftSide.add(squares[2]);
                leftSide.add(squares[3]);

                rightSide.add(squares[1]);
                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[3]);
                bottomSide.add(squares[1]);
                break;
        }
    }

    @Override
    byte getWidth() {
        if (rotation % 2 == 0) {
            /*
             * J
             * J J J
             or
             * J J J
             *     J
             * */
            return 3;
        } else {
            /*
             *   J
             *   J
             * J J
             or
             * J J
             * J
             * J
             * */
            return 2;
        }
    }

    @Override
    byte getHeight() {
        if (rotation % 2 == 0) {
            /*
             * J
             * J J J
             or
             * J J J
             *     J
             * */
            return 2;
        } else {
            /*
             *   J
             *   J
             * J J
             or
             * J J
             * J
             * J
             * */
            return 3;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "J";
    }
}
