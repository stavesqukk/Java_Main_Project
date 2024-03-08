package org.example;

import org.example.service.EventManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Main {

    public static final String EVENT_MANAGER_TABLE = "event_managers";
    public static final String COLUMN_MANAGER_ID = "managerId";
    public static final String COLUMN_MANAGER_NAME = "managerName";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static void main(String[] args) {

        EventManager eventManager = new EventManager(1212,"John Doe","john.doe","password123");

        String url = "jdbc:sqlite:C:\\Users\\Hp\\IdeaProjects\\Event_management\\target\\event_management.db";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                EVENT_MANAGER_TABLE +
                " (" +
                COLUMN_MANAGER_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COLUMN_MANAGER_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";

        String insertRecordSQL = "INSERT INTO " +
                EVENT_MANAGER_TABLE +
                "(" +
                COLUMN_MANAGER_NAME + ", " +
                COLUMN_USERNAME + ", " +
                COLUMN_PASSWORD +
                ") VALUES (?, ?, ?)";

        String selectData = "SELECT * FROM " + EVENT_MANAGER_TABLE;

        try {
            Connection connection = DriverManager.getConnection(url);

            System.out.println("Connected");
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);

            PreparedStatement preparedStatement = connection.prepareStatement(insertRecordSQL);
            preparedStatement.setString(1, eventManager.getManagerName());
            preparedStatement.setString(2, eventManager.getUsername());
            preparedStatement.setString(3, eventManager.getPassword());

            preparedStatement.executeUpdate();
            System.out.println("Record Inserted");

            List<EventManager> eventManagers = new ArrayList<>();
            ResultSet rs = statement.executeQuery(selectData);
            while (rs.next()) {
                int managerId = rs.getInt(COLUMN_MANAGER_ID);
                String managerName = rs.getString(COLUMN_MANAGER_NAME);
                String username = rs.getString(COLUMN_USERNAME);
                String password = rs.getString(COLUMN_PASSWORD);

                EventManager retrievedEventManager = new EventManager(managerId, managerName, username, password);
                eventManagers.add(retrievedEventManager);
            }

            for (EventManager em : eventManagers) {
                System.out.println("Event Manager ID: " + em.getManagerId() +
                        ", Name: " + em.getManagerName() +
                        ", Username: " + em.getUsername() +
                        ", Password: " + em.getPassword());
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}