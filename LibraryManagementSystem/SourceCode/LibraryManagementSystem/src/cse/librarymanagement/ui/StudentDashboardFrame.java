package cse.librarymanagement.ui;

import cse.librarymanagement.dao.BookDAO;
import cse.librarymanagement.dao.BookRequestDAO;
import cse.librarymanagement.dao.BorrowDAO;
import cse.librarymanagement.dao.StudentDAO;
import cse.librarymanagement.model.BorrowRecord;
import cse.librarymanagement.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;

public class StudentDashboardFrame extends JFrame {

    private String studentIdNumber;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JTextField searchField;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;

    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    private JTextField requestTitleField;
    private JTextField requestAuthorField;
    private JTextArea requestReasonArea;

    private JTable requestTable;
    private DefaultTableModel requestTableModel;

    public StudentDashboardFrame(String studentIdNumber) {

        this.studentIdNumber = studentIdNumber;

        setTitle("Student Dashboard - Library Management System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(35, 55, 80));
        headerPanel.setPreferredSize(new Dimension(1100, 65));

        JLabel titleLabel = new JLabel("  LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new GridLayout(6, 1, 5, 5));
        sidebarPanel.setBackground(new Color(235, 240, 245));
        sidebarPanel.setPreferredSize(new Dimension(220, 0));

        String[] menuItems = {
                "Dashboard",
                "My Profile",
                "Available Books",
                "My Borrow History",
                "My Book Requests",
                "Logout"
        };

        for (String item : menuItems) {

            JButton button = new JButton(item);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);

            button.addActionListener(e -> handleMenuClick(item));

            sidebarPanel.add(button);
        }

        // Main Content
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel(
                "Welcome to Student Dashboard",
                SwingConstants.CENTER
        );

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        dashboardPanel.add(welcomeLabel, BorderLayout.CENTER);

        contentPanel.add(dashboardPanel, "Dashboard");

        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Dashboard");
    }

    // Handles sidebar button actions
    private void handleMenuClick(String menu) {

        switch (menu) {

            case "Dashboard":
                cardLayout.show(contentPanel, "Dashboard");
                break;

            case "My Profile":
                showMyProfile();
                break;

            case "Available Books":
                showAvailableBooks();
                break;

            case "My Borrow History":
                showBorrowHistory();
                break;

            case "My Book Requests":
                showBookRequests();
                break;

            case "Logout":
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    new WelcomeFrame().setVisible(true);
                    dispose();
                }
                break;
        }
    }

    // Displays student profile and password change form.
    private void showMyProfile() {

        StudentDAO studentDAO = new StudentDAO();

        Student student =
                studentDAO.getStudentByStudentIdNumber(studentIdNumber);

        if (student == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load student profile.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("MY PROFILE");
        heading.setFont(new Font("Arial", Font.BOLD, 26));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        profilePanel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String[][] details = {
                {"Student ID", student.getStudentIdNumber()},
                {"Membership ID", student.getMembershipId()},
                {"Full Name", student.getFullName()},
                {"Email", student.getEmail()},
                {"Phone", student.getPhone()},
                {"Department", student.getDepartment()},
                {"Join Date", student.getJoinDate().format(formatter)}
        };

        // Displays student information.
        for (int i = 0; i < details.length; i++) {

            gbc.gridx = 0;
            gbc.gridy = i + 1;

            JLabel label = new JLabel(details[i][0] + ":");
            label.setFont(new Font("Arial", Font.BOLD, 15));

            profilePanel.add(label, gbc);

            gbc.gridx = 1;

            JLabel value = new JLabel(
                    details[i][1] == null ? "N/A" : details[i][1]
            );

            value.setFont(new Font("Arial", Font.PLAIN, 15));

            profilePanel.add(value, gbc);
        }

        // Password change section.
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel passwordHeading = new JLabel("CHANGE PASSWORD");
        passwordHeading.setFont(new Font("Arial", Font.BOLD, 20));

        profilePanel.add(passwordHeading, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JPasswordField currentPasswordField = new JPasswordField(18);
        JPasswordField newPasswordField = new JPasswordField(18);
        JPasswordField confirmPasswordField = new JPasswordField(18);

        gbc.gridx = 0;
        gbc.gridy = 10;
        profilePanel.add(new JLabel("Current Password:"), gbc);

        gbc.gridx = 1;
        profilePanel.add(currentPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        profilePanel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        profilePanel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        profilePanel.add(new JLabel("Confirm New Password:"), gbc);

        gbc.gridx = 1;
        profilePanel.add(confirmPasswordField, gbc);

        JButton changePasswordButton =
                new JButton("CHANGE PASSWORD");

        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        profilePanel.add(changePasswordButton, gbc);

        // Handles password change.
        changePasswordButton.addActionListener(e -> {

            char[] currentPassword =
                    currentPasswordField.getPassword();

            char[] newPassword =
                    newPasswordField.getPassword();

            char[] confirmPassword =
                    confirmPasswordField.getPassword();

            try {

                if (currentPassword.length == 0 ||
                        newPassword.length == 0 ||
                        confirmPassword.length == 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill in all password fields."
                    );

                    return;
                }

                if (newPassword.length < 8) {

                    JOptionPane.showMessageDialog(
                            this,
                            "New password must be at least 8 characters long."
                    );

                    return;
                }

                if (!Arrays.equals(newPassword, confirmPassword)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "New Password and Confirm Password do not match."
                    );

                    return;
                }

                boolean success = studentDAO.changeStudentPassword(
                        studentIdNumber,
                        currentPassword,
                        newPassword
                );

                if (success) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Password changed successfully!"
                    );

                    currentPasswordField.setText("");
                    newPasswordField.setText("");
                    confirmPasswordField.setText("");

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Current password is incorrect.",
                            "Password Change Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } finally {

                Arrays.fill(currentPassword, '\0');
                Arrays.fill(newPassword, '\0');
                Arrays.fill(confirmPassword, '\0');
            }
        });

        contentPanel.add(profilePanel, "My Profile");
        cardLayout.show(contentPanel, "My Profile");
    }

    // Displays available books
    private void showAvailableBooks() {

        JPanel booksPanel = new JPanel(new BorderLayout(10, 10));
        booksPanel.setBackground(Color.WHITE);
        booksPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel heading = new JLabel("AVAILABLE BOOKS");
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField();

        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel searchButtons = new JPanel(new FlowLayout());
        searchButtons.add(searchButton);
        searchButtons.add(refreshButton);

        searchPanel.add(searchButtons, BorderLayout.EAST);

        // Table
        String[] columns = {
                "ISBN", "Book Title", "Author", "Publisher",
                "Year", "Category", "Shelf Code",
                "Total Copies", "Available Copies"
        };

        bookTableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(bookTableModel);
        bookTable.setRowHeight(30);
        bookTable.setFillsViewportHeight(true);
        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        booksPanel.add(topPanel, BorderLayout.NORTH);
        booksPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        searchButton.addActionListener(e -> searchAvailableBooks());

        searchField.addActionListener(e -> searchAvailableBooks());

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadAvailableBooks();
        });

        contentPanel.add(booksPanel, "Available Books");

        loadAvailableBooks();

        cardLayout.show(contentPanel, "Available Books");
    }

    // Loads all available books
    private void loadAvailableBooks() {

        BookDAO bookDAO = new BookDAO();

        List<Object[]> books = bookDAO.getAvailableBooksWithDetails();

        bookTableModel.setRowCount(0);

        for (Object[] book : books) {

            Object[] row = {
                    book[1], book[2], book[3], book[4],
                    book[5], book[6], book[7], book[8], book[9]
            };

            bookTableModel.addRow(row);
        }
    }

    // Searches available books
    private void searchAvailableBooks() {

        String keyword = searchField.getText().trim();

        BookDAO bookDAO = new BookDAO();

        List<Object[]> books =
                bookDAO.searchAvailableBooksWithDetails(keyword);

        bookTableModel.setRowCount(0);

        for (Object[] book : books) {

            Object[] row = {
                    book[1], book[2], book[3], book[4],
                    book[5], book[6], book[7], book[8], book[9]
            };

            bookTableModel.addRow(row);
        }
    }

    // Displays borrow history of the logged-in student
    private void showBorrowHistory() {

        JPanel historyPanel = new JPanel(new BorderLayout(10, 10));
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel heading = new JLabel("MY BORROW HISTORY");
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        // Table
        String[] columns = {
                "Borrow ID", "Book Title", "Borrow Date",
                "Due Date", "Return Date", "Fine (BDT)", "Status"
        };

        historyTableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(30);
        historyTable.setFillsViewportHeight(true);
        historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(historyTable);

        // Refresh Button
        JButton refreshButton = new JButton("Refresh");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(heading, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        historyPanel.add(topPanel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> loadBorrowHistory());

        contentPanel.add(historyPanel, "My Borrow History");

        loadBorrowHistory();

        cardLayout.show(contentPanel, "My Borrow History");
    }

    // Loads borrow records for the logged-in student
    private void loadBorrowHistory() {

        BorrowDAO borrowDAO = new BorrowDAO();

        int studentId =
                borrowDAO.getStudentIdByStudentIdNumber(studentIdNumber);

        historyTableModel.setRowCount(0);

        if (studentId == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to find student information.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        List<BorrowRecord> records =
                borrowDAO.getBorrowHistoryByStudentId(studentId);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (BorrowRecord record : records) {

            Object[] row = {
                    record.getBorrowId(),
                    record.getBookTitle(),
                    record.getBorrowDate().format(formatter),
                    record.getDueDate().format(formatter),
                    record.getReturnDate() == null
                            ? "Not Returned"
                            : record.getReturnDate().format(formatter),
                    record.getFineAmount(),
                    record.getStatus()
            };

            historyTableModel.addRow(row);
        }
    }

    // Displays the book request form and request history
    private void showBookRequests() {

        JPanel requestPanel = new JPanel(new BorderLayout(10, 10));
        requestPanel.setBackground(Color.WHITE);
        requestPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel heading = new JLabel("MY BOOK REQUESTS");
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        // Request Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createTitledBorder("Submit New Book Request")
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        requestTitleField = new JTextField(25);
        requestAuthorField = new JTextField(25);
        requestReasonArea = new JTextArea(3, 25);
        requestReasonArea.setLineWrap(true);
        requestReasonArea.setWrapStyleWord(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Book Title:"), gbc);

        gbc.gridx = 1;
        formPanel.add(requestTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Author:"), gbc);

        gbc.gridx = 1;
        formPanel.add(requestAuthorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Reason:"), gbc);

        gbc.gridx = 1;
        formPanel.add(new JScrollPane(requestReasonArea), gbc);

        JButton submitButton = new JButton("Submit Request");

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(submitButton, gbc);

        // Request History Table
        String[] columns = {
                "Request ID", "Book Title", "Author",
                "Reason", "Request Date", "Status", "Admin Note"
        };

        requestTableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        requestTable = new JTable(requestTableModel);
        requestTable.setRowHeight(30);
        requestTable.setFillsViewportHeight(true);
        requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane tableScrollPane = new JScrollPane(requestTable);

        JButton refreshButton = new JButton("Refresh");

        JPanel tableHeadingPanel = new JPanel(new BorderLayout());
        tableHeadingPanel.setBackground(Color.WHITE);
        tableHeadingPanel.add(
                new JLabel("MY REQUEST HISTORY"),
                BorderLayout.WEST
        );
        tableHeadingPanel.add(refreshButton, BorderLayout.EAST);

        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(tableHeadingPanel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        requestPanel.add(topPanel, BorderLayout.NORTH);
        requestPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Actions
        submitButton.addActionListener(e -> submitBookRequest());
        refreshButton.addActionListener(e -> loadBookRequests());

        contentPanel.add(requestPanel, "My Book Requests");

        loadBookRequests();

        cardLayout.show(contentPanel, "My Book Requests");
    }

    // Submits a new book request
    private void submitBookRequest() {

        String title = requestTitleField.getText().trim();
        String author = requestAuthorField.getText().trim();
        String reason = requestReasonArea.getText().trim();

        if (title.isEmpty() || author.isEmpty() || reason.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BorrowDAO borrowDAO = new BorrowDAO();

        int studentId =
                borrowDAO.getStudentIdByStudentIdNumber(studentIdNumber);

        if (studentId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to find student information.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        BookRequestDAO requestDAO = new BookRequestDAO();

        boolean success = requestDAO.submitBookRequest(
                studentId, title, author, reason
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Book request submitted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            requestTitleField.setText("");
            requestAuthorField.setText("");
            requestReasonArea.setText("");

            loadBookRequests();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to submit book request.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Loads book requests for the logged-in student
    private void loadBookRequests() {

        BorrowDAO borrowDAO = new BorrowDAO();

        int studentId =
                borrowDAO.getStudentIdByStudentIdNumber(studentIdNumber);

        requestTableModel.setRowCount(0);

        if (studentId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to find student information.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        BookRequestDAO requestDAO = new BookRequestDAO();

        List<Object[]> requests =
                requestDAO.getBookRequestsByStudentId(studentId);

        for (Object[] request : requests) {

            Object[] row = {
                    request[0],
                    request[1],
                    request[2],
                    request[3],
                    request[4],
                    request[5],
                    request[6] == null ? "N/A" : request[6]
            };

            requestTableModel.addRow(row);
        }
    }
}
