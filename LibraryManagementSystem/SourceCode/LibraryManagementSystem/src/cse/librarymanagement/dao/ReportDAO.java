package cse.librarymanagement.dao;

import cse.librarymanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    // Retrieves overall library statistics.
    public Object[] getOverallStatistics() {

        Object[] statistics = new Object[7];

        String sql = """
                SELECT
                    (SELECT COALESCE(SUM(total_copies), 0)
                     FROM books) AS total_books,

                    (SELECT COUNT(*)
                     FROM students
                     WHERE registration_status = 'APPROVED')
                     AS total_students,

                    (SELECT COUNT(*)
                     FROM borrow_records
                     WHERE status = 'BORROWED'
                     AND due_date >= CURDATE())
                     AS borrowed_books,

                    (SELECT COUNT(*)
                     FROM borrow_records
                     WHERE status = 'RETURNED')
                     AS returned_books,

                    (SELECT COUNT(*)
                     FROM borrow_records
                     WHERE status <> 'RETURNED'
                     AND due_date < CURDATE())
                     AS overdue_books,

                    (SELECT COUNT(*)
                     FROM students
                     WHERE registration_status = 'PENDING')
                     AS pending_registrations,

                    (SELECT COUNT(*)
                     FROM book_requests
                     WHERE status = 'PENDING')
                     AS pending_requests
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {

                statistics[0] = result.getInt("total_books");
                statistics[1] = result.getInt("total_students");
                statistics[2] = result.getInt("borrowed_books");
                statistics[3] = result.getInt("returned_books");
                statistics[4] = result.getInt("overdue_books");
                statistics[5] = result.getInt("pending_registrations");
                statistics[6] = result.getInt("pending_requests");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statistics;
    }

    // Retrieves borrowing details for the report.
    public List<Object[]> getBorrowingReport() {

        List<Object[]> records = new ArrayList<>();

        String sql = """
            SELECT s.student_id_number,
                   s.membership_id,
                   s.full_name,
                   b.title,
                   br.borrow_date,
                   br.due_date,
                   br.return_date,
                   br.fine_amount,
                   CASE
                       WHEN br.status <> 'RETURNED'
                       AND br.due_date < CURDATE()
                       THEN 'OVERDUE'
                       ELSE br.status
                   END AS display_status
            FROM borrow_records br
            JOIN students s ON br.student_id = s.student_id
            JOIN books b ON br.book_id = b.book_id
            ORDER BY br.borrow_id DESC
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getString("student_id_number"),
                        result.getString("membership_id"),
                        result.getString("full_name"),
                        result.getString("title"),
                        result.getDate("borrow_date"),
                        result.getDate("due_date"),
                        result.getDate("return_date"),
                        result.getDouble("fine_amount"),
                        result.getString("display_status")
                };

                records.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    // Retrieves student details for the report.
    public List<Object[]> getStudentReport() {

        List<Object[]> students = new ArrayList<>();

        String sql = """
            SELECT student_id_number,
                   membership_id,
                   full_name,
                   email,
                   phone,
                   department,
                   join_date
            FROM students
            WHERE registration_status = 'APPROVED'
            ORDER BY full_name
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getString("student_id_number"),
                        result.getString("membership_id"),
                        result.getString("full_name"),
                        result.getString("email"),
                        result.getString("phone"),
                        result.getString("department"),
                        result.getDate("join_date")
                };

                students.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Retrieves book details for the report.
    public List<Object[]> getBookReport() {

        List<Object[]> books = new ArrayList<>();

        String sql = """
            SELECT b.isbn,
                   b.title,
                   b.author,
                   b.publisher,
                   c.category_name,
                   sh.shelf_code,
                   b.total_copies,
                   b.available_copies
            FROM books b
            LEFT JOIN categories c
                ON b.category_id = c.category_id
            LEFT JOIN shelves sh
                ON b.shelf_id = sh.shelf_id
            ORDER BY b.title
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getString("isbn"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("publisher"),
                        result.getString("category_name"),
                        result.getString("shelf_code"),
                        result.getInt("total_copies"),
                        result.getInt("available_copies")
                };

                books.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}