package cse.librarymanagement.dao;

import cse.librarymanagement.model.BorrowRecord;
import cse.librarymanagement.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    private static final double DAILY_FINE = 5.0;

    // Borrows a book for a student.
    public boolean borrowBook(int studentId, int bookId,
                              LocalDate dueDate) {

        String checkBookSQL =
                "SELECT available_copies FROM books " +
                        "WHERE book_id = ? FOR UPDATE";

        String checkDuplicateSQL =
                "SELECT COUNT(*) FROM borrow_records " +
                        "WHERE student_id = ? " +
                        "AND book_id = ? " +
                        "AND status IN ('BORROWED', 'OVERDUE')";

        String insertSQL =
                "INSERT INTO borrow_records " +
                        "(student_id, book_id, borrow_date, due_date, status) " +
                        "VALUES (?, ?, ?, ?, 'BORROWED')";

        String updateBookSQL =
                "UPDATE books SET available_copies = available_copies - 1 " +
                        "WHERE book_id = ? AND available_copies > 0";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // Check duplicate borrowing first.
                try (PreparedStatement statement =
                             connection.prepareStatement(checkDuplicateSQL)) {

                    statement.setInt(1, studentId);
                    statement.setInt(2, bookId);

                    try (ResultSet result = statement.executeQuery()) {

                        if (result.next() && result.getInt(1) > 0) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                int availableCopies;

                // Lock and check book availability.
                try (PreparedStatement statement =
                             connection.prepareStatement(checkBookSQL)) {

                    statement.setInt(1, bookId);

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            connection.rollback();
                            return false;
                        }

                        availableCopies =
                                result.getInt("available_copies");
                    }
                }

                if (availableCopies <= 0) {
                    connection.rollback();
                    return false;
                }

                // Insert the new borrow record.
                try (PreparedStatement statement =
                             connection.prepareStatement(insertSQL)) {

                    statement.setInt(1, studentId);
                    statement.setInt(2, bookId);
                    statement.setDate(
                            3, Date.valueOf(LocalDate.now())
                    );
                    statement.setDate(
                            4, Date.valueOf(dueDate)
                    );

                    statement.executeUpdate();
                }

                // Decrease available book copies.
                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookSQL)) {

                    statement.setInt(1, bookId);

                    if (statement.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {

                connection.rollback();
                e.printStackTrace();
                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Checks whether a student already has this book.
    public boolean hasActiveBorrow(int studentId, int bookId) {

        String sql =
                "SELECT COUNT(*) FROM borrow_records " +
                        "WHERE student_id = ? " +
                        "AND book_id = ? " +
                        "AND status IN ('BORROWED', 'OVERDUE')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, bookId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Retrieves all borrow records.
    public List<BorrowRecord> getAllBorrowRecords() {

        List<BorrowRecord> records = new ArrayList<>();

        String sql = "SELECT br.borrow_id, br.student_id, br.book_id, " +
                "s.full_name AS student_name, " +
                "s.student_id_number AS student_id_number, " +
                "s.membership_id, b.title AS book_title, " +
                "br.borrow_date, br.due_date, br.return_date, " +
                "br.fine_amount, " +
                "CASE WHEN br.status <> 'RETURNED' " +
                "AND br.due_date < CURDATE() " +
                "THEN 'OVERDUE' ELSE br.status END AS display_status " +
                "FROM borrow_records br " +
                "JOIN students s ON br.student_id = s.student_id " +
                "JOIN books b ON br.book_id = b.book_id " +
                "ORDER BY br.borrow_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                records.add(mapBorrowRecord(result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    // Retrieves currently borrowed and overdue records.
    public List<BorrowRecord> getActiveBorrowRecords() {

        List<BorrowRecord> records = new ArrayList<>();

        String sql = "SELECT br.borrow_id, br.student_id, br.book_id, " +
                "s.full_name AS student_name, " +
                "s.student_id_number AS student_id_number, " +
                "s.membership_id, b.title AS book_title, " +
                "br.borrow_date, br.due_date, br.return_date, " +
                "br.fine_amount, " +
                "CASE WHEN br.due_date < CURDATE() " +
                "THEN 'OVERDUE' ELSE br.status END AS display_status " +
                "FROM borrow_records br " +
                "JOIN students s ON br.student_id = s.student_id " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.status <> 'RETURNED' " +
                "ORDER BY br.borrow_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                records.add(mapBorrowRecord(result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    // Returns a borrowed book and calculates its fine.
    public boolean returnBook(int borrowId) {

        String checkSQL =
                "SELECT book_id, due_date, status " +
                        "FROM borrow_records WHERE borrow_id = ? FOR UPDATE";

        String returnSQL =
                "UPDATE borrow_records SET return_date = ?, " +
                        "fine_amount = ?, status = 'RETURNED' " +
                        "WHERE borrow_id = ?";

        String updateBookSQL =
                "UPDATE books SET available_copies = " +
                        "available_copies + 1 WHERE book_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                int bookId;
                LocalDate dueDate;
                String status;

                // Retrieve borrow information.
                try (PreparedStatement statement =
                             connection.prepareStatement(checkSQL)) {

                    statement.setInt(1, borrowId);

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            connection.rollback();
                            return false;
                        }

                        bookId = result.getInt("book_id");
                        dueDate = result.getDate("due_date").toLocalDate();
                        status = result.getString("status");
                    }
                }

                if ("RETURNED".equals(status)) {
                    connection.rollback();
                    return false;
                }

                // Calculate overdue days and fine.
                LocalDate returnDate = LocalDate.now();

                long overdueDays = Math.max(
                        0,
                        ChronoUnit.DAYS.between(dueDate, returnDate)
                );

                double fineAmount = overdueDays * DAILY_FINE;

                // Update the borrow record.
                try (PreparedStatement statement =
                             connection.prepareStatement(returnSQL)) {

                    statement.setDate(
                            1, Date.valueOf(returnDate)
                    );
                    statement.setDouble(2, fineAmount);
                    statement.setInt(3, borrowId);

                    if (statement.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                // Increase available book copies.
                try (PreparedStatement statement =
                             connection.prepareStatement(updateBookSQL)) {

                    statement.setInt(1, bookId);

                    if (statement.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                connection.commit();
                return true;

            } catch (SQLException e) {

                connection.rollback();
                e.printStackTrace();
                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Converts database information into a BorrowRecord object.
    private BorrowRecord mapBorrowRecord(ResultSet result)
            throws SQLException {

        Date returnDate = result.getDate("return_date");

        BorrowRecord record = new BorrowRecord(
                result.getInt("borrow_id"),
                result.getInt("student_id"),
                result.getInt("book_id"),
                result.getString("student_name"),
                result.getString("membership_id"),
                result.getString("book_title"),
                result.getDate("borrow_date").toLocalDate(),
                result.getDate("due_date").toLocalDate(),
                returnDate == null ? null : returnDate.toLocalDate(),
                result.getDouble("fine_amount"),
                result.getString("display_status")
        );

        record.setStudentIdNumber(
                result.getString("student_id_number")
        );

        return record;
    }

    // Finds a student using the actual Student ID.
    // Returns the internal database student_id for Foreign Key use.
    public int getStudentIdByStudentIdNumber(String studentIdNumber) {

        String sql = "SELECT student_id FROM students " +
                "WHERE student_id_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentIdNumber);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt("student_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Retrieves books that have available copies.
    public List<Object[]> getAvailableBooks() {

        List<Object[]> books = new ArrayList<>();

        String sql = "SELECT book_id, title, author, available_copies " +
                "FROM books WHERE available_copies > 0 " +
                "ORDER BY title";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getInt("book_id"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getInt("available_copies")
                };

                books.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Retrieves borrow history for a specific student.
    public List<BorrowRecord> getBorrowHistoryByStudentId(int studentId) {

        List<BorrowRecord> records = new ArrayList<>();

        String sql = "SELECT br.borrow_id, br.student_id, br.book_id, " +
                "s.full_name AS student_name, " +
                "s.student_id_number AS student_id_number, " +
                "s.membership_id, b.title AS book_title, " +
                "br.borrow_date, br.due_date, br.return_date, " +
                "br.fine_amount, " +
                "CASE WHEN br.status <> 'RETURNED' " +
                "AND br.due_date < CURDATE() " +
                "THEN 'OVERDUE' ELSE br.status END AS display_status " +
                "FROM borrow_records br " +
                "JOIN students s ON br.student_id = s.student_id " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.student_id = ? " +
                "ORDER BY br.borrow_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    records.add(mapBorrowRecord(result));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }
}
