import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class DeleteStudent extends JFrame {
    JTextField rollNoField;
    JButton deleteButton;
    JPanel mainPanel, textFieldPanel, buttonPanel, labelPanel;

    DeleteStudent() {
        setTitle("Delete Student");
        setSize(300, 200); // Adjusted window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(5, 5));
        labelPanel = new JPanel();
        textFieldPanel = new JPanel();
        buttonPanel = new JPanel();

        // Input Label
        JLabel rollNoLabel = new JLabel("Enter Roll No to Delete:");
        labelPanel.add(rollNoLabel);

        // Input TextField
        rollNoField = new JTextField(10);
        rollNoField.setPreferredSize(new Dimension(200, 25)); // Set proper size
        textFieldPanel.add(rollNoField);

        // Delete Button
        deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 30)); // Proper button size
        buttonPanel.add(deleteButton);

        // Adding Components to Main Panel
        mainPanel.add(labelPanel, BorderLayout.NORTH);
        mainPanel.add(textFieldPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true); // Moved here

        // Delete action
        deleteButton.addActionListener(e -> deleteStudent());
    }

    public void deleteStudent() {
        String rollNo = rollNoField.getText().trim();

        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll No cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");
            String sql = "DELETE FROM students WHERE roll_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, rollNo);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
                dispose(); // Close the delete window
                new AdminDashboard(); // Reopen Admin Dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Deletion Failed! Student not found.");
            }

            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new DeleteStudent();
    }
}

