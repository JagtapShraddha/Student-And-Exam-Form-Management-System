import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.*;

class LogIn extends JFrame {
    JLabel usernameLabel, passwordLabel;
    JTextField usernameTextField;
    JPasswordField passwordField;
    JButton submit;
    JPanel mainPanel, formPanel, buttonPanel;

    String sql;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    LogIn() {
        setTitle("Admin Login");
        setSize(500, 300); // Adjusted window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Prevent window resizing

        mainPanel = new JPanel(new BorderLayout(5, 5));

        formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        usernameLabel = new JLabel("Enter Username:");
        passwordLabel = new JLabel("Enter Password:");

        usernameTextField = new JTextField(10);
        passwordField = new JPasswordField(10); // Secure password input
        
        usernameTextField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(100, 30));

        formPanel.add(usernameLabel);
        formPanel.add(usernameTextField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        buttonPanel = new JPanel();
        submit = new JButton("Submit");
        submit.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(submit);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validateAdmin();
            }
        });
    }

    // JDBC validation
    public void validateAdmin() {
        String username = usernameTextField.getText();
        String password = new String(passwordField.getPassword()); // Correct way to get password

        try {
            // Load PostgreSQL Driver
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook", "shraddha", "");

            sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                dispose(); // Close login window
            	new AdminDashboard(); 
                // Proceed to next screen (Admin Dashboard)
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "JDBC Driver Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LogIn();
    }
}

