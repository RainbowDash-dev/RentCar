package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UsersEntity {

    private long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String drivingLicense;
    private BigDecimal moneyBalance;
    private String login;
    private String password;

    public UsersEntity(long id, String firstName, String lastName, LocalDate birthDate,
                       String drivingLicense, BigDecimal moneyBalance,
                       String login, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.drivingLicense = drivingLicense;
        this.moneyBalance = moneyBalance;
        this.login = login;
        this.password = password;
    }

    public UsersEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public BigDecimal getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(BigDecimal moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsersEntity { " +
               "availableCarId = " + id +
               ", firstName = '" + firstName + '\'' +
               ", lastName = '" + lastName + '\'' +
               ", birthDate = " + birthDate +
               ", drivingLicense = '" + drivingLicense + '\'' +
               ", moneyBalance = " + moneyBalance +
               ", login = '" + login + '\'' +
               ", password = '" + password + '\'' +
               '}';
    }
}
