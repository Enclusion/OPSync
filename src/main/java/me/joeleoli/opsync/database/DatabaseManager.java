package me.joeleoli.opsync.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import me.joeleoli.opsync.data.GenericCallback;
import me.joeleoli.opsync.data.DataCache;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private Plugin plugin;
    private DatabaseCredentials credentials;
    private HikariDataSource hikariDataSource;

    public DatabaseManager(Plugin plugin, DatabaseCredentials credentials) {
        this.plugin = plugin;
        this.credentials = credentials;

        this.setupConneciton();
    }

    private void setupConneciton() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(this.credentials.getJdbcUrl());
        config.setUsername(this.credentials.getUsername());
        config.setPassword(this.credentials.getPassword());

        this.hikariDataSource = new HikariDataSource(config);
    }

    private Connection getConnection() throws SQLException {
        try {
            return this.hikariDataSource.getConnection();
        }
        catch (SQLException e) {
            this.plugin.getLogger().severe("Could not retrieve MySQL connection from data source.");
            e.printStackTrace();
        }

        this.plugin.getLogger().severe("Could not retrieve MySQL connection from data source.");

        return null;
    }

    private void closeComponents(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
            catch (Exception e) {}
        }

        if (statement != null) {
            try {
                if (!statement.isClosed()) {
                    statement.close();
                }
            }
            catch (Exception e) {}
        }

        if (resultSet != null) {
            try {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                }
            }
            catch (Exception e) {}
        }
    }

    public void generateTables() {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = DatabaseManager.this.getConnection();
                    statement = connection.prepareStatement("CREATE TABLE `authorized_users` (`player_uuid` VARCHAR(36) NOT NULL)");
                    statement.executeUpdate();
                }
                catch (Exception e) {
                    if (!e.getMessage().toLowerCase().contains("already exists")) {
                        e.printStackTrace();
                    }
                }
                finally {
                    DatabaseManager.this.closeComponents(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public void refreshAuthorizedCache(GenericCallback callback) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet result = null;

                try {
                    connection = DatabaseManager.this.getConnection();

                    if (connection == null) {
                        throw new Exception("The connection is null.");
                    }

                    statement = connection.prepareStatement("SELECT * FROM `authorized_users`");
                    result = statement.executeQuery();

                    if (result == null) {
                        throw new Exception("Failed query.");
                    }

                    List<UUID> authorized = new ArrayList<>();

                    while (result.next()) {
                        authorized.add(UUID.fromString(result.getString("player_uuid")));
                    }

                    DataCache.getInstance().setAuthorized(authorized);

                    callback.call(result.next(), false);
                    callback.setCalled(true);
                }
                catch (Exception e) {
                    DatabaseManager.this.plugin.getLogger().severe("Failed to refresh authorized.");
                    e.printStackTrace();
                }
                finally {
                    DatabaseManager.this.closeComponents(connection, statement, result);

                    if (!callback.isCalled()) {
                        callback.call(false, true);
                    }
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public void isAuthorized(UUID uuid, GenericCallback callback) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet result = null;

                try {
                    connection = DatabaseManager.this.getConnection();

                    if (connection == null) {
                        throw new Exception("The connection is null.");
                    }

                    statement = connection.prepareStatement("SELECT * FROM `authorized_users` WHERE `player_uuid`=?");
                    statement.setString(1, uuid.toString());

                    result = statement.executeQuery();

                    if (result == null) {
                        throw new Exception("Failed query.");
                    }

                    callback.call(result.next(), false);
                    callback.setCalled(true);
                }
                catch (Exception e) {
                    DatabaseManager.this.plugin.getLogger().severe("Failed to check if authorized.");
                    e.printStackTrace();
                }
                finally {
                    DatabaseManager.this.closeComponents(connection, statement, result);

                    if (!callback.isCalled()) {
                        callback.call(false, true);
                    }
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public void addAuthorized(UUID uuid) {
        DataCache.getInstance().addAuthorized(uuid);

        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = DatabaseManager.this.getConnection();

                    if (connection == null) {
                        throw new Exception("The connection is null.");
                    }

                    statement = connection.prepareStatement("INSERT INTO `authorized_users` (player_uuid) VALUES (?);");
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                }
                catch (Exception e) {
                    DatabaseManager.this.plugin.getLogger().severe("Failed to add authorized.");
                    e.printStackTrace();
                }
                finally {
                    DatabaseManager.this.closeComponents(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    public void removeAuthorized(UUID uuid) {
        DataCache.getInstance().removeAuthorized(uuid);

        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                ResultSet result = null;

                try {
                    connection = DatabaseManager.this.getConnection();

                    if (connection == null) {
                        throw new Exception("The connection is null.");
                    }

                    statement = connection.prepareStatement("DELETE FROM `authorized_users` WHERE `player_uuid`=?;");
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
                }
                catch (Exception e) {
                    DatabaseManager.this.plugin.getLogger().severe("Failed to remove authorized.");
                    e.printStackTrace();
                }
                finally {
                    DatabaseManager.this.closeComponents(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

}