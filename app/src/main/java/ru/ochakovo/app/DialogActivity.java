package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {
    private String[] dialogues;
    private int currentIndex = 0;
    private String nextActivityClass;
    private TextView tvName, tvText;
    private Button btnNext;
    private boolean isTyping = false;
    private Handler handler = new Handler();
    private Runnable typeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        tvName = findViewById(R.id.tvCharacterName);
        tvText = findViewById(R.id.tvDialogText);
        ImageView imgChar = findViewById(R.id.imgCharacter);
        btnNext = findViewById(R.id.btnNext);

        Intent intent = getIntent();
        dialogues = intent.getStringArrayExtra("DIALOGUES");
        nextActivityClass = intent.getStringExtra("NEXT_ACTIVITY");
        tvName.setText(intent.getStringExtra("CHARACTER_NAME"));
        imgChar.setImageResource(intent.getIntExtra("CHARACTER_IMAGE", R.drawable.kvasenok));

        startTyping(dialogues[0]);

        btnNext.setOnClickListener(v -> {
            if (isTyping) {
                stopTypingAndShowFullText();
            } else {
                currentIndex++;
                if (currentIndex < dialogues.length) {
                    startTyping(dialogues[currentIndex]);
                } else {
                    goToNextActivity();
                }
            }
        });
    }

    private void startTyping(String text) {
        isTyping = true;
        btnNext.setText("Пропустить ⏩");
        tvText.setText("");
        final int[] i = {0};
        typeRunnable = new Runnable() {
            @Override
            public void run() {
                if (i[0] < text.length()) {
                    tvText.append(String.valueOf(text.charAt(i[0])));
                    i[0]++;
                    handler.postDelayed(this, 30);
                } else {
                    finishTyping();
                }
            }
        };
        handler.post(typeRunnable);
    }

    private void stopTypingAndShowFullText() {
        handler.removeCallbacks(typeRunnable);
        tvText.setText(dialogues[currentIndex]);
        finishTyping();
    }

    private void finishTyping() {
        isTyping = false;
        btnNext.setText("Далее ▶");
    }

    private void goToNextActivity() {
        try {
            Class<?> clazz = Class.forName(nextActivityClass);
            Intent intent = new Intent(this, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
}