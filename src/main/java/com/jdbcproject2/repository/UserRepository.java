package com.jdbcproject2.repository;

import com.jdbcproject2.model.Habit;
import com.jdbcproject2.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public static void registerUser(User user) {
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?);";

        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
            System.out.println("Пользователь успешно зарегистрирован.");
            System.out.println(user);
        } catch (SQLException e) {
            System.out.println("\n\tОшибка при регистрации пользователя: " + e.getMessage());
        }
    }

    public static User getUserByEmail(String email) {
        User user = new User();
        List<Habit> habits = new ArrayList<>();
        Habit habit = null;

        String userQuery = "SELECT * FROM users WHERE email = ?";
        String habitQuery = "SELECT * FROM habits WHERE user_id = ?";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement userStmt = connection.prepareStatement(userQuery);
             PreparedStatement habitStmt = connection.prepareStatement(habitQuery)) {
            userStmt.setString(1, email);
            ResultSet userResultSet = userStmt.executeQuery();

            if (userResultSet.next()) {
                user.setId(userResultSet.getInt("id"));
                user.setName(userResultSet.getString("name"));
                user.setEmail(userResultSet.getString("email"));
                user.setPassword(userResultSet.getString("password"));
            }

            ResultSet habitResultSet = habitStmt.executeQuery();
            if (habitResultSet.next()) {
                int id = habitResultSet.getInt("id");
                String title = habitResultSet.getString("title");
                String description = habitResultSet.getString("description");
                String frequency = habitResultSet.getString("frequency");
                String date = habitResultSet.getString("date");
                boolean isCompleted = habitResultSet.getBoolean("isCompleted");
                int userId = habitResultSet.getInt("user_Id");

                habit = new Habit();
                habit.setId(id);
                habit.setTitle(title);
                habit.setDescription(description);
                habit.setFrequency(frequency);
                habit.setDate(date);
                habit.setCompleted(isCompleted);
                habit.setUserId(userId);

                habits.add(habit);
            }
            user.setHabits(habits);
        } catch (SQLException e) {
            System.out.println("Ошибка при получении пользователя по email: " + e.getMessage());

        }
        return null;
    }

    public static int isEmailExist(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?;";

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? 1 : -1; // Возвращаем 1, если email существует, и -1, если не существует
        } catch (SQLException e) {
            System.out.println("Ошибка в isEmailExist() при проверке существования email: " + e.getMessage());
            return 0; // Возвращаем 0 в случае ошибки подключения
        }
    }

    public static void userChangeName(String newName, String email) {
        String query = "UPDATE users SET name = ? WHERE email = ?;";

        try (Connection connection = DbUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newName);
            statement.setString(2, email);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Имя пользователя успешно обновлено.");
            } else {
                System.out.println("Пользователь с таки email не был найден.");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка в методе userChangeName(): " + e.getMessage());
        }

    }

    public static void userChangePassword(String newPassword, String email) {
        String query = "UPDATE users SET password = ? WHERE email = ?;";
        try (Connection connection = DbUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setString(2, email);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Пароль был успешно изменен.");
            } else {
                System.out.println("Пользователь с таким email не был найден");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка в методе userChangePassword() " + e.getMessage());
        }
    }

    public static void userChangeEmail(String newEmail, String email) {
        String query = "UPDATE users SET email = ? WHERE email = ?;";

        try (Connection connection = DbUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newEmail);
            statement.setString(2, email);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Email был успешно изменен");
            } else {
                System.out.println("\n\tПроизошла внутренняя ошибка при смене email");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка в методе userChangeEmail() " + e.getMessage());
        }
    }

    public static void userDeleteAccount(User user) {
        String query = "DELETE FROM users WHERE email = ?;";

        try (Connection connection = DbUtils.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getEmail());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0)
                System.out.printf("Пользователь %s был успешно удален.", user.getEmail());
            else {
                System.out.println("Произошла внутренняя ошибка при удалении аккаунта");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка в методе userDeleteAccount() " + e.getMessage());
        }
    }

    // TODO  и для updateUser и deleteUser
}
