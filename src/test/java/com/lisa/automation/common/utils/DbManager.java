package com.lisa.automation.common.utils;

import java.sql.*;

import static com.lisa.automation.common.constants.PropertyNames.*;

public class DbManager {

    private Connection connection = null;

    private Connection createConnection() throws SQLException, ClassNotFoundException {
        String host = AppProperties.getValueFor(DB_HOST);
        String username = AppProperties.getValueFor(DB_USERNAME);
        String password = AppProperties.getValueFor(DB_PASSWORD);
        String driver = AppProperties.getValueFor(DB_DRIVER);
        System.out.println("host: " + host + "\nusername: " + username + "\npassword: " + password + "\ndriver: " + driver);
        if(connection == null) {
            Class.forName(driver);
            return DriverManager.getConnection(host, username, password);
        }
        return connection;
    }

    public ResultSet runQuery(String query) throws SQLException, ClassNotFoundException {
        Statement statement = createConnection().createStatement();
        return statement.executeQuery(query);
    }

}
