package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserRentedCarsDto(

        long carId,
        String mark,
        String model,
        int year,
        BigDecimal cost,
        int maxSpeed,
        long carTypeId,
        String plateNumber,
        String vinCode,
        boolean isAvailable,
        LocalDateTime startDate)
{

}



