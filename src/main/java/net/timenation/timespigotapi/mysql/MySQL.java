package net.timenation.timespigotapi.mysql;

import net.timenation.timespigotapi.TimeSpigotAPI;
import net.timenation.timespigotapi.data.logger.LoggerType;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQL {

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;
    private Connection connection;
    private final ExecutorService executorService;

    public MySQL(String database) {
        this.host = TimeSpigotAPI.getInstance().getTimeConfig().getCredentials().getHost();
        this.port = 3306;
        this.user = TimeSpigotAPI.getInstance().getTimeConfig().getCredentials().getUser();
        this.password = TimeSpigotAPI.getInstance().getTimeConfig().getCredentials().getPassword();
        this.database = database;
        this.executorService = Executors.newCachedThreadPool();

        connectToDatabase();
    }

    public void connectToDatabase() {
        if (!isConnectedToDatabase()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
                TimeSpigotAPI.getInstance().getTimeLogger().log("MySQL connection is connected", LoggerType.INFO);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                TimeSpigotAPI.getInstance().getTimeLogger().log("MySQL connection has not connected. ERROR #" + sqlException.getErrorCode(), LoggerType.ERROR);
            }
        }
    }

    public boolean isConnectedToDatabase() {
        return connection != null;
    }

    public void disconnectFromDatabase() {
        if (isConnectedToDatabase()) {
            try {
                connection.close();
                TimeSpigotAPI.getInstance().getTimeLogger().log("MySQL Connection ➳➳ Closed", LoggerType.INFO);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }

    public Connection getConnectionToDatabase() {
        return connection;
    }

    public void updateDatabase(String query) {
        executorService.execute(() -> {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException var11) {
                var11.printStackTrace();
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException var10) {
                    var10.printStackTrace();
                }
            }
        });
    }

    public ResultSet getDatabaseResult(String query) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }
}
