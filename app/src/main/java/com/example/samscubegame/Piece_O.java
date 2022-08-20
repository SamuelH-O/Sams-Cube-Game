package com.example.samscubegame;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Piece_O extends Piece {

    Piece_O(float blockSize, final Resources resources, byte blockDesign) {
        super(blockSize, resources, blockDesign);
        this.paint.setColor(resources.getColor(R.color.O_color, null));
    }

    private Piece_O(Piece_O pieceToCopy) {
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
        blocks[0].setPos((byte) (posX - 1), (byte) (posY - 1));
        blocks[1].setPos(posX, (byte) (posY - 1));
        blocks[2].setPos((byte) (posX - 1), posY);
        blocks[3].setPos(posX, posY);

        leftSide.add(blocks[0]);
        leftSide.add(blocks[2]);

        rightSide.add(blocks[1]);
        rightSide.add(blocks[3]);

        bottomSide.add(blocks[2]);
        bottomSide.add(blocks[3]);
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
