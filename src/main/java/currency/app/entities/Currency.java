package currency.app.entities;

public class Currency {
    public int id;
    public String fullName;
    public String sign;
    public String code;

    public Currency(String fullName, String sign, String code, int id) {
        this.fullName = fullName;
        this.sign = sign;
        this.code = code;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
