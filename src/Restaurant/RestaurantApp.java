package Restaurant;
import Employee.EmployeeService;
import Restaurant.RestaurantService;
import Reservation.ReservationService;
import java.util.Scanner;

public class RestaurantApp {
    private RestaurantService restaurantService;
    private ReservationService reservationService;
    private EmployeeService employeeService;

    public RestaurantApp() {
        this.restaurantService = new RestaurantService();
        this.reservationService = new ReservationService();
        this.employeeService = new EmployeeService();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("1. Kelola Menu");
            System.out.println("2. Kelola Reservasi");
            System.out.println("3. Kelola Employee");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    restaurantService.manageMenu();
                    break;
                case 2:
                    reservationService.manageReservations(employeeService);
                    break;
                case 3:
                    employeeService.manageEmployees();
                    break;
                case 4:
                    System.out.println("Keluar dari aplikasi. Terima kasih!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (choice != 4);
    }
}