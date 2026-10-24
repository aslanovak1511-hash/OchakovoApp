package ru.ochakovo.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResumeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        SessionManager session = new SessionManager(this);
        User user = AppDatabase.getInstance(this).userDao().getUserById(session.getUserId());

        EditText etSurname = findViewById(R.id.etSurname);
        EditText etName = findViewById(R.id.etName);
        EditText etPatronymic = findViewById(R.id.etPatronymic);
        EditText etPhone = findViewById(R.id.etPhone);
        Spinner spinnerCity = findViewById(R.id.spinnerCity);
        Spinner spinnerVacancy = findViewById(R.id.spinnerVacancy);
        Button btnSend = findViewById(R.id.btnSend);

        // Настройка списков
        String[] cities = {"Москва", "Московская область", "Другой город"};
        spinnerCity.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities));

        String[] vacancies = {
                "Лаборатория (лаборант, от 60 000 ₽)",
                "Цех розлива (оператор, от 70 000 ₽)",
                "Технологический цех (оператор, от 70 000 ₽)",
                "Склад (оператор/кладовщик, от 56 000 ₽)",
                "IT / Маркетинг (проектная практика)"
        };
        spinnerVacancy.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vacancies));

        // Предзаполняем, если уже вводили ранее
        if (user.surname != null) etSurname.setText(user.surname);
        if (user.name != null) etName.setText(user.name);
        if (user.patronymic != null) etPatronymic.setText(user.patronymic);
        if (user.phone != null) etPhone.setText(user.phone);

        btnSend.setOnClickListener(v -> {
            String surname = etSurname.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String patronymic = etPatronymic.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String city = spinnerCity.getSelectedItem().toString();
            String vacancy = spinnerVacancy.getSelectedItem().toString();

            // Проверка на заполнение
            if (surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Заполните все поля ФИО и телефон", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем данные в БД
            user.surname = surname;
            user.name = name;
            user.patronymic = patronymic;
            user.phone = phone;
            user.city = city;
            user.selectedVacancy = vacancy;
            AppDatabase.getInstance(this).userDao().updateUser(user);

            startActivity(new Intent(this, ResultActivity.class));
            finish();
        });
    }
}