package cse.librarymanagement.dao;

import cse.librarymanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Retrieves all categories from the database.
    public List<String> getAllCategories() {

        List<String> categories = new ArrayList<>();

        String sql = "SELECT category_name FROM categories ORDER BY category_name";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                categories.add(result.getString("category_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Finds a category ID using its name.
    public int getCategoryId(String categoryName) {

        String sql = "SELECT category_id FROM categories WHERE category_name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoryName);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt("category_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Adds a new book category to the database.
    public boolean addCategory(String categoryName) {

        if (categoryName == null || categoryName.trim().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO categories (category_name) VALUES (?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoryName.trim());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
