package com.example.samscubegame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
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

    private float squareSize;

    private float[] gridPoints;

    private final boolean showGrid;

    GameSurfaceCallback(GameActivity gameActivity, final boolean showGrid, PreviewSurfaceCallback previewSurfaceCallback) {
        this.gameActivity = gameActivity;
        this.showGrid = showGrid;
        this.previewSurfaceCallback = previewSurfaceCallback;

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockHardwareCanvas();

        // Get the size of the squares by the smaller side of the canvas
        if (canvas.getWidth() < canvas.getHeight()) squareSize = (float) canvas.getWidth() / NB_COLUMNS;
        else squareSize = (float) canvas.getHeight() / NB_COLUMNS;

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
            currentPiece.setSquareSize(squareSize);
            // Place the first piece
            currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));
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

        // Draw the first frame
        drawFrame();

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
                    currentPiece.addToWall(grid);
                    currentPiece = previewSurfaceCallback.getNextPiece();
                    currentPiece.setSquareSize(squareSize);

                    currentPiece.setPosAndRot((byte) (4), (byte) (0), (byte) (0));

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
