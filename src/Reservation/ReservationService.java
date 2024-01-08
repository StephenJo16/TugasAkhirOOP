package Reservation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Main.DatabaseManager;
import Employee.EmployeeService;
import java.util.*;

public class ReservationService {
    private Connection connection;

    public ReservationService() {
        try {
            connection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void manageReservations(EmployeeService employeeService) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("1. Tambah Reservasi");
            System.out.println("2. Ubah Status Reservasi");
            System.out.println("3. Check Out");
            System.out.println("4. Tampilkan Semua Reservasi");
            System.out.println("5. Kembali");
            System.out.print("Pilih menu: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addReservation(employeeService);
                    break;
                case 2:
                    updateReservationStatus();
                    break;
                case 3:
                    checkOut();
                    break;
                case 4:
                    displayAllReservations();
                    break;
                case 5:
                    System.out.println("Kembali ke menu utama manajemen reservasi.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (choice != 5);
    }

    private void addReservation(EmployeeService employeeService) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama pemesan: ");
        String customerName = scanner.nextLine();
        System.out.print("Masukkan jumlah meja: ");
        int tableCount = scanner.nextInt();
        System.out.print("Masukkan tipe meja (Romantic/General/Family): ");
        String tableType = scanner.next();
        System.out.print("Masukkan jumlah orang per meja: ");
        int peopleCount = scanner.nextInt();
        String branchLocation = employeeService.getEmployeeBranchLocation();
        insertReservationToDatabase(customerName, tableCount, tableType, peopleCount, branchLocation);

        System.out.println("Reservasi berhasil ditambahkan!");
    }

    private void insertReservationToDatabase(String customerName, int tableCount, String tableType, int peopleCount, String branchLocation) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reservations (customer_name, table_count, table_type, people_count, branch_location, status) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, customerName);
            preparedStatement.setInt(2, tableCount);
            preparedStatement.setString(3, tableType);
            preparedStatement.setInt(4, peopleCount);
            preparedStatement.setString(5, branchLocation);
            preparedStatement.setString(6, ReservationStatus.IN_RESERVE.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateReservationStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan ID reservasi yang akan diubah: ");
        int reservationId = scanner.nextInt();
        System.out.print("Masukkan status baru (IN_RESERVE/IN_ORDER/FINALIZED): ");
        String newStatus = scanner.next();
        updateReservationStatusInDatabase(reservationId, newStatus);

        System.out.println("Status reservasi berhasil diubah!");
    }

    private void updateReservationStatusInDatabase(int reservationId, String newStatus) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE reservations SET status = ? WHERE id = ?")) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, reservationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkOut() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan ID reservasi yang akan di-check out: ");
        int reservationId = scanner.nextInt();
        checkOutReservation(reservationId);
    }
    public enum ReservationStatus {
        PENDING, CONFIRMED, FINALIZED, IN_RESERVE;
    }
    private void checkOutReservation(int reservationId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reservations WHERE id = ?")) {
            preparedStatement.setInt(1, reservationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String customerName = resultSet.getString("customer_name");
                int tableCount = resultSet.getInt("table_count");
                String tableType = resultSet.getString("table_type");
                int peopleCount = resultSet.getInt("people_count");
                ReservationStatus reservationStatus = ReservationStatus.valueOf(resultSet.getString("status"));
                double totalPrice = calculateTotalPrice(tableType, peopleCount);
                displayBill(customerName, tableCount, tableType, peopleCount, totalPrice);
                updateReservationStatusInDatabase(reservationId, ReservationStatus.FINALIZED.toString());

                System.out.println("Check out berhasil!");
            } else {
                System.out.println("Reservasi dengan ID " + reservationId + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private double calculateTotalPrice(String tableType, int peopleCount) {
        double basePrice;
        switch (tableType) {
            case "Romantic":
                basePrice = 100000.0;
                break;
            case "General":
                basePrice = 75000.0;
                break;
            case "Family":
                basePrice = 120000.0;
                break;
            default:
                basePrice = 0.0;
        }

        return basePrice * peopleCount;
    }

    private void displayBill(String customerName, int tableCount, String tableType, int peopleCount, double totalPrice) {
        System.out.println("========== Bill ==========");
        System.out.println("Customer: " + customerName);
        System.out.println("Table Count: " + tableCount);
        System.out.println("Table Type: " + tableType);
        System.out.println("People Count: " + peopleCount);
        System.out.println("Total Price: Rp " + totalPrice);
        System.out.println("===========================");
    }

    private void displayAllReservations() {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM reservations";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("========== Semua Reservasi ==========");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customer_name");
                int tableCount = resultSet.getInt("table_count");
                String tableType = resultSet.getString("table_type");
                int peopleCount = resultSet.getInt("people_count");
                String status = resultSet.getString("status");

                System.out.println("ID: " + id + ", Customer: " + customerName + ", Table Count: " + tableCount + ", Table Type: " + tableType + ", People Count: " + peopleCount + ", Status: " + status);
            }
            System.out.println("=====================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}