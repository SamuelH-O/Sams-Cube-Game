package com.example.samscubegame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Piece {
    PieceTypes piece;

    byte posX, posY, rotation;

    private final Square[] squares;

    final private ArrayList<Square> leftSide = new ArrayList<>(), rightSide = new ArrayList<>(), bottomSide = new ArrayList<>();

    Piece(PieceTypes t, float squareSize, final Resources resources) {
        this.piece = t;

        squares = new Square[4];
        for (byte i = 0; i < 4; i++) {
            squares[i] = new Square(squareSize, t, resources);
        }
    }
    
    void draw(final Canvas canvas) {
        for (Square i : squares) {
            i.draw(canvas);
        }
    }
    
    boolean canMoveLeft(GridOfGame grid) {
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

    boolean canMoveRight(GridOfGame grid) {
        boolean ret;
        if (posX + this.getWidth() < 10) {
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

    boolean canMoveBottom(GridOfGame grid) {
        boolean ret;
        if (posX + this.getHeight() < 16) {
            ret = true;
            for (Square i : bottomSide) {
                if (grid.isFilledAt(i.posX, (byte) (i.posY + 1))) {
                    ret = false;
                }
            }
        } else {
            ret = false;
        }
        return ret;
    }

    void drop(GridOfGame grid) {
        byte posYOfPieceDropped = 16;
        byte squareAbove, bestSquareAbove = 15;
        for (Square i : bottomSide) {
            if (bestSquareAbove >= (squareAbove = (byte) (grid.getFilledSquareBelow(i.posX, i.posY) - 1))) {
                bestSquareAbove = squareAbove;
                posYOfPieceDropped = (byte) (squareAbove - (i.posY - squares[0].posY));
            }
        }
        setPosAndRot(posX, posYOfPieceDropped, rotation);
    }

    void figureOutNextRotation(final GridOfGame grid) {// TODO: Make the piece stay centered during rotation
        byte[] values = {posX, posY, rotation};
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I [0] | Next -> [1]
                    if (posY + 3 >= 16) {
                        values[1] = 16 - 4;
                    }
                    values[2] = 1;
                } else {
                    /*
                     * I [1] | Next -> [0]
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
                         * J J J [0] | Next -> [1]
                         *     J
                         or
                         * J    [2] | Next -> [3]
                         * J J J
                         or
                         *     L [0] | Next -> [1]
                         * L L L
                         or
                         * L L L [2] | Next -> [3]
                         * L
                         or
                         *   T   [0] | Next -> [1]
                         * T T T
                         or
                         * T T T [2] | Next -> [3]
                         *   T
                         * */
                        if (posY + 2 >= 16) {
                            values[1] = 16 - 3;
                        }
                        values[2] = (byte) (values[2] + 1);
                        break;
                    case 1:
                        /*
                         *   J [1] | Next -> [2]
                         *   J
                         * J J
                         or
                         * L   [1] | Next -> [2]
                         * L
                         * L L
                         or
                         * T   [1] | Next -> [2]
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
                         * J J [3] | Next -> [0]
                         * J
                         * J
                         or
                         * L L [3] | Next -> [0]
                         *   L
                         *   L
                         or
                         *   T [3] | Next -> [0]
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
                }
                break;
            case S:
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     *   S S [0] | Next -> [1]
                     * S S
                     or
                     * Z Z   [0] | Next -> [1]
                     *   Z Z
                     * */
                    if (posY + 2 >= 16) {
                        values[1] = 16 - 3;
                    }
                    values[2] = (byte) (values[2] + 1);
                } else {
                    /*
                     * S   [1] | Next -> [0]
                     * S S
                     *   S
                     or
                     *   Z [1] | Next -> [0]
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
        boolean shouldGoUp;
        goUp: do {
            shouldGoUp = false;
            setPosAndRot(values[0], values[1], values[2]);
            for (Square i : squares) {
                if (grid.isFilledAt(i.posX, i.posY)) {
                    shouldGoUp = true;
                    if (values[1] - 1 >= 0) {
                        values[1] = (byte) (values[1] - 1);
                    } else {
                        Log.e("End error", "A piece should go up but cant because it would go outside the canvas, would normally game over but there it's not implemented yet");
                        break goUp;
                    }
                }
            }
        } while (shouldGoUp);
    }

    void setPosAndRot(byte posX, byte posY, byte rotation) {
        this.posX = posX;
        this.posY = posY;
        this.rotation = rotation;
        leftSide.clear();
        rightSide.clear();
        bottomSide.clear();
        switch (piece) {
            case I:
                if (rotation % 2 == 0) {
                    // I I I I
                    for (byte i = 0; i < 4; i++) {
                        squares[i].setPos((byte) (posX + i), posY);
                    }
                    
                    leftSide.add(squares[0]);
                    
                    rightSide.add(squares[3]);

                    bottomSide.addAll(Arrays.asList(squares));
                } else {
                    /*
                     * I
                     * I
                     * I
                     * I
                     * */
                    for (byte i = 0; i < 4; i++) {
                        squares[i].setPos(posX, (byte) (posY + i));
                    }
                    
                    leftSide.addAll(Arrays.asList(squares));
                    
                    rightSide.addAll(Arrays.asList(squares));

                    bottomSide.add(squares[3]);
                }
                break;
            case J:
                switch (rotation) {
                    case 0:
                        /*
                         * J J J
                         *     J
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos((byte) (posX + 1), posY);
                        squares[2].setPos((byte) (posX + 2), posY);
                        squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[3]);
                        
                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[0]);
                        bottomSide.add(squares[1]);
                        bottomSide.add(squares[3]);
                        break;
                    case 1:
                        /*
                         *   J
                         *   J
                         * J J
                         * */
                        squares[0].setPos((byte) (posX + 1), posY);
                        squares[1].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[2].setPos(posX, (byte) (posY + 2));
                        squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);
                        leftSide.add(squares[2]);
                        
                        rightSide.add(squares[0]);
                        rightSide.add(squares[1]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[2]);
                        bottomSide.add(squares[3]);
                        break;
                    case 2:
                        /*
                         * J
                         * J J J
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos(posX, (byte) (posY + 1));
                        squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);
                        
                        rightSide.add(squares[0]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[1]);
                        bottomSide.add(squares[2]);
                        bottomSide.add(squares[3]);
                        break;
                    case 3:
                        /*
                         * J J
                         * J
                         * J
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos((byte) (posX + 1), posY);
                        squares[2].setPos(posX, (byte) (posY + 1));
                        squares[3].setPos(posX, (byte) (posY + 2));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[2]);
                        leftSide.add(squares[3]);

                        rightSide.add(squares[1]);
                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[3]);
                        bottomSide.add(squares[1]);
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
                        squares[0].setPos((byte) (posX + 2), posY);
                        squares[1].setPos(posX, (byte) (posY + 1));
                        squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);

                        rightSide.add(squares[0]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[1]);
                        bottomSide.add(squares[2]);
                        bottomSide.add(squares[3]);
                        break;
                    case 1:
                        /*
                         * L
                         * L
                         * L L
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos(posX, (byte) (posY + 1));
                        squares[2].setPos(posX, (byte) (posY + 2));
                        squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);
                        leftSide.add(squares[2]);

                        rightSide.add(squares[0]);
                        rightSide.add(squares[1]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[2]);
                        bottomSide.add(squares[3]);
                        break;
                    case 2:
                        /*
                         * L L L
                         * L
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos((byte) (posX + 1), posY);
                        squares[2].setPos((byte) (posX + 2), posY);
                        squares[3].setPos(posX, (byte) (posY + 1));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[3]);

                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[3]);
                        bottomSide.add(squares[1]);
                        bottomSide.add(squares[2]);
                        break;
                    case 3:
                        /*
                         * L L
                         *   L
                         *   L
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos((byte) (posX + 1), posY);
                        squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[2]);
                        leftSide.add(squares[3]);

                        rightSide.add(squares[1]);
                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[0]);
                        bottomSide.add(squares[3]);
                        break;
                }
                break;
            case O:
                /*
                 * O O
                 * O O
                 * */
                squares[0].setPos(posX, posY);
                squares[1].setPos((byte) (posX + 1), posY);
                squares[2].setPos(posX, (byte) (posY + 1));
                squares[3].setPos((byte) (posX + 1), (byte) (posY + 1));
                
                leftSide.add(squares[0]);
                leftSide.add(squares[2]);

                rightSide.add(squares[1]);
                rightSide.add(squares[3]);

                bottomSide.add(squares[2]);
                bottomSide.add(squares[3]);
                break;
            case S:
                if (rotation % 2 == 0) {
                    /*
                     *   S S
                     * S S
                     * */
                    squares[0].setPos((byte) (posX + 1), posY);
                    squares[1].setPos((byte) (posX + 2), posY);
                    squares[2].setPos(posX, (byte) (posY + 1));
                    squares[3].setPos((byte) (posX + 1), (byte) (posY + 1));
                    
                    leftSide.add(squares[0]);
                    leftSide.add(squares[2]);

                    rightSide.add(squares[1]);
                    rightSide.add(squares[3]);

                    bottomSide.add(squares[2]);
                    bottomSide.add(squares[3]);
                    bottomSide.add(squares[1]);
                } else {
                    /*
                     * S
                     * S S
                     *   S
                     * */
                    squares[0].setPos(posX, posY);
                    squares[1].setPos(posX, (byte) (posY + 1));
                    squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                    squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));
                    
                    leftSide.add(squares[0]);
                    leftSide.add(squares[1]);
                    leftSide.add(squares[3]);

                    rightSide.add(squares[0]);
                    rightSide.add(squares[2]);
                    rightSide.add(squares[3]);

                    bottomSide.add(squares[1]);
                    bottomSide.add(squares[3]);
                }
                break;
            case T:
                switch (rotation) {
                    case 0:
                        /*
                         *   T
                         * T T T
                         * */
                        squares[0].setPos((byte) (posX + 1), posY);
                        squares[1].setPos(posX, (byte) (posY + 1));
                        squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);

                        rightSide.add(squares[0]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[1]);
                        bottomSide.add(squares[2]);
                        bottomSide.add(squares[3]);
                    case 1:
                        /*
                         * T
                         * T T
                         * T
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos(posX, (byte) (posY + 1));
                        squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[3].setPos(posX, (byte) (posY + 2));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);
                        leftSide.add(squares[3]);

                        rightSide.add(squares[0]);
                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[3]);
                        bottomSide.add(squares[2]);
                        break;
                    case 2:
                        /*
                         * T T T
                         *   T
                         * */
                        squares[0].setPos(posX, posY);
                        squares[1].setPos((byte) (posX + 1), posY);
                        squares[2].setPos((byte) (posX + 2), posY);
                        squares[3].setPos((byte) (posX + 1), (byte) (posY + 1));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[3]);

                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[0]);
                        bottomSide.add(squares[3]);
                        bottomSide.add(squares[2]);
                        break;
                    case 3:
                        /*
                         *   T
                         * T T
                         *   T
                         * */
                        squares[0].setPos((byte) (posX + 1), posY);
                        squares[1].setPos(posX, (byte) (posY + 1));
                        squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                        squares[3].setPos((byte) (posX + 1), (byte) (posY + 2));
                        
                        leftSide.add(squares[0]);
                        leftSide.add(squares[1]);
                        leftSide.add(squares[3]);

                        rightSide.add(squares[1]);
                        rightSide.add(squares[2]);
                        rightSide.add(squares[3]);

                        bottomSide.add(squares[1]);
                        bottomSide.add(squares[3]);
                        break;
                }
                break;
            case Z:
                if (rotation % 2 == 0) {
                    /*
                     * Z Z
                     *   Z Z
                     * */
                    squares[0].setPos(posX, posY);
                    squares[1].setPos((byte) (posX + 1), posY);
                    squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                    squares[3].setPos((byte) (posX + 2), (byte) (posY + 1));
                    
                    leftSide.add(squares[0]);
                    leftSide.add(squares[2]);

                    rightSide.add(squares[1]);
                    rightSide.add(squares[3]);

                    bottomSide.add(squares[0]);
                    bottomSide.add(squares[2]);
                    bottomSide.add(squares[3]);
                } else {
                    /*
                     *   Z
                     * Z Z
                     * Z
                     * */
                    squares[0].setPos((byte) (posX + 1), posY);
                    squares[1].setPos(posX, (byte) (posY + 1));
                    squares[2].setPos((byte) (posX + 1), (byte) (posY + 1));
                    squares[3].setPos(posX, (byte) (posY + 2));
                    
                    leftSide.add(squares[0]);
                    leftSide.add(squares[1]);
                    leftSide.add(squares[3]);

                    rightSide.add(squares[0]);
                    rightSide.add(squares[2]);
                    rightSide.add(squares[3]);

                    bottomSide.add(squares[3]);
                    bottomSide.add(squares[2]);
                }
                break;
        }
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
