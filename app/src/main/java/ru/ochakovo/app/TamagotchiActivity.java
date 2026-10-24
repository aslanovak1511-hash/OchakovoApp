package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TamagotchiActivity extends AppCompatActivity {
    private User currentUser;
    private SeekBar progressBar;
    private TextView tvLevel, tvDialog;
    private Button btnAction1, btnResume, btnChooseLevel, btnLogout;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamagotchi);

        session = new SessionManager(this);
        currentUser = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());

        progressBar = findViewById(R.id.progressBarGrowth);
        tvLevel = findViewById(R.id.tvLevelNumber);
        tvDialog = findViewById(R.id.tvDialog);
        btnAction1 = findViewById(R.id.btnAction1);
        btnResume = findViewById(R.id.btnGoToResume);
        btnChooseLevel = findViewById(R.id.btnChooseLevel);
        btnLogout = findViewById(R.id.btnLogout);

        updateUI();

        btnResume.setOnClickListener(v -> startActivity(new Intent(this, ResumeActivity.class)));
        btnChooseLevel.setOnClickListener(v -> showLevelPicker());
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Выход")
                    .setMessage("Вы уверены, что хотите выйти?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        session.logout();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session != null) {
            currentUser = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());
            updateUI();
        }
    }

    private void updateUI() {
        int level = currentUser.currentLevel;
        progressBar.setProgress((level - 1) * 20);
        tvLevel.setText(String.valueOf(level));

        btnResume.setVisibility(View.GONE);
        btnAction1.setVisibility(View.VISIBLE);

        switch (level) {
            case 1:
                tvDialog.setText("Квасенок: Хм, я хочу что-то освежающее и фруктовое... Поможешь выбрать?");
                btnAction1.setText("Помочь выбрать");
                btnAction1.setOnClickListener(v -> startDialog("Квасенок", R.drawable.kvasenok,
                        new String[]{"Привет! Я Квасенок. У меня закончились запасы.", "Выбери, пожалуйста, нужный напиток!"},
                        Level1BottleActivity.class.getName()));
                break;

            case 2:
                tvDialog.setText("Квасенок: Ой! У нас закончились напитки... Всё раскупили! 🦊 Пойдем на завод!");
                btnAction1.setText("Пойдем в лабораторию");
                btnAction1.setOnClickListener(v -> startDialog("Ай-Ай", R.drawable.ptolemey,
                        new String[]{
                                "Квасенок: Смотри! Это наш сотрудник лаборатории — Ай-Ай!",
                                "Ай-Ай: Привет! Я работаю в лаборатории, исследую компоненты и слежу за качеством.",
                                "Ай-Ай: Попробуй сделать мою работу! Нажми на правильную склянку 15 раз."
                        },
                        Level2BottleActivity.class.getName()));
                break;

            case 3:
                tvDialog.setText("Квасенок: Это цех розлива! Давай поговорим с Китоглавом.");
                btnAction1.setText("Поговорить с Китоглавом");
                btnAction1.setOnClickListener(v -> startDialog("Китоглав Кирилл", R.drawable.krill,
                        new String[]{
                                "Китоглав: Привет! Меня зовут Кирилл. 🦅",
                                "Китоглав: Я работаю в цехе розлива, слежу за линиями и качеством.",
                                "Китоглав: Сейчас мы проверим твою память! Найди все пары профессий."
                        },
                        Level4MemoryActivity.class.getName())); // ЗАПУСК МЕМОРИ
                break;

            case 4:
                tvDialog.setText("Квасенок: А это технологический цех! Пойдем к Акуле.");
                btnAction1.setText("Поговорить с Акулой");
                btnAction1.setOnClickListener(v -> startDialog("Акула Барон", R.drawable.baron,
                        new String[]{
                                "Пользователь: А где море?",
                                "Акула: Ха-ха-ха, очень смешно! 🦈",
                                "Акула: Ты попал в технологический цех!",
                                "Акула: Помоги мне собрать ингредиенты!"
                        },
                        Level3RunnerActivity.class.getName())); // ЗАПУСК РАННЕРА
                break;

            case 5:
                tvDialog.setText("Квасенок: А вот и склад! Крокодил Зубастик ждет нас.");
                btnAction1.setText("Поговорить с Крокодилом");
                btnAction1.setOnClickListener(v -> startDialog("Крокодил Зубастик", R.drawable.zubastics,
                        new String[]{
                                "Крокодил: Привет, я Зубастик! 🐊",
                                "Крокодил: Я работаю на складе, отвечаю за отгрузку и коробки.",
                                "Крокодил: А теперь помоги мне поймать коробки!"
                        },
                        Level5CrocodileActivity.class.getName())); // ЛОВЛЯ КОРОБОК
                break;

            case 6:
                tvDialog.setText("Квасенок: Ну всё, хватит! Пойдем, ты расскажешь о себе.");
                btnAction1.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showLevelPicker() {
        int maxLevel = currentUser.currentLevel;
        String[] levels = new String[maxLevel];
        for (int i = 0; i < maxLevel; i++) levels[i] = "Уровень " + (i + 1);

        new AlertDialog.Builder(this)
                .setTitle("Выбери уровень")
                .setItems(levels, (dialog, which) -> {
                    int selectedLevel = which + 1;
                    Intent intent = null;
                    switch (selectedLevel) {
                        case 1: intent = new Intent(this, Level1BottleActivity.class); break;
                        case 2: intent = new Intent(this, Level2BottleActivity.class); break;
                        case 3: intent = new Intent(this, Level4MemoryActivity.class); break;
                        case 4: intent = new Intent(this, Level3RunnerActivity.class); break;
                        case 5: intent = new Intent(this, Level5CrocodileActivity.class); break;
                    }
                    if (intent != null) startActivity(intent);
                })
                .show();
    }

    private void startDialog(String name, int imageRes, String[] lines, String nextClass) {
        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("CHARACTER_NAME", name);
        intent.putExtra("CHARACTER_IMAGE", imageRes);
        intent.putExtra("DIALOGUES", lines);
        intent.putExtra("NEXT_ACTIVITY", nextClass);
        startActivity(intent);
    }

    public static void levelCompleted(android.app.Activity activity) {
        SessionManager session = new SessionManager(activity);
        User user = AppDatabase.getInstance(activity).userDao().getUserById(session.getUserId());
        if (user.currentLevel < 6) {
            user.currentLevel++;
            AppDatabase.getInstance(activity).userDao().updateUser(user);
        }
        activity.startActivity(new Intent(activity, TamagotchiActivity.class));
        activity.finish();
    }
}