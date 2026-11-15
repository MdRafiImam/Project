package cse.librarymanagement.dao;

import cse.librarymanagement.model.Student;
import cse.librarymanagement.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentApprovalDAO {

    // Get all pending student registrations
    public List<Student> getPendingRegistrations() {

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, student_id_number,
                       membership_id, full_name, email,
                       phone, department, join_date
                FROM students
                WHERE registration_status = 'PENDING'
                ORDER BY student_id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Student student = new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("student_id_number"),
                        resultSet.getString("membership_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("department"),
                        resultSet.getDate("join_date").toLocalDate()
                );

                students.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

    // Approve a student registration
    public boolean approveRegistration(int studentId) {

        String selectSql = """
                SELECT full_name, student_id_number
                FROM students
                WHERE student_id = ?
                AND registration_status = 'PENDING'
                """;

        String updateSql = """
                UPDATE students
                SET membership_id = ?,
                    registration_status = 'APPROVED'
                WHERE student_id = ?
                AND registration_status = 'PENDING'
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                String fullName;
                String studentIdNumber;

                // Get actual Student ID and name
                try (PreparedStatement statement =
                             connection.prepareStatement(selectSql)) {

                    statement.setInt(1, studentId);

                    try (ResultSet resultSet = statement.executeQuery()) {

                        if (!resultSet.next()) {
                            connection.rollback();
                            return false;
                        }

                        fullName = resultSet.getString("full_name");
                        studentIdNumber =
                                resultSet.getString("student_id_number");
                    }
                }

                // Validate Student ID
                if (studentIdNumber == null ||
                        !studentIdNumber.matches("\\d{3,}")) {

                    connection.rollback();
                    return false;
                }

                // Get first letter of student's name
                if (fullName == null || fullName.isBlank()) {
                    connection.rollback();
                    return false;
                }

                String firstLetter = fullName.trim()
                        .substring(0, 1)
                        .toUpperCase();

                // Get last 3 digits of actual Student ID
                String lastThreeDigits =
                        studentIdNumber.substring(
                                studentIdNumber.length() - 3
                        );

                // Generate Membership ID
                String membershipId = String.format(
                        "LIB-%s-%s",
                        firstLetter,
                        lastThreeDigits
                );

                // Update registration
                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {

                    statement.setString(1, membershipId);
                    statement.setInt(2, studentId);

                    int rows = statement.executeUpdate();

                    if (rows > 0) {
                        connection.commit();
                        return true;
                    }

                    connection.rollback();
                    return false;
                }

            } catch (SQLException e) {

                connection.rollback();

                // Duplicate Membership ID
                if (e.getErrorCode() == 1062) {
                    System.out.println(
                            "Membership ID already exists: " +
                                    e.getMessage()
                    );
                    return false;
                }

                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Reject a student registration
    public boolean rejectRegistration(int studentId) {

        String sql = """
                UPDATE students
                SET registration_status = 'REJECTED'
                WHERE student_id = ?
                AND registration_status = 'PENDING'
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
