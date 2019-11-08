package com.simbirsoft.bot.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService {
    private static final String INSERT_QUERY = "INSERT INTO %s (\"user\", post) VALUES (?, ?)";

    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM %s WHERE \"user\" = ? AND post = ?";

    private final String insertQuery;

    private final String countQuery;

    private final Connection connection;

    public DatabaseService(Connection connection, String postsTable) {
        this.connection = connection;
        insertQuery = String.format(INSERT_QUERY, postsTable);
        countQuery = String.format(COUNT_QUERY, postsTable);
    }

    public void insert(String userEmail, int post) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(insertQuery);
        stmt.setString(1, userEmail);
        stmt.setInt(2, post);

        stmt.execute();
    }

    public boolean existsForUser(String userEmail, int post) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(countQuery);
        stmt.setString(1, userEmail);
        stmt.setInt(2, post);

        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt(1) != 0;
    }
}
