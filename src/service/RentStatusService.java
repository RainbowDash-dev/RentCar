package service;

import dao.RentStatusDao;
import entity.RentStatusEntity;
import enums.StatusCodeEnums;

import java.time.LocalDateTime;

public class RentStatusService {

    private static final RentStatusService INSTANCE = new RentStatusService();

    public static RentStatusService getInstance() {
        return INSTANCE;
    }

    public void createOrUpdateRentStatus(long userId, long availableCarId, StatusCodeEnums statusCodeEnums) {


        RentStatusEntity lastStatus = RentStatusDao.getInstance().findLatestByUserIdAndCarId(userId, availableCarId);

        RentStatusEntity newStatusEntity = new RentStatusEntity();
        newStatusEntity.setUserId(userId);
        newStatusEntity.setAvailableCarId(availableCarId);
        if (lastStatus == null) {
            newStatusEntity.setStartDate(LocalDateTime.now());
        } else {
            newStatusEntity.setStartDate(lastStatus.getStartDate());
        }
        newStatusEntity.setUpdateDate(LocalDateTime.now());
        newStatusEntity.setStatusCode(statusCodeEnums.getCode());
        newStatusEntity.setStatusDescription(statusCodeEnums.getDescription());

        RentStatusDao.getInstance().insertNewStatus(newStatusEntity);
    }
}
