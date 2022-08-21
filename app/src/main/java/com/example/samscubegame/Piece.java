package com.example.samscubegame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.S)
abstract class Piece {
    Paint paint;

    byte posX, posY, rotation;

    final Block[] blocks = new Block[4];

    final ArrayList<Block> leftSide = new ArrayList<>(), rightSide = new ArrayList<>(), bottomSide = new ArrayList<>();

    byte blockDesign;

    Piece(float blockSize, final Resources resources, byte blockDesign) {
        this.paint = new Paint();
        this.blockDesign = blockDesign;
        if (blockDesign == 0) {
            for (byte i = 0; i < 4; i++) {
                this.blocks[i] = new BlockColor(blockSize, paint);
            }
        } else if (blockDesign == 1) {
            for (byte i = 0; i < 4; i++) {
                this.blocks[i] = new BlockMono(blockSize, paint);
            }
        }
    }

    @SuppressWarnings("CopyConstructorMissesField")
    Piece(Piece pieceToCopy) {
    }

    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return (Piece) super.clone();
    }

    void draw(final Canvas canvas) {
        for (Block i : blocks) {
            i.draw(canvas);
        }
    }

    boolean canMoveLeft(final GridOfSurfaces grid) {
        boolean ret = true;
        for (Block i : leftSide) {
            if (i.posX - 1 >= 0) {
                if (grid.isFilledAt((byte) (i.posX - 1), i.posY)) {
                    ret = false;
                }
            } else {
                ret = false;
            }
        }
        return ret;
    }

    boolean canMoveRight(final GridOfSurfaces grid) {
        boolean ret = true;
        for (Block i : rightSide) {
            if (i.posX + 1 < grid.getNbColumns()) {
                if (grid.isFilledAt((byte) (i.posX + 1), i.posY)) {
                    ret = false;
                }
            } else {
                ret = false;
            }
        }
        return ret;
    }

    boolean canMoveBottom(final GridOfSurfaces grid) {
        boolean ret;
        if (posY + this.getHeight() / 2 < grid.getNbRows()) {// Bug since commit 55b727dadc640d02751b1d8900f28b5ce8e22f59
            ret = true;
            for (Block i : bottomSide) {
                if (i.posY + 1 < grid.getNbRows()) {
                    if (grid.isFilledAt(i.posX, (byte) (i.posY + 1))) {
                        ret = false;
                    }
                } else {
                    ret = false;
                }
            }
        } else {
            ret = false;
        }
        return ret;
    }

    void drop(final GridOfSurfaces grid) {
        byte offset = 0;
        incrementLoop: while (true) {
            for (Block j : bottomSide) {
                if (j.posY + offset < grid.getNbRows() - 1) {
                    if (grid.isFilledAt(j.posX, (byte) (j.posY + offset + 1))) {
                        break incrementLoop;
                    }
                } else {
                    break incrementLoop;
                }
            }
            offset = (byte) (offset + 1);
        }
        setPosAndRot(posX, (byte) (posY + offset), rotation);
    }

    /*TODO: Make the piece stay centered during rotation*/
    abstract void figureOutNextRotation(final GridOfSurfaces grid);

    void applyRotation(final GridOfSurfaces grid, byte posX, byte posY, byte rotation) {
        boolean shouldGoUp;
        goUp: do {
            shouldGoUp = false;
            setPosAndRot(posX, posY, rotation);
            for (Block i : blocks) {
                if (grid.isFilledAt(i.posX, i.posY)) {
                    shouldGoUp = true;
                    if (posY - 1 >= 0) {
                        posY = (byte) (posY - 1);
                    } else {
                        Log.e("End error", "A piece should go up but cant because it would go outside the canvas, would normally game over but there it's not implemented yet");
                        break goUp;
                    }
                }
            }
        } while (shouldGoUp);
    }

    abstract void setPosAndRot(byte posX, byte posY, byte rotation);

    void addToWall(GridOfSurfaces grid) {
        for (byte i = 0; i < 4; i++) {
            grid.setBlock(blocks[i]);
        }
    }

    void setBlockSize(float blockSize) {
        for (Block i : blocks) {
            i.setSize(blockSize);
        }
    }

    abstract byte getWidth();

    abstract byte getHeight();
}
