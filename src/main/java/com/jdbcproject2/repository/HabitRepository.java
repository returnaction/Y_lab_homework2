package com.jdbcproject2.repository;

import com.jdbcproject2.model.Habit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitRepository {
    public void addHabit(Habit habit) throws SQLException {
        String query = "INSERT INTO habits_app.habit (title, description, frequency) VALUES (?, ?, ?)";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, habit.getTitle());
            stmt.setString(2, habit.getDescription());
            stmt.setString(3, habit.getFrequency());
            stmt.executeUpdate();
        }
    }

    public List<Habit> getAllHabits() throws SQLException {
        List<Habit> habits = new ArrayList<>();
        String query = "SELECT * FROM habits_app.habit";
        try (Connection conn = DbUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                habits.add(new Habit(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("frequency")));
            }
        }
        return habits;
    }

    // Аналогично для updateHabit и deleteHabit
}
