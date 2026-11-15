package cse.librarymanagement.dao;

import cse.librarymanagement.model.Student;
import cse.librarymanagement.util.DatabaseConnection;
import cse.librarymanagement.service.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Retrieves all students.
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, student_id_number, membership_id,
                       full_name, email, phone, department, join_date
                FROM students
                ORDER BY student_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                students.add(mapStudent(result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Searches students using Student ID, Membership ID or other information.
    public List<Student> searchStudents(String keyword) {

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, student_id_number, membership_id,
                       full_name, email, phone, department, join_date
                FROM students
                WHERE student_id_number LIKE ?
                   OR membership_id LIKE ?
                   OR full_name LIKE ?
                   OR email LIKE ?
                   OR department LIKE ?
                ORDER BY student_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            for (int i = 1; i <= 5; i++) {
                statement.setString(i, searchValue);
            }

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    students.add(mapStudent(result));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Updates student information without changing Membership ID or password.
    public boolean updateStudent(Student student) {

        String sql = """
                UPDATE students
                SET student_id_number = ?,
                    full_name = ?,
                    email = ?,
                    phone = ?,
                    department = ?
                WHERE student_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getStudentIdNumber());
            statement.setString(2, student.getFullName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPhone());
            statement.setString(5, student.getDepartment());
            statement.setInt(6, student.getStudentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cancels an approved student's membership.
    // Students with unreturned or overdue books cannot cancel membership.
    public boolean cancelMembership(int studentId) {

        String sql = """
                UPDATE students
                SET registration_status = 'CANCELLED',
                    membership_id = NULL,
                    password = NULL
                WHERE student_id = ?
                AND registration_status = 'APPROVED'
                AND NOT EXISTS (
                    SELECT 1
                    FROM borrow_records
                    WHERE borrow_records.student_id = students.student_id
                    AND borrow_records.status IN ('BORROWED', 'OVERDUE')
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves an approved student using Student ID.
    public Student getStudentByStudentIdNumber(String studentIdNumber) {

        String sql = """
                SELECT student_id, student_id_number, membership_id,
                       full_name, email, phone, department, join_date
                FROM students
                WHERE student_id_number = ?
                AND registration_status = 'APPROVED'
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentIdNumber);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return mapStudent(result);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Changes student password after verifying the current password.
    public boolean changeStudentPassword(
            String studentIdNumber,
            char[] currentPassword,
            char[] newPassword) {

        String selectSql = """
                SELECT password
                FROM students
                WHERE student_id_number = ?
                AND registration_status = 'APPROVED'
                """;

        String updateSql = """
                UPDATE students
                SET password = ?
                WHERE student_id_number = ?
                AND registration_status = 'APPROVED'
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            String storedPassword;

            try (PreparedStatement selectStatement =
                         connection.prepareStatement(selectSql)) {

                selectStatement.setString(1, studentIdNumber);

                try (ResultSet result = selectStatement.executeQuery()) {

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

            String newHashedPassword =
                    PasswordUtil.hashPassword(newPassword);

            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql)) {

                updateStatement.setString(1, newHashedPassword);
                updateStatement.setString(2, studentIdNumber);

                return updateStatement.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Converts database row into Student object.
    private Student mapStudent(ResultSet result) throws SQLException {

        return new Student(
                result.getInt("student_id"),
                result.getString("student_id_number"),
                result.getString("membership_id"),
                result.getString("full_name"),
                result.getString("email"),
                result.getString("phone"),
                result.getString("department"),
                result.getDate("join_date").toLocalDate()
        );
    }
}
