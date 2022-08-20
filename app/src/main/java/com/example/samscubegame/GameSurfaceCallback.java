package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameSurfaceCallback implements SurfaceHolder.Callback {

    private final GameActivity gameActivity;

    private SurfaceHolder surfaceHolder;

    @SuppressWarnings("FieldCanBeLocal")
    private final byte NB_ROWS = 16, NB_COLUMNS = 10;

    private GridOfSurfaces grid;

    private Runnable gameLoopRunnable;

    private Piece currentPiece = null;

    private final PreviewSurfaceCallback previewSurfaceCallback;

    private float blockSize;

    private final SharedPreferences sharedPref;

    GameSurfaceCallback(GameActivity gameActivity, final SharedPreferences sharedPref, PreviewSurfaceCallback previewSurfaceCallback) {
        this.gameActivity = gameActivity;
        this.sharedPref = sharedPref;
        this.previewSurfaceCallback = previewSurfaceCallback;

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

        // Get the size of the blocks by the smaller side of the canvas
        if (canvas.getWidth() < canvas.getHeight()) blockSize = (float) canvas.getWidth() / NB_COLUMNS;
        else blockSize = (float) canvas.getHeight() / NB_COLUMNS;

        boolean showGrid = sharedPref.getBoolean(gameActivity.getResources().getString(R.string.show_grid_key), false);
        boolean showGridNumbers = sharedPref.getBoolean(gameActivity.getResources().getString(R.string.show_grid_numbers_key), false);
        grid = new GridOfSurfaces(NB_COLUMNS, NB_ROWS, canvas, showGrid, showGridNumbers);

        surfaceHolder.unlockCanvasAndPost(canvas);

        startGame();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}

    @SuppressLint("NonConstantResourceId") // TODO: Remove once the debugs options are removed
    private void startGame() {
        if (currentPiece == null) {
            currentPiece = previewSurfaceCallback.getNextPiece();
            currentPiece.setBlockSize(blockSize);
            // Place the first piece
            if (!(currentPiece.getWidth() % 2 == 0)) {
                currentPiece.setPosAndRot((byte) ((NB_COLUMNS / 2) - 1), (byte) (2), (byte) (0));
            } else {
                currentPiece.setPosAndRot((byte) (NB_COLUMNS / 2), (byte) (2), (byte) (0));
            }
        }

        // Initialize controls with method findViewById()
        Button buttonMoveLeft = gameActivity.findViewById(R.id.buttonMoveLeft);
        Button buttonMoveRight = gameActivity.findViewById(R.id.buttonMoveRight);
        Button buttonRotateRight = gameActivity.findViewById(R.id.buttonRotateRight);
        Button buttonSnap = gameActivity.findViewById(R.id.buttonSnap);
        Button buttonMoveBottom = gameActivity.findViewById(R.id.buttonMoveBottom);

        // Add OnClickListener to ImageViewMoveLeft (controls left)
        buttonMoveLeft.setOnClickListener(view -> {
            // Move piece to the left if possible
            if (currentPiece.canMoveLeft(grid)) {
                currentPiece.setPosAndRot((byte) (currentPiece.posX - 1), currentPiece.posY, currentPiece.rotation);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveRight (controls right)
        buttonMoveRight.setOnClickListener(view -> {
            // Move piece to the right if possible
            if (currentPiece.canMoveRight(grid)) {
                currentPiece.setPosAndRot((byte) (currentPiece.posX + 1), currentPiece.posY, currentPiece.rotation);
            }
            drawFrame();
        });

        // Add OnClickListener to ImageViewRotateRight (controls rotate)
        buttonRotateRight.setOnClickListener(view -> {
            currentPiece.figureOutNextRotation(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewSnap (controls snap)
        buttonSnap.setOnClickListener(view -> {
            currentPiece.drop(grid);
            drawFrame();
        });

        // Add OnClickListener to ImageViewMoveBottom (controls bottom)
        buttonMoveBottom.setOnClickListener(view -> {
            // Move piece to the bottom if possible
            if (currentPiece.canMoveBottom(grid)) {
                currentPiece.setPosAndRot(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation);
            }
            drawFrame();
        });

        // start the game loop
        gameLoop();
    }

    private void gameLoop() {
        double level = 1d;
        double timeBtwnStep = Math.pow((0.8d-((level-1d)*0.007d)), (level-1d)) * 1000d;
        Handler gameLoopHandler = new Handler();
        gameLoopRunnable = new Runnable() {
            int i = 0;
            @Override
            public void run() {
                // Move piece one step to the bottom if possible
                if (currentPiece.canMoveBottom(grid) && i > 0) {
                    currentPiece.setPosAndRot(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation);
                } else if (i > 0) {
                    Log.d("CanMoveBot", "" + currentPiece.canMoveBottom(grid));
                    currentPiece.addToWall(grid);
                    currentPiece = previewSurfaceCallback.getNextPiece();
                    currentPiece.setBlockSize(blockSize);

                    if (!(currentPiece.getWidth() % 2 == 0)) {
                        currentPiece.setPosAndRot((byte) ((NB_COLUMNS / 2) - 1), (byte) (2), (byte) (0));
                    } else {
                        currentPiece.setPosAndRot((byte) (NB_COLUMNS / 2), (byte) (2), (byte) (0));
                    }

                    grid.checkForLines();
                }
                drawFrame();
                i = i + 1;
                gameLoopHandler.postDelayed(this, (long) timeBtwnStep);
            }
        };
        gameLoopHandler.post(gameLoopRunnable);
    }

    private void drawFrame() {
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

        grid.setBackground(canvas);
        grid.draw(canvas);
        currentPiece.draw(canvas);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}
