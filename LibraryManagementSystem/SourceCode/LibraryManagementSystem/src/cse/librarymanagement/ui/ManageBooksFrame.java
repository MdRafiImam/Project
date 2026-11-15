package cse.librarymanagement.ui;

import cse.librarymanagement.dao.BookDAO;
import cse.librarymanagement.model.Book;
import cse.librarymanagement.dao.CategoryDAO;
import cse.librarymanagement.dao.ShelfDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Provides the graphical interface for managing library books.
public class ManageBooksFrame extends JPanel {

    private JTextField searchField;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // Initializes the book management interface.
    public ManageBooksFrame() {

        setLayout(new BorderLayout(10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("BOOK MANAGEMENT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        searchField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All Books");
        JButton addButton = new JButton("Add New Book");
        JButton updateButton = new JButton("Update Selected Book");
        JButton deleteButton = new JButton("Delete Selected Book");

        topPanel.add(titleLabel);
        topPanel.add(Box.createHorizontalStrut(30));
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showAllButton);

        add(topPanel, BorderLayout.NORTH);

        // Book Table
        String[] columns = {
                "Book ID", "ISBN", "Title", "Author",
                "Publisher", "Year", "Category",
                "Shelf", "Total Copies", "Available Copies"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(bookTable);

        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for book actions.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Button Actions
        searchButton.addActionListener(e -> searchBooks());

        showAllButton.addActionListener(e -> loadBooks());

        addButton.addActionListener(e -> openAddBookForm());

        updateButton.addActionListener(e -> updateSelectedBook());

        deleteButton.addActionListener(e -> deleteSelectedBook());

        // Load books when window opens
        loadBooks();
    }


    // Loads all books with their category names and shelf codes.
    private void loadBooks() {

        BookDAO bookDAO = new BookDAO();

        List<Object[]> books = bookDAO.getAllBooksWithDetails();

        tableModel.setRowCount(0);

        for (Object[] book : books) {
            tableModel.addRow(book);
        }
    }

    // Searches books and displays their category names and shelf codes.
    private void searchBooks() {

        String keyword = searchField.getText().trim();

        BookDAO bookDAO = new BookDAO();

        List<Object[]> books =
                bookDAO.searchBooksWithDetails(keyword);

        tableModel.setRowCount(0);

        for (Object[] book : books) {
            tableModel.addRow(book);
        }

        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No books found!"
            );
        }
    }

    // Displays a form to add a new book.
    private void openAddBookForm() {

        JTextField isbnField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField copiesField = new JTextField();

        CategoryDAO categoryDAO = new CategoryDAO();
        ShelfDAO shelfDAO = new ShelfDAO();

        JComboBox<String> categoryCombo =
                new JComboBox<>(categoryDAO.getAllCategories().toArray(new String[0]));

        JComboBox<String> shelfCombo =
                new JComboBox<>(shelfDAO.getAllShelves().toArray(new String[0]));

        if (categoryCombo.getItemCount() == 0 ||
                shelfCombo.getItemCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No categories or shelves available!"
            );
            return;
        }

        Object[] fields = {
                "ISBN:", isbnField,
                "Book Title:", titleField,
                "Author:", authorField,
                "Publisher:", publisherField,
                "Publication Year:", yearField,
                "Category:", categoryCombo,
                "Shelf:", shelfCombo,
                "Total Copies:", copiesField
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Book",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String title = titleField.getText().trim();
            String author = authorField.getText().trim();

            if (title.isEmpty() || author.isEmpty()) {
                throw new IllegalArgumentException(
                        "Book Title and Author are required!"
                );
            }

            int year = Integer.parseInt(yearField.getText().trim());
            int copies = Integer.parseInt(copiesField.getText().trim());

            if (copies <= 0) {
                throw new IllegalArgumentException(
                        "Total copies must be greater than zero!"
                );
            }

            int categoryId = categoryDAO.getCategoryId(
                    (String) categoryCombo.getSelectedItem()
            );

            int shelfId = shelfDAO.getShelfId(
                    (String) shelfCombo.getSelectedItem()
            );

            if (categoryId == -1 || shelfId == -1) {
                throw new IllegalArgumentException(
                        "Invalid category or shelf selection!"
                );
            }

            Book book = new Book(
                    0,
                    isbnField.getText().trim(),
                    title,
                    author,
                    publisherField.getText().trim(),
                    year,
                    categoryId,
                    shelfId,
                    copies,
                    copies
            );

            BookDAO bookDAO = new BookDAO();

            if (bookDAO.addBook(book)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book Added Successfully!"
                );

                loadBooks();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add book!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Year and Total Copies must be numbers!"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    // Updates the selected book's information.
    private void updateSelectedBook() {

        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to update!"
            );
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        String isbn = (String) tableModel.getValueAt(selectedRow, 1);
        String title = (String) tableModel.getValueAt(selectedRow, 2);
        String author = (String) tableModel.getValueAt(selectedRow, 3);
        String publisher = (String) tableModel.getValueAt(selectedRow, 4);
        String category = (String) tableModel.getValueAt(selectedRow, 6);
        String shelf = (String) tableModel.getValueAt(selectedRow, 7);

        JTextField isbnField = new JTextField(isbn);
        JTextField titleField = new JTextField(title);
        JTextField authorField = new JTextField(author);
        JTextField publisherField = new JTextField(publisher);

        JTextField yearField = new JTextField(
                String.valueOf(tableModel.getValueAt(selectedRow, 5))
        );

        CategoryDAO categoryDAO = new CategoryDAO();
        ShelfDAO shelfDAO = new ShelfDAO();

        JComboBox<String> categoryCombo =
                new JComboBox<>(categoryDAO.getAllCategories().toArray(new String[0]));

        JComboBox<String> shelfCombo =
                new JComboBox<>(shelfDAO.getAllShelves().toArray(new String[0]));

        categoryCombo.setSelectedItem(category);
        shelfCombo.setSelectedItem(shelf);

        Object[] fields = {
                "ISBN:", isbnField,
                "Title:", titleField,
                "Author:", authorField,
                "Publisher:", publisherField,
                "Year:", yearField,
                "Category:", categoryCombo,
                "Shelf:", shelfCombo
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Update Book",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            int categoryId = categoryDAO.getCategoryId(
                    (String) categoryCombo.getSelectedItem()
            );

            int shelfId = shelfDAO.getShelfId(
                    (String) shelfCombo.getSelectedItem()
            );

            if (categoryId == -1 || shelfId == -1) {
                throw new IllegalArgumentException(
                        "Invalid category or shelf selection!"
                );
            }

            Book book = new Book(
                    bookId,
                    isbnField.getText().trim(),
                    titleField.getText().trim(),
                    authorField.getText().trim(),
                    publisherField.getText().trim(),
                    Integer.parseInt(yearField.getText().trim()),
                    categoryId,
                    shelfId,
                    (int) tableModel.getValueAt(selectedRow, 8),
                    (int) tableModel.getValueAt(selectedRow, 9)
            );

            if (book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Title and Author are required!"
                );
                return;
            }

            BookDAO bookDAO = new BookDAO();

            if (bookDAO.updateBook(book)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Book Updated Successfully!"
                );
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update book!"
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Publication Year must be a number!"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }

    // Deletes the selected book after confirmation.
    private void deleteSelectedBook() {

        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book to delete!"
            );

            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        String bookTitle = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete:\n" + bookTitle + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            BookDAO bookDAO = new BookDAO();

            if (bookDAO.deleteBook(bookId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Book Deleted Successfully!"
                );

                loadBooks();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete book!"
                );
            }
        }
    }
}