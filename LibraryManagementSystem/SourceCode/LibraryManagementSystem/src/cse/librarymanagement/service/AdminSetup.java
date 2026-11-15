package cse.librarymanagement.service;

import cse.librarymanagement.util.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminSetup {

    public static void main(String[] args) {

        JTextField nameField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
                "Full Name:", nameField,
                "Username:", usernameField,
                "Password:", passwordField
        };

        int result = JOptionPane.showConfirmDialog(
                null,
                fields,
                "Create Admin Account",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String fullName = nameField.getText().trim();
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (fullName.isEmpty() || username.isEmpty()
                || password.length == 0) {

            JOptionPane.showMessageDialog(
                    null, "All fields are required!"
            );
            return;
        }

        String sql = "INSERT INTO admin " +
                "(username, password, full_name) VALUES (?, ?, ?)";

        try {
            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            try (Connection connection =
                         DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                statement.setString(3, fullName);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        null,
                        "Admin Account Created Successfully!"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Admin Creation Failed: " + e.getMessage()
            );

        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }
}
