package ru.ochakovo.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Level2BottleActivity extends AppCompatActivity {
    private int score = 0;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction);

        tvStatus = findViewById(R.id.tvStatus);
        Button b1 = findViewById(R.id.btn1);
        Button b2 = findViewById(R.id.btn2);
        Button b3 = findViewById(R.id.btn3);
        Button b4 = findViewById(R.id.btn4);

        Button[] buttons = {b1, b2, b3, b4};
        int correctIndex = new Random().nextInt(4);
        Button correctButton = buttons[correctIndex];
        correctButton.setTextColor(Color.RED);

        android.view.View.OnClickListener listener = v -> {
            if (v == correctButton) {
                score++;
                tvStatus.setText("Ай-Ай: Отлично! Нажато: " + score + "/15");
                if (score >= 15) {
                    tvStatus.setText("Ай-Ай: Ты прирожденный лаборант!");

                    // ЖЕСТКО СТАВИМ 3 УРОВЕНЬ (Китоглав)
                    SessionManager session = new SessionManager(this);
                    User user = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());
                    if (user.currentLevel < 3) {
                        user.currentLevel = 3;
                        AppDatabase.getInstance(this).userDao().updateUser(user);
                    }

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(this, DialogActivity.class);
                        intent.putExtra("CHARACTER_NAME", "Ай-Ай");
                        intent.putExtra("CHARACTER_IMAGE", R.drawable.ptolemey);
                        intent.putExtra("DIALOGUES", new String[]{
                                "Ай-Ай: Ты отлично справился! 🧪",
                                "Ай-Ай: У нас есть вакансия Лаборанта (от 60 000 рублей, график 2/2).",
                                "Ай-Ай: Пойдем дальше, в цех розлива!"
                        });
                        intent.putExtra("NEXT_ACTIVITY", TamagotchiActivity.class.getName());
                        startActivity(intent);
                        finish();
                    }, 2000);
                }
            } else {
                score = 0;
                tvStatus.setText("Ай-Ай: Ой, не то! Начинаем заново (0/15)");
            }
        };

        b1.setOnClickListener(listener);
        b2.setOnClickListener(listener);
        b3.setOnClickListener(listener);
        b4.setOnClickListener(listener);
    }
}