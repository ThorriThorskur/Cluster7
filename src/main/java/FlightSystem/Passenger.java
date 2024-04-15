package FlightSystem;

import java.util.UUID;

public class Passenger {
    public UUID id; //do we want dashes?
    private String name;
    private int passportNumber;
    private String address;
    private String phoneNumber;
    private String email;

    //Constructor
    public Passenger(String name, int passportNumber, String address, String phoneNumber, String email) {
        id = UUID.randomUUID(); //setKennitala(kennitala); instead?
        this.name = name; //setName(name);?
        this.passportNumber = passportNumber; //setPassportnumber(passportNumber);?
        this.address = address; //setAddress(address);?
        this.phoneNumber = phoneNumber; //setPhonenumber(phoneNumber);?
        this.email = email;
    }

    /*
    public long getKennitala() {
        return kennitala;
    }
    public void setKennitala(int kennitala) {
        this.kennitala = kennitala;
    }
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
