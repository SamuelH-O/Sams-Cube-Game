package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

class GridOfGame {
    // First number = columns | Second number = rows
    private final Square[][] grid = new Square[10][16];

    GridOfGame(float size, final Resources resources) {
        for(byte i = 0; i < 10; i++) {
            for(byte j = 0; j < 16; j++) {
                grid[i][j] = new Square(size, TetrominoTypes.NULL, resources);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void draw(final Canvas canvas) {
        for(byte i = 0; i < 10; i++) {
            for (byte j = 0; j < 16; j++) {
                if (grid[i][j].type != TetrominoTypes.NULL) {
                    grid[i][j].draw(grid[i][j].posX, grid[i][j].posY, canvas);
                }
            }
        }
    }

    boolean isFreeAt(byte posX, byte posY) {
        Log.d("Type", "" + posX + " " + posY + " = " + grid[posX][posY].type);
        return (grid[posX][posY].type == TetrominoTypes.NULL);
    }

    void setSquare(Square square) {
        grid[square.posX][square.posY] = square;
    }

    byte getOneAboveBottomSquareFromPos(byte column, byte posY) {
        byte i = posY;
        while(i < 15 && grid[column][i].type == TetrominoTypes.NULL) {
            i++;
        }
        return i;
    }

    private char tetrominoTypeToString(TetrominoTypes t) {
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
        for(byte i = 0; i < 10; i++) {
            for(byte j = 0; j < 16; j++) {
                str.append("|").append(tetrominoTypeToString(grid[i][j].type));
            }
            str.append("|\n").append("#####################\n");
        }
        Log.i(GameActivity.TAG, "State of the grid :\n" + str);
    }
}
