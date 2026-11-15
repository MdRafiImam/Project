package cse.librarymanagement.ui;

import cse.librarymanagement.dao.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    // Creates the admin login window.
    public LoginFrame() {

        setTitle("Library Management System - Admin Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel =
                new JLabel("LIBRARY MANAGEMENT SYSTEM");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> loginAdmin());

        add(panel);
    }

    // Validates admin login.
    private void loginAdmin() {

        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter Username and Password!"
            );

            Arrays.fill(password, '\0');
            return;
        }

        loginButtonProcess(username, password);
    }

    // Checks credentials and opens the admin dashboard.
    private void loginButtonProcess(
            String username, char[] password) {

        try {

            AdminDAO adminDAO = new AdminDAO();

            boolean success = adminDAO.login(
                    username, password
            );

            if (success) {

                AdminDashboard dashboard = new AdminDashboard();
                dashboard.setVisible(true);

                this.dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } finally {

            Arrays.fill(password, '\0');
            passwordField.setText("");
        }
    }
}
