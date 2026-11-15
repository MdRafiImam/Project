package cse.librarymanagement.dao;

import cse.librarymanagement.service.PasswordUtil;
import cse.librarymanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

public class StudentRegistrationDAO {

    public boolean registerStudent(
            String studentIdNumber,
            String fullName,
            String email,
            String phone,
            String department,
            char[] password
    ) {

        String updateSql = """
                UPDATE students
                SET password = ?,
                    full_name = ?,
                    email = ?,
                    phone = ?,
                    department = ?,
                    join_date = ?,
                    membership_id = NULL,
                    registration_status = 'PENDING'
                WHERE student_id_number = ?
                AND registration_status = 'CANCELLED'
                """;

        String insertSql = """
                INSERT INTO students
                (student_id_number, password, full_name,
                 email, phone, department, join_date,
                 registration_status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING')
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            // First, check whether this Student had cancelled membership.
            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql)) {

                updateStatement.setString(1, hashedPassword);
                updateStatement.setString(2, fullName);
                updateStatement.setString(3, email);
                updateStatement.setString(4, phone);
                updateStatement.setString(5, department);
                updateStatement.setDate(6, Date.valueOf(LocalDate.now()));
                updateStatement.setString(7, studentIdNumber);

                int updatedRows = updateStatement.executeUpdate();

                if (updatedRows > 0) {
                    return true;
                }
            }

            // If no cancelled record exists, create a new student.
            try (PreparedStatement insertStatement =
                         connection.prepareStatement(insertSql)) {

                insertStatement.setString(1, studentIdNumber);
                insertStatement.setString(2, hashedPassword);
                insertStatement.setString(3, fullName);
                insertStatement.setString(4, email);
                insertStatement.setString(5, phone);
                insertStatement.setString(6, department);
                insertStatement.setDate(7, Date.valueOf(LocalDate.now()));

                return insertStatement.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
    }
}
