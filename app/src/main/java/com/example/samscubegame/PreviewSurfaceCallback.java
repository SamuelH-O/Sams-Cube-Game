package com.example.samscubegame;

import android.content.SharedPreferences;
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

    private final SharedPreferences sharedPref;

    PreviewSurfaceCallback(GameActivity gameActivity, final SharedPreferences sharedPref) {
        // Set the random seed to the current time
        rndm.setSeed(Instant.now().toEpochMilli());

        this.gameActivity = gameActivity;
        this.sharedPref = sharedPref;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

        // Measure debug grid points
        boolean showGrid= sharedPref.getBoolean(gameActivity.getResources().getString(R.string.show_grid_key), false);
        boolean showGridNumbers = sharedPref.getBoolean(gameActivity.getResources().getString(R.string.show_grid_numbers_key), false);
        grid = new GridOfSurfaces(NB_COLUMNS, NB_ROWS, canvas, showGrid, showGridNumbers);

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
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private Piece getRandomPiece() {
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
        if (!(nextPiece.getWidth() % 2 == 0)) {
            nextPiece.setPosAndRot((byte) ((NB_COLUMNS / 2) - 1), (byte) ((NB_ROWS / 2)), (byte) (0));
        } else {
            nextPiece.setPosAndRot((byte) (NB_COLUMNS / 2), (byte) (NB_ROWS / 2), (byte) (0));
        }
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
