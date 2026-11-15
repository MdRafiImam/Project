package cse.librarymanagement.dao;

import cse.librarymanagement.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRequestDAO {

    // Submits a new book request.
    public boolean submitBookRequest(int studentId,
                                     String requestedTitle,
                                     String requestedAuthor,
                                     String reason) {

        String sql = "INSERT INTO book_requests " +
                "(student_id, requested_title, requested_author, reason) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setString(2, requestedTitle);
            statement.setString(3, requestedAuthor);
            statement.setString(4, reason);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves book requests for a specific student.
    public List<Object[]> getBookRequestsByStudentId(int studentId) {

        List<Object[]> requests = new ArrayList<>();

        String sql = "SELECT request_id, requested_title, " +
                "requested_author, reason, request_date, " +
                "status, admin_note " +
                "FROM book_requests " +
                "WHERE student_id = ? " +
                "ORDER BY request_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Object[] row = {
                            result.getInt("request_id"),
                            result.getString("requested_title"),
                            result.getString("requested_author"),
                            result.getString("reason"),
                            result.getDate("request_date"),
                            result.getString("status"),
                            result.getString("admin_note")
                    };

                    requests.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    // Retrieves all book requests with student information.
    public List<Object[]> getAllBookRequests() {

        List<Object[]> requests = new ArrayList<>();

        String sql = "SELECT br.request_id, s.student_id_number, " +
                "s.membership_id, s.full_name, " +
                "br.requested_title, br.requested_author, " +
                "br.reason, br.request_date, br.status, br.admin_note " +
                "FROM book_requests br " +
                "JOIN students s ON br.student_id = s.student_id " +
                "ORDER BY br.request_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getInt("request_id"),
                        result.getString("student_id_number"),
                        result.getString("membership_id"),
                        result.getString("full_name"),
                        result.getString("requested_title"),
                        result.getString("requested_author"),
                        result.getString("reason"),
                        result.getDate("request_date"),
                        result.getString("status"),
                        result.getString("admin_note")
                };

                requests.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    // Updates the status and admin note of a book request.
    public boolean updateRequestStatus(
            int requestId, String status, String adminNote) {

        String sql = "UPDATE book_requests " +
                "SET status = ?, admin_note = ? " +
                "WHERE request_id = ? AND status = 'PENDING'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setString(2, adminNote);
            statement.setInt(3, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
