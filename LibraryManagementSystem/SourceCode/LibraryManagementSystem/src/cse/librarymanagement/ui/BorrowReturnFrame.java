package cse.librarymanagement.ui;

import cse.librarymanagement.dao.BorrowDAO;
import cse.librarymanagement.model.BorrowRecord;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BorrowReturnFrame extends JPanel {

    private JTextField studentIdField;
    private JComboBox<String> bookBox;
    private JTextField dueDateField;

    private JTable borrowTable;
    private DefaultTableModel tableModel;

    private int selectedStudentId = -1;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private List<Object[]> availableBooks;

    public BorrowReturnFrame() {

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("BOOK BORROW / RETURN MANAGEMENT");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);

        // Borrow Panel
        JPanel borrowPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        borrowPanel.setBorder(
                BorderFactory.createTitledBorder("Borrow Book")
        );

        studentIdField = new JTextField();

        // Reset selected student whenever Student ID changes.
        studentIdField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        selectedStudentId = -1;
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        selectedStudentId = -1;
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        selectedStudentId = -1;
                    }
                }
        );

        JButton findStudentButton = new JButton("Find Student");

        bookBox = new JComboBox<>();

        dueDateField = new JTextField(
                LocalDate.now().plusDays(14).format(dateFormatter)
        );

        JButton borrowButton = new JButton("Borrow Book");

        borrowPanel.add(new JLabel("Student ID:"));
        borrowPanel.add(studentIdField);

        borrowPanel.add(new JLabel("Find Student:"));
        borrowPanel.add(findStudentButton);

        borrowPanel.add(new JLabel("Select Book:"));
        borrowPanel.add(bookBox);

        borrowPanel.add(new JLabel("Due Date (DD-MM-YYYY):"));
        borrowPanel.add(dueDateField);

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(borrowPanel, BorderLayout.CENTER);
        topPanel.add(borrowButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.PAGE_START);

        // Borrow Records Table
        String[] columns = {
                "Borrow ID",
                "Student Name",
                "Student ID",
                "Membership ID",
                "Book Title",
                "Borrow Date",
                "Due Date",
                "Return Date",
                "Fine",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowTable = new JTable(tableModel);
        borrowTable.setRowHeight(25);

        add(new JScrollPane(borrowTable), BorderLayout.CENTER);

        // Bottom Buttons
        JPanel bottomPanel = new JPanel();

        JButton returnButton = new JButton("Return Book");
        JButton refreshButton = new JButton("Refresh Records");

        bottomPanel.add(returnButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        findStudentButton.addActionListener(e -> findStudent());

        borrowButton.addActionListener(e -> borrowBook());

        returnButton.addActionListener(e -> returnBook());

        refreshButton.addActionListener(e -> {
            loadBorrowRecords();
            loadAvailableBooks();
        });

        loadAvailableBooks();
        loadBorrowRecords();
    }

    // Finds a student using the actual Student ID number.
    private void findStudent() {

        String studentIdNumber =
                studentIdField.getText().trim();

        selectedStudentId = -1;

        if (studentIdNumber.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please enter Student ID!"
            );

            return;
        }

        BorrowDAO borrowDAO = new BorrowDAO();

        selectedStudentId =
                borrowDAO.getStudentIdByStudentIdNumber(studentIdNumber);

        if (selectedStudentId != -1) {

            JOptionPane.showMessageDialog(
                    this, "Student Found Successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                    this, "Student Not Found!"
            );
        }
    }

    // Loads available books into the dropdown.
    private void loadAvailableBooks() {

        BorrowDAO borrowDAO = new BorrowDAO();

        availableBooks = borrowDAO.getAvailableBooks();

        bookBox.removeAllItems();

        for (Object[] book : availableBooks) {

            String bookInfo =
                    book[1] + " - " + book[2] +
                            " (Available: " + book[3] + ")";

            bookBox.addItem(bookInfo);
        }
    }

    // Borrows a selected book.
    private void borrowBook() {

        if (selectedStudentId == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please find a valid student first!"
            );

            return;
        }

        int selectedIndex = bookBox.getSelectedIndex();

        if (selectedIndex == -1) {

            JOptionPane.showMessageDialog(
                    this, "No available book selected!"
            );

            return;
        }

        try {

            LocalDate dueDate = LocalDate.parse(
                    dueDateField.getText().trim(),
                    dateFormatter
            );

            if (!dueDate.isAfter(LocalDate.now())) {

                JOptionPane.showMessageDialog(
                        this, "Due Date must be after today!"
                );

                return;
            }

            int bookId =
                    (int) availableBooks.get(selectedIndex)[0];

            BorrowDAO borrowDAO = new BorrowDAO();

            // Check whether this student already has the selected book.
            if (borrowDAO.hasActiveBorrow(selectedStudentId, bookId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Borrow Failed!\n" +
                                "You have already borrowed this book and have not returned it yet."
                );

                return;
            }

            // Check whether the book borrowing was successful.
            if (borrowDAO.borrowBook(
                    selectedStudentId, bookId, dueDate)) {

                JOptionPane.showMessageDialog(
                        this, "Book Borrowed Successfully!"
                );

                selectedStudentId = -1;
                studentIdField.setText("");

                loadAvailableBooks();
                loadBorrowRecords();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Borrow Failed! No available copies or database error."
                );
            }

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Date! Use DD-MM-YYYY format."
            );
        }
    }

    // Loads all borrow records.
    private void loadBorrowRecords() {

        BorrowDAO borrowDAO = new BorrowDAO();

        List<BorrowRecord> records =
                borrowDAO.getAllBorrowRecords();

        tableModel.setRowCount(0);

        for (BorrowRecord record : records) {

            Object[] row = {
                    record.getBorrowId(),
                    record.getStudentName(),
                    record.getStudentIdNumber(),
                    record.getMembershipId(),
                    record.getBookTitle(),
                    record.getBorrowDate().format(dateFormatter),
                    record.getDueDate().format(dateFormatter),
                    record.getReturnDate() == null
                            ? "-"
                            : record.getReturnDate().format(dateFormatter),
                    record.getFineAmount(),
                    record.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    // Returns a selected borrowed book.
    private void returnBook() {

        int selectedRow = borrowTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please select a borrow record!"
            );

            return;
        }

        int modelRow =
                borrowTable.convertRowIndexToModel(selectedRow);

        // Status is at column index 9.
        String status =
                tableModel.getValueAt(modelRow, 9).toString();

        if ("RETURNED".equals(status)) {

            JOptionPane.showMessageDialog(
                    this, "This book has already been returned!"
            );

            return;
        }

        int borrowId =
                (int) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to return this book?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            BorrowDAO borrowDAO = new BorrowDAO();

            if (borrowDAO.returnBook(borrowId)) {

                JOptionPane.showMessageDialog(
                        this, "Book Returned Successfully!"
                );

                loadBorrowRecords();
                loadAvailableBooks();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Book Return Failed!"
                );
            }
        }
    }
}
