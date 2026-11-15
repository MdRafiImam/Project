package cse.librarymanagement.ui;

import cse.librarymanagement.dao.BookRequestDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookRequestsFrame extends JPanel {

    private final BookRequestDAO bookRequestDAO;
    private JTable requestTable;
    private DefaultTableModel tableModel;

    // Creates the book requests window.
    public BookRequestsFrame() {

        bookRequestDAO = new BookRequestDAO();

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(
                "Manage Book Requests", JLabel.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Request ID",
                "Student ID",
                "Membership ID",
                "Student Name",
                "Book Title",
                "Author",
                "Reason",
                "Request Date",
                "Status",
                "Admin Note"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        requestTable = new JTable(tableModel);
        requestTable.setRowHeight(25);
        requestTable.setFillsViewportHeight(true);
        requestTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");

        approveButton.addActionListener(e -> handleRequest("APPROVED"));
        rejectButton.addActionListener(e -> handleRequest("REJECTED"));

        JButton refreshButton = new JButton("Refresh");

        refreshButton.addActionListener(e -> loadBookRequests());

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(approveButton);
        bottomPanel.add(rejectButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        loadBookRequests();
    }

    // Loads all book requests from the database.
    private void loadBookRequests() {

        tableModel.setRowCount(0);

        List<Object[]> requests =
                bookRequestDAO.getAllBookRequests();

        for (Object[] request : requests) {
            tableModel.addRow(request);
        }
    }

    // Approves or rejects the selected book request.
    private void handleRequest(String status) {

        int selectedRow = requestTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a book request first."
            );
            return;
        }

        int modelRow = requestTable.convertRowIndexToModel(selectedRow);

        int requestId = (int) tableModel.getValueAt(modelRow, 0);

        String adminNote = JOptionPane.showInputDialog(
                this, "Enter admin note:"
        );

        if (adminNote == null) {
            return;
        }

        boolean updated = bookRequestDAO.updateRequestStatus(
                requestId, status, adminNote.trim()
        );

        if (updated) {
            JOptionPane.showMessageDialog(
                    this, "Book request " + status.toLowerCase() + " successfully."
            );
            loadBookRequests();
        } else {
            JOptionPane.showMessageDialog(
                    this, "Unable to update request. It may have already been processed."
            );
        }
    }
}