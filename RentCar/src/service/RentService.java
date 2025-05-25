package service;

import dao.AvailableCarsDao;
import dao.CarTypeDao;
import dao.UsersDao;
import dto.AvailableCarsDto;
import dto.CarTypeDto;
import entity.AvailableCarsEntity;
import entity.CarTypeEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RentService {
    Scanner sc = new Scanner(System.in);
    private static final RentService INSTANCE = new RentService();

    public static RentService getInstance() {
        return INSTANCE;
    }


    public List<CarTypeDto> getAllCarType() {
        List<CarTypeEntity> carTypeEntityList = CarTypeDao.getInstance().findAllCarType();
        return buildCarTypeDtoList(carTypeEntityList);
    }

    private List<CarTypeDto> buildCarTypeDtoList(List<CarTypeEntity> CarTypeEntityList) {
        List<CarTypeDto> list = new ArrayList<>();
        for (CarTypeEntity carTypeEntities : CarTypeEntityList) {
            CarTypeDto dt = new CarTypeDto();

            dt.setId(carTypeEntities.getId());
            dt.setMark(carTypeEntities.getMark());
            dt.setModel(carTypeEntities.getModel());
            dt.setYear(carTypeEntities.getYear());
            dt.setCost(carTypeEntities.getCost());
            dt.setMaxSpeed(carTypeEntities.getMaxSpeed());

            list.add(dt);

        }
        return list;
    }


    public List<AvailableCarsDto> getAvailableCars(Long carTypeId) {
        List<AvailableCarsEntity> availableCarsEntityList = AvailableCarsDao.getInstance().findByCarTypeId(carTypeId);
        return buildAvailableCarsDtoList(availableCarsEntityList);
    }

    private List<AvailableCarsDto> buildAvailableCarsDtoList(List<AvailableCarsEntity> availableCarsEntityList) {
        List<AvailableCarsDto> availableList = new ArrayList<>();
        for (AvailableCarsEntity availableCarsEntity : availableCarsEntityList) {
            AvailableCarsDto availableCarsDto = new AvailableCarsDto(
                    availableCarsEntity.getId(),
                    availableCarsEntity.getCarTypeId(),
                    availableCarsEntity.getPlateNumber(),
                    availableCarsEntity.getVinCode(),
                    availableCarsEntity.isAvailable());

            availableList.add(availableCarsDto);
        }
        return availableList;
    }


    public Long chooseCarTypeById(Long userID) {

        List<CarTypeDto> carsTypeId = RentService.getInstance().getAllCarType();
        for (CarTypeDto dto : carsTypeId) {
            System.out.println(dto);
        }
        System.out.print("\nВыберите availableCarId интересующей Вас машины: ");
        String chooseCarTypeID = sc.nextLine().trim();
        return Long.valueOf(chooseCarTypeID);
    }


    public Long chooseAvailableCarById(Long id) {

        List<AvailableCarsDto> availableCarsDtoList = RentService.getInstance().getAvailableCars(id);
        if (availableCarsDtoList.isEmpty()) {
            return null;
        }
        System.out.println("Список доступных машин: ");
        availableCarsDtoList.forEach(System.out::println);

        System.out.print("Выберите availableCarId доступной Вам машины: ");
        String inputAvailableCarId = sc.nextLine().trim();


        try {
            long matchId = Long.parseLong(inputAvailableCarId);
            boolean isMatchId = availableCarsDtoList.stream().anyMatch(listAvlCarsDto -> listAvlCarsDto.getId() == matchId);
            if (!isMatchId) {
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите число!");
        }


        List<AvailableCarsEntity> byCarTypeId = AvailableCarsDao.getInstance().findById(Long.parseLong(inputAvailableCarId));
        System.out.print("Вы выбрали машину: ");
        byCarTypeId.forEach(System.out::println);
        return Long.valueOf(inputAvailableCarId);

    }


    public void changeIsAvailable(Long id) {

        AvailableCarsDao.getInstance().updateIsAvailableByID(id);
    }

    public boolean balanceAndCostComparison(Long UserID, Long costID) {
        Optional<BigDecimal> userBalanceOptional = UsersDao.getInstance().getUserBalance(UserID);
        Optional<Long> costOptional = CarTypeDao.getInstance().getCost(costID);

        System.out.println("Проверка баланса... ");

        if (userBalanceOptional.isEmpty() || costOptional.isEmpty()) {
            System.out.println("Ошибка! Отсутствуют данные в таблице users (money_balance) или car_type (cost)");
            return false;
        }

        BigDecimal userBalance = userBalanceOptional.get();
        BigDecimal cost = BigDecimal.valueOf(costOptional.get());

        if (userBalance.compareTo(cost) < 0) {
            System.out.println("Отказано! На Вашем счете недостаточно средств.");
            return false;
        }
        System.out.println("Одобрено! Средств достаточно для аренды!");
        return true;
    }


    public BigDecimal subtractCostFromBalance(Long userID, Long costID) {
        Optional<BigDecimal> userBalanceOptional = UsersDao.getInstance().getUserBalance(userID);
        Optional<Long> costOptional = CarTypeDao.getInstance().getCost(costID);

        BigDecimal userBalance = userBalanceOptional.get();
        BigDecimal cost = BigDecimal.valueOf(costOptional.get());

        return userBalance.subtract(cost);
    }

    public void updateUserBalance(Long userID, BigDecimal newBalance) {
        UsersDao.getInstance().updateUserBalance(userID, newBalance);
    }


}







