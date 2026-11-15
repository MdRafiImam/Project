package cse.librarymanagement.dao;

import cse.librarymanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to library shelves.
 */
public class ShelfDAO {

    // Retrieves all shelf codes from the database.
    public List<String> getAllShelves() {

        List<String> shelves = new ArrayList<>();

        String sql = "SELECT shelf_code FROM shelves ORDER BY shelf_code";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                shelves.add(result.getString("shelf_code"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shelves;
    }

    // Finds a shelf ID using its code.
    public int getShelfId(String shelfCode) {

        String sql = "SELECT shelf_id FROM shelves WHERE shelf_code = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, shelfCode);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("shelf_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Retrieves all shelves with category names.
    public List<Object[]> getAllShelvesWithDetails() {

        List<Object[]> shelves = new ArrayList<>();

        String sql = "SELECT s.shelf_id, s.shelf_code, " +
                "c.category_name, s.capacity " +
                "FROM shelves s " +
                "JOIN categories c ON s.category_id = c.category_id " +
                "ORDER BY s.shelf_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getInt("shelf_id"),
                        result.getString("shelf_code"),
                        result.getString("category_name"),
                        result.getInt("capacity")
                };

                shelves.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return shelves;
    }

    // Adds a new shelf.
    public boolean addShelf(String code, int categoryId, int capacity) {

        String sql = "INSERT INTO shelves " +
                "(shelf_code, category_id, capacity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);
            statement.setInt(2, categoryId);
            statement.setInt(3, capacity);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Updates an existing shelf.
    public boolean updateShelf(int shelfId, String code,
                               int categoryId, int capacity) {

        String sql = "UPDATE shelves SET shelf_code = ?, " +
                "category_id = ?, capacity = ? WHERE shelf_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);
            statement.setInt(2, categoryId);
            statement.setInt(3, capacity);
            statement.setInt(4, shelfId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Deletes a shelf from the database.
    public boolean deleteShelf(int shelfId) {

        String sql = "DELETE FROM shelves WHERE shelf_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, shelfId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
