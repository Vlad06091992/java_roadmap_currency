package currency.app.DTO;

import java.util.List;

public class UserDTO {
    private String name;
    private String surname;
    private String salary;
    private String departament;
    private List<AddressesDTO> addresses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public List<AddressesDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesDTO> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", salary='" + salary + '\'' +
                ", departament='" + departament + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
