package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Piece_Z extends Piece {

    Piece_Z(float blockSize, final Resources resources, byte blockDesign) {
        super(blockSize, resources, blockDesign);
        this.paint.setColor(resources.getColor(R.color.Z_color, null));
    }

    private Piece_Z(Piece_Z pieceToCopy) {
        super(pieceToCopy);
        if (pieceToCopy.blockDesign == 0) {
            for (byte i = 0; i < 4; i++) {
                this.blocks[i] = new BlockColor(pieceToCopy.blocks[0].size, pieceToCopy.paint);
            }
        } else if (pieceToCopy.blockDesign  == 1) {
            for (byte i = 0; i < 4; i++) {
                this.blocks[i] = new BlockMono(pieceToCopy.blocks[0].size, pieceToCopy.paint);
            }
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
                if (posY + 1 >= grid.getNbRows()) {
                    values[1] = (byte) (grid.getNbRows() - 2);
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
                if (posX + 2 >= grid.getNbColumns()) {
                    values[0] = (byte) (grid.getNbColumns() - 2);
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
                 * Z Z
                 *   Z Z
                 * */
                blocks[0].setPos((byte) (posX - 1), (byte) (posY - 1));
                blocks[1].setPos(posX, (byte) (posY - 1));
                blocks[2].setPos(posX, posY);
                blocks[3].setPos((byte) (posX + 1), posY);

                leftSide.add(blocks[0]);
                leftSide.add(blocks[2]);

                rightSide.add(blocks[1]);
                rightSide.add(blocks[3]);

                bottomSide.add(blocks[0]);
                bottomSide.add(blocks[2]);
                bottomSide.add(blocks[3]);
                break;
            case 1:
                /*
                 *   Z
                 * Z Z
                 * Z
                 * */
                blocks[0].setPos((byte) (posX + 1), (byte) (posY - 1));
                blocks[1].setPos(posX, posY);
                blocks[2].setPos((byte) (posX + 1), posY);
                blocks[3].setPos(posX, (byte) (posY + 1));

                leftSide.add(blocks[0]);
                leftSide.add(blocks[1]);
                leftSide.add(blocks[3]);

                rightSide.add(blocks[0]);
                rightSide.add(blocks[2]);
                rightSide.add(blocks[3]);

                bottomSide.add(blocks[3]);
                bottomSide.add(blocks[2]);
                break;
            case 2:
                /*
                 * Z Z
                 *   Z Z
                 * */
                blocks[0].setPos((byte) (posX - 1), posY);
                blocks[1].setPos(posX, posY);
                blocks[2].setPos(posX, (byte) (posY + 1));
                blocks[3].setPos((byte) (posX + 1), (byte) (posY + 1));

                leftSide.add(blocks[0]);
                leftSide.add(blocks[2]);

                rightSide.add(blocks[1]);
                rightSide.add(blocks[3]);

                bottomSide.add(blocks[0]);
                bottomSide.add(blocks[2]);
                bottomSide.add(blocks[3]);
                break;
            case 3:
                /*
                 *   Z
                 * Z Z
                 * Z
                 * */
                blocks[0].setPos(posX, (byte) (posY - 1));
                blocks[1].setPos((byte) (posX - 1), posY);
                blocks[2].setPos(posX, posY);
                blocks[3].setPos((byte) (posX - 1), (byte) (posY + 1));

                leftSide.add(blocks[0]);
                leftSide.add(blocks[1]);
                leftSide.add(blocks[3]);

                rightSide.add(blocks[0]);
                rightSide.add(blocks[2]);
                rightSide.add(blocks[3]);

                bottomSide.add(blocks[3]);
                bottomSide.add(blocks[2]);
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
