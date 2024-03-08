package org.example.crud;
import org.example.model.Event;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class EventDAO {

    public static final String EVENT_TABLE = "events";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DATE_TIME = "eventDateTime";
    public static final String EVENT_LOCATION = "location";

    private final String url;

    public EventDAO(String url) {
        this.url = url;
        createEventTable();
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Users\\Hp\\IdeaProjects\\Event_management\\target\\event_management.db";
        EventDAO eventDAO = new EventDAO(url);

        Scanner scanner = new Scanner(System.in);
        int choice;
        char continueChoice;

        do {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEVENT INFORMATION");
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t1: Add Event");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2: Delete Event");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t3: Update Event");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t4: Search Event");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t5: Exit program");
            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your selection: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Add Event
                    Event newEvent = createEventFromUserInput(scanner);
                    eventDAO.createEvent(newEvent);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent added successfully!");
                    break;
                case 2:
                    // Delete Event
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Event ID to delete: ");
                    int eventIdToDelete = scanner.nextInt();
                    eventDAO.deleteEvent(eventIdToDelete);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent deleted successfully!");
                    break;
                case 3:
                    // Update Event
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Event ID to update: ");
                    int eventIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Ask which fields to update
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tWhich fields would you like to update?");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t1: Event Name");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2: Event Date and Time");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t3: Event Location");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t4: Update All Fields");
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your selection: ");
                    int updateChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Initialize variables for updated fields
                    String updatedEventName = null;
                    LocalDateTime updatedEventDateTime = null;
                    String updatedLocation = null;

                    // Process the user's choice
                    switch (updateChoice) {
                        case 1:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Event Name: ");
                            updatedEventName = scanner.nextLine();
                            break;
                        case 2:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Event Date and Time (YYYY-MM-DD HH:mm): ");
                            String dateTimeInput = scanner.nextLine();
                            updatedEventDateTime = LocalDateTime.parse(dateTimeInput);
                            break;
                        case 3:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Event Location: ");
                            updatedLocation = scanner.nextLine();
                            break;
                        case 4:
                            // Update all fields
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Event Name: ");
                            updatedEventName = scanner.nextLine();

                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Event Date and Time (YYYY-MM-DD HH:mm): ");
                            dateTimeInput = scanner.nextLine();
                            updatedEventDateTime = LocalDateTime.parse(dateTimeInput);

                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Event Location: ");
                            updatedLocation = scanner.nextLine();
                            break;
                        default:
                            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. No fields will be updated.");
                            return;
                    }

                    // Update the event with the specified fields
                    eventDAO.updateEvent(eventIdToUpdate, updatedEventName, updatedEventDateTime, updatedLocation);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent updated successfully!");
                    break;

                case 4:
                    // Search Event
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Event ID to search: ");
                    int eventIdToSearch = scanner.nextInt();
                    Event searchedEvent = eventDAO.readEvent(eventIdToSearch);
                    if (searchedEvent != null) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tFound Event ");
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent ID: " + searchedEvent.getEventId());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent Name: " + searchedEvent.getEventName());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent DateTime: " + searchedEvent.getEventDateTime());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent Location: " + searchedEvent.getLocation());
                    } else {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent not found!");
                    }
                    break;
                case 5:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tExiting program. Goodbye!");
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please try again.");
            } if (choice != 5) {
                System.out.print("\t\t\t\t\t\t\t\t\t\t\t\tDo you want to continue (y/n)? ");
                continueChoice = scanner.next().charAt(0);
                scanner.nextLine(); // Consume the newline character
            } else {
                continueChoice = 'n'; // Exit the loop if the user chose to exit
            }

        } while (Character.toLowerCase(continueChoice) == 'y');

        scanner.close();
    }


    private static Event createEventFromUserInput(Scanner scanner) {
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Event Name: ");
        String eventName = scanner.nextLine();

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Event Date and Time (YYYY-MM-DD HH:mm): ");
        String dateTimeInput = scanner.nextLine();
        LocalDateTime eventDateTime = LocalDateTime.parse(dateTimeInput);

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Event Location: ");
        String location = scanner.nextLine();

        return new Event(0, eventName, eventDateTime, location);
    }
    private void createEventTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                EVENT_TABLE +
                " (" +
                EVENT_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                EVENT_NAME + " TEXT, " +
                EVENT_DATE_TIME + " TEXT, " +
                EVENT_LOCATION + " TEXT)";
        executeCreateTableStatement(createTableSQL);
    }

    public void createEvent(Event event) {
        String insertEventSQL = "INSERT INTO " +
                EVENT_TABLE +
                "(" +
                EVENT_NAME + ", " +
                EVENT_DATE_TIME + ", " +
                EVENT_LOCATION +
                ") VALUES (?, ?, ?)";
        saveRecordAndGetId(insertEventSQL, event.getEventName(), event.getEventDateTime().toString(), event.getLocation());
    }

    public Event readEvent(int eventId) {
        String selectEventSQL = "SELECT * FROM " + EVENT_TABLE + " WHERE " + EVENT_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectEventSQL)) {

            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractEventFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error reading event");
        }

        return null;
    }

    public void updateEvent(int eventId, String eventName, LocalDateTime eventDateTime, String location) {
        String updateEventSQL = "UPDATE " + EVENT_TABLE + " SET ";

        if (eventName != null) {
            updateEventSQL += EVENT_NAME + " = '" + eventName + "', ";
        }

        if (eventDateTime != null) {
            updateEventSQL += EVENT_DATE_TIME + " = '" + eventDateTime.toString() + "', ";
        }

        if (location != null) {
            updateEventSQL += EVENT_LOCATION + " = '" + location + "', ";
        }

        // Remove the trailing comma and space
        updateEventSQL = updateEventSQL.replaceAll(", $", "");

        updateEventSQL += " WHERE " + EVENT_ID + " = " + eventId;

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            int rowsUpdated = statement.executeUpdate(updateEventSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating event");
        }
    }

    public void deleteEvent(int eventId) {
        String deleteEventSQL = "DELETE FROM " + EVENT_TABLE + " WHERE " + EVENT_ID + " = ?";
        executeDeleteStatement(deleteEventSQL, eventId);
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

    private void executeDeleteStatement(String deleteSQL, int eventId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tEvent deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting event");
        }
    }

    private Event extractEventFromResultSet(ResultSet resultSet) throws SQLException {
        int eventId = resultSet.getInt(EVENT_ID);
        String eventName = resultSet.getString(EVENT_NAME);
        String dateAndTimeStr = resultSet.getString(EVENT_DATE_TIME);
        LocalDateTime eventDateTime = (dateAndTimeStr != null) ? LocalDateTime.parse(dateAndTimeStr) : null;
        String location = resultSet.getString(EVENT_LOCATION);

        return new Event(eventId, eventName, eventDateTime, location);
    }

}