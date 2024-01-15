package com.appliances.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AppliancesService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ann";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "MySQL@annmary1";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database");

            createTable(connection); // Create the table if not exists

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("1. Add Appliance");
                System.out.println("2. View Appliances");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addAppliance(connection);
                        break;
                    case 2:
                        viewAppliances(connection);
                        break;
                    case 3:
                        System.out.println("Exiting the program");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 3);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS appliances ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "brand VARCHAR(255),"
                + "type VARCHAR(255),"
                + "price DOUBLE"
                + ")";

        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.execute();
        }
    }

    private static void addAppliance(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Appliance Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter Type: ");
        String type = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();

        String insertQuery = "INSERT INTO appliances (name, brand, type, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, name);
            statement.setString(2, brand);
            statement.setString(3, type);
            statement.setDouble(4, price);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appliance added successfully!");
            } else {
                System.out.println("Failed to add the appliance.");
            }
        }
    }

    private static void viewAppliances(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM appliances";

        try (PreparedStatement statement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Appliance List:");
            System.out.println("ID\tName\tBrand\tType\tPrice");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String brand = resultSet.getString("brand");
                String type = resultSet.getString("type");
                double price = resultSet.getDouble("price");

                System.out.println(id + "\t" + name + "\t" + brand + "\t" + type + "\t" + price);
            }
        }
    }
}
