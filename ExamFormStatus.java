import java.awt.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ExamFormStatus extends JFrame {
    JTable statusTable;
    DefaultTableModel model;
    JPanel panel;
    JButton send, addExamForm;
    JLabel statusLabel;

    ExamFormStatus() {
        setTitle("Exam Form Submission Status");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Define Table Columns
        String[] columns = {"Roll No", "Name", "Submitted", "Date"};
        model = new DefaultTableModel(columns, 0);
        statusTable = new JTable(model); // Link model to JTable
        JScrollPane scrollPane = new JScrollPane(statusTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchExamFormData(); // Load Data into JTable

        // Panel for Buttons
        panel = new JPanel();
        send = new JButton("Send Reminder to All");
        addExamForm = new JButton("Add Student to Exam Form");
        statusLabel = new JLabel("");

        panel.add(send);
        panel.add(addExamForm);
        panel.add(statusLabel);
        add(panel, BorderLayout.SOUTH);

        // Button Actions
        addExamForm.addActionListener(e -> new AddExamForm());
        send.addActionListener(e -> sendEmailReminders());

        setVisible(true);
    }

    // Fetch Exam Form Data
    public void fetchExamFormData() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");
            String sql = "SELECT students.roll_no, students.name, exam_form.submitted, exam_form.submitted_on FROM students LEFT JOIN exam_form ON students.roll_no = exam_form.roll_no";
            
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            model.setRowCount(0); // Clear previous data
            while (rs.next()) {
                String roll_no = rs.getString("roll_no");
                String name = rs.getString("name");
                String submitted = rs.getString("submitted");
                String date = rs.getString("submitted_on");

                if (submitted == null || submitted.equalsIgnoreCase("no")) {
                    submitted = "No";
                    date = "N/A";
                } else {
                    submitted = "Yes";
                }

                model.addRow(new Object[]{roll_no, name, submitted, date});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Send Email Reminders
    public void sendEmailReminders() {
    System.out.println("🔹 Fetching student emails...");

    ArrayList<String> studentEmails = new ArrayList<>();
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");
        String sql = "SELECT students.email FROM students,exam_form where students.roll_no = exam_form.roll_no and submitted ='no'";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            studentEmails.add(rs.getString("email"));
        }

        System.out.println("✅ Emails fetched: " + studentEmails);

        rs.close();
        ps.close();
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (studentEmails.isEmpty()) {
        System.out.println("❌ No pending students.");
        JOptionPane.showMessageDialog(this, "No Pending Students!", "Reminder Status", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    System.out.println("🚀 Sending emails...");
    for (String email : studentEmails) {
        boolean success = EmailSender.sendEmail(email, "Exam Form Reminder", "Reminder: Please submit your exam form ASAP!");
        if (!success) {
            System.out.println("❌ Failed to send email to: " + email);
        } else {
            System.out.println("✅ Email sent to: " + email);
        }
    }

    JOptionPane.showMessageDialog(this, "Email reminders sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
}

    public static void main(String[] args) {
        new ExamFormStatus();
    }
}

