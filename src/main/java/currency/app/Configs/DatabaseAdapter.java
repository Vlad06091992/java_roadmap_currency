package currency.app.Configs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

public class DatabaseAdapter {
    public PreparedStatement preparedStatement(String query) throws java.sql.SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String uname = "java_user";
            String pass = "java_password";
            String url = "jdbc:postgresql://localhost:5432/currency_exchange";
            Connection connection = DriverManager.getConnection(url, uname, pass); // Establishes the connection to the database
            return connection.prepareStatement(query);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // принимает     возвращет -- дженерики
    public <T> Optional<T> getEntity(String query, String[] columns, Class<T> clazz, Object... queryParams) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        //TODO 'PreparedStatement' used without 'try'-with-resources statement
        PreparedStatement myStmt = preparedStatement(query);

        for (int i = 0; i < queryParams.length; i++) {
            int index = i + 1;
            myStmt.setObject(index, queryParams[i]);
        }

        ResultSet myRs = myStmt.executeQuery();
        Map<String, Object> map = new HashMap<>();

        while (myRs.next()) {
            for (String column : columns) {
                Object result = myRs.getObject(column);
                map.put(column, result);

            }
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<T> constructor = (Constructor<T>) constructors[0];
        Object[] array = map.values().toArray();
        return Optional.of(constructor.newInstance(array));
    }


    public Map<String, Object> executeRaw(String query, String[] columns, Object... queryParams) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        String stringQuery = String.join(", ",columns);
       String newQuery = query.replace("*", stringQuery);

        //TODO 'PreparedStatement' used without 'try'-with-resources statement
        PreparedStatement myStmt = preparedStatement(newQuery);

        for (int i = 0; i < queryParams.length; i++) {
            int index = i + 1;
            myStmt.setObject(index, queryParams[i]);
        }

        ResultSet myRs = myStmt.executeQuery();

        while (myRs.next()) {
            for (String column : columns) {
                Object result = myRs.getObject(column);
                map.put(column, result);

            }
        }

        return map;
    }

    public <T> List<T> getListEntities(String query, String[] columns, Class<T> clazz, Object... queryParams) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        List<T> list = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>();

        //TODO 'PreparedStatement' used without 'try'-with-resources statement
        PreparedStatement myStmt = preparedStatement(query);

        for (int i = 0; i < queryParams.length; i++) {
            int index = i + 1;
            myStmt.setObject(index, queryParams[i]);
        }

        ResultSet myRs = myStmt.executeQuery();
        while (myRs.next()) {
            Map<String, Object> map = new HashMap<>();
            for (String column : columns) {
                Object result = myRs.getObject(column);
                map.put(column, result);
            }
            maps.add(map);
        }


        for (Map<String, Object> map : maps) {
            Constructor<?>[] constructors = clazz.getConstructors();
            Constructor<T> constructor = (Constructor<T>) constructors[0];
            Object[] array = map.values().toArray();
            list.add(constructor.newInstance(array));
        }

        return list;
    }


    public int executeWithReturningId(String query, Object... queryParams) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        int id = 0;

        PreparedStatement myStmt = preparedStatement(query);
        for (int i = 0; i < queryParams.length; i++) {
            int index = i + 1;
            myStmt.setObject(index, queryParams[i]);
        }

        ResultSet myRs = myStmt.executeQuery();
        if (myRs.next()) {
            int result = myRs.getInt("id");
            id = result;
        }

        return id;
    }
}
