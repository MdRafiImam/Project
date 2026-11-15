package cse.librarymanagement.ui;

import cse.librarymanagement.dao.StudentRegistrationDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class StudentRegistrationFrame extends JFrame {

    private JTextField studentIdField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField departmentField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public StudentRegistrationFrame() {

        setTitle("Student Registration");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        studentIdField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        departmentField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);

        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        panel.add(new JLabel("Department:"));
        panel.add(departmentField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        panel.add(registerButton);
        panel.add(backButton);

        registerButton.addActionListener(e -> registerStudent());

        backButton.addActionListener(e -> {
            new StudentLoginFrame().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void registerStudent() {

        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String department = departmentField.getText().trim();

        char[] password = passwordField.getPassword();
        char[] confirmPassword = confirmPasswordField.getPassword();

        if (studentId.isEmpty() || name.isEmpty()
                || email.isEmpty() || phone.isEmpty()
                || department.isEmpty()
                || password.length == 0
                || confirmPassword.length == 0) {

            Arrays.fill(password, '\0');
            Arrays.fill(confirmPassword, '\0');

            JOptionPane.showMessageDialog(
                    this, "All fields are required!"
            );
            return;
        }

        if (password.length < 8) {

            Arrays.fill(password, '\0');
            Arrays.fill(confirmPassword, '\0');

            JOptionPane.showMessageDialog(
                    this, "Password must contain at least 8 characters!"
            );
            return;
        }

        if (!Arrays.equals(password, confirmPassword)) {

            Arrays.fill(password, '\0');
            Arrays.fill(confirmPassword, '\0');

            JOptionPane.showMessageDialog(
                    this, "Passwords do not match!"
            );
            return;
        }

        Arrays.fill(confirmPassword, '\0');

        StudentRegistrationDAO dao = new StudentRegistrationDAO();

        boolean success = dao.registerStudent(
                studentId, name, email,
                phone, department, password
        );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Successful!\n"
                            + "Your account is pending Admin approval."
            );

            new StudentLoginFrame().setVisible(true);
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Failed!\n"
                            + "Student ID or Email may already exist."
            );
        }
    }
}
