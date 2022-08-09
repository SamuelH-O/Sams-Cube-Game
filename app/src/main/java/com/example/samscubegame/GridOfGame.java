package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.util.Log;

class GridOfGame {
    // First number = columns Second number = rows
    TetrominoTypes[][] grid = new TetrominoTypes[10][16];

    GridOfGame() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 16; j++) {
                grid[i][j] = TetrominoTypes.NULL;
            }
        }
    }

    void setSquare(int x, int y, TetrominoTypes t) {
        grid[x][y] = t;
    }

    int getOneAboveBottomSquareFromPos(int column, int posY) {
        int i = posY;
        while(i < 15 && grid[column][i] == TetrominoTypes.NULL) {
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
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 16; j++) {
                str.append("|").append(tetrominoTypeToString(grid[i][j]));
            }
            str.append("|\n").append("#####################\n");
        }
        Log.i(GameActivity.TAG, "State of the grid :\n" + str);
    }
}
