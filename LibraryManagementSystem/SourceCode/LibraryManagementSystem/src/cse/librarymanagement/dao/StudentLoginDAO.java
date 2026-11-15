package cse.librarymanagement.dao;

import cse.librarymanagement.service.PasswordUtil;
import cse.librarymanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentLoginDAO {

    public boolean login(String studentIdNumber, char[] password) {

        String sql = """
                SELECT password
                FROM students
                WHERE student_id_number = ?
                AND registration_status = 'APPROVED'
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentIdNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String storedHash = resultSet.getString("password");

                    if (storedHash == null || storedHash.isBlank()) {
                        return false;
                    }

                    return PasswordUtil.verifyPassword(
                            password,
                            storedHash
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
