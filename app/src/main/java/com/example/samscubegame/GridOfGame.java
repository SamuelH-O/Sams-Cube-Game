package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
class GridOfGame {
    // First number = columns | Second number = rows
    private final Square[][] grid = new Square[10][16];

    GridOfGame() {
        for(byte i = 0; i < 10; i++) {
            for(byte j = 0; j < 16; j++) {
                grid[i][j] = null;
            }
        }
    }

    void draw(final Canvas canvas) {
        for(byte i = 0; i < 10; i++) {
            for (byte j = 0; j < 16; j++) {
                if (grid[i][j] != null) {
                    grid[i][j].draw(canvas);
                }
            }
        }
    }

    boolean isFilledAt(byte posX, byte posY) {
        return (grid[posX][posY] != null);
    }

    void setSquare(Square square) {
        grid[square.posX][square.posY] = square;
    }

    byte getFilledSquareBelow(byte posX, byte posY) {
        for (byte i = posY; i < 16; i++) {
            if (grid[posX][i] != null) {
                return i;
            }
        }
        return (byte) (16);
    }

    @SuppressLint("InlinedApi")
    void printGridState() {
        StringBuilder str = new StringBuilder();
        for(byte i = 0; i < 16; i++) {
            for(byte j = 0; j < 10; j++) {
                if (grid[j][i] != null) {
                    str.append("|").append(grid[j][i].toString());
                } else {
                    str.append("|").append(" ");
                }
            }
            str.append("|\n").append("#####################\n");
        }
        Log.d("Grid", "State of the grid :\n" + str);
    }
}
