import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class AddStudent extends JFrame {
    JTextField rollNoField, nameField, phoneField, emailField, addressField;
    JButton addButton;

    AddStudent() {
        setTitle("Add New Student");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Roll No:"));
        rollNoField = new JTextField();
        add(rollNoField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        addButton = new JButton("Add Student");
        add(addButton);
        
        setVisible(true);

        // Button Action
        addButton.addActionListener(e -> addStudentToDB());
    }

    public void addStudentToDB() {
        String rollNo = rollNoField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        // Validation: Check if fields are empty
        if (rollNo.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validation: Check phone number (exactly 10 digits)
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Invalid phone number! Must be exactly 10 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validation: Check email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");
            String sql = "INSERT INTO students (roll_no, name, phone, email, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(rollNo));
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, address);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Student Added Successfully!");
                dispose(); // Close window after successful addition
                new AdminDashboard(); // Refresh Admin Dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Insertion Failed!");
            }

            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AddStudent();
    }
}

