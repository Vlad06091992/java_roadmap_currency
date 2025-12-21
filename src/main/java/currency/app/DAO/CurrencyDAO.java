package currency.app.DAO;

import currency.app.Configs.DBSetup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import currency.app.entities.Currency;

import java.util.Optional;

public class CurrencyDAO implements DAO<Currency> {

    private final DBSetup db;

    public CurrencyDAO(DBSetup dbSetup) {
        this.db = dbSetup;  // 👉 зависимость извне
    }

    @Override
    public Optional<Currency> findById(int id) throws SQLException {

        String query = "SELECT * FROM currencies WHERE id = ?";

        Connection connection = db.getConnection();


        PreparedStatement myStmt;
        myStmt = connection.prepareStatement(query);
        myStmt.setInt(1,id);
        ResultSet myRs = myStmt.executeQuery();

        while (myRs.next()) {

//            myRs.next();
            String fullName = myRs.getString("fullName");
            String sign = myRs.getString("sign");
            String code = myRs.getString("code");
            int _id = myRs.getInt("id");


            Currency currency = new Currency(fullName,sign,code,_id);
            System.out.println(currency.toString());
            return Optional.of(currency);


        }


//        myRs.next();
//        String fullName = myRs.getString("fullName");
//        String sign = myRs.getString("sign");
//        String code = myRs.getString("code");
//        int _id = myRs.getInt("id");
//
//
//        Currency currency = new Currency(fullName,sign,code,_id);

//        while (myRs.next()) {
//
//            int id = myRs.getInt("id");
//
//
//            int age = myRs.getInt("age");
//
//            // Process the retrieved data
//
//        }

        return Optional.empty();
    }

//    @Override
//    public Optional<Currency> findById(Integer id) throws SQLException {
//        return Optional.empty();
//    }


//    @Override
//    public List<Currency> getAll(String id) {
//        return List.of();
//    }
//
//    @Override
//    public int add(Currency entity) {
//        return 0;
//    }
}
