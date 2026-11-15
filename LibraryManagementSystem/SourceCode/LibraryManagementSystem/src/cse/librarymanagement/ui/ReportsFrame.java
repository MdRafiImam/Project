package cse.librarymanagement.ui;

import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    // Creates the reports window.
    public ReportsFrame() {

        setTitle("Library Reports - Admin Panel");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(
                "Library Reports", JLabel.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        );

        String[] reportNames = {
                "Overall Statistics",
                "Borrowing Report",
                "Student Report",
                "Book Report"
        };

        for (String name : reportNames) {

            JButton reportButton = new JButton(name);

            reportButton.setFont(
                    new Font("Arial", Font.BOLD, 18)
            );

            if (name.equals("Overall Statistics")) {
                reportButton.addActionListener(e -> {

                    cse.librarymanagement.dao.ReportDAO reportDAO =
                            new cse.librarymanagement.dao.ReportDAO();

                    Object[] stats = reportDAO.getOverallStatistics();

                    String message =
                            "Total Books: " + stats[0] +
                                    "\nTotal Students: " + stats[1] +
                                    "\nCurrently Borrowed Books: " + stats[2] +
                                    "\nReturned Books: " + stats[3] +
                                    "\nOverdue Books: " + stats[4] +
                                    "\nPending Registrations: " + stats[5] +
                                    "\nPending Book Requests: " + stats[6];

                    JOptionPane.showMessageDialog(
                            this,
                            message,
                            "Overall Library Statistics",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            }

            if (name.equals("Borrowing Report")) {

                reportButton.addActionListener(e -> {

                    cse.librarymanagement.dao.ReportDAO reportDAO =
                            new cse.librarymanagement.dao.ReportDAO();

                    java.util.List<Object[]> records =
                            reportDAO.getBorrowingReport();

                    String[] columns = {
                            "Student ID",
                            "Membership ID",
                            "Student Name",
                            "Book Title",
                            "Borrow Date",
                            "Due Date",
                            "Return Date",
                            "Fine",
                            "Status"
                    };

                    javax.swing.table.DefaultTableModel model =
                            new javax.swing.table.DefaultTableModel(
                                    columns, 0
                            ) {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };

                    for (Object[] record : records) {
                        model.addRow(record);
                    }

                    JTable table = new JTable(model);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(1000, 400));

                    JOptionPane.showMessageDialog(
                            this,
                            scrollPane,
                            "Borrowing Report",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            }

            if (name.equals("Student Report")) {

                reportButton.addActionListener(e -> {

                    cse.librarymanagement.dao.ReportDAO reportDAO =
                            new cse.librarymanagement.dao.ReportDAO();

                    java.util.List<Object[]> students =
                            reportDAO.getStudentReport();

                    String[] columns = {
                            "Student ID",
                            "Membership ID",
                            "Full Name",
                            "Email",
                            "Phone",
                            "Department",
                            "Join Date"
                    };

                    javax.swing.table.DefaultTableModel model =
                            new javax.swing.table.DefaultTableModel(
                                    columns, 0
                            ) {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };

                    for (Object[] student : students) {
                        model.addRow(student);
                    }

                    JTable table = new JTable(model);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(900, 400));

                    JOptionPane.showMessageDialog(
                            this,
                            scrollPane,
                            "Student Report",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            }

            if (name.equals("Book Report")) {

                reportButton.addActionListener(e -> {

                    cse.librarymanagement.dao.ReportDAO reportDAO =
                            new cse.librarymanagement.dao.ReportDAO();

                    java.util.List<Object[]> books =
                            reportDAO.getBookReport();

                    String[] columns = {
                            "ISBN",
                            "Book Title",
                            "Author",
                            "Publisher",
                            "Category",
                            "Shelf",
                            "Total Copies",
                            "Available Copies"
                    };

                    javax.swing.table.DefaultTableModel model =
                            new javax.swing.table.DefaultTableModel(
                                    columns, 0
                            ) {
                                @Override
                                public boolean isCellEditable(int row, int column) {
                                    return false;
                                }
                            };

                    for (Object[] book : books) {
                        model.addRow(book);
                    }

                    JTable table = new JTable(model);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(950, 400));

                    JOptionPane.showMessageDialog(
                            this,
                            scrollPane,
                            "Book Report",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            }
            mainPanel.add(reportButton);
        }

        add(mainPanel, BorderLayout.CENTER);
    }
}