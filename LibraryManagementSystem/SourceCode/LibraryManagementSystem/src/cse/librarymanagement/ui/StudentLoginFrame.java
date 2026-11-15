package cse.librarymanagement.ui;

import cse.librarymanagement.dao.StudentLoginDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class StudentLoginFrame extends JFrame {

    private JTextField studentIdField;
    private JPasswordField passwordField;

    // Creates the student login window.
    public StudentLoginFrame() {

        setTitle("Student Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel(
                "STUDENT LOGIN",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(
                BorderFactory.createEmptyBorder(25, 10, 10, 10)
        );

        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 20, 35)
        );

        studentIdField = new JTextField();
        passwordField = new JPasswordField();

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(studentIdField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 10));

        buttonPanel.add(loginButton);

        // Registration Link
        JLabel registerLabel = new JLabel(
                "<html>Don't have an account? "
                        + "<font color='blue'><u>Register here</u></font>"
                        + "</html>",
                SwingConstants.CENTER
        );

        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                new StudentRegistrationFrame().setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(registerLabel);

        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> loginStudent());
    }

    // Validates student login and opens the dashboard.
    private void loginStudent() {

        String studentId = studentIdField.getText().trim();
        char[] password = passwordField.getPassword();

        if (studentId.isEmpty() || password.length == 0) {

            Arrays.fill(password, '\0');

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter Student ID and Password."
            );

            return;
        }

        try {

            StudentLoginDAO studentDAO = new StudentLoginDAO();

            if (studentDAO.login(studentId, password)) {

                passwordField.setText("");

                new StudentDashboardFrame(studentId).setVisible(true);
                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Student ID or Password.\n"
                                + "Make sure your registration is approved.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } finally {

            Arrays.fill(password, '\0');
        }
    }
}
