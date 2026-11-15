
package cse.librarymanagement.dao;

import cse.librarymanagement.model.Book;
import cse.librarymanagement.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Handles database operations related to books.
public class BookDAO {

    // Adds a new book to the database.
    public boolean addBook(Book book) {

        String sql = "INSERT INTO books " +
                "(isbn, title, author, publisher, publication_year, " +
                "category_id, shelf_id, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getPublicationYear());
            statement.setInt(6, book.getCategoryId());
            statement.setInt(7, book.getShelfId());
            statement.setInt(8, book.getTotalCopies());
            statement.setInt(9, book.getAvailableCopies());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves all books from the database.
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Book book = new Book(
                        result.getInt("book_id"),
                        result.getString("isbn"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("publisher"),
                        result.getInt("publication_year"),
                        result.getInt("category_id"),
                        result.getInt("shelf_id"),
                        result.getInt("total_copies"),
                        result.getInt("available_copies")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Searches books by title or author.
    public List<Book> searchBooks(String keyword) {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books " + "WHERE title LIKE ? OR author LIKE ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Book book = new Book(
                            result.getInt("book_id"),
                            result.getString("isbn"),
                            result.getString("title"),
                            result.getString("author"),
                            result.getString("publisher"),
                            result.getInt("publication_year"),
                            result.getInt("category_id"),
                            result.getInt("shelf_id"),
                            result.getInt("total_copies"),
                            result.getInt("available_copies")
                    );

                    books.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Updates the information of an existing book.
    public boolean updateBook(Book book) {

        String sql = "UPDATE books SET " +
                "isbn = ?, title = ?, author = ?, " +
                "publisher = ?, publication_year = ?, " +
                "category_id = ?, shelf_id = ? " +
                "WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getPublicationYear());
            statement.setInt(6, book.getCategoryId());
            statement.setInt(7, book.getShelfId());
            statement.setInt(8, book.getBookId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deletes a book from the database using its ID.
    public boolean deleteBook(int bookId) {

        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieves books with their category names and shelf codes.
    public List<Object[]> getAllBooksWithDetails() {

        List<Object[]> books = new ArrayList<>();

        String sql = "SELECT b.book_id, b.isbn, b.title, b.author, " +
                "b.publisher, b.publication_year, c.category_name, " +
                "s.shelf_code, b.total_copies, b.available_copies " +
                "FROM books b " +
                "JOIN categories c ON b.category_id = c.category_id " +
                "JOIN shelves s ON b.shelf_id = s.shelf_id " +
                "ORDER BY b.book_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getInt("book_id"),
                        result.getString("isbn"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("publisher"),
                        result.getInt("publication_year"),
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

    // Searches books by title, author, category, or shelf code.
    public List<Object[]> searchBooksWithDetails(String keyword) {

        List<Object[]> books = new ArrayList<>();

        String sql = "SELECT b.book_id, b.isbn, b.title, b.author, " +
                "b.publisher, b.publication_year, c.category_name, " +
                "s.shelf_code, b.total_copies, b.available_copies " +
                "FROM books b " +
                "JOIN categories c ON b.category_id = c.category_id " +
                "JOIN shelves s ON b.shelf_id = s.shelf_id " +
                "WHERE b.title LIKE ? " +
                "OR b.author LIKE ? " +
                "OR c.category_name LIKE ? " +
                "OR s.shelf_code LIKE ? " +
                "ORDER BY b.book_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            statement.setString(1, searchValue);
            statement.setString(2, searchValue);
            statement.setString(3, searchValue);
            statement.setString(4, searchValue);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Object[] row = {
                            result.getInt("book_id"),
                            result.getString("isbn"),
                            result.getString("title"),
                            result.getString("author"),
                            result.getString("publisher"),
                            result.getInt("publication_year"),
                            result.getString("category_name"),
                            result.getString("shelf_code"),
                            result.getInt("total_copies"),
                            result.getInt("available_copies")
                    };

                    books.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    // Retrieves only available books with category and shelf details.
    public List<Object[]> getAvailableBooksWithDetails() {

        List<Object[]> books = new ArrayList<>();

        String sql = """
                SELECT b.book_id, b.isbn, b.title, b.author,
                       b.publisher, b.publication_year, c.category_name,
                       s.shelf_code, b.total_copies, b.available_copies
                FROM books b
                JOIN categories c ON b.category_id = c.category_id
                JOIN shelves s ON b.shelf_id = s.shelf_id
                WHERE b.available_copies > 0
                ORDER BY b.title
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Object[] row = {
                        result.getInt("book_id"),
                        result.getString("isbn"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("publisher"),
                        result.getInt("publication_year"),
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

    // Searches only available books by title, author, category or shelf.
    public List<Object[]> searchAvailableBooksWithDetails(String keyword) {

        List<Object[]> books = new ArrayList<>();

        String sql = """
                SELECT b.book_id, b.isbn, b.title, b.author,
                       b.publisher, b.publication_year, c.category_name,
                       s.shelf_code, b.total_copies, b.available_copies
                FROM books b
                JOIN categories c ON b.category_id = c.category_id
                JOIN shelves s ON b.shelf_id = s.shelf_id
                WHERE b.available_copies > 0
                AND (
                    b.title LIKE ?
                    OR b.author LIKE ?
                    OR c.category_name LIKE ?
                    OR s.shelf_code LIKE ?
                )
                ORDER BY b.title
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            for (int i = 1; i <= 4; i++) {
                statement.setString(i, searchValue);
            }

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Object[] row = {
                            result.getInt("book_id"),
                            result.getString("isbn"),
                            result.getString("title"),
                            result.getString("author"),
                            result.getString("publisher"),
                            result.getInt("publication_year"),
                            result.getString("category_name"),
                            result.getString("shelf_code"),
                            result.getInt("total_copies"),
                            result.getInt("available_copies")
                    };

                    books.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}
