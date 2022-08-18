package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.HashSet;

@RequiresApi(api = Build.VERSION_CODES.S)
class GridOfGame {
    // First number = columns | Second number = rows
    private final Square[][] grid = new Square[GameActivity.NB_COLUMNS][GameActivity.NB_ROWS];

    GridOfGame() {
        for(byte i = 0; i < GameActivity.NB_COLUMNS; i++) {
            for(byte j = 0; j < GameActivity.NB_ROWS; j++) {
                grid[i][j] = null;
            }
        }
    }

    void draw(final Canvas canvas) {
        for (byte i = 0; i < GameActivity.NB_COLUMNS; i++) {
            for (byte j = 0; j < GameActivity.NB_ROWS; j++) {
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

    void checkForLines() {
        byte nbFilledSquares;
        HashSet<Byte> rowsToRemove = new HashSet<>();
        for (byte i = 0; i < GameActivity.NB_ROWS; i++) {
            nbFilledSquares = 0;
            for(byte j = 0; j < GameActivity.NB_COLUMNS; j++) {
                if (grid[j][i] == null) {
                    break;
                } else {
                    nbFilledSquares = (byte) (nbFilledSquares + 1);
                }
            }
            if (nbFilledSquares == GameActivity.NB_COLUMNS) {
                rowsToRemove.add(i);
            }
        }
        if (!rowsToRemove.isEmpty()) {
            removeRows(rowsToRemove);
        }
    }

    private void removeRows(HashSet<Byte> rowsToRemove) {
        byte nbRowsToDrop = 0;
        for (byte i = GameActivity.NB_ROWS - 1; i >= 0; i--) {
            for (byte j = 0; j < GameActivity.NB_COLUMNS; j++) {
                if (rowsToRemove.contains(i)) {
                    grid[j][i] = null;
                    if (j == 0) {
                        nbRowsToDrop = (byte) (nbRowsToDrop + 1);
                    }
                } else if (nbRowsToDrop > 0) {
                    if (grid[j][i] != null) {
                        Square tmp = grid[j][i];
                        tmp.setPos(j, (byte) (i + nbRowsToDrop));
                        setSquare(tmp);
                    }
                    grid[j][i] = null;
                }
            }
        }
    }

    @SuppressLint("InlinedApi")
    void printGridState() {
        StringBuilder str = new StringBuilder();
        for(byte i = 0; i < GameActivity.NB_ROWS; i++) {
            for(byte j = 0; j < GameActivity.NB_COLUMNS; j++) {
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
