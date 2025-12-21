package currency.app.DTO;

public class AddressesDTO {
    private String city;
    private String flat;
    private String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressesDTO{" +
                "city='" + city + '\'' +
                ", flat='" + flat + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
