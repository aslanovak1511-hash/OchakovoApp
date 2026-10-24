package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Level5CrocodileActivity extends AppCompatActivity {
    private ImageView tvPlayer;
    private TextView tvScore, tvStatus;
    private FrameLayout gameField;
    private int score = 0;
    private boolean gameOver = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crocodile);

        tvPlayer = findViewById(R.id.tvPlayer);
        tvScore = findViewById(R.id.tvScore);
        tvStatus = findViewById(R.id.tvStatus);
        gameField = findViewById(R.id.gameField);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnRight = findViewById(R.id.btnRight);

        btnLeft.setOnClickListener(v -> movePlayer(-100f));
        btnRight.setOnClickListener(v -> movePlayer(100f));

        startSpawning();
    }

    private void movePlayer(float dx) {
        float newX = tvPlayer.getX() + dx;
        if (newX >= 0 && newX <= gameField.getWidth() - tvPlayer.getWidth()) {
            tvPlayer.setX(newX);
        }
    }

    private void startSpawning() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameOver) return;
                ImageView box = new ImageView(Level5CrocodileActivity.this);
                box.setImageResource(android.R.drawable.ic_menu_save);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(80, 80);
                params.gravity = Gravity.TOP;
                params.topMargin = -80;
                params.leftMargin = random.nextInt(Math.max(1, gameField.getWidth() - 80));
                box.setLayoutParams(params);
                gameField.addView(box);

                box.animate().translationY(gameField.getHeight()).setDuration(2000).withEndAction(() -> {
                    if (box.getParent() != null) gameField.removeView(box);
                }).start();

                handler.postDelayed(() -> {
                    if (box.getParent() != null) {
                        float boxY = box.getY();
                        if (boxY > gameField.getHeight() - 200 && Math.abs(box.getX() - tvPlayer.getX()) < 100) {
                            gameField.removeView(box);
                            score++;
                            tvScore.setText("Поймано: " + score + "/10");
                            if (score >= 10) winGame();
                        }
                    }
                }, 1800);

                if (!gameOver) handler.postDelayed(this, 1000);
            }
        }, 500);
    }

    private void winGame() {
        if (gameOver) return;
        gameOver = true;
        tvStatus.setText("Крокодил: Отлично!");

        // ЖЕСТКО СТАВИМ 6 УРОВЕНЬ (Анкета)
        SessionManager session = new SessionManager(this);
        User user = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());
        user.currentLevel = 6;
        AppDatabase.getInstance(this).userDao().updateUser(user);

        handler.postDelayed(() -> {
            Intent intent = new Intent(this, DialogActivity.class);
            intent.putExtra("CHARACTER_NAME", "Крокодил Зубастик");
            intent.putExtra("CHARACTER_IMAGE", R.drawable.zubastics);
            intent.putExtra("DIALOGUES", new String[]{
                    "Крокодил: Ты поймал все коробки! 🐊",
                    "Крокодил: На складе нам нужны Операторы (от 60 000 руб) и Кладовщики (от 56 000).",
                    "Крокодил: Беги к Квасенку!"
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