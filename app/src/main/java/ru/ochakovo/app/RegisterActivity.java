package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etLogin = findViewById(R.id.etRegLogin);
        EditText etPass = findViewById(R.id.etRegPass);
        Button btnSave = findViewById(R.id.btnSaveReg);

        btnSave.setOnClickListener(v -> {
            String login = etLogin.getText().toString();
            String pass = etPass.getText().toString();

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.username = login;
            newUser.password = pass;
            newUser.currentLevel = 1;

            long id = AppDatabase.getInstance(this).userDao().register(newUser);

            new SessionManager(this).saveUserId((int) id);
            startActivity(new Intent(this, TamagotchiActivity.class));
            finish();
        });
    }
}