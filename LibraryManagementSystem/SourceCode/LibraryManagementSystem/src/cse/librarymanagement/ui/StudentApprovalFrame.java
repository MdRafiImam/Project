package cse.librarymanagement.ui;

import cse.librarymanagement.dao.StudentApprovalDAO;
import cse.librarymanagement.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentApprovalFrame extends JPanel {

    private JTable studentTable;
    private DefaultTableModel tableModel;

    // Internal database IDs are stored separately, not displayed.
    private final java.util.List<Integer> internalStudentIds =
            new java.util.ArrayList<>();

    private final StudentApprovalDAO approvalDAO =
            new StudentApprovalDAO();

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public StudentApprovalFrame() {

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "PENDING STUDENT REGISTRATIONS",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 10, 10)
        );

        add(title, BorderLayout.NORTH);

        String[] columns = {
                "Student ID",
                "Full Name",
                "Email",
                "Phone",
                "Department",
                "Join Date",
                "Registration Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);

        studentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        studentTable.setRowHeight(28);

        // Set column widths so all information remains visible.
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(190);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(110);
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(studentTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        add(scrollPane, BorderLayout.CENTER);

        JButton approveButton = new JButton("APPROVE");
        JButton rejectButton = new JButton("REJECT");
        JButton refreshButton = new JButton("REFRESH");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(approveButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        approveButton.addActionListener(e -> approveStudent());
        rejectButton.addActionListener(e -> rejectStudent());
        refreshButton.addActionListener(e -> loadPendingStudents());

        loadPendingStudents();
    }

    private void loadPendingStudents() {

        tableModel.setRowCount(0);
        internalStudentIds.clear();

        List<Student> students =
                approvalDAO.getPendingRegistrations();

        for (Student student : students) {

            // Keep internal database ID separately.
            internalStudentIds.add(student.getStudentId());

            tableModel.addRow(new Object[]{
                    student.getStudentIdNumber(),
                    student.getFullName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDepartment(),
                    student.getJoinDate().format(dateFormatter),
                    "PENDING"
            });
        }
    }

    private void approveStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please select a student first!"
            );

            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(row);

        int studentId = internalStudentIds.get(modelRow);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to approve this student?",
                "Confirm Approval",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (approvalDAO.approveRegistration(studentId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student approved successfully!\n"
                            + "Membership ID has been generated."
            );

            loadPendingStudents();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Approval failed! Please try again."
            );
        }
    }

    private void rejectStudent() {

        int row = studentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this, "Please select a student first!"
            );

            return;
        }

        int modelRow = studentTable.convertRowIndexToModel(row);

        int studentId = internalStudentIds.get(modelRow);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to reject this registration?",
                "Confirm Rejection",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (approvalDAO.rejectRegistration(studentId)) {

            JOptionPane.showMessageDialog(
                    this, "Registration rejected successfully!"
            );

            loadPendingStudents();

        } else {

            JOptionPane.showMessageDialog(
                    this, "Rejection failed! Please try again."
            );
        }
    }
}
