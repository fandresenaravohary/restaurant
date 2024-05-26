package com.kfc.repository;

import com.kfc.config.ConnectionDB;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AutoCrudOperation<T, ID> implements CrudOperation<T, ID>{
    protected abstract String getTableName();

    protected abstract T mapResultSetEntity(ResultSet resultSet);

    @Override
    public T getById(ID id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + getTableName() + " WHERE id = " + id + ";";

        try {
            connection = ConnectionDB.createConnection();
            assert connection != null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return mapResultSetEntity(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + getTableName();
        List<T> listAll = new ArrayList<>();

        try {
            connection = ConnectionDB.createConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listAll.add(mapResultSetEntity(resultSet));
            }
            return listAll;
        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public T save(T toSave) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionDB.createConnection();
            assert connection != null;
            statement = connection.createStatement();

            Class<?> toSaveClass = toSave.getClass();

            Field[] fields = toSaveClass.getDeclaredFields();

            StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + getTableName() + " (");
            for (Field field : fields) {
                queryBuilder.append(field.getName()).append(", ");
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(") VALUES ( ");

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(toSave);
                queryBuilder.append("'" + value + "', ");
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(")");

            String insertQuery = queryBuilder.toString();

            statement.executeUpdate(insertQuery);
            return toSave;

        } catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T deleteById(ID id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "DELETE FROM " + getTableName() + " WHERE id = " + id + ";";

        try {
            connection = ConnectionDB.createConnection();
            assert connection != null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return mapResultSetEntity(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public T updateById(ID toUpdate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.createConnection();

            Class<?> toUpdateClass = toUpdate.getClass();
            Field[] fields = toUpdateClass.getDeclaredFields();

            StringBuilder queryBuilder = new StringBuilder("UPDATE " + getTableName() + " SET ");
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    queryBuilder.append(field.getName() + " = ?, ");
                }
            }
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());

            queryBuilder.append(" WHERE id = ?");

            String updateQuery = queryBuilder.toString();
            assert connection != null;
            preparedStatement = connection.prepareStatement(updateQuery);

            int parameterIndex = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    Object value = field.get(toUpdate);
                    preparedStatement.setObject(parameterIndex++, value);
                }
            }
            preparedStatement.setObject(parameterIndex++, getById((ID) toUpdate));

            preparedStatement.executeUpdate();
            return (T) toUpdate;

        } catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

