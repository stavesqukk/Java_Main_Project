package org.example;

import org.example.model.Event;
import org.example.model.Participant;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main1 {

    public static final String EVENT_TABLE = "events";
    public static final String PARTICIPANT_TABLE = "participants";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DATE_TIME = "eventDateTime";
    public static final String EVENT_LOCATION = "location";

    public static final String PARTICIPANT_ID = "participantId";
    public static final String PARTICIPANT_NAME = "participantName";
    public static final String PARTICIPANT_EMAIL = "email";

    public static void main(String[] args) {
        // Connect to the database
        String url = "jdbc:sqlite:C:\\Users\\Hp\\IdeaProjects\\Event_management\\target\\event_management.db";

        // Create tables if they don't exist
        createEventTable(url);
        createParticipantTable(url);

        // Insert a sample event
        Event event = new Event(11771, "Sample Event", LocalDateTime.of(20444, 3, 10, 15, 30), "Sample Location");
        saveEvent(url, event);

        // Insert participants for the event
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(8881, "Participant3", "participant3@example.com"));
        participants.add(new Participant(999, "Participant4", "participant42@example.com"));
        linkParticipantsToEvent(url, event.getEventId(), participants);

        // Retrieve events
        List<Event> events = getEvents(url);
        for (Event e : events) {
            System.out.println("Event ID: " + e.getEventId());
            System.out.println("Event Name: " + e.getEventName());
            System.out.println("Event Date Time: " + e.getEventDateTime());
            System.out.println("Event Location: " + e.getLocation());
            System.out.println("------------");
        }
    }

    private static void createEventTable(String url) {
        // Create event table SQL
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                EVENT_TABLE +
                " (" +
                EVENT_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                EVENT_NAME + " TEXT, " +
                EVENT_DATE_TIME + " TEXT, " +
                EVENT_LOCATION + " TEXT)";
        // Execute SQL statement
        executeCreateTableStatement(url, createTableSQL);
    }

    private static void createParticipantTable(String url) {
        // Create participant table SQL
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                PARTICIPANT_TABLE +
                " (" +
                PARTICIPANT_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                PARTICIPANT_NAME + " TEXT, " +
                PARTICIPANT_EMAIL + " TEXT)";
        // Execute SQL statement
        executeCreateTableStatement(url, createTableSQL);
    }

    private static void executeCreateTableStatement(String url, String createTableSQL) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating table");
        }
    }

    private static void saveEvent(String url, Event event) {
        // Save event SQL
        String insertEventSQL = "INSERT INTO " +
                EVENT_TABLE +
                "(" +
                EVENT_NAME + ", " +
                EVENT_DATE_TIME + ", " +
                EVENT_LOCATION +
                ") VALUES (?, ?, ?)";
        // Execute SQL statement
        saveRecordAndGetId(url, insertEventSQL, event.getEventName(), event.getEventDateTime().toString(), event.getLocation());
    }

    private static void linkParticipantsToEvent(String url, int eventId, List<Participant> participants) {
        // Link participants to event SQL
        String linkParticipantsSQL = "INSERT INTO event_participants (eventId, participantId) VALUES (?, ?)";
        // Execute SQL statement
        for (Participant participant : participants) {
            saveRecordAndGetId(url, linkParticipantsSQL, eventId, participant.getParticipantId());
        }
    }

    private static int saveRecordAndGetId(String url, String insertSQL, Object... params) {
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

    private static List<Event> getEvents(String url) {
        List<Event> events = new ArrayList<>();

        // Select events SQL
        String selectEventsSQL = "SELECT * FROM " + EVENT_TABLE;
        // Execute SQL statement
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectEventsSQL)) {

            while (resultSet.next()) {
                int eventId = resultSet.getInt(EVENT_ID);
                String eventName = resultSet.getString(EVENT_NAME);
                LocalDateTime eventDateTime = LocalDateTime.parse(resultSet.getString(EVENT_DATE_TIME));
                String location = resultSet.getString(EVENT_LOCATION);

                Event event = new Event(eventId, eventName, eventDateTime, location);
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching records");
        }

        return events;
    }
}
