package ru.tgc.hw.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class JDBCUtils {

    private String url;
    private String username;
    private String password;

    private void getProperties() {

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("C:/Users/Алексей/IdeaProjects/hw/src/main/resources/application.properties")) {

            properties.load(fileInputStream);

            url = properties.getProperty("dp.host");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("ERROR WITH FIND DB DRIVER", e);
        }
        getProperties();
        return DriverManager.getConnection(url, username, password);
    }
}
