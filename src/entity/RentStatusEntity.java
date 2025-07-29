package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentStatusEntity {

    private long id;
    private long userId;
    private long availableCarId;
    private LocalDateTime startDate;
    private LocalDateTime updateDate;
    private String statusCode;
    private String statusDescription;

    public RentStatusEntity(long id, long userId, long availableCarId, LocalDateTime startDate,
                            LocalDateTime updateDate, String statusCode, String statusDescription) {
        this.id = id;
        this.userId = userId;
        this.availableCarId = availableCarId;
        this.startDate = startDate;
        this.updateDate = updateDate;
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }

    public RentStatusEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setStartDate(LocalDateTime startDate) { // Позволяем установить startDate только если он ещё не был установлен
        if (this.startDate == null) {
            this.startDate = startDate;
        }

    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @Override
    public String toString() {
        return "RentStatusEntity { " +
               "availableCarId = " + id +
               ", userId = " + userId +
               ", availableCarId = " + availableCarId +
               ", startDate = " + startDate +
               ", updateDate = " + updateDate +
               ", statusCode = '" + statusCode + '\'' +
               ", statusDescription = '" + statusDescription + '\'' +
               '}';
    }
}