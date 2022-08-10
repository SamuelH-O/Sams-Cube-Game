package com.example.samscubegame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Piece {
    TetrominoTypes piece;

    byte posX, posY, rotation;

    Square[] squares;

    Piece(TetrominoTypes t, float squareSize, final Resources resources) {
        this.piece = t;

        squares = new Square[4];
        for (byte i = 0; i < 4; i++) {
            squares[i] = new Square(squareSize, t, resources);
        }
    }

    void draw(final Canvas canvas) {// TODO: make parameters optional (use current if not provided) (maybe, don't know if it's a good idea)
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I
                    for (byte i = 0; i < 4; i++) {
                        squares[i].draw((byte) (posX + i), posY, canvas);
                    }
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * */
                    for (byte i = 0; i < 4; i++) {
                        squares[i].draw(posX, (byte) (posY + i), canvas);
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
                        squares[1].draw((byte) (posX + 1), posY, canvas);
                        squares[2].draw((byte) (posX + 2), posY, canvas);
                        squares[3].draw((byte) (posX + 2), (byte) (posY + 1), canvas);
                        break;
                    case 1:
                        /*
                         *   J
                         *   J
                         * J J
                         * */
                        squares[0].draw((byte) (posX + 1), posY, canvas);
                        squares[1].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[2].draw(posX, (byte) (posY + 2), canvas);
                        squares[3].draw((byte) (posX + 1), (byte) (posY + 2), canvas);
                        break;
                    case 2:
                        /*
                         * J
                         * J J J
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX, (byte) (posY + 1), canvas);
                        squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[3].draw((byte) (posX + 2), (byte) (posY + 1), canvas);
                        break;
                    case 3:
                        /*
                         * J J
                         * J
                         * J
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw((byte) (posX + 1), posY, canvas);
                        squares[2].draw(posX, (byte) (posY + 1), canvas);
                        squares[3].draw(posX, (byte) (posY + 2), canvas);
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
                        squares[0].draw((byte) (posX + 2), posY, canvas);
                        squares[1].draw(posX, (byte) (posY + 1), canvas);
                        squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[3].draw((byte) (posX + 2), (byte) (posY + 1), canvas);
                        break;
                    case 1:
                        /*
                         * L
                         * L
                         * L L
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX, (byte) (posY + 1), canvas);
                        squares[2].draw(posX, (byte) (posY + 2), canvas);
                        squares[3].draw((byte) (posX + 1), (byte) (posY + 2), canvas);
                        break;
                    case 2:
                        /*
                         * L L L
                         * L
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw((byte) (posX + 1), posY, canvas);
                        squares[2].draw((byte) (posX + 2), posY, canvas);
                        squares[3].draw(posX, (byte) (posY + 1), canvas);
                        break;
                    case 3:
                        /*
                         * L L
                         *   L
                         *   L
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw((byte) (posX + 1), posY, canvas);
                        squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[3].draw((byte) (posX + 1), (byte) (posY + 2), canvas);
                        break;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                squares[0].draw(posX, posY, canvas);
                squares[1].draw((byte) (posX + 1), posY, canvas);
                squares[2].draw(posX, (byte) (posY + 1), canvas);
                squares[3].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                break;
            case S:
                if (rotation % 2 == 0) {
                    /*
                     *   S S
                     * S S
                     * */
                    squares[0].draw((byte) (posX + 1), posY, canvas);
                    squares[1].draw((byte) (posX + 2), posY, canvas);
                    squares[2].draw(posX, (byte) (posY + 1), canvas);
                    squares[3].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                } else {
                    /*
                     * S
                     * S S
                     *   S
                     * */
                    squares[0].draw(posX, posY, canvas);
                    squares[1].draw(posX, (byte) (posY + 1), canvas);
                    squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                    squares[3].draw((byte) (posX + 1), (byte) (posY + 2), canvas);
                }
                break;
            case T:
                switch (rotation) {
                    case 0:
                        /*
                         *   T
                         * T T T
                         * */
                        squares[0].draw((byte) (posX + 1), posY, canvas);
                        squares[1].draw(posX, (byte) (posY + 1), canvas);
                        squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[3].draw((byte) (posX + 2), (byte) (posY + 1), canvas);
                        break;
                    case 1:
                        /*
                         * T
                         * T T
                         * T
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw(posX, (byte) (posY + 1), canvas);
                        squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[3].draw(posX, (byte) (posY + 2), canvas);
                        break;
                    case 2:
                        /*
                         * T T T
                         *   T
                         * */
                        squares[0].draw(posX, posY, canvas);
                        squares[1].draw((byte) (posX + 1), posY, canvas);
                        squares[2].draw((byte) (posX + 2), posY, canvas);
                        squares[3].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        break;
                    case 3:
                        /*
                         *   T
                         * T T
                         *   T
                         * */
                        squares[0].draw((byte) (posX + 1), posY, canvas);
                        squares[1].draw(posX, (byte) (posY + 1), canvas);
                        squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                        squares[3].draw((byte) (posX + 1), (byte) (posY + 2), canvas);
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
                    squares[1].draw((byte) (posX + 1), posY, canvas);
                    squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                    squares[3].draw((byte) (posX + 2), (byte) (posY + 1), canvas);
                } else {
                    /*
                     *   Z
                     * Z Z
                     * Z
                     * */
                    squares[0].draw((byte) (posX + 1), posY, canvas);
                    squares[1].draw(posX, (byte) (posY + 1), canvas);
                    squares[2].draw((byte) (posX + 1), (byte) (posY + 1), canvas);
                    squares[3].draw(posX, (byte) (posY + 2), canvas);
                }
                break;
        }
    }

    // TODO: Figure out a way to check boundary with squares instead of switch+rotation
    boolean canMoveLeft(GridOfGame grid) {
        boolean ret = false;
        if (posX - 1 >= 0) {
            switch (piece) {
                case I:
                    if (rotation % 2 == 0) {
                        // I I I I
                        ret = grid.isFreeAt((byte) (posX - 1), posY);
                    } else {
                        /*
                         * I
                         * I
                         * I
                         * I
                         * */
                        ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 2))
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 3)));
                    }
                    break;
                case J:
                    switch (rotation) {
                        case 0:
                            /*
                             * J J J
                             *     J
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt((byte) (posX + 1), (byte) (posY + 1)));
                            break;
                        case 1:
                            /*
                             *   J
                             *   J
                             * J J
                             * */
                            ret = (grid.isFreeAt(posX, posY)
                                    && grid.isFreeAt(posX, (byte) (posY - 1))
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 2)));
                            break;
                        case 2:
                            /*
                             * J
                             * J J J
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1)));
                            break;
                        case 3:
                            /*
                             * J J
                             * J
                             * J
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 2)));
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
                            ret = (grid.isFreeAt((byte) (posX + 1), posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1)));
                            break;
                        case 1:
                            /*
                             * L
                             * L
                             * L L
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 2)));
                            break;
                        case 2:
                            /*
                             * L L L
                             * L
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1)));
                            break;
                        case 3:
                            /*
                             * L L
                             *   L
                             *   L
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt(posX, (byte) (posY + 1))
                                    && grid.isFreeAt(posX, (byte) (posY + 2)));
                            break;
                    }
                    break;
                case O:
                    /*
                     * O O
                     * O O
                     * */
                    ret = (grid.isFreeAt((byte) (posX - 1), posY)
                            && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1)));
                    break;
                case S:
                    if (rotation % 2 == 0) {
                        /*
                         *   S S
                         * S S
                         * */
                        ret = (grid.isFreeAt(posX, posY)
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1)));
                    } else {
                        /*
                         * S
                         * S S
                         *   S
                         * */
                        ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                && grid.isFreeAt(posX, (byte) (posY + 2)));
                    }
                    break;
                case T:
                    switch (rotation) {
                        case 0:
                            /*
                             *   T
                             * T T T
                             * */
                            ret = (grid.isFreeAt(posX, posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1)));
                            break;
                        case 1:
                            /*
                             * T
                             * T T
                             * T
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 2)));
                            break;
                        case 2:
                            /*
                             * T T T
                             *   T
                             * */
                            ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                    && grid.isFreeAt(posX, (byte) (posY + 1)));
                            break;
                        case 3:
                            /*
                             *   T
                             * T T
                             *   T
                             * */
                            ret = (grid.isFreeAt(posX, posY)
                                    && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                    && grid.isFreeAt(posX, (byte) (posY + 2)));
                            break;
                    }
                    break;
                case Z:
                    if (rotation % 2 == 0) {
                        /*
                         * Z Z
                         *   Z Z
                         * */
                        ret = (grid.isFreeAt((byte) (posX - 1), posY)
                                && grid.isFreeAt(posX, (byte) (posY + 1)));
                    } else {
                        /*
                         *   Z
                         * Z Z
                         * Z
                         * */
                        ret = (grid.isFreeAt(posX, posY)
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 1))
                                && grid.isFreeAt((byte) (posX - 1), (byte) (posY + 2)));
                    }
                    break;
            }
        }
        return ret;
    }

    byte getRowToSnapTo(GridOfGame grid) {
        byte highestPoint = 15;
        byte tmp;
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I
                    for (byte i = posX; i < posX + 4; i++) {
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
                     * */
                    highestPoint = (byte) (grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 3)) - 3);
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
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 2), (byte) (posY + 1)))) {
                            highestPoint = (byte) (tmp - 1);
                        }
                        break;
                    case 1:
                        /*
                         *   J
                         *   J
                         * J J
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        break;
                    case 2:
                        /*
                         * J
                         * J J J
                         * */
                        for (byte i = posX; i <= posX + 2; i++) {
                            if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, (byte) (posY + 1)))) {
                                highestPoint = (byte) (tmp - 1);
                            }
                        }
                        break;
                    case 3:
                        /*
                         * J J
                         * J
                         * J
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), posY))) {
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
                        for (byte i = posX; i <= posX + 2; i++) {
                            if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, (byte) (posY + 1)))) {
                                highestPoint = (byte) (tmp - 1);
                            }
                        }
                        break;
                    case 1:
                        /*
                         * L
                         * L
                         * L L
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        break;
                    case 2:
                        /*
                         * L L L
                         * L
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 1)))) {
                            highestPoint = (byte) (tmp - 1);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), posY))) {
                            highestPoint = tmp;
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), posY))) {
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
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        break;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 1)))) {
                    highestPoint = (byte) (tmp - 1);
                }
                if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 1)))) {
                    highestPoint = (byte) (tmp - 1);
                }
                break;
            case S:
                if (rotation % 2 == 0) {
                    /*
                     *   S S
                     * S S
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 1)))) {
                        highestPoint = (byte) (tmp - 1);
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 1)))) {
                        highestPoint = (byte) (tmp - 1);
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 2), posY))) {
                        highestPoint = tmp;
                    }
                } else {
                    /*
                     * S
                     * S S
                     *   S
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 1)))) {
                        highestPoint = (byte) (tmp - 1);
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 2)))) {
                        highestPoint = (byte) (tmp - 2);
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
                        for (byte i = posX; i <= posX + 2; i++) {
                            if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(i, (byte) (posY + 1)))) {
                                highestPoint = (byte) (tmp - 1);
                            }
                        }
                        break;
                    case 1:
                        /*
                         * T
                         * T T
                         * T
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 1)))) {
                            highestPoint = (byte) (tmp - 1);
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
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 1)))) {
                            highestPoint = (byte) (tmp - 1);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 2), posY))) {
                            highestPoint = tmp;
                        }
                        break;
                    case 3:
                        /*
                         *   T
                         * T T
                         *   T
                         * */
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 1)))) {
                            highestPoint = (byte) (tmp - 1);
                        }
                        if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 2)))) {
                            highestPoint = (byte) (tmp - 2);
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
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 1)))) {
                        highestPoint = (byte) (tmp - 1);
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 2), (byte) (posY + 1)))) {
                        highestPoint = (byte) (tmp - 1);
                    }
                } else {
                    /*
                     *   Z
                     * Z Z
                     * Z
                     * */
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos(posX, (byte) (posY + 2)))) {
                        highestPoint = (byte) (tmp - 2);
                    }
                    if (highestPoint >= (tmp = grid.getOneAboveBottomSquareFromPos((byte) (posX + 1), (byte) (posY + 1)))) {
                        highestPoint = (byte) (tmp - 1);
                    }
                }
                break;
        }
        Log.d("Highest Point", "" + highestPoint);
        return highestPoint;
    }

    void figureOutNextRotation() {
        byte[] values = {posX, posY, rotation};
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I
                    if (posY + 3 >= 16) {
                        values[1] = 16 - 4;
                    }
                    values[2] = 1;
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * */
                    if (posX + 3 >= 10) {
                        values[0] = 10 - 4;
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
                        values[2] = (byte) (values[2] + 1);
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
                        values[2] = (byte) (values[2] + 1);
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
                    values[2] = (byte) (values[2] + 1);
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
                    values[2] = (byte) (values[2] + 1);
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
        setPosAndRot(values[0], values[1], values[2]);
    }

    void setPosAndRot(byte posX, byte posY, byte rotation) {
        this.posX = posX;
        this.posY = posY;
        this.rotation = rotation;
    }

    void placeInGrid(GridOfGame grid) {
        for (byte i = 0; i < 4; i++) {
            grid.setSquare(squares[i]);
        }
    }

    byte getWidth() {
        byte width = 0;
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I
                    width = 4;
                } else {
                    /*
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

    byte getHeight() {
        byte height = 0;
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I
                    height = 1;
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * */
                    height = 4;
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
