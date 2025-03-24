import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

class SearchStudent extends JFrame {
    JButton search;
    JTextField rollnoTextField;
    JPanel mainPanel;
    JTable studentTable;
    DefaultTableModel model;

    SearchStudent() {
        setTitle("Search Student Details");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // UI Components
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Roll No: "));
        rollnoTextField = new JTextField(10);
        search = new JButton("Search Student");
        inputPanel.add(rollnoTextField);
        inputPanel.add(search);

        String[] columnNames = {"Roll No", "Name", "Phone", "Email", "Address", "Submitted"};
        model = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(studentTable); // Add JScrollPane

        // Add Components
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        // Search Button Click Event
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
    }

    // Search Student by Roll No
    public void searchStudent() {
        String rollno = rollnoTextField.getText().trim();
        if (rollno.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Roll Number!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollnoInt = Integer.parseInt(rollno); // Convert to Integer

            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");

            String sql = "SELECT students.roll_no, students.name, students.phone, students.email, " +
                         "students.address, exam_form.submitted, exam_form.submitted_on " +
                         "FROM students " +
                         "JOIN exam_form ON students.roll_no = exam_form.roll_no " +
                         "WHERE students.roll_no = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, rollnoInt);
            ResultSet rs = ps.executeQuery();

            // Clear old data
            model.setRowCount(0);

            if (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String submitted = rs.getString("submitted");
               

                model.addRow(new Object[]{rollno, name, phone, email, address, submitted});
            } else {
                JOptionPane.showMessageDialog(this, "Student Does Not Exist!", "Check Roll No", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Roll Number! Must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SearchStudent();
    }
}

