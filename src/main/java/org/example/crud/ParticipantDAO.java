package org.example.crud;

import org.example.model.Participant;

import java.sql.*;
import java.util.Scanner;

public class ParticipantDAO {




    public static final String PARTICIPANT_TABLE = "participants";
    public static final String PARTICIPANT_ID = "participantId";
    public static final String PARTICIPANT_NAME = "participantName";
    public static final String PARTICIPANT_EMAIL = "email";


    private final String url;

    public ParticipantDAO(String url) {
        this.url = url;
        createParticipantTable();
    }

    public void createParticipant(Participant participant) {
        String insertParticipantSQL = "INSERT INTO " +
                PARTICIPANT_TABLE +
                "(" +
                PARTICIPANT_NAME + ", " +
                PARTICIPANT_EMAIL +
                ") VALUES (?, ?)";

        saveRecordAndGetId(insertParticipantSQL, participant.getParticipantName(), participant.getEmail());
    }


    public Participant readParticipant(int participantId) {
        String selectParticipantSQL = "SELECT * FROM " + PARTICIPANT_TABLE + " WHERE " + PARTICIPANT_ID + " = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectParticipantSQL)) {

            preparedStatement.setInt(1, participantId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractParticipantFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error reading participant");
        }

        return null;
    }

    public void updateParticipant(int participantId, String participantName, String participantEmail, String participantPhone) {
        String updateParticipantSQL = "UPDATE " + PARTICIPANT_TABLE + " SET ";

        if (participantName != null) {
            updateParticipantSQL += PARTICIPANT_NAME + " = '" + participantName + "', ";
        }

        if (participantEmail != null) {
            updateParticipantSQL += PARTICIPANT_EMAIL + " = '" + participantEmail + "', ";
        }

        // Remove the trailing comma and space
        updateParticipantSQL = updateParticipantSQL.replaceAll(", $", "");

        updateParticipantSQL += " WHERE " + PARTICIPANT_ID + " = " + participantId;

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            int rowsUpdated = statement.executeUpdate(updateParticipantSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating participant");
        }
    }

    public void deleteParticipant(int participantId) {
        String deleteParticipantSQL = "DELETE FROM " + PARTICIPANT_TABLE + " WHERE " + PARTICIPANT_ID + " = ?";
        executeDeleteStatement(deleteParticipantSQL, participantId);
    }

    private void createParticipantTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " +
                PARTICIPANT_TABLE +
                " (" +
                PARTICIPANT_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                PARTICIPANT_NAME + " TEXT, " +
                PARTICIPANT_EMAIL + " TEXT)";
        executeCreateTableStatement(createTableSQL);
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

    private void executeDeleteStatement(String deleteSQL, int participantId) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, participantId);
            preparedStatement.executeUpdate();
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting participant");
        }
    }

    private Participant extractParticipantFromResultSet(ResultSet resultSet) throws SQLException {
        int participantId = resultSet.getInt(PARTICIPANT_ID);
        String participantName = resultSet.getString(PARTICIPANT_NAME);
        String email = resultSet.getString(PARTICIPANT_EMAIL);

        return new Participant(participantId, participantName, email);
    }

    private void executeCreateTableStatement(String createTableSQL) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
//            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant table created");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating participant table");
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Users\\Hp\\IdeaProjects\\Event_management\\target\\event_management.db";
        ParticipantDAO participantDAO = new ParticipantDAO(url);

        Scanner scanner = new Scanner(System.in);
        int choice;
        char continueChoice;

        do {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\tPARTICIPANT INFORMATION\t\t\t\t\t\t\t\t\t\t\t\t\t");
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t1: Add Participant\t\t\t\t\t\t\t\t\t\t\t\t\t");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2: Delete Participant\t\t\t\t\t\t\t\t\t\t\t\t\t");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t3: Update Participant\t\t\t\t\t\t\t\t\t\t\t\t\t");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t4: Search Participant\t\t\t\t\t\t\t\t\t\t\t\t\t");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t5: Exit program\t\t\t\t\t\t\t\t\t\t\t\t\t");
            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your selection: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Add Participant
                    Participant newParticipant = createParticipantFromUserInput(scanner);
                    participantDAO.createParticipant(newParticipant);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant added successfully!");
                    break;
                case 2:
                    // Delete Participant
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Participant ID to delete: ");
                    int participantIdToDelete = scanner.nextInt();
                    participantDAO.deleteParticipant(participantIdToDelete);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant deleted successfully!");
                    break;
                case 3:
                    // Update Participant
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Participant ID to update: ");
                    int participantIdToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Ask which fields to update
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tWhich fields would you like to update?");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t1: Participant Name");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2: Participant Email");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t3: Participant Phone");
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t4: Update All Fields");
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your selection: ");
                    int updateChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Initialize variables for updated fields
                    String updatedParticipantName = null;
                    String updatedParticipantEmail = null;
                    String updatedParticipantPhone = null;

                    // Process the user's choice
                    switch (updateChoice) {
                        case 1:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Participant Name: ");
                            updatedParticipantName = scanner.nextLine();
                            break;
                        case 2:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Participant Email: ");
                            updatedParticipantEmail = scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Participant Phone: ");
                            updatedParticipantPhone = scanner.nextLine();
                            break;
                        case 4:
                            // Update all fields
                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Participant Name: ");
                            updatedParticipantName = scanner.nextLine();

                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Participant Email: ");
                            updatedParticipantEmail = scanner.nextLine();

                            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter new Participant Phone: ");
                            updatedParticipantPhone = scanner.nextLine();
                            break;
                        default:
                            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. No fields will be updated.");
                            return;
                    }

                    // Update the participant with the specified fields
                    participantDAO.updateParticipant(participantIdToUpdate, updatedParticipantName, updatedParticipantEmail, updatedParticipantPhone);
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant updated successfully!");
                    break;

                case 4:
                    // Search Participant
                    System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the Participant ID to search: ");
                    int participantIdToSearch = scanner.nextInt();
                    Participant searchedParticipant = participantDAO.readParticipant(participantIdToSearch);
                    if (searchedParticipant != null) {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tFound Participant ");
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant ID: " + searchedParticipant.getParticipantId());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant Name: " + searchedParticipant.getParticipantName());
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant Email: " + searchedParticipant.getEmail());
                    } else {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tParticipant not found!");
                    }
                    break;
                case 5:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tExiting program. Goodbye!");
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please try again.");
            }if (choice != 5) {
                System.out.print("\t\t\t\t\t\t\t\t\t\t\t\tDo you want to continue (y/n)? ");
                continueChoice = scanner.next().charAt(0);
                scanner.nextLine(); // Consume the newline character
            } else {
                continueChoice = 'n'; // Exit the loop if the user chose to exit
            }

        } while (Character.toLowerCase(continueChoice) == 'y');

        scanner.close();
    }

    private static Participant createParticipantFromUserInput(Scanner scanner) {

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Participant Id: ");
        int participantId = Integer.parseInt(scanner.nextLine());

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Participant Name: ");
        String participantName = scanner.nextLine();

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Participant Email: ");
        String email = scanner.nextLine();

        return new Participant(participantId, participantName, email);
    }


}