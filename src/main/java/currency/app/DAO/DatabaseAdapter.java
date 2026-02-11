package currency.app.DAO;

import currency.app.entities.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseAdapter {
    public PreparedStatement preparedStatement(String query) throws SQLException {
        try {
            Connection connection = DataSource.getConnection();
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // принимает     возвращет -- дженерики
    public <T> Optional<T> getEntityByParam(Class<T> clazz, String paramName, Object param) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String query = "";

        if (paramName.equals("id")) {
            query = clazz.getAnnotation(FindByIdQuery.class).query();
        }

        if (paramName.equals("code")) {
            query = clazz.getAnnotation(FindByCodeQuery.class).query();
        }

        String[] columns = clazz.getAnnotation(Columns.class).columns();
        PreparedStatement myStmt = preparedStatement(query);


        myStmt.setObject(1, param);
        ResultSet myRs = myStmt.executeQuery();
        Map<String, Object> map = new HashMap<>();

        while (myRs.next()) {
            for (String column : columns) {
                Object result = myRs.getObject(column);
                map.put(column, result);

            }
        }

        Constructor<?>[] constructors = clazz.getConstructors();

        Constructor<?> constructor = null;

        for (Constructor<?> c : constructors) {
            if (c.getParameterCount() == columns.length) {
                constructor = c;
            }
        }

        Object[] array = map.values().toArray();
        if (constructor != null) {
            return (Optional<T>) Optional.of(constructor.newInstance(array));

        }
        return Optional.empty();
    }


    public Map<String, Object> executeRaw(String query, String[] columns, Object... queryParams) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try {
            String mode = query.split(" ")[0];
            Map<String, Object> map = new HashMap<>();
            String stringQuery = String.join(", ", columns);
            String newQuery = query.replace("*", stringQuery);

            //TODO 'PreparedStatement' used without 'try'-with-resources statement
            PreparedStatement myStmt = preparedStatement(newQuery);

            for (int i = 0; i < queryParams.length; i++) {
                int index = i + 1;
                myStmt.setObject(index, queryParams[i]);
            }

            if (mode.equals(QUERY_MODES.UPDATE.toString())) {
                int myRs = myStmt.executeUpdate();
                map.put("updated", myRs);
                return map;
            }

            ResultSet myRs = myStmt.executeQuery();


            while (myRs.next()) {
                for (String column : columns) {
                    Object result = myRs.getObject(column);
                    map.put(column, result);

                }
            }

            return map;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public <T> List<T> getListEntities(Class<T> clazz) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {


        String query = clazz.getAnnotation(FindAllQuery.class).query();
        String[] columns = clazz.getAnnotation(Columns.class).columns();

        List<T> list = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>();

        //TODO 'PreparedStatement' used without 'try'-with-resources statement
        PreparedStatement myStmt = preparedStatement(query);

        ResultSet myRs = myStmt.executeQuery();
        while (myRs.next()) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String column : columns) {
                Object result = myRs.getObject(column);
                map.put(column, result);
            }
            maps.add(map);
        }


        for (Map<String, Object> map : maps) {
            Constructor<?>[] constructors = clazz.getConstructors();

            Constructor<?> constructor = null;

            for (Constructor<?> c : constructors) {
                if (c.getParameterCount() == columns.length) {
                    constructor = c;
                }
            }

            Object[] array = map.values().toArray();
            if (constructor != null) {
                list.add((T) constructor.newInstance(array));

            }
        }

        return list;
    }


    public <T> int saveAndReturnId(Class<T> clazz, Object[] queryParams) throws
            SQLException {

        String query = clazz.getAnnotation(InsertWithReturningIdQuery.class).query();
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
