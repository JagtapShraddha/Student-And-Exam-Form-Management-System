import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class AddExamForm extends JFrame {
    JComboBox<String> rollNoDropdown;
    JComboBox<String> submittedBox;
    JTextField dateField;
    JButton addButton;
    JPanel formPanel, buttonPanel;

    AddExamForm() {
        setTitle("Add Student to Exam Form");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        
        formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

       
        formPanel.add(new JLabel("Select Roll No:"));
        rollNoDropdown = new JComboBox<>();
        loadRollNumbers();
        formPanel.add(rollNoDropdown);

        
        formPanel.add(new JLabel("Submitted (Yes/No):"));
        submittedBox = new JComboBox<>(new String[]{"Yes", "No"});
        formPanel.add(submittedBox);

       
        formPanel.add(new JLabel("Submission Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

       
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add to Exam Form");
        addButton.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(addButton);

       
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

     
        addButton.addActionListener(e -> addStudentToExamForm());
    }

   
    private void loadRollNumbers() {
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");
            String sql = "SELECT roll_no FROM students WHERE roll_no NOT IN (SELECT roll_no FROM exam_form)";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rollNoDropdown.addItem(rs.getString("roll_no"));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   //add those student to exam form table who have not submitted exam form yet
    public void addStudentToExamForm() {
        String rollNo = (String) rollNoDropdown.getSelectedItem();
        String submitted = (String) submittedBox.getSelectedItem();
        String date = dateField.getText().trim();

//choose yes no from combo box
        if (submitted.equals("Yes") && date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter submission date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");
            String sql = "INSERT INTO exam_form (roll_no, submitted, submitted_on) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(rollNo));
            ps.setString(2, submitted.toLowerCase());
            if (submitted.equals("Yes")) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(date);   //for date we use java.sql.Date
            ps.setDate(3, sqlDate);
        } else {
            ps.setNull(3, java.sql.Types.DATE);
        }

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Student Added to Exam Form!");
                dispose();
                new ExamFormStatus(); 
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
        new AddExamForm();
    }
}

