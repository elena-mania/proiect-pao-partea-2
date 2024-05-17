package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Math.max;
import static utils.Constants.*;

public class PrimaryKeyGenerator {
    public static int getNextPrimaryKey(String tableName, String primaryKeyColumn) {
        int nextPrimaryKey = 0;
        String query = "SELECT MAX(" + primaryKeyColumn + ") FROM dbproiectpao." + tableName;

        try (Connection connection = DriverManager.getConnection(JDBC_DRIVER, JDBC_USER, JDBC_PWD );
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                nextPrimaryKey = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextPrimaryKey;
    }
    public static int getNextPrimaryKeyJoinTables(String tableName1,String tableName2, String primaryKeyColumn) {
        int nextPrimaryKey1 = 0,nextPrimaryKey2=0;
        String query1 = "SELECT MAX(" + primaryKeyColumn + ") FROM dbproiectpao." + tableName1;
        String query2 = "SELECT MAX(" + primaryKeyColumn + ") FROM dbproiectpao." + tableName2;

        try (Connection connection = DriverManager.getConnection(JDBC_DRIVER, JDBC_USER, JDBC_PWD );
             PreparedStatement statement = connection.prepareStatement(query1);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                nextPrimaryKey1 = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(JDBC_DRIVER, JDBC_USER, JDBC_PWD );
              PreparedStatement statement = connection.prepareStatement(query2);
              ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                nextPrimaryKey2 = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max(nextPrimaryKey1,nextPrimaryKey2);
    }

}
