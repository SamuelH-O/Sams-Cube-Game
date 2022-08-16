package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
class GridOfGame {
    // First number = columns | Second number = rows
    private final Square[][] grid = new Square[10][16];

    GridOfGame(float size, final Resources resources) {
        for(byte i = 0; i < 10; i++) {
            for(byte j = 0; j < 16; j++) {
                grid[i][j] = new Square(size, PieceTypes.NULL, resources);
            }
        }
    }

    void draw(final Canvas canvas) {
        for(byte i = 0; i < 10; i++) {
            for (byte j = 0; j < 16; j++) {
                if (grid[i][j].type != PieceTypes.NULL) {
                    grid[i][j].draw(canvas);
                }
            }
        }
    }

    boolean isFilledAt(byte posX, byte posY) {
        return (grid[posX][posY].type != PieceTypes.NULL);
    }

    void setSquare(Square square) {
        grid[square.posX][square.posY] = square;
    }

    byte getFilledSquareBelow(byte posX, byte posY) {
        for (byte i = posY; i < 16; i++) {
            if (grid[posX][i].type != PieceTypes.NULL) {
                return i;
            }
        }
        return (byte) (16);
    }

    private char tetrominoTypeToString(PieceTypes t) {
        switch(t) {
            case I:
                return 'I';
            case J:
                return 'J';
            case L:
                return 'L';
            case O:
                return 'O';
            case S:
                return 'S';
            case T:
                return 'T';
            case Z:
                return 'Z';
            case NULL:
                return ' ';
        }
        return ' ';
    }

    @SuppressLint("InlinedApi")
    void printGridState() {
        StringBuilder str = new StringBuilder();
        for(byte i = 0; i < 16; i++) {
            for(byte j = 0; j < 10; j++) {
                str.append("|").append(tetrominoTypeToString(grid[j][i].type));
            }
            str.append("|\n").append("#####################\n");
        }
        Log.d("Grid", "State of the grid :\n" + str);
    }
}
