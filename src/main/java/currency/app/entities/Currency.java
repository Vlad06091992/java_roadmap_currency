package currency.app.entities;

import currency.app.entities.annotations.*;

@Columns(columns = {"fullName", "sign", "code", "id"})
@FindAllQuery(query = "SELECT fullName, sign, code, id FROM currencies")
@FindByCodeQuery(query = "SELECT * FROM currencies WHERE code = ?")
@FindByIdQuery(query = "SELECT * FROM currencies WHERE id = ?")
@InsertWithReturningIdQuery(
        query = """
        INSERT INTO public.currencies(
            code, fullname, sign)
        VALUES (?, ?, ?) RETURNING id;
        """
)
public class Currency {
    private int id;
    private String name;
    private String sign;
    private String code;

    public Currency(String name, String sign, String code, int id) {
        super();
        this.name = name;
        this.sign = sign;
        this.code = code;
        this.id = id;
    }

    public Currency(String name, String sign, String code) {
        this.name = name;
        this.sign = sign;
        this.code = code;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}