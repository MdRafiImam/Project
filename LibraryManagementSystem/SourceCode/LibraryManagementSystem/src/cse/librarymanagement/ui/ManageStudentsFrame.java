package cse.librarymanagement.ui;

import cse.librarymanagement.dao.StudentDAO;
import cse.librarymanagement.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ManageStudentsFrame extends JPanel {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    // Internal database IDs are stored separately.
    private final List<Integer> internalStudentIds =
            new ArrayList<>();

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final StudentDAO studentDAO = new StudentDAO();

    public ManageStudentsFrame() {

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("MANAGE STUDENTS");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10)
        );

        add(title, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel();

        searchField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        searchPanel.add(new JLabel("Search Student:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.PAGE_START);

        // Student Table
        String[] columns = {
                "Student ID",
                "Membership ID",
                "Full Name",
                "Email",
                "Phone",
                "Department",
                "Join Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);

        studentTable.setRowHeight(28);
        studentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set column widths.
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(170);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(220);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(160);
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();

        JButton updateButton = new JButton("Update Student");
        JButton cancelButton = new JButton("Cancel Membership");

        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> updateStudent());
        cancelButton.addActionListener(e -> cancelMembership());

        searchButton.addActionListener(e -> searchStudents());
        refreshButton.addActionListener(e -> loadStudents());

        loadStudents();
    }

    // Adds student information to the table.
    private void addStudentToTable(Student student) {

        // Store internal database ID separately.
        internalStudentIds.add(student.getStudentId());

        Object[] row = {
                student.getStudentIdNumber(),
                student.getMembershipId(),
                student.getFullName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment(),
                student.getJoinDate().format(dateFormatter)
        };

        tableModel.addRow(row);
    }

    // Loads all students.
    private void loadStudents() {

        List<Student> students = studentDAO.getAllStudents();

        tableModel.setRowCount(0);
        internalStudentIds.clear();

        for (Student student : students) {
            addStudentToTable(student);
        }
    }

    // Searches students.
    private void searchStudents() {

        String keyword = searchField.getText().trim();

        List<Student> students =
                studentDAO.searchStudents(keyword);

        tableModel.setRowCount(0);
        internalStudentIds.clear();

        for (Student student : students) {
            addStudentToTable(student);
        }

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "No student found!"
            );
        }
    }

    // Updates selected student.
    private void updateStudent() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a student!"
            );
            return;
        }

        int row = studentTable.convertRowIndexToModel(selectedRow);

        int studentId = internalStudentIds.get(row);

        String studentIdNumber =
                tableModel.getValueAt(row, 0).toString();

        String membershipId =
                (String) tableModel.getValueAt(row, 1);

        JTextField studentIdField =
                new JTextField(studentIdNumber);

        JTextField membershipField =
                new JTextField(
                        membershipId == null ? "" : membershipId
                );

        membershipField.setEditable(false);

        JTextField nameField =
                new JTextField(tableModel.getValueAt(row, 2).toString());

        JTextField emailField =
                new JTextField(tableModel.getValueAt(row, 3).toString());

        JTextField phoneField =
                new JTextField(tableModel.getValueAt(row, 4).toString());

        JTextField departmentField =
                new JTextField(tableModel.getValueAt(row, 5).toString());

        Object[] fields = {
                "Student ID:", studentIdField,
                "Membership ID:", membershipField,
                "Full Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Department:", departmentField
        };

        int result = JOptionPane.showConfirmDialog(
                this, fields, "Update Student",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String updatedStudentId =
                studentIdField.getText().trim();

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String department = departmentField.getText().trim();

        if (updatedStudentId.isEmpty() || name.isEmpty()
                || email.isEmpty() || phone.isEmpty()
                || department.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "All fields are required!"
            );
            return;
        }

        LocalDate joinDate;

        try {
            joinDate = LocalDate.parse(
                    tableModel.getValueAt(row, 6).toString(),
                    dateFormatter
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Invalid join date!"
            );
            return;
        }

        Student student = new Student(
                studentId,
                updatedStudentId,
                membershipId,
                name,
                email,
                phone,
                department,
                joinDate
        );

        if (studentDAO.updateStudent(student)) {

            JOptionPane.showMessageDialog(
                    this, "Student Updated Successfully!"
            );

            loadStudents();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update student! Student ID or Email may already exist."
            );
        }
    }

    // Cancels selected student's membership.
    private void cancelMembership() {

        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a student!"
            );
            return;
        }

        int row = studentTable.convertRowIndexToModel(selectedRow);

        int studentId = internalStudentIds.get(row);

        String studentName =
                tableModel.getValueAt(row, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel the membership of "
                        + studentName + "?\n\n"
                        + "The student will no longer be able to login.\n"
                        + "Previous borrowing history will be preserved.",
                "Confirm Membership Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (studentDAO.cancelMembership(studentId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Membership cancelled successfully!"
            );

            loadStudents();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Membership cancellation failed!\n\n"
                            + "Only approved memberships can be cancelled.\n"
                            + "Make sure the student has returned all borrowed books."
            );
        }
    }
}
