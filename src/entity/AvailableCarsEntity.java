package entity;

public class AvailableCarsEntity {

    private long id;
    private long carTypeId;
    private String plateNumber;
    private String vinCode;
    private boolean isAvailable;

    public AvailableCarsEntity(long id, long carTypeId, String plateNumber,
                               String vinCode, boolean isAvailable) {
        this.id = id;
        this.carTypeId = carTypeId;
        this.plateNumber = plateNumber;
        this.vinCode = vinCode;
        this.isAvailable = isAvailable;
    }

    public AvailableCarsEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "AvailableCarsEntity { " +
               "availableCarId = " + id +
               ", carTypeId = " + carTypeId +
               ", plateNumber = '" + plateNumber + '\'' +
               ", vinCode = '" + vinCode + '\'' +
               ", isAvailable = " + isAvailable +
               '}';
    }
}
