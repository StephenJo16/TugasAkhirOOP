package Employee;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Main.DatabaseManager;




public class EmployeeService {
    private Connection connection;

    public EmployeeService() {
        try {
            connection = DatabaseManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int currentEmployeeId = 1;
    public String getEmployeeBranchLocation() {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT branch_location FROM employees WHERE id = ?")) {
            preparedStatement.setInt(1, currentEmployeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("branch_location");
            } else {
                System.out.println("Employee with ID " + currentEmployeeId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void manageEmployees() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("1. Tambah Employee");
            System.out.println("2. Hapus Employee");
            System.out.println("3. Tampilkan Semua Employees");
            System.out.println("4. Kembali");
            System.out.print("Pilih menu: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    deleteEmployee();
                    break;
                case 3:
                    displayAllEmployees();
                    break;
                case 4:
                    System.out.println("Kembali ke menu utama manajemen employee.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (choice != 4);
    }

    private void addEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama employee: ");
        String employeeName = scanner.nextLine();
        System.out.print("Masukkan cabang tempat bekerja: ");
        String branchLocation = scanner.nextLine();
        insertEmployeeToDatabase(employeeName, branchLocation);
        System.out.println("Employee " + employeeName + " berhasil ditambahkan!");
    }

    private void insertEmployeeToDatabase(String employeeName, String branchLocation) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employees (name, branch_location) VALUES (?, ?)")) {
            preparedStatement.setString(1, employeeName);
            preparedStatement.setString(2, branchLocation);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan ID employee yang akan dihapus: ");
        int employeeId = scanner.nextInt();
        deleteEmployeeFromDatabase(employeeId);

        System.out.println("Employee dengan ID " + employeeId + " berhasil dihapus!");
    }

    private void deleteEmployeeFromDatabase(int employeeId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employees WHERE id = ?")) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayAllEmployees() {
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM employees";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("========== Semua Employees ==========");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String employeeName = resultSet.getString("name");
                String branchLocation = resultSet.getString("branch_location");

                System.out.println("ID: " + id + ", Name: " + employeeName + ", Branch: " + branchLocation);
            }
            System.out.println("=====================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

