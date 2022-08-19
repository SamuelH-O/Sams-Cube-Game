package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Piece_Z extends Piece {

    Piece_Z(float squareSize, Resources resources) {
        super(squareSize, resources);
        this.paint.setColor(resources.getColor(R.color.Z_color, null));
    }

    private Piece_Z(Piece_Z pieceToCopy) {
        super(pieceToCopy);
        for (byte i = 0; i < 4; i++) {
            this.squares[i] = new Square(pieceToCopy.squares[0].size, pieceToCopy.paint);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return new Piece_Z(this);
    }

    @Override
    void figureOutNextRotation(final GridOfSurfaces grid) {
        byte[] values = {posX, posY, rotation};

        switch (rotation) {
            case 0:
                /*
                 * Z Z   [0] | Next -> [1]
                 *   Z Z
                 * */
                if (posY + 1 >= GridOfSurfaces.NB_ROWS) {
                    values[1] = (byte) (GridOfSurfaces.NB_ROWS - 2);
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 1:
                /*
                 *   Z [1] | Next -> [0]
                 * Z Z
                 * Z
                 * */
                if (posX - 1 <= 0) {
                    values[0] = (byte) (1);
                }
                if (posX + 2 >= GridOfSurfaces.NB_COLUMNS) {
                    values[0] = (byte) (GridOfSurfaces.NB_COLUMNS - 2);
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 2:
                /*
                 * Z Z   [0] | Next -> [1]
                 *   Z Z
                 * */
                values[2] = (byte) (values[2] + 1);
                break;
            case 3:
                /*
                 *   Z [3] | Next -> [0]
                 * Z Z
                 * Z
                 * */
                if (posX + 2 >= GridOfSurfaces.NB_COLUMNS) {
                    values[0] = (byte) (GridOfSurfaces.NB_COLUMNS - 2);
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
                 * Z Z
                 *   Z Z
                 * */
                squares[0].setPos((byte) (posX - 1), (byte) (posY - 1));
                squares[1].setPos(posX, (byte) (posY - 1));
                squares[2].setPos(posX, posY);
                squares[3].setPos((byte) (posX + 1), posY);

                leftSide.add(squares[0]);
                leftSide.add(squares[2]);

                rightSide.add(squares[1]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[0]);
                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                break;
            case 1:
                /*
                 *   Z
                 * Z Z
                 * Z
                 * */
                squares[0].setPos((byte) (posX + 1), (byte) (posY - 1));
                squares[1].setPos(posX, posY);
                squares[2].setPos((byte) (posX + 1), posY);
                squares[3].setPos(posX, (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[1]);
                leftSide.add(squares[3]);

                rightSide.add(squares[0]);
                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[3]);
                bottomSide.add(squares[2]);
                break;
            case 2:
                /*
                 * Z Z
                 *   Z Z
                 * */
                squares[0].setPos((byte) (posX - 1), posY);
                squares[1].setPos(posX, posY);
                squares[2].setPos(posX, (byte) (posY + 1));
                squares[3].setPos((byte) (posX + 1), (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[2]);

                rightSide.add(squares[1]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[0]);
                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                break;
            case 3:
                /*
                 *   Z
                 * Z Z
                 * Z
                 * */
                squares[0].setPos(posX, (byte) (posY - 1));
                squares[1].setPos((byte) (posX - 1), posY);
                squares[2].setPos(posX, posY);
                squares[3].setPos((byte) (posX - 1), (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[1]);
                leftSide.add(squares[3]);

                rightSide.add(squares[0]);
                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[3]);
                bottomSide.add(squares[2]);
                break;
        }
    }

    @Override
    byte getWidth() {
        if (rotation % 2 == 0) {
            /*
             * Z Z
             *   Z Z
             * */
            return 3;
        } else {
            /*
             *   Z
             * Z Z
             * Z
             * */
            return 2;
        }
    }

    @Override
    byte getHeight() {
        if (rotation % 2 == 0) {
            /*
             * Z Z
             *   Z Z
             * */
            return 2;
        } else {
            /*
             *   Z
             * Z Z
             * Z
             * */
            return 3;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Z";
    }
}
