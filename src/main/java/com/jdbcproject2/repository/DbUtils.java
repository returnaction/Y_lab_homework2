package com.jdbcproject2.repository;

import java.sql.*;

public class DbUtils {

    public static final String DB_NAME = "habit_db";
    public static final String URL_POSTGRES = "jdbc:postgresql://localhost:5432/";
    public static final String URL = "jdbc:postgresql://localhost:5432/habit_db";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";


    public static void createDatabase() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL_POSTGRES, USER, PASSWORD);
            statement = connection.createStatement();

            String checkDbExistsQuery = String.format("SELECT 1 FROM pg_database WHERE datname='%s';", DB_NAME);
            resultSet = statement.executeQuery(checkDbExistsQuery);

            if (resultSet.next()) {
                System.out.println("База данных " + DB_NAME + " уже существует");
            } else {
                String createDbQuery = String.format("CREATE DATABASE %S;", DB_NAME);
                statement.executeUpdate(createDbQuery);
                System.out.println("База данных " + DB_NAME + " успешно создана");
            }


        } catch (SQLException e) {
            System.out.println("Ошибка в createDatabase() метод " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
    }

    public static void createTables() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();

            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "password VARCHAR(100) NOT NULL" +
                    ");";
            int resultUserTable = statement.executeUpdate(createUserTableQuery);
            if (resultUserTable == 0)
                System.out.println("Таблица users успешно создана или уже существует.");


            String createHabitTableQuery = "CREATE TABLE IF NOT EXISTS habits (" +
                    "id SERIAL PRIMARY KEY," +
                    "user_id INT NOT NULL," +
                    "title VARCHAR(100) NOT NULL," +
                    "description TEXT," +
                    "frequency VARCHAR(50)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" + // Связь один ко многим
                    ");";
            int resultHabitTable = statement.executeUpdate(createHabitTableQuery);
            if (resultHabitTable == 0)
                System.out.println("Таблица habit успешно создана или уже существует.");

        } catch (SQLException e) {
            System.out.println("Ошибка в createTable() метод " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}

