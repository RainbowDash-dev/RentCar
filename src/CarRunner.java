import dao.UsersDao;
import entity.RentStatusEntity;
import enums.StatusCodeEnums;
import service.RentService;
import service.RentStatusService;
import service.UserRentedCarsService;
import service.UserService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class CarRunner {



    private static final int MAX_ATTEMPTS = 3;
    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws SQLException {

        CarRunner carRunner = new CarRunner();


        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            System.out.print("""
                    1. Регистрация нового пользователя.
                    2. Вход в свой аккаунт.
                    3. Выход.
                    Выберите интересующее Вас меню.""");
            System.out.print(" Введите число от 1 до 3: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> UserService.getInstance().registerNewUserFromConsole();
                case "2" -> carRunner.accountMenu();
                case "3" -> {
                    System.out.println("Выход!");
                    return;
                }
                default -> System.out.println("Некорректный выбор. ");
            }
            if (i == MAX_ATTEMPTS - 1) {
                System.out.println("Превышено количество попыток. Программа завершена.\n");
                return;
            }
        }
    }


    public void accountMenu() {

        Long userId = UserService.getInstance().authenticateUser();
        if (userId == null) {
            return;
        }

        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {


            System.out.println("\nМеню Аккаунта.");
            System.out.print("""
                    1. Проверить Баланс
                    2. Список Ваших арендованных авто
                    3. Арендовать машину
                    4. Вернуть арендованную машину
                    5. Внести деньги на свой счет
                    6. Вернуться назад
                    """);
            System.out.print("Выберите интересующее Вас меню: ");
            String chooseAccountMenu = sc.nextLine();
            System.out.println();


            switch (chooseAccountMenu) {

                case "1" -> System.out.println("Ваш баланс = " + UsersDao.getInstance().getUserBalance(userId).get());
                case "2" -> UserRentedCarsService.getInstance().getUserRentedCars(userId);
                case "3" -> {
                    Long carId = RentService.getInstance().chooseCarTypeById(userId);
                    Long availableCarId = RentService.getInstance().chooseAvailableCarById(carId);

                    if (availableCarId == null) {
                        System.out.println("Автомобиль с таким ID не найден. Попробуйте другую машину.");
                        continue;
                    }

                    RentStatusService.getInstance().createOrUpdateRentStatus(userId, availableCarId, StatusCodeEnums.RESERVED);
                    boolean isComparedBalance = RentService.getInstance().balanceAndCostComparison(userId, carId);


                    if (isComparedBalance) {
                        BigDecimal subtractUserBalance = RentService.getInstance().subtractCostFromBalance(userId, carId);
                        RentService.getInstance().updateUserBalance(userId, subtractUserBalance);
                        RentService.getInstance().changeIsAvailable(availableCarId);
                    } else {
                        RentStatusService.getInstance().createOrUpdateRentStatus(userId, carId, StatusCodeEnums.PAYMENT_FAILED);
                        continue;
                    }

                    UserRentedCarsService.getInstance().insertRentedCarAndUser(userId, availableCarId);
                    RentStatusService.getInstance().createOrUpdateRentStatus(userId, availableCarId, StatusCodeEnums.RENTED);
                }
                case "4" -> UserRentedCarsService.getInstance().returnCar(userId);
                case "5" -> UserService.getInstance().addBalance(userId);
                case "6" -> {
                    return;
                }
                default -> {
                    if (attempts == MAX_ATTEMPTS - 1) {
                        System.out.println("Отказ! Превышено количество попыток. Программа завершена!\n");
                        return;
                    } else {
                        System.out.println("Некорректный ввод. Выберите цифры от 1 до 5.");
                        attempts++;
                    }
                }
            }
        }
    }
}

