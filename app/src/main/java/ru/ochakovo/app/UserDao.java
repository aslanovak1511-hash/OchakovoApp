package ru.ochakovo.app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    long register(User user);

    @Query("SELECT * FROM users WHERE username = :login AND password = :pass")
    User login(String login, String pass);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);

    @Update
    void updateUser(User user);
}