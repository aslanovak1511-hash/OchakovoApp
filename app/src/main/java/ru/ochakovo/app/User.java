package ru.ochakovo.app;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // Данные для входа
    public String username;
    public String password;

    // Данные для анкеты HR
    public String surname;    // Фамилия
    public String name;       // Имя
    public String patronymic; // Отчество
    public String phone;      // Телефон (без Telegram)
    public String city;
    public String selectedVacancy;

    public int currentLevel = 1;
}