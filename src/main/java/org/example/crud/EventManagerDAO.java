package org.example.crud;

import org.example.service.EventManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventManagerDAO {

    public static final String MANAGER_TABLE = "event_managers";
    public static final String MANAGER_ID = "managerId";
    public static final String MANAGER_NAME = "managerName";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private final String url;

    public EventManagerDAO(String url) {
        this.url = url;
        createManagerTable();
    }

    public List<EventManager> getAllManagers() {
        List<EventManager> managers = new ArrayList<>();

        String selectManagersSQL = "SELECT * FROM " + MANAGER_TABLE;

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectManagersSQL)) {

            while (resultSet.next()) {
                managers.add(extractManagerFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching managers");
        }

        return managers;
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Users\\Hp\\IdeaProjects\\Event_management\\target\\event_management.db";
        EventManagerDAO managerDAO = new EventManagerDAO(url);

        Scanner scanner = new Scanner(System.in);
        int choice;
        char continueChoice;

        do {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEVENT MANAGER INFORMATION");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t1: Add Manager");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2: Delete Manager");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t3: Update Manager");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t4: Search Manager");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t5: Exit program");
            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your selection: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character


            switch (choice) {
                case 1:
                    // Add Manager
                    EventManager newManager = createManagerFromUserInput(scanner);
                    managerDAO.createManager(newManager);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tManager added successfully!");
                    break;
                case 2:
                    // Delete Manager
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Manager ID to delete: ");
                    int managerIdToDelete = scanner.nextInt();
                    managerDAO.deleteManager(managerIdToDelete);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tManager deleted successfully!");
                    break;
                case 3:
                    // Update Manager
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Manager ID to update: ");
                    int managerIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Ask which fields to update
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tWhich fields would you like to update?");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t1: Manager Name");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2: Username");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t3: Password");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t4: Update All Fields");
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your selection: ");
                    int updateChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Initialize variables for updated fields
                    String updatedManagerName = null;
                    String updatedUsername = null;
                    String updatedPassword = null;

                    // Process the user's choice
                    switch (updateChoice) {
                        case 1:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Manager Name: ");
                            updatedManagerName = scanner.nextLine();
                            break;
                        case 2:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Username: ");
                            updatedUsername = scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Password: ");
                            updatedPassword = scanner.nextLine();
                            break;
                        case 4:
                            // Update all fields
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Manager Name: ");
                            updatedManagerName = scanner.nextLine();

                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Username: ");
                            updatedUsername = scanner.nextLine();

                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Password: ");
                            updatedPassword = scanner.nextLine();
                            break;
                        default:
                            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. No fields will be updated.");
                            return;
                    }

                    // Update the manager with the specified fields
                    managerDAO.updateManager(managerIdToUpdate, updatedManagerName, updatedUsername, updatedPassword);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tManager updated successfully!");
                    break;
                case 4:
                    // Search Manager
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Manager ID to search: ");
                    int managerIdToSearch = scanner.nextInt();
                    EventManager searchedManager = managerDAO.readManager(managerIdToSearch);
                    if (searchedManager != null) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tFound Manager ");
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tManager ID: " + searchedManager.getManagerId());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tManager Name: " + searchedManager.getManagerName());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tUsername: " + searchedManager.getUsername());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tPassword: " + searchedManager.getPassword());
                    } else {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tManager not found!");
                    }
                    break;
                case 5:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tExiting program. Goodbye!");
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please try again.");
            }
            if (choice != 5) {
                System.out.print("\t\t\t\t\t\t\t\t\t\t\t\tDo you want to continue (y/n)? ");
                continueChoice = scanner.next().charAt(0);
                scanner.nextLine(); // Consume the newline character
            } else {
                continueChoice = 'n'; // Exit the loop if the user chose to exit
            }

        } while (Character.toLowerCase(continueChoice) == 'y');

        scanner.close();
    }
    private static EventManager createManagerFromUserInput(Scanner scanner) {
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Manager Name: ");
        String managerName = scanner.nextLine();

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Username: ");
        String username = scanner.nextLine();

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Password: ");
        String password = scanner.nextLine();

        return new EventManager(0, managerName, username, password);
    }

    private void createManagerTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                MANAGER_TABLE +
                " (" +
                MANAGER_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                MANAGER_NAME + " TEXT, " +
                USERNAME + " TEXT, " +
                PASSWORD + " TEXT)";
        executeCreateTableStatement(createTableSQL);
    }

    public void createManager(EventManager manager) {
        String insertManagerSQL = "INSERT INTO " +
                MANAGER_TABLE +
                "(" +
                MANAGER_NAME + ", " +
                USERNAME + ", " +
                PASSWORD +
                ") VALUES (?, ?, ?)";
        saveRecordAndGetId(insertManagerSQL, manager.getManagerName(), manager.getUsername(), manager.getPassword());
    }

    public EventManager readManager(int managerId) {
        String selectManagerSQL = "SELECT * FROM " + MANAGER_TABLE + " WHERE " + MANAGER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectManagerSQL)) {

            preparedStatement.setInt(1, managerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractManagerFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error reading manager");
        }

        return null;
    }

    public void updateManager(int managerId, String managerName, String username, String password) {
        String updateManagerSQL = "UPDATE " + MANAGER_TABLE + " SET ";

        if (managerName != null) {
            updateManagerSQL += MANAGER_NAME + " = '" + managerName + "', ";
        }

        if (username != null) {
            updateManagerSQL += USERNAME + " = '" + username + "', ";
        }

        if (password != null) {
            updateManagerSQL += PASSWORD + " = '" + password + "', ";
        }

        // Remove the trailing comma and space
        updateManagerSQL = updateManagerSQL.replaceAll(", $", "");

        updateManagerSQL += " WHERE " + MANAGER_ID + " = " + managerId;

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(updateManagerSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating manager");
        }
    }

    public void deleteManager(int managerId) {
        String deleteManagerSQL = "DELETE FROM " + MANAGER_TABLE + " WHERE " + MANAGER_ID + " = ?";
        executeDeleteStatement(deleteManagerSQL, managerId);
    }

    private void executeCreateTableStatement(String createTableSQL) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating table");
        }
    }

    private int saveRecordAndGetId(String insertSQL, Object... params) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating record failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting record");
            return -1;
        }
    }

    private void executeDeleteStatement(String deleteSQL, int managerId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, managerId);
            preparedStatement.executeUpdate();
            System.out.println("Manager deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting manager");
        }
    }

    private EventManager extractManagerFromResultSet(ResultSet resultSet) throws SQLException {
        int managerId = resultSet.getInt(MANAGER_ID);
        String managerName = resultSet.getString(MANAGER_NAME);
        String username = resultSet.getString(USERNAME);
        String password = resultSet.getString(PASSWORD);

        return new EventManager(managerId, managerName, username, password);
    }
}
