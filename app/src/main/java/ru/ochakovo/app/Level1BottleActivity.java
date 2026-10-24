package ru.ochakovo.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class Level1BottleActivity extends AppCompatActivity {
    private TextView tvRequest, tvResult;
    private int targetChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);

        tvRequest = findViewById(R.id.tvRequest);
        tvResult = findViewById(R.id.tvResult);
        ImageView imgMojito = findViewById(R.id.imgMojito);
        ImageView imgKvas = findViewById(R.id.imgKvas);
        ImageView imgPivo = findViewById(R.id.imgPivo);

        targetChoice = new Random().nextInt(3);

        if (targetChoice == 0) tvRequest.setText("Квасенок: Хочу Мохито! Оно такое освежающее.");
        else if (targetChoice == 1) tvRequest.setText("Квасенок: Хочу наш легендарный Квас!");
        else tvRequest.setText("Квасенок: Сегодня хочу Пиво! (18+)");

        imgMojito.setOnClickListener(v -> checkChoice(0, "Мохито", "«Очаково» делает мохито только на основе натурального сока лайма и мяты!"));
        imgKvas.setOnClickListener(v -> checkChoice(1, "Квас", "Наш квас готовится по технологии двойного брожения, как в старинных деревнях!"));
        imgPivo.setOnClickListener(v -> checkChoice(2, "Пиво", "Наше пиво варится на чистейшей воде из артезианских скважин!"));
    }

    private void checkChoice(int choice, String name, String fact) {
        findViewById(R.id.imgMojito).setEnabled(false);
        findViewById(R.id.imgKvas).setEnabled(false);
        findViewById(R.id.imgPivo).setEnabled(false);

        tvResult.setVisibility(View.VISIBLE);

        if (choice == targetChoice) {
            tvResult.setText("Квасенок: Да! Спасибо за " + name + "! 😍\n\n" + fact);
        } else {
            tvResult.setText("Квасенок: Ой, я же просил другое... Но ладно, " + name + " тоже вкусно.\n\n" + fact);
        }

        new Handler().postDelayed(() -> TamagotchiActivity.levelCompleted(this), 4000);
    }
}