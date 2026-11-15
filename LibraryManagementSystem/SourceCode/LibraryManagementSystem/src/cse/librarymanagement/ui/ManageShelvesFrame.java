package cse.librarymanagement.ui;

import cse.librarymanagement.dao.ShelfDAO;
import cse.librarymanagement.dao.CategoryDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageShelvesFrame extends JPanel {

    private JTable shelfTable;
    private DefaultTableModel tableModel;

    public ManageShelvesFrame() {

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("MANAGE LIBRARY SHELVES");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);

        String[] columns = {
                "Shelf ID", "Shelf Code", "Category", "Capacity"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        shelfTable = new JTable(tableModel);
        shelfTable.setRowHeight(25);

        add(new JScrollPane(shelfTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Shelf");
        JButton updateButton = new JButton("Update Shelf");
        JButton deleteButton = new JButton("Delete Shelf");
        JButton addCategoryButton = new JButton("Add Category");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addCategoryButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addShelf());
        updateButton.addActionListener(e -> updateShelf());
        deleteButton.addActionListener(e -> deleteShelf());
        addCategoryButton.addActionListener(e -> addCategory());
        refreshButton.addActionListener(e -> loadShelves());

        loadShelves();
    }

    // Loads all shelves from the database.
    private void loadShelves() {

        ShelfDAO shelfDAO = new ShelfDAO();

        List<Object[]> shelves = shelfDAO.getAllShelvesWithDetails();

        tableModel.setRowCount(0);

        for (Object[] shelf : shelves) {
            tableModel.addRow(shelf);
        }
    }

    // Adds a new category to the database.
    private void addCategory() {

        String categoryName = JOptionPane.showInputDialog(
                this,
                "Enter New Category Name:",
                "Add New Category",
                JOptionPane.PLAIN_MESSAGE
        );

        if (categoryName == null) {
            return;
        }

        categoryName = categoryName.trim();

        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Category name cannot be empty!"
            );
            return;
        }

        CategoryDAO categoryDAO = new CategoryDAO();

        if (categoryDAO.getCategoryId(categoryName) != -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "This category already exists!"
            );
            return;
        }

        if (categoryDAO.addCategory(categoryName)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Category Added Successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add category!"
            );
        }
    }

    // Opens the form for adding a new shelf.
    private void addShelf() {

        JTextField codeField = new JTextField();

        CategoryDAO categoryDAO = new CategoryDAO();

        JComboBox<String> categoryBox =
                new JComboBox<>(
                        categoryDAO.getAllCategories().toArray(new String[0])
                );

        JTextField capacityField = new JTextField();

        Object[] fields = {
                "Shelf Code:", codeField,
                "Category:", categoryBox,
                "Capacity:", capacityField
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Add New Shelf",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {

            String code = codeField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
            int capacity = Integer.parseInt(capacityField.getText().trim());

            if (code.isEmpty() || category == null || capacity <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid shelf information!"
                );
                return;
            }

            int categoryId = categoryDAO.getCategoryId(category);

            ShelfDAO shelfDAO = new ShelfDAO();

            if (shelfDAO.addShelf(code, categoryId, capacity)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Shelf Added Successfully!"
                );

                loadShelves();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add shelf!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Capacity must be a valid number!"
            );
        }
    }

    // Updates the selected shelf.
    private void updateShelf() {

        int row = shelfTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a shelf!"
            );
            return;
        }

        int shelfId = (int) tableModel.getValueAt(row, 0);

        JTextField codeField = new JTextField(
                tableModel.getValueAt(row, 1).toString()
        );

        CategoryDAO categoryDAO = new CategoryDAO();

        JComboBox<String> categoryBox =
                new JComboBox<>(
                        categoryDAO.getAllCategories().toArray(new String[0])
                );

        categoryBox.setSelectedItem(
                tableModel.getValueAt(row, 2).toString()
        );

        JTextField capacityField = new JTextField(
                tableModel.getValueAt(row, 3).toString()
        );

        Object[] fields = {
                "Shelf Code:", codeField,
                "Category:", categoryBox,
                "Capacity:", capacityField
        };

        int result = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Update Shelf",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {

            String code = codeField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
            int capacity = Integer.parseInt(capacityField.getText().trim());

            if (code.isEmpty() || category == null || capacity <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid shelf information!"
                );
                return;
            }

            int categoryId = categoryDAO.getCategoryId(category);

            ShelfDAO shelfDAO = new ShelfDAO();

            if (shelfDAO.updateShelf(
                    shelfId, code, categoryId, capacity)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Shelf Updated Successfully!"
                );

                loadShelves();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update shelf!"
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Capacity must be a valid number!"
            );
        }
    }

    // Deletes the selected shelf.
    private void deleteShelf() {

        int row = shelfTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a shelf!"
            );
            return;
        }

        int shelfId = (int) tableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this shelf?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            ShelfDAO shelfDAO = new ShelfDAO();

            if (shelfDAO.deleteShelf(shelfId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Shelf Deleted Successfully!"
                );

                loadShelves();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cannot delete this shelf. It may contain books!"
                );
            }
        }
    }
}
