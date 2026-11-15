package cse.librarymanagement.ui;

import cse.librarymanagement.dao.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AdminCredentialsFrame extends JPanel {

    private JTextField currentUsernameField;
    private JPasswordField currentPasswordField;
    private JTextField newUsernameField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    // Creates the admin credentials form.
    public AdminCredentialsFrame() {

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("CHANGE ADMIN CREDENTIALS");
        heading.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(heading, gbc);

        gbc.gridwidth = 1;

        currentUsernameField = new JTextField(20);
        currentPasswordField = new JPasswordField(20);
        newUsernameField = new JTextField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        // Adds credential input fields.
        addField("Current Username:", currentUsernameField, gbc, 1);
        addField("Current Password:", currentPasswordField, gbc, 2);
        addField("New Username:", newUsernameField, gbc, 3);
        addField("New Password:", newPasswordField, gbc, 4);
        addField("Confirm New Password:", confirmPasswordField, gbc, 5);

        JButton updateButton = new JButton("UPDATE CREDENTIALS");

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(updateButton, gbc);

        updateButton.addActionListener(e -> updateCredentials());
    }

    // Adds a label and input field to the form.
    private void addField(
            String labelText,
            JComponent field,
            GridBagConstraints gbc,
            int row) {

        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        add(field, gbc);
    }

    // Validates and updates admin credentials.
    private void updateCredentials() {

        String currentUsername =
                currentUsernameField.getText().trim();

        String newUsername =
                newUsernameField.getText().trim();

        char[] currentPassword =
                currentPasswordField.getPassword();

        char[] newPassword =
                newPasswordField.getPassword();

        char[] confirmPassword =
                confirmPasswordField.getPassword();

        try {

            if (currentUsername.isEmpty() ||
                    newUsername.isEmpty() ||
                    currentPassword.length == 0 ||
                    newPassword.length == 0 ||
                    confirmPassword.length == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields."
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
                        "New passwords do not match."
                );

                return;
            }

            AdminDAO adminDAO = new AdminDAO();

            boolean success = adminDAO.updateAdminCredentials(
                    currentUsername,
                    currentPassword,
                    newUsername,
                    newPassword
            );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Admin credentials updated successfully!\n"
                                + "Please use the new credentials next time."
                );

                currentUsernameField.setText("");
                currentPasswordField.setText("");
                newUsernameField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Current credentials are incorrect, or the new "
                                + "username is already in use.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } finally {

            Arrays.fill(currentPassword, '\0');
            Arrays.fill(newPassword, '\0');
            Arrays.fill(confirmPassword, '\0');
        }
    }
}
