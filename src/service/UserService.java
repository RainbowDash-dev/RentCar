package service;

import dao.UsersDao;
import entity.UsersEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;

public class UserService {

    private static final UserService INSTANCE = new UserService();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_ATTEMPTS = 3;
    private static final int MAX_NAMES_LENGTH = 15;
    private static final BigDecimal MAX_BALANCE = new BigDecimal("100000");
    private String regexNames = "^[a-zA-Zа-яА-Я]+$";
    private static final String DIGIT_REGEX = "^[0-9]+(\\.[0-9]{2})?$";
    private static final String DIGIT_LENGTH_REGEX = "^[0-9]{6}$";
    private static final String POSITIVE_DECIMAL_REGEX = "^\\d+(\\.\\d{1,2})?$";


    Scanner scanner = new Scanner(System.in);
    UsersEntity newUser = new UsersEntity();

    public static UserService getInstance() {
        return INSTANCE;
    }

    //--------------------------- Регистрация ----------------------------

    public void registerNewUserFromConsole() {

        System.out.println("Регистрация нового пользователя");
        System.out.println("Введите Ваши данные");

        //--------------------------- Имя ----------------------------

        for (int i = 0; i <= MAX_ATTEMPTS; i++) {

            if (i == MAX_ATTEMPTS) {
                System.out.println("Отказано. Слишком много попыток. Попробуйте позже.\n ");
                return;
            }
            System.out.print("Имя: ");
            String firstName = scanner.nextLine().trim();

            if (firstName.isEmpty()) {
                System.out.print("Поле не может быть пустым. Попробуйте еще раз. ");
                continue;
            }
            if (firstName.length() > MAX_NAMES_LENGTH) {
                System.out.printf("Имя не должно превышать %d символов. Попробуйте еще раз. ", MAX_NAMES_LENGTH);
                continue;
            }
            if (firstName.matches(regexNames)) {
                newUser.setFirstName(firstName);
                break;
            }
            System.out.print("Имя должно содержать только буквы. ");
        }

        //------------------------- Фамилия --------------------------

        for (int i = 0; i <= MAX_ATTEMPTS; i++) {
            if (i == MAX_ATTEMPTS) {
                System.out.println("Отказано. Слишком много попыток. Попробуйте позже.");
                return;
            }
            System.out.print("Фамилия: ");
            String lastName = scanner.nextLine().trim();

            if (lastName.isEmpty()) {
                System.out.print("Поле не может быть пустым. Попробуйте еще раз. ");
                continue;
            }
            if (lastName.length() > MAX_NAMES_LENGTH) {
                System.out.printf("Фамилия не должна превышать %d символов. Попробуйте еще раз. ", MAX_NAMES_LENGTH);
                continue;
            }
            if (lastName.matches(regexNames)) {
                newUser.setLastName(lastName);
                break;
            }
            System.out.print("Фамилия должна содержать только буквы. Попробуйте еще раз. ");
        }

        //---------------------- Дата рождения -----------------------

        LocalDate strictDate = readDateWithAttempts(scanner);
        if (strictDate == null) {
            return;
        }
        newUser.setBirthDate(strictDate);

        //---------------------- Номер прав --------------------------

        for (int i = 0; i <= MAX_ATTEMPTS; i++) {
            if (i == MAX_ATTEMPTS) {
                System.out.println("Отказано. Слишком много попыток. Попробуйте позже.");
                return;
            }
            System.out.print("Номер прав (6 цифр): ");
            String driverLicense = scanner.nextLine().trim();

            if (driverLicense.isEmpty()) {
                System.out.println("Поле не может быть пустым. Попробуйте еще раз. ");
                continue;
            }
            if (!driverLicense.matches(DIGIT_LENGTH_REGEX)) {
                System.out.println("Номер прав должен содержать только 6 цифр. Попробуйте еще раз. ");
                continue;
            }
            newUser.setDrivingLicense(driverLicense);
            break;
        }


        //---------------------- Внесенная сумма ---------------------

        for (int i = 0; i <= MAX_ATTEMPTS; i++) {
            if (i == MAX_ATTEMPTS) {
                System.out.println("Отказано. Слишком много попыток. Попробуйте позже.");
                return;
            }
            System.out.print("Внесенная сумма: ");
            String balance = scanner.nextLine();

            if (balance.isEmpty()) {
                System.out.print("Поле не может быть пустым. Попробуйте еще раз. ");
                continue;
            }
            if (!balance.matches(DIGIT_REGEX)) {
                System.out.print("Сумма должна содержать только цифры. Попробуйте еще раз. ");
                continue;
            }
            BigDecimal money = new BigDecimal(balance);
            if (money.compareTo(MAX_BALANCE) > 0) {
                System.out.print("Внесенная сумма не должна превышать " + MAX_BALANCE + " Попробуйте еще раз: ");
                continue;
            }
            newUser.setMoneyBalance(new BigDecimal(balance));
            break;
        }

        //-------------------------- Логин ---------------------------

        System.out.print("Логин: ");
        newUser.setLogin(scanner.nextLine().trim());

        //-------------------------- Пароль --------------------------

        System.out.print("Пароль: ");
        newUser.setPassword(scanner.nextLine());


        UsersDao.getInstance().insertNewUser(newUser);
        System.out.println("\nПользователь успешно зарегистрирован с ID: " + newUser.getId());
    }

    private LocalDate readDateWithAttempts(Scanner scanner) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Дата рождения в формате ГГГГ-ММ-ДД: ");
            String input = scanner.nextLine().trim();

            try {
                LocalDate date = LocalDate.parse(input, DATE_FORMAT);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Дата не может быть в будущем. Попробуйте еще раз.");
                    attempts++;
                    continue;
                }
                if (date.getYear() < 1950) {
                    System.out.println("Слишком старая дата. Попробуйте еще раз.");
                    attempts++;
                    continue;
                }
                if (date.plusYears(18).isAfter(LocalDate.now())) {
                    System.out.println("Отказано. Вам должно быть 18+ лет.");
                    return null;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат. Пример \"1990-01-30\"");
                attempts++;
            }
        }
        System.out.println("Слишком много попыток ввода. Регистрация прекращена");
        return null;
    }

    //------------------------- Авторизация --------------------------

    public Long authenticateUser() {

        var sc = new Scanner(System.in);

        System.out.println("Вход в свой аккаунт.");

        for (int i = 0; i <= MAX_ATTEMPTS; i++) {
            if (i == MAX_ATTEMPTS) {
                System.out.println("Превышено количество попыток. Отказано в доступе.");
                return null;
            }

            System.out.print("Введите логин: ");
            String myLogin = sc.nextLine().trim();

            if (myLogin.isEmpty()) {
                System.out.print("Поле Логин не может быть пустым. ");
                continue;
            }
            if (!UsersDao.getInstance().isLoginExists(myLogin)) {
                System.out.print("Такого Логина не существует. ");
                continue;
            }

            System.out.print("Введите пароль: ");
            String myPassword = sc.nextLine().trim();

            if (myPassword.isEmpty()) {
                System.out.println("Поле Пароля не может быть пустым.");
                continue;
            }

            Optional<Long> id = UsersDao.getInstance().isSignInUser(myLogin, myPassword);
            if (id.isPresent()) {
                System.out.printf("Вы успешно вошли в аккаунт %s%n", myLogin);
                return id.get();
            }
            System.out.print("Неверный пароль. ");
        }
        return null;
    }

   /* public void deleteUser(long userId) {
        if (UserRentedCarsService.getInstance().getUserRentedCars(userId) == null) {
            List<String> loginList = UsersDao.getInstance().getUser(userId).stream().map(UsersEntity::getLogin).toList();
            UsersDao.getInstance().deleteUserById(userId);
            System.out.println("User с логином " + loginList + " успешно удален!");
        } else {
            System.out.println("Невозможно удалить пользователя, так как у него еще имеются арендованные машины.");
        }
    }*/

    public void addBalance(long userId) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Какую сумму Вы хотите внести: ");
            String inputNewSum = scanner.nextLine();

            if (inputNewSum.isEmpty()) {
                System.out.print("Поле не может быть пустым. ");
                attempts++;
                continue;
            }


            try {
                BigDecimal decimalInput = new BigDecimal(inputNewSum);
                if (decimalInput.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Ошибка. Сумма должна быть больше нуля.");
                    attempts++;
                    continue;
                }
                Optional<BigDecimal> myBalance = UsersDao.getInstance().getUserBalance(userId);
                BigDecimal decimalMyBalance = myBalance.orElse(BigDecimal.ZERO);
                UsersDao.getInstance().updateUserBalance(userId, decimalInput.add(decimalMyBalance.setScale(2, RoundingMode.HALF_UP)));
                System.out.println("\nСумма успешно внесена.");
                return;

            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Некорректный формат числа! ");
            }
            if (!inputNewSum.matches(POSITIVE_DECIMAL_REGEX)) {
                System.out.println("Сумма должна быть в формате 99 | 99.9 | 99.99");
                attempts++;
            }
        }
        System.out.println("Отказано. Превышено количество попыток.");
    }
}
