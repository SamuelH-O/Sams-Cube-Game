package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Piece_S extends Piece {

    Piece_S(float squareSize, Resources resources) {
        super(squareSize, resources);
        this.paint.setColor(resources.getColor(R.color.S_color, null));
    }

    private Piece_S(Piece_S pieceToCopy) {
        super(pieceToCopy);
        for (byte i = 0; i < 4; i++) {
            this.squares[i] = new Square(pieceToCopy.squares[0].size, pieceToCopy.paint);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return new Piece_S(this);
    }

    @Override
    void figureOutNextRotation(final GridOfSurfaces grid) {
        byte[] values = {posX, posY, rotation};

        switch (rotation) {
            case 0:
                /*
                 *   S S [0] | Next -> [1]
                 * S S
                 * */
                if (posY + 1 >= grid.getNbRows()) {
                    values[1] = (byte) (grid.getNbRows() - 2);
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 1:
                /*
                 * S   [1] | Next -> [2]
                 * S S
                 *   S
                 * */
                if (posX - 1 <= 0) {
                    values[0] = (byte) (1);
                }
                if (posX + 2 >= grid.getNbColumns()) {
                    values[0] = (byte) (grid.getNbColumns() - 2);
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 2:
                /*
                 *   S S [2] | Next -> [3]
                 * S S
                 * */
                values[2] = (byte) (values[2] + 1);
                break;
            case 3:
                /*
                 * S   [3] | Next -> [0]
                 * S S
                 *   S
                 * */
                if (posX + 2 >= grid.getNbColumns()) {
                    values[0] = (byte) (grid.getNbColumns() - 2);
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
                 *   S S
                 * S S
                 * */
                squares[0].setPos(posX, (byte) (posY - 1));
                squares[1].setPos((byte) (posX + 1), (byte) (posY - 1));
                squares[2].setPos((byte) (posX - 1), posY);
                squares[3].setPos(posX, posY);

                leftSide.add(squares[0]);
                leftSide.add(squares[2]);

                rightSide.add(squares[1]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                bottomSide.add(squares[1]);
                break;
            case 1:
                /*
                 * S
                 * S S
                 *   S
                 * */
                squares[0].setPos(posX, (byte) (posY - 1));
                squares[1].setPos(posX, posY);
                squares[2].setPos((byte) (posX + 1), posY);
                squares[3].setPos((byte) (posX + 1), (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[1]);
                leftSide.add(squares[3]);

                rightSide.add(squares[0]);
                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[1]);
                bottomSide.add(squares[3]);
                break;
            case 2:
                /*
                 *   S S
                 * S S
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos((byte) (posX + 1), posY);
                squares[2].setPos((byte) (posX - 1), (byte) (posY + 1));
                squares[3].setPos(posX, (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[2]);

                rightSide.add(squares[1]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                bottomSide.add(squares[1]);
                break;
            case  3:
                /*
                 * S
                 * S S
                 *   S
                 * */
                squares[0].setPos((byte) (posX - 1), (byte) (posY - 1));
                squares[1].setPos((byte) (posX - 1), posY);
                squares[2].setPos(posX, posY);
                squares[3].setPos(posX, (byte) (posY + 1));

                leftSide.add(squares[0]);
                leftSide.add(squares[1]);
                leftSide.add(squares[3]);

                rightSide.add(squares[0]);
                rightSide.add(squares[2]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[1]);
                bottomSide.add(squares[3]);
                break;
        }
    }

    @Override
    byte getWidth() {
        if (rotation % 2 == 0) {
            /*
             *   S S
             * S S
             * */
            return 3;
        } else {
            /*
             * S
             * S S
             *   S
             * */
            return 2;
        }
    }

    @Override
    byte getHeight() {
        if (rotation % 2 == 0) {
            /*
             *   S S
             * S S
             * */
            return 2;
        } else {
            /*
             * S
             * S S
             *   S
             * */
            return 3;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "S";
    }
}
