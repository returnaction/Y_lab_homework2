package com.jdbcproject2.repository;

import com.jdbcproject2.model.Habit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitRepository {
    public static void addHabit(Habit habit, int userId) {
        String query = "INSERT INTO habits (title, description, frequency, date, isCompleted, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, habit.getTitle());
            stmt.setString(2, habit.getDescription());
            stmt.setString(3, habit.getFrequency());
            stmt.setString(4, habit.getDate());
            stmt.setBoolean(5, habit.isCompleted());
            stmt.setInt(6, userId);
            stmt.executeUpdate();

            System.out.printf("Привычка '%s' успешна добавлена", habit.getTitle());
        } catch (SQLException e) {
            System.out.println("Ошибка в методе addHabit() " + e.getMessage());
        }
    }

    public static List<Habit> getAllHabits(int userId, String filter) {
        List<Habit> habits = new ArrayList<>();
        String query = null;
        if (filter.equals("создания")) {
            query = "SELECT * FROM habits WHERE user_id = ? ORDER BY date ASC;";
        } else if (filter.equals("выполнения")) {
            query = "SELECT * FROM habits WHERE user_id = ? ORDER BY isCompleted ASC, date ASC;";
        } else {
            query = "SELECT * FROM habits WHERE user_id = ?;";
        }

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Habit habit = new Habit();
                habit.setId(resultSet.getInt("id"));
                habit.setTitle(resultSet.getString("title"));
                habit.setDescription(resultSet.getString("description"));
                habit.setFrequency(resultSet.getString("frequency"));
                habit.setDate(resultSet.getString("date"));
                habit.setCompleted(resultSet.getBoolean("isCompleted"));
                habit.setUserId(resultSet.getInt("user_Id"));

                habits.add(habit);
                habit = null;
            }
            return habits;
        } catch (SQLException e) {
            System.out.println("\n\tОшибка в методе getAllHabits() " + e.getMessage());
            return null;
        }

    }

    public static boolean deleteHabit(int habitId) {

        String query = "DELETE FROM habits WHERE id= ?;";

        try (Connection connection = DbUtils.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, habitId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Привычка успешно удалена");
                return true;
            } else {
                System.out.printf("Привычка с ID = %s не была найдена", habitId);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("\n\tОшибка в методе deleteHabit() " + e.getMessage());
            return false;
        }

    }

    // Аналогично для updateHabit и deleteHabit
}
