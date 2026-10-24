package ru.ochakovo.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level4MemoryActivity extends AppCompatActivity {
    private TextView firstCard = null, secondCard = null;
    private int pairsFound = 0;
    private boolean isLocked = false;
    private int[] cardValues = {1, 2, 3, 1, 2, 3};
    private String[] icons = {"⚙️", "🥼", "📦"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        GridLayout grid = findViewById(R.id.gridLayout);
        List<Integer> list = new ArrayList<>();
        for (int val : cardValues) list.add(val);
        Collections.shuffle(list);

        for (int i = 0; i < list.size(); i++) {
            TextView card = new TextView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; params.height = 0;
            params.columnSpec = GridLayout.spec(i % 3, 1f);
            params.rowSpec = GridLayout.spec(i / 3, 1f);
            params.setMargins(8, 8, 8, 8);
            card.setLayoutParams(params);
            card.setBackgroundColor(Color.WHITE);
            card.setGravity(android.view.Gravity.CENTER);
            card.setTextSize(40f);
            card.setText("?");
            card.setTextColor(Color.parseColor("#D50032"));
            card.setTag(list.get(i));
            card.setOnClickListener(this::onCardClick);
            grid.addView(card);
        }
    }

    private void onCardClick(View v) {
        TextView card = (TextView) v;
        if (isLocked || card.getText().toString().equals("⭐")) return;

        card.setText(icons[(int) card.getTag() - 1]);

        if (firstCard == null) firstCard = card;
        else if (secondCard == null) {
            secondCard = card;
            checkMatch();
        }
    }

    private void checkMatch() {
        isLocked = true;
        if (firstCard.getTag().equals(secondCard.getTag())) {
            new Handler().postDelayed(() -> {
                firstCard.setText("⭐"); secondCard.setText("⭐");
                pairsFound++;
                reset();
                if (pairsFound == 3) {
                    TextView tvStatus = findViewById(R.id.tvStatus);
                    tvStatus.setText("Китоглав: Память отличная!");

                    // ЖЕСТКО СТАВИМ 4 УРОВЕНЬ (Акула)
                    SessionManager session = new SessionManager(this);
                    User user = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());
                    if (user.currentLevel < 4) {
                        user.currentLevel = 4;
                        AppDatabase.getInstance(this).userDao().updateUser(user);
                    }

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(this, DialogActivity.class);
                        intent.putExtra("CHARACTER_NAME", "Китоглав Кирилл");
                        intent.putExtra("CHARACTER_IMAGE", R.drawable.krill);
                        intent.putExtra("DIALOGUES", new String[]{
                                "Китоглав: Ты справился с тестом на память! 🦅",
                                "Китоглав: В цеху розлива нам нужны Операторы (от 70 000 руб).",
                                "Китоглав: Пойдем дальше, нас ждет Акула!"
                        });
                        intent.putExtra("NEXT_ACTIVITY", TamagotchiActivity.class.getName());
                        startActivity(intent);
                        finish();
                    }, 2000);
                }
            }, 500);
        } else {
            new Handler().postDelayed(() -> {
                firstCard.setText("?"); secondCard.setText("?");
                reset();
            }, 1000);
        }
    }

    private void reset() { firstCard = null; secondCard = null; isLocked = false; }
}