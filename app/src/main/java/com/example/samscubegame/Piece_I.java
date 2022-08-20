package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Piece_I extends Piece{

    Piece_I(float blockSize, final Resources resources, byte blockDesign) {
        super(blockSize, resources, blockDesign);
        this.paint.setColor(resources.getColor(R.color.I_color, null));
    }

    private Piece_I(Piece_I pieceToCopy) {
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
        return new Piece_I(this);
    }

    @Override
    void figureOutNextRotation(final GridOfSurfaces grid) {
        byte[] values = {posX, posY, rotation};

        switch (rotation) {
            case 0:
            case 2:
                // I I I I [0] | Next -> [1]
                // I I I I [2] | Next -> [3]
                if (posY - 3 <= 0) {
                    values[1] = (byte) (2);
                }
                if (posY + 3 >= grid.getNbRows()) {
                    values[1] = (byte) (grid.getNbRows() - 2);
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 1:
                /*
                 * I [1] | Next -> [2]
                 * I
                 * I
                 * I
                 * */
                if (posX - 3 <= 0) {
                    values[0] = (byte) (2);
                }
                if (posX + 3 >= grid.getNbColumns()) {
                    values[0] = (byte) (grid.getNbColumns() - 2);
                }
                values[2] = (byte) (values[2] + 1);
                break;
            case 3:
                /*
                 * I [3] | Next -> [0]
                 * I
                 * I
                 * I
                 * */
                if (posX - 2 <= 0) {
                    values[0] = (byte) (2);
                }
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
                // I I I I
                blocks[0].setPos((byte) (posX - 2), (byte) (posY - 1));
                blocks[1].setPos((byte) (posX - 1), (byte) (posY - 1));
                blocks[2].setPos(posX, (byte) (posY - 1));
                blocks[3].setPos((byte) (posX + 1), (byte) (posY - 1));

                leftSide.add(blocks[0]);

                rightSide.add(blocks[3]);

                bottomSide.addAll(Arrays.asList(blocks));
                break;
            case 1:
                /*
                 * I
                 * I
                 * I
                 * I
                 * */
                blocks[0].setPos(posX, (byte) (posY - 2));
                blocks[1].setPos(posX, (byte) (posY - 1));
                blocks[2].setPos(posX, posY);
                blocks[3].setPos(posX, (byte) (posY + 1));

                leftSide.addAll(Arrays.asList(blocks));

                rightSide.addAll(Arrays.asList(blocks));

                bottomSide.add(blocks[3]);
                break;
            case 2:
                // I I I I
                blocks[0].setPos((byte) (posX - 2), posY);
                blocks[1].setPos((byte) (posX - 1), posY);
                blocks[2].setPos(posX, posY);
                blocks[3].setPos((byte) (posX + 1), posY);

                leftSide.add(blocks[0]);

                rightSide.add(blocks[3]);

                bottomSide.addAll(Arrays.asList(blocks));
                break;
            case 3:
                /*
                 * I [3] | Next -> [0]
                 * I
                 * I
                 * I
                 * */
                blocks[0].setPos((byte) (posX - 1), (byte) (posY - 2));
                blocks[1].setPos((byte) (posX - 1), (byte) (posY - 1));
                blocks[2].setPos((byte) (posX - 1), posY);
                blocks[3].setPos((byte) (posX - 1), (byte) (posY + 1));

                leftSide.addAll(Arrays.asList(blocks));

                rightSide.addAll(Arrays.asList(blocks));

                bottomSide.add(blocks[3]);
                break;
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
