package com.example.samscubegame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Piece {
    float squareSize;

    TetrominoTypes piece;

    int posX, posY, rotation;

    Square[] squares;

    Piece(TetrominoTypes t, final Canvas canvas, final Resources resources) {
        this.piece = t;

        // Get the size of the squares by the smaller side of the canvas
        if (canvas.getWidth() < canvas.getHeight()) this.squareSize = (float) canvas.getWidth() / 10;
        else this.squareSize = (float) canvas.getHeight() / 10;

        squares = new Square[4];
        for (int i = 0; i < 4; i++) {
            squares[i] = new Square(squareSize, t, resources);
        }
    }

    void placeInGrid(GridOfGame grid) {
        for (int i = 0; i < 4; i++) {
            squares[i].setInGrid(grid);
        }
    }

    void draw(int posX, int posY, int rotation, final Canvas canvas) {// TODO: make parameters optional (use current if not provided) (maybe, don't know if it's a good idea)
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I I
                    for (int i = 0; i < 4; i++) {
                        squares[i].draw(posX + i, posY, canvas);
                    }
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * I
                     * */
                    for (int i = 0; i < 4; i++) {
                        squares[i].draw(posX, posY + i, canvas);
                    }
                }
                break;
            case J:
                switch (rotation) {
                    case 0:
                        /*
                         * J J J
                         *     J
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX + 1, posY, canvas);
                        squares[2].draw(posX + 2, posY, canvas);
                        squares[3].draw(posX + 2, posY + 1, canvas);
                        break;
                    case 1:
                        /*
                         *   J
                         *   J
                         * J J
                         * */
                        squares[0].draw(posX + 1, posY, canvas);
                        squares[1].draw(posX + 1, posY + 1, canvas);
                        squares[2].draw(posX, posY + 2, canvas);
                        squares[3].draw(posX + 1, posY + 2, canvas);
                        break;
                    case 2:
                        /*
                         * J
                         * J J J
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX, posY + 1, canvas);
                        squares[2].draw(posX + 1, posY + 1, canvas);
                        squares[3].draw(posX + 2, posY + 1, canvas);
                        break;
                    case 3:
                        /*
                         * J J
                         * J
                         * J
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX + 1, posY, canvas);
                        squares[2].draw(posX, posY + 1, canvas);
                        squares[3].draw(posX, posY + 2, canvas);
                        break;
                }
                break;
            case L:
                switch (rotation) {
                    case 0:
                        /*
                         *     L
                         * L L L
                         * */
                        squares[0].draw(posX + 2, posY, canvas);
                        squares[1].draw(posX, posY + 1, canvas);
                        squares[2].draw(posX + 1, posY + 1, canvas);
                        squares[3].draw(posX + 2, posY + 1, canvas);
                        break;
                    case 1:
                        /*
                         * L
                         * L
                         * L L
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX, posY + 1, canvas);
                        squares[2].draw(posX, posY + 2, canvas);
                        squares[3].draw(posX + 1, posY + 2, canvas);
                        break;
                    case 2:
                        /*
                         * L L L
                         * L
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX + 1, posY, canvas);
                        squares[2].draw(posX + 2, posY, canvas);
                        squares[3].draw(posX, posY + 1, canvas);
                        break;
                    case 3:
                        /*
                         * L L
                         *   L
                         *   L
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX + 1, posY, canvas);
                        squares[2].draw(posX + 1, posY + 1, canvas);
                        squares[3].draw(posX + 1, posY + 2, canvas);
                        break;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                squares[0].draw(posX, posY, canvas);
                squares[1].draw(posX + 1, posY, canvas);
                squares[2].draw(posX, posY + 1, canvas);
                squares[3].draw(posX + 1, posY + 1, canvas);
                break;
            case S:
                if (rotation % 2 == 0) {
                    /*
                     *   S S
                     * S S
                     * */
                    squares[0].draw(posX + 1, posY, canvas);
                    squares[1].draw(posX + 2, posY, canvas);
                    squares[2].draw(posX, posY + 1, canvas);
                    squares[3].draw(posX + 1, posY + 1, canvas);
                } else {
                    /*
                     * S
                     * S S
                     *   S
                     * */
                    squares[0].draw(posX, posY, canvas);
                    squares[1].draw(posX, posY + 1, canvas);
                    squares[2].draw(posX + 1, posY + 1, canvas);
                    squares[3].draw(posX + 1, posY + 2, canvas);
                }
                break;
            case T:
                switch (rotation) {
                    case 0:
                        /*
                         *   T
                         * T T T
                         * */
                        squares[0].draw(posX + 1, posY, canvas);
                        squares[1].draw(posX, posY + 1, canvas);
                        squares[2].draw(posX + 1, posY + 1, canvas);
                        squares[3].draw(posX + 2, posY + 1, canvas);
                        break;
                    case 1:
                        /*
                         * T
                         * T T
                         * T
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX, posY + 1, canvas);
                        squares[2].draw(posX + 1, posY + 1, canvas);
                        squares[3].draw(posX, posY + 2, canvas);
                        break;
                    case 2:
                        /*
                         * T T T
                         *   T
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX + 1, posY, canvas);
                        squares[2].draw(posX + 2, posY, canvas);
                        squares[3].draw(posX + 1, posY + 1, canvas);
                        break;
                    case 3:
                        /*
                         *   T
                         * T T
                         *   T
                         * */
                        squares[0].draw(posX + 1, posY, canvas);
                        squares[1].draw(posX, posY + 1, canvas);
                        squares[2].draw(posX + 1, posY + 1, canvas);
                        squares[3].draw(posX + 1, posY + 2, canvas);
                        break;
                }
                break;
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     * Z Z
                     *   Z Z
                     * */
                    squares[0].draw(posX, posY, canvas);
                    squares[1].draw(posX + 1, posY, canvas);
                    squares[2].draw(posX + 1, posY + 1, canvas);
                    squares[3].draw(posX + 2, posY + 1, canvas);
                } else {
                    /*
                     *   Z
                     * Z Z
                     * Z
                     * */
                    squares[0].draw(posX + 1, posY, canvas);
                    squares[1].draw(posX, posY + 1, canvas);
                    squares[2].draw(posX + 1, posY + 1, canvas);
                    squares[3].draw(posX, posY + 2, canvas);
                }
                break;
        }
        this.posX = posX;
        this.posY = posY;
        this.rotation = rotation;
    }

    int getRowToSnapTo(GridOfGame grid) {// TODO: change return type to unsigned short
        int highestPoint = 15;
        int tmp;
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I I
                    for (int i = posX; i <= posX + 4; i++) {
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, posY))) {
                            highestPoint = tmp;
                        }
                    }
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * I
                     * */
                    highestPoint = grid.getOneAboveBottomSquareFromPos(posX, posY + 4) - 5;
                }
                break;
            case J:
                switch (rotation) {// TODO: test
                    case 0:
                        /*
                         * J J J
                         *     J
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 2, posY + 1))) {
                            highestPoint = tmp - 1;
                        }
                        break;
                    case 1:
                        /*
                         *   J
                         *   J
                         * J J
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        break;
                    case 2:
                        /*
                         * J
                         * J J J
                         * */
                        for (int i = posX; i <= posX + 2; i++) {
                            if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, posY + 1))) {
                                highestPoint = tmp - 1;
                            }
                        }
                        break;
                    case 3:
                        /*
                         * J J
                         * J
                         * J
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY))) {
                            highestPoint = tmp;
                        }
                        break;
                }
                break;
            case L:
                switch (rotation) {
                    case 0:
                        /*
                         *     L
                         * L L L
                         * */
                        for (int i = posX; i <= posX + 2; i++) {
                            if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, posY + 1))) {
                                highestPoint = tmp - 1;
                            }
                        }
                        break;
                    case 1:
                        /*
                         * L
                         * L
                         * L L
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        break;
                    case 2:
                        /*
                         * L L L
                         * L
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 1))) {
                            highestPoint = tmp - 1;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY))) {
                            highestPoint = tmp;
                        }
                        break;
                    case 3:
                        /*
                         * L L
                         *   L
                         *   L
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        break;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 1))) {
                    highestPoint = tmp - 1;
                }
                if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 1))) {
                    highestPoint = tmp - 1;
                }
                break;
            case S:
                if (rotation % 2 == 0) {
                    /*
                     *   S S
                     * S S
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 1))) {
                        highestPoint = tmp - 1;
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 1))) {
                        highestPoint = tmp - 1;
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 2, posY))) {
                        highestPoint = tmp;
                    }
                } else {
                    /*
                     * S
                     * S S
                     *   S
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 1))) {
                        highestPoint = tmp - 1;
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 2))) {
                        highestPoint = tmp - 2;
                    }
                }
                break;
            case T:
                switch (rotation) {
                    case 0:
                        /*
                         *   T
                         * T T T
                         * */
                        for (int i = posX; i <= posX + 2; i++) {
                            if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, posY + 1))) {
                                highestPoint = tmp - 1;
                            }
                        }
                        break;
                    case 1:
                        /*
                         * T
                         * T T
                         * T
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 1))) {
                            highestPoint = tmp - 1;
                        }
                        break;
                    case 2:
                        /*
                         * T T T
                         *   T
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 1))) {
                            highestPoint = tmp - 1;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 2, posY))) {
                            highestPoint = tmp;
                        }
                        break;
                    case 3:
                        /*
                         *   T
                         * T T
                         *   T
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 1))) {
                            highestPoint = tmp - 1;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 2))) {
                            highestPoint = tmp - 2;
                        }
                        break;
                }
                break;
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     * Z Z
                     *   Z Z
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY))) {
                        highestPoint = tmp;
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 1))) {
                        highestPoint = tmp - 1;
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 2, posY + 1))) {
                        highestPoint = tmp - 1;
                    }
                } else {
                    /*
                     *   Z
                     * Z Z
                     * Z
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, posY + 2))) {
                        highestPoint = tmp - 2;
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX + 1, posY + 1))) {
                        highestPoint = tmp - 1;
                    }
                }
                break;
        }
        return highestPoint;
    }

    void drawNextRotation(final Canvas canvas) {
        int[] values = {posX, posY, rotation};
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I I
                    if (posY + 4 >= 16) {
                        values[1] = 16 - 5;
                    }
                    values[2] = 1;
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * I
                     * */
                    if (posX + 4 >= 10) {
                        values[0] = 10 - 5;
                    }
                    values[2] = 0;
                }
                break;
            case J:
            case L:
            case T:
                switch (rotation) {
                    case 0:
                    case 2:
                        /*
                         * J J J
                         *     J
                         or
                         * J
                         * J J J
                         or
                         *     L
                         * L L L
                         or
                         * L L L
                         * L
                         or
                         *   T
                         * T T T
                         or
                         * T T T
                         *   T
                         * */
                        if (posY + 2 >= 16) {
                            values[1] = 16 - 3;
                        }
                        values[2] = values[2] + 1;
                        break;
                    case 1:
                        /*
                         *   J
                         *   J
                         * J J
                         or
                         * L
                         * L
                         * L L
                         or
                         * T
                         * T T
                         * T
                         * */
                        if (posX + 2 >= 10) {
                            values[0] = 10 - 3;
                        }
                        values[2] = values[2] + 1;
                        break;
                    case 3:
                        /*
                         * J J
                         * J
                         * J
                         or
                         * L L
                         *   L
                         *   L
                         or
                         *   T
                         * T T
                         *   T
                         * */
                        if (posX + 2 >= 10) {
                            values[0] = 10 - 3;
                        }
                        values[2] = 0;
                        break;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                if (rotation >= 3) {
                    values[2] = 0;
                } else {
                    values[2] = values[2] + 1;
                }
                break;
            case S:
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     *   S S
                     * S S
                     or
                     * Z Z
                     *   Z Z
                     * */
                    if (posY + 2 >= 16) {
                        values[1] = 16 - 3;
                    }
                    values[2] = values[2] + 1;
                } else {
                    /*
                     * S
                     * S S
                     *   S
                     or
                     *   Z
                     * Z Z
                     * Z
                     * */
                    if (posX + 2 >= 10) {
                        values[0] = 10 - 3;
                    }
                    values[2] = 0;
                }
                break;
        }
        draw(values[0], values[1], values[2], canvas);
    }

    int getWidth() {
        int width = 0;
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I I
                    width = 5;
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * I
                     * */
                    width = 1;
                }
                break;
            case J:
            case L:
            case T:
            case S:
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     * J
                     * J J J
                     or
                     * J J J
                     *     J
                     or
                     * L
                     * L L L
                     or
                     * L L L
                     * L
                     or
                     *   T
                     * T T T
                     or
                     * T T T
                     *   T
                     or
                     *   S S
                     * S S
                     or
                     * Z Z
                     *   Z Z
                     * */
                    width = 3;
                } else {
                    /*
                     *   J
                     *   J
                     * J J
                     or
                     * J J
                     * J
                     * J
                     or
                     * L
                     * L
                     * L L
                     or
                     * L L
                     *   L
                     *   L
                     or
                     * T
                     * T T
                     * T
                     or
                     *   T
                     * T T
                     *   T
                     or
                     * S
                     * S S
                     *   S
                     or
                     *   Z
                     * Z Z
                     * Z
                     * */
                    width = 2;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                width = 2;
                break;
        }
        return width;
    }

    int getHeight() {
        int height = 0;
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I I
                    height = 1;
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * I
                     * */
                    height = 5;
                }
                break;
            case J:
            case L:
            case T:
            case S:
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     * J
                     * J J J
                     or
                     * J J J
                     *     J
                     or
                     * L
                     * L L L
                     or
                     * L L L
                     * L
                     or
                     *   T
                     * T T T
                     or
                     * T T T
                     *   T
                     or
                     *   S S
                     * S S
                     or
                     * Z Z
                     *   Z Z
                     * */
                    height = 2;
                } else {
                    /*
                     *   J
                     *   J
                     * J J
                     or
                     * J J
                     * J
                     * J
                     or
                     * L
                     * L
                     * L L
                     or
                     * L L
                     *   L
                     *   L
                     or
                     * T
                     * T T
                     * T
                     or
                     *   T
                     * T T
                     *   T
                     or
                     * S
                     * S S
                     *   S
                     or
                     *   Z
                     * Z Z
                     * Z
                     * */
                    height = 3;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                height = 2;
                break;
        }
        return height;
    }
}
