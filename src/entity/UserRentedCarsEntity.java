package entity;

import java.time.LocalDateTime;

public class UserRentedCarsEntity {
    private long userId;
    private long availableCarId;
    private LocalDateTime startDate;

    public UserRentedCarsEntity(long userId, long availableCarId, LocalDateTime startDate) {
        this.userId = userId;
        this.availableCarId = availableCarId;
        this.startDate = startDate;
    }

    public UserRentedCarsEntity() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAvailableCarId() {
        return availableCarId;
    }

    public void setAvailableCarId(long availableCarId) {
        this.availableCarId = availableCarId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "UserRentedCarsEntity { " +
               "userId = " + userId +
               ", availableCarId = " + availableCarId +
               ", startDate = " + startDate +
               '}';
    }
}
