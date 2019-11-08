package com.simbirsoft.bot;

import com.simbirsoft.bot.db.DatabaseService;
import com.simbirsoft.bot.message.HabrLinkRequestMessageToResponseMapper;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Application {
    public static void main(String[] args) throws Exception {
        InputStream stream = Application.class.getClassLoader().getResourceAsStream("bot.properties");
        Properties properties = new Properties();
        properties.load(stream);

        String url = properties.getProperty("app.url");
        String email = properties.getProperty("bot.email");
        String password = properties.getProperty("bot.password");

        Bot bot = new Bot(new URL(url), email, password);

        String connectionUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");
        String dbTable = properties.getProperty("db.table");

        Connection connection = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
        HabrLinkRequestMessageToResponseMapper mapper = new HabrLinkRequestMessageToResponseMapper(
                new DatabaseService(connection, dbTable)
        );

        bot.run(mapper);
    }
}
