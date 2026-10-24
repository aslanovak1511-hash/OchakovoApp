package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etLogin = findViewById(R.id.etLogin);
        EditText etPass = findViewById(R.id.etPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnReg = findViewById(R.id.btnGoRegister);

        SessionManager session = new SessionManager(this);

        // Если уже авторизован — пропускаем экран входа
        if (session.getUserId() != -1) {
            startActivity(new Intent(this, TamagotchiActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            User user = AppDatabase.getInstance(this).userDao()
                    .login(etLogin.getText().toString(), etPass.getText().toString());

            if (user != null) {
                session.saveUserId(user.id);
                startActivity(new Intent(this, TamagotchiActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });

        btnReg.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}