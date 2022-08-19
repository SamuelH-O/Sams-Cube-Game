package com.example.samscubegame;

import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.S)
public class PreviewSurfaceCallback implements SurfaceHolder.Callback {

    private final GameActivity gameActivity;

    private SurfaceHolder surfaceHolder;

    @SuppressWarnings("FieldCanBeLocal")
    private final byte NB_ROWS = 4, NB_COLUMNS = 6;
    private GridOfSurfaces grid;

    private ArrayList<Piece> fullBag = null;
    private ArrayList<Piece> rndmBag = null;
    private final Random rndm = new Random();

    private Piece nextPiece = null;

    private final boolean showGrid;

    private float[] gridPoints;

    PreviewSurfaceCallback(GameActivity gameActivity, final boolean showGrid) {
        // Set the random seed to the current time
        rndm.setSeed(Instant.now().toEpochMilli());

        this.gameActivity = gameActivity;
        this.showGrid = showGrid;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

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

        grid = new GridOfSurfaces(NB_COLUMNS, NB_ROWS, canvas, showGrid, gridPoints);

        // Get the size of the squares by the smaller side of the canvas
        float squareSize;
        squareSize = (float) canvas.getWidth() / NB_COLUMNS;

        surfaceHolder.unlockCanvasAndPost(canvas);

        if (fullBag == null) {
            fullBag = new ArrayList<>();
            fullBag.add(new Piece_I(squareSize, gameActivity.getResources()));
            fullBag.add(new Piece_J(squareSize, gameActivity.getResources()));
            fullBag.add(new Piece_L(squareSize, gameActivity.getResources()));
            fullBag.add(new Piece_O(squareSize, gameActivity.getResources()));
            fullBag.add(new Piece_S(squareSize, gameActivity.getResources()));
            fullBag.add(new Piece_T(squareSize, gameActivity.getResources()));
            fullBag.add(new Piece_Z(squareSize, gameActivity.getResources()));
        }

        if (rndmBag == null) {
            rndmBag = new ArrayList<>();
            for (Piece i : fullBag) {
                try {
                    rndmBag.add(i.clone());
                    Log.d("cloned", "" + i);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (nextPiece == null) {
            nextPiece = getRandomPiece();
            drawFrame();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    Piece getRandomPiece() {
        // Refill the bag if empty
        if (rndmBag.isEmpty()) {
            for (Piece i : fullBag) {
                try {
                    rndmBag.add(i.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        int rndmInt = rndm.nextInt(rndmBag.size());
        Piece ret = rndmBag.get(rndmInt);
        rndmBag.remove(rndmInt);
        return ret;
    }

    Piece getNextPiece() {
        Piece tmp = nextPiece;
        nextPiece = getRandomPiece();
        nextPiece.setPosAndRot((byte) (1), (byte) (1) , (byte) (0));
        drawFrame();
        return tmp;
    }

    private void drawFrame() {
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

        grid.setBackground(canvas);
        nextPiece.draw(canvas);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}
