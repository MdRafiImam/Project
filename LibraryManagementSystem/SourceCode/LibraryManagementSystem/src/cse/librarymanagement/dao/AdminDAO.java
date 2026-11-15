package cse.librarymanagement.dao;

import cse.librarymanagement.service.PasswordUtil;
import cse.librarymanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public boolean login(String username, char[] password) {

        String sql = "SELECT password FROM admin WHERE username = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    String storedPassword =
                            result.getString("password");

                    return PasswordUtil.verifyPassword(
                            password, storedPassword
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Updates admin username and password after verifying current credentials.
    public boolean updateAdminCredentials(
            String currentUsername,
            char[] currentPassword,
            String newUsername,
            char[] newPassword) {

        String selectSql =
                "SELECT password FROM admin WHERE username = ?";

        String updateSql = """
                UPDATE admin
                SET username = ?, password = ?
                WHERE username = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            String storedPassword;

            try (PreparedStatement selectStatement =
                         connection.prepareStatement(selectSql)) {

                selectStatement.setString(1, currentUsername);

                try (ResultSet result =
                             selectStatement.executeQuery()) {

                    if (!result.next()) {
                        return false;
                    }

                    storedPassword = result.getString("password");
                }
            }

            if (storedPassword == null ||
                    !PasswordUtil.verifyPassword(
                            currentPassword, storedPassword)) {
                return false;
            }

            String hashedPassword =
                    PasswordUtil.hashPassword(newPassword);

            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql)) {

                updateStatement.setString(1, newUsername);
                updateStatement.setString(2, hashedPassword);
                updateStatement.setString(3, currentUsername);

                return updateStatement.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
