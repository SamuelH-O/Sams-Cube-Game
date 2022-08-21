package com.example.samscubegame;

import static android.graphics.Color.pack;

import android.annotation.SuppressLint;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.HashSet;

@RequiresApi(api = Build.VERSION_CODES.S)
class GridOfSurfaces {
    private final byte NB_COLUMNS, NB_ROWS;

    private final Paint gradientPaint = new Paint();
    private final Paint greyPaint = new Paint();
    
    // First number = columns | Second number = rows
    private final Block[][] grid;

    private final boolean showGrid, showGridNumbers;

    private float[] gridPoints;

    private final Paint whitePaint = new Paint();

    GridOfSurfaces(final byte nbColumns, final byte nbRows, final Canvas canvas, final boolean showGrid, final boolean showGridNumbers) {
        NB_COLUMNS = nbColumns;
        NB_ROWS = nbRows;
        grid = new Block[NB_COLUMNS][NB_ROWS];
        this.showGrid = showGrid;
        this.showGridNumbers = showGridNumbers;

        for(byte i = 0; i < NB_COLUMNS; i++) {
            for(byte j = 0; j < NB_ROWS; j++) {
                grid[i][j] = null;
            }
        }

        // Create the paint of the debug grid
        whitePaint.setARGB(255, 255, 255, 255);
        whitePaint.setBlendMode(BlendMode.SRC_OVER);
        whitePaint.setTextSize(canvas.getHeight() / (float) NB_ROWS / 3f);
        whitePaint.setTextAlign(Paint.Align.CENTER);

        // Measure debug grid points
        if (showGrid) {
            gridPoints = new float[(4 * NB_ROWS) + (4 * NB_COLUMNS)];
            float linesYOffset = 0f, columnsXOffset = 0f;
            boolean isFirstPoint = true;
            for (int i = 0; i < (4 * NB_ROWS) + (4 * NB_COLUMNS); i++) {
                if (i < (4 * NB_ROWS)) {// Is points for lines
                    if (isFirstPoint) {// Is first point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = 0f;
                        } else {// Is y
                            gridPoints[i] = linesYOffset + (canvas.getHeight() / (float) NB_ROWS);
                            linesYOffset = linesYOffset + (canvas.getHeight() / (float) NB_ROWS);
                            isFirstPoint = false;
                        }
                    } else {// Is second point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = canvas.getWidth();
                        } else {// Is y
                            gridPoints[i] = linesYOffset;
                            isFirstPoint = true;
                        }
                    }
                } else {// Is points for columns
                    if (isFirstPoint) {// Is first point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = columnsXOffset + (canvas.getWidth() / (float) NB_COLUMNS);
                            columnsXOffset = columnsXOffset + (canvas.getWidth() / (float) NB_COLUMNS);
                        } else {// Is y
                            gridPoints[i] = 0f;
                            isFirstPoint = false;
                        }
                    } else {// Is second point
                        if (i % 2 == 0) {// Is x
                            gridPoints[i] = columnsXOffset;
                        } else {// Is y
                            gridPoints[i] = canvas.getHeight();
                            isFirstPoint = true;
                        }
                    }
                }
            }
        }

        // Create the colors for the gradient (TODO: add the option to modify them)
        long[] bgColors = new long[3];
        bgColors[0] = pack(0.847f, 0.035f, 0.494f, 1.0f);
        bgColors[1] = pack(0.549f, 0.341f, 0.611f,1.0f);
        bgColors[2] =  pack(0.141f, 0.274f, 0.556f,1.0f);

        // Create the gradient from the top left to the bottom right (TODO: add the option to modify the direction)
        LinearGradient bgGradient = new LinearGradient(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), bgColors, null, Shader.TileMode.CLAMP);

        // Set the gradient to a paint with the right parameters
        gradientPaint.setDither(true);
        gradientPaint.setShader(bgGradient);

        // Add a semi-transparent grey paint to help focus (TODO: add the ability to modify this)
        greyPaint.setARGB(63, 61, 61, 61);
        greyPaint.setBlendMode(BlendMode.DARKEN);
    }

    void draw(final Canvas canvas) {
        for (byte i = 0; i < NB_COLUMNS; i++) {
            for (byte j = 0; j < NB_ROWS; j++) {
                if (grid[i][j] != null) {
                    grid[i][j].draw(canvas);
                }
            }
        }
    }

    boolean isFilledAt(byte posX, byte posY) {
        return (grid[posX][posY] != null);
    }

    void setBlock(Block block) {
        grid[block.posX][block.posY] = block;
    }

    void checkForLines() {
        byte nbFilledBlocks;
        HashSet<Byte> rowsToRemove = new HashSet<>();
        for (byte i = 0; i < NB_ROWS; i++) {
            nbFilledBlocks = 0;
            for(byte j = 0; j < NB_COLUMNS; j++) {
                if (grid[j][i] == null) {
                    break;
                } else {
                    nbFilledBlocks = (byte) (nbFilledBlocks + 1);
                }
            }
            if (nbFilledBlocks == NB_COLUMNS) {
                rowsToRemove.add(i);
            }
        }
        if (!rowsToRemove.isEmpty()) {
            removeRows(rowsToRemove);
        }
    }

    private void removeRows(HashSet<Byte> rowsToRemove) {
        byte nbRowsToDrop = 0;
        for (byte i = (byte) (NB_ROWS - 1); i >= 0; i--) {
            for (byte j = 0; j < NB_COLUMNS; j++) {
                if (rowsToRemove.contains(i)) {
                    grid[j][i] = null;
                    if (j == 0) {
                        nbRowsToDrop = (byte) (nbRowsToDrop + 1);
                    }
                } else if (nbRowsToDrop > 0) {
                    if (grid[j][i] != null) {
                        Block tmp = grid[j][i];
                        tmp.setPos(j, (byte) (i + nbRowsToDrop));
                        setBlock(tmp);
                    }
                    grid[j][i] = null;
                }
            }
        }
    }

    @SuppressLint("InlinedApi")
    void printGridState() {
        StringBuilder str = new StringBuilder();
        for(byte i = 0; i < NB_ROWS; i++) {
            for(byte j = 0; j < NB_COLUMNS; j++) {
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

    void setBackground(final Canvas canvas) {
        // Add a fancy background
        canvas.drawPaint(gradientPaint);

        // Add a grey layer to the background
        canvas.drawPaint(greyPaint);

        // Add debug grid
        if (showGrid) {
            canvas.drawLines(gridPoints, whitePaint);
        }

        // Add debug numbers
        if (showGridNumbers) {
            String str;
            float xOffset = canvas.getWidth() / (float) NB_COLUMNS, yOffset = canvas.getHeight() / (float) NB_ROWS;
            for (int i = 0; i < NB_COLUMNS; i++) {
                for (int j = 0; j < NB_ROWS; j++) {
                    str = ("" + i + " " + j);
                    canvas.drawText(str, (xOffset * i) + (xOffset / 2f), (yOffset * j) + (yOffset / 2f) + ((whitePaint.descent() - whitePaint.ascent()) / 2), whitePaint);
                }
            }
        }
    }

    Byte getNbColumns() {
        return NB_COLUMNS;
    }

    Byte getNbRows() {
        return NB_ROWS;
    }
}
