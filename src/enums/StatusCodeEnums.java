package enums;

public enum StatusCodeEnums {
    AVAILABLE("AVAILABLE", "Car is available for rent"),
    RESERVED("RESERVED", "Car is reserved by user"),
    RENTED("RENTED", "Car is currently rented"),
    DELETED("DELETED", "Car is no longer in the fleet"),
    PAYMENT_FAILED("PAY_ERROR", "Payment failed (lack of funds in the user's account)"),
    END_RENT ("END_RENT", "Car has returned");

    private final String code;
    private final String description;

    StatusCodeEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static StatusCodeEnums fromCode(String code) {   // преобразовать строку из БД обратно в enum
        for (StatusCodeEnums status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный код статуса: " + code);
    }
}
