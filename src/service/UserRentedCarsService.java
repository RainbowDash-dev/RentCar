package service;

import dao.UserRentedCarsDao;
import dto.UserRentedCarsDto;
import entity.UserRentedCarsEntity;
import enums.StatusCodeEnums;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class UserRentedCarsService {

    private static final UserRentedCarsService INSTANCE = new UserRentedCarsService();
    private static final int MAX_ATTEMPTS = 3;
    Scanner sc = new Scanner(System.in);


    public static UserRentedCarsService getInstance() {
        return INSTANCE;
    }

    public void insertRentedCarAndUser(long userId, long availableCarId) {
        UserRentedCarsEntity newRentedEntity = new UserRentedCarsEntity();
        newRentedEntity.setUserId(userId);
        newRentedEntity.setAvailableCarId(availableCarId);
        newRentedEntity.setStartDate(LocalDateTime.now());
        UserRentedCarsDao.getInstance().insertUserAndCar(newRentedEntity);
    }

    public List<UserRentedCarsDto> getUserRentedCars(long userId) {
        List<UserRentedCarsDto> rentedCarsByUserId = UserRentedCarsDao.getInstance().getRentedCarsByUserId(userId);
        if (rentedCarsByUserId.isEmpty()) {
            System.out.println("У Вас нет арендованных машин.");
            return null;
        }
        rentedCarsByUserId.forEach(System.out::println);
        return rentedCarsByUserId;
    }

    public void returnCar(long userId) {

        List<UserRentedCarsDto> userRentedCars = getUserRentedCars(userId);
        if (userRentedCars == null) {
            return;
        }

        List<Long> existIds = userRentedCars.stream().
                map(UserRentedCarsDto::carId).toList();

        int attempts = 0;

        System.out.print("Введите ID машины которую хотите вернуть: ");

        while (attempts < MAX_ATTEMPTS) {
            try {
                long inputCarId = Long.parseLong(sc.nextLine());
                if (existIds.contains(inputCarId)) {
                    UserRentedCarsDao.getInstance().delete(userId, inputCarId);
                    RentStatusService.getInstance().createOrUpdateRentStatus(userId, inputCarId, StatusCodeEnums.END_RENT);
                    RentService.getInstance().changeIsAvailable(inputCarId);
                    System.out.println("Машина с ID " + inputCarId + " успешно возвращена!");
                    return;
                }
                else {
                    System.out.println("Ошибка. У вас нет машины с ID " + inputCarId);
                }

            } catch (NumberFormatException e) {
                System.out.println("Ошибка. Введите число (ID машины).");
            }
            attempts++;
            if (attempts < MAX_ATTEMPTS) {
                System.out.print("Попробуйте еще раз: ");
            }
        }
        System.out.println("Превышено количество попыток. Возврат машины отменён!");
    }
}
