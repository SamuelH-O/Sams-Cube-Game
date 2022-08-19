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
public abstract class Piece {
    Paint paint;

    byte posX, posY, rotation;

    final Square[] squares = new Square[4];

    final ArrayList<Square> leftSide = new ArrayList<>(), rightSide = new ArrayList<>(), bottomSide = new ArrayList<>();

    Piece(float squareSize, final Resources resources) {
        this.paint = new Paint();
        for (byte i = 0; i < 4; i++) {
            squares[i] = new Square(squareSize, paint);
        }
    }

    @SuppressWarnings("CopyConstructorMissesField")
    Piece(Piece pieceToCopy) {}

    @NonNull
    @Override
    protected Piece clone() throws CloneNotSupportedException {
        return (Piece) super.clone();
    }

    void draw(final Canvas canvas) {
        for (Square i : squares) {
            i.draw(canvas);
        }
    }

    boolean canMoveLeft(final GridOfSurfaces grid) {
        boolean ret;
        if (posX - 1 >= 0) {
            ret = true;
            for (Square i : leftSide) {
                if (grid.isFilledAt((byte) (i.posX - 1), i.posY)) {
                    ret = false;
                }
            }
        } else {
            ret = false;
        }
        return ret;
    }

    boolean canMoveRight(final GridOfSurfaces grid) {
        boolean ret;
        if (posX + this.getWidth() < GridOfSurfaces.NB_COLUMNS) {
            ret = true;
            for (Square i : rightSide) {
                if (grid.isFilledAt((byte) (i.posX + 1), i.posY)) {
                    ret = false;
                }
            }
        } else {
            ret = false;
        }
        return ret;
    }

    boolean canMoveBottom(final GridOfSurfaces grid) {
        boolean ret;
        if (posX + this.getHeight() - 1 < GridOfSurfaces.NB_ROWS) {
            ret = true;
            for (Square i : bottomSide) {
                if (i.posY < 15) {
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
            for (Square j : bottomSide) {
                if (j.posY + offset < 15) {
                    if (grid.isFilledAt(j.posX, (byte) (j.posY + offset + 1))) {
                        break incrementLoop;
                    }
                } else {
                    break incrementLoop;
                }
            }
            offset = (byte) (offset + 1);
        }
        setPosAndRot(posX, (byte) (squares[0].posY + offset), rotation);
    }

    /*TODO: Make the piece stay centered during rotation*/
    abstract void figureOutNextRotation(final GridOfSurfaces grid);

    void applyRotation(final GridOfSurfaces grid, byte posX, byte posY, byte rotation) {
        boolean shouldGoUp;
        goUp: do {
            shouldGoUp = false;
            setPosAndRot(posX, posY, rotation);
            for (Square i : squares) {
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
            grid.setSquare(squares[i]);
        }
    }

    void setSquareSize(float squareSize) {
        for (Square i : squares) {
            i.setSize(squareSize);
        }
    }

    abstract byte getWidth();

    abstract byte getHeight();
}
