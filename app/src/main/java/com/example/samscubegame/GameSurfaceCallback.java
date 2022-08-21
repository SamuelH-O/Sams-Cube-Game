package com.example.samscubegame;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.S)
public class GameSurfaceCallback implements SurfaceHolder.Callback {

    private final GameActivity gameActivity;

    private final File highScoreFile;

    private SurfaceHolder surfaceHolder;

    @SuppressWarnings("FieldCanBeLocal")
    private final byte NB_ROWS = 16, NB_COLUMNS = 10;

    private GridOfSurfaces grid;

    private Runnable gameLoopRunnable;

    private Piece currentPiece = null;

    private final PreviewSurfaceCallback previewSurfaceCallback;

    private float blockSize;

    private final SharedPreferences sharedPref;

    private long score = 0;

    private final TextView scoreTextView;

    private final TextView highScoreTextView;

    GameSurfaceCallback(GameActivity gameActivity, final SharedPreferences sharedPref, PreviewSurfaceCallback previewSurfaceCallback, File highScoreFile) {
        this.gameActivity = gameActivity;
        this.sharedPref = sharedPref;
        this.previewSurfaceCallback = previewSurfaceCallback;
        this.highScoreFile = highScoreFile;
        this.scoreTextView = gameActivity.findViewById(R.id.textView_score_number);
        this.highScoreTextView = gameActivity.findViewById(R.id.textView_high_score_number);
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

        scoreTextView.setText("0");
        highScoreTextView.setText(getHighScore());

        // Initialize controls with method findViewById()
        Button buttonMoveLeft = gameActivity.findViewById(R.id.button_move_left);
        Button buttonMoveRight = gameActivity.findViewById(R.id.button_move_right);
        Button buttonRotateRight = gameActivity.findViewById(R.id.button_rotate_right);
        Button buttonSnap = gameActivity.findViewById(R.id.button_snap);
        Button buttonMoveBottom = gameActivity.findViewById(R.id.button_move_bottom);

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
        double timeBtwnStep = Math.pow((0.8d-((grid.level-1d)*0.007d)), (grid.level-1d)) * 1000d;
        Handler gameLoopHandler = new Handler();
        gameLoopRunnable = new Runnable() {// TODO: Add T-Spin, back to back, drops and combo
            int i = 0;
            @Override
            public void run() {
                // Move piece one step to the bottom if possible
                if (currentPiece.canMoveBottom(grid) && i > 0) {
                    currentPiece.setPosAndRot(currentPiece.posX, (byte) (currentPiece.posY + 1), currentPiece.rotation);
                } else if (i > 0) {
                    currentPiece.addToWall(grid);
                    currentPiece = previewSurfaceCallback.getNextPiece();
                    currentPiece.setBlockSize(blockSize);

                    if (!(currentPiece.getWidth() % 2 == 0)) {
                        currentPiece.setPosAndRot((byte) ((NB_COLUMNS / 2) - 1), (byte) (2), (byte) (0));
                    } else {
                        currentPiece.setPosAndRot((byte) (NB_COLUMNS / 2), (byte) (2), (byte) (0));
                    }

                    score = score + grid.checkForLines();
                    scoreTextView.setText(String.valueOf(score));
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

    private boolean addToHighScore() {
        try {
            FileInputStream fis = gameActivity.getApplicationContext().openFileInput(highScoreFile.getName());
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                int i = 0;
                while (line != null) {
                    if (Long.parseLong(line.substring(0, line.indexOf(','))) < score) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        stringBuilder.append(score).append(',').append(dtf.format(now)).append('\n');
                    }
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                    i = i + 1;
                }
            } catch (IOException e) {
                Toast.makeText(gameActivity.getApplicationContext(), "Unable to save score.", LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
            String contents = stringBuilder.toString();
            try (FileOutputStream fos = gameActivity.getApplicationContext().openFileOutput(highScoreFile.getName(), Context.MODE_PRIVATE)) {
                fos.write(contents.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                Toast.makeText(gameActivity.getApplicationContext(), "Unable to save score.", LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            Toast.makeText(gameActivity.getApplicationContext(), "Unable to save score.", LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    private String getHighScore() {
        try {
            FileInputStream fis = gameActivity.getApplicationContext().openFileInput(highScoreFile.getName());
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                if (line == null) {
                    return "0";
                } else {
                    return line.substring(0, line.indexOf(','));
                }
            } catch (IOException e) {
                Toast.makeText(gameActivity.getApplicationContext(), "Unable to save score.", LENGTH_SHORT).show();
                e.printStackTrace();
                return "0";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }
}
