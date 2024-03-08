package org.example;

import org.example.crud.EventManagerDAO;
import org.example.crud.ParticipantDAO;
import org.example.crud.EventDAO;

import java.util.Scanner;

public class EventManagementApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char continueChoice;

        do {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\tWELCOME TO EVENT MANAGEMENT COMPANY");
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\tWHAT IS YOUR ROLE IN THIS EVENT?");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t1. Event Manager");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2. Participant of Event");
            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");

            int roleChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (roleChoice) {
                case 1:
                    // Event Manager
                    showEventManagerOptions(scanner);
                    break;
                case 2:
                    // Participant
                    ParticipantDAO.main(new String[0]);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Exiting application.");
            }

            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tDo you want to continue (y/n)? ");
            continueChoice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume the newline character

        } while (Character.toLowerCase(continueChoice) == 'y');

        scanner.close();
    }

    private static void showEventManagerOptions(Scanner scanner) {
        char continueChoice;

        do {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEvent Manager Options:");
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t1. Your Account");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t2. Event Information");
            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");

            int managerChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (managerChoice) {
                case 1:
                    // Your Account
                    EventManagerDAO.main(new String[0]);
                    break;
                case 2:
                    EventDAO.main(new String[0]);
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Exiting application.");
            }

            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\tDo you want to continue (y/n)? ");
            continueChoice = scanner.next().charAt(0);
            scanner.nextLine(); // Consume the newline character

        } while (Character.toLowerCase(continueChoice) == 'y');
    }
}