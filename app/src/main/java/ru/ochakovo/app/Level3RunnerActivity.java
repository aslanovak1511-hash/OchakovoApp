package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Level3RunnerActivity extends AppCompatActivity {
    private ImageView tvPlayer;
    private TextView tvScore, tvStatus;
    private FrameLayout gameField;
    private int score = 0;
    private boolean gameOver = false;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Random random = new Random();
    private float screenWidth;
    private boolean isJumping = false;
    private TextView currentFruit;
    private float fruitX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner);

        tvPlayer = findViewById(R.id.tvPlayer);
        tvScore = findViewById(R.id.tvScore);
        tvStatus = findViewById(R.id.tvStatus);
        gameField = findViewById(R.id.gameField);

        gameField.post(() -> {
            screenWidth = gameField.getWidth();
            startGameLoop();
        });

        findViewById(android.R.id.content).setOnClickListener(v -> jump());
    }

    private void jump() {
        if (isJumping || gameOver) return;
        isJumping = true;
        tvPlayer.animate().translationY(-300f).setDuration(250)
                .withEndAction(() -> tvPlayer.animate().translationY(0f).setDuration(250)
                        .withEndAction(() -> isJumping = false).start())
                .start();
    }

    private void startGameLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameOver) return;
                if (currentFruit == null) createFruit();
                if (currentFruit != null) {
                    fruitX -= 10;
                    currentFruit.setX(fruitX);
                    float playerX = tvPlayer.getX();
                    if (isJumping && fruitX <= playerX + tvPlayer.getWidth() && fruitX >= playerX - 50) {
                        gameField.removeView(currentFruit);
                        currentFruit = null;
                        score++;
                        tvScore.setText("Собрано: " + score + "/10");
                        if (score >= 10) { winGame(); return; }
                    }
                    if (fruitX < -100) {
                        gameField.removeView(currentFruit);
                        currentFruit = null;
                    }
                }
                handler.postDelayed(this, 16);
            }
        }, 100);
    }

    private void createFruit() {
        currentFruit = new TextView(this);
        String[] items = {"🍎", "🍋", "🍇", "🍊"};
        currentFruit.setText(items[random.nextInt(items.length)]);
        currentFruit.setTextSize(40f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(80, 80);
        params.gravity = Gravity.BOTTOM;
        params.leftMargin = (int) screenWidth;
        params.bottomMargin = 250;
        currentFruit.setLayoutParams(params);
        currentFruit.setGravity(Gravity.CENTER);
        gameField.addView(currentFruit);
        fruitX = screenWidth;
    }

    private void winGame() {
        if (gameOver) return;
        gameOver = true;
        tvStatus.setText("Акула: Ингредиенты собраны!");

        // ЖЕСТКО СТАВИМ 5 УРОВЕНЬ (Крокодил)
        SessionManager session = new SessionManager(this);
        User user = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());
        if (user.currentLevel < 5) {
            user.currentLevel = 5;
            AppDatabase.getInstance(this).userDao().updateUser(user);
        }

        handler.postDelayed(() -> {
            Intent intent = new Intent(this, DialogActivity.class);
            intent.putExtra("CHARACTER_NAME", "Акула Барон");
            intent.putExtra("CHARACTER_IMAGE", R.drawable.baron);
            intent.putExtra("DIALOGUES", new String[]{
                    "Акула: Отличный забег! 🦈",
                    "Акула: В технологическом цеху открыта вакансия Оператора (от 70 000 руб).",
                    "Акула: Пойдем дальше!"
            });
            intent.putExtra("NEXT_ACTIVITY", TamagotchiActivity.class.getName());
            startActivity(intent);
            finish();
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}