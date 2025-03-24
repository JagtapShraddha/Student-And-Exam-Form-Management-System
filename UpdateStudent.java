import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class UpdateStudent extends JFrame {
    JLabel rollnoLabel, nameLabel, phoneLabel, emailLabel, addressLabel;
    JTextField rollnoTextField, nameTextField, phoneTextField, emailTextField, addressTextField;
    JButton searchButton, updateButton;
    JPanel mainPanel, labelPanel, textFieldPanel, buttonPanel;

    UpdateStudent() {
        setTitle("Update Student");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel(new BorderLayout(10, 10));

        // Labels
        rollnoLabel = new JLabel("Enter Roll No:");
        nameLabel = new JLabel("Enter Name:");
        phoneLabel = new JLabel("Enter Phone:");
        emailLabel = new JLabel("Enter Email:");
        addressLabel = new JLabel("Enter Address:");

        labelPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        labelPanel.add(rollnoLabel);
        labelPanel.add(nameLabel);
        labelPanel.add(phoneLabel);
        labelPanel.add(emailLabel);
        labelPanel.add(addressLabel);

        // Text Fields
        rollnoTextField = new JTextField(10);
        nameTextField = new JTextField(10);
        phoneTextField = new JTextField(10);
        emailTextField = new JTextField(10);
        addressTextField = new JTextField(10);

        textFieldPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        textFieldPanel.add(rollnoTextField);
        textFieldPanel.add(nameTextField);
        textFieldPanel.add(phoneTextField);
        textFieldPanel.add(emailTextField);
        textFieldPanel.add(addressTextField);

        // Buttons
        buttonPanel = new JPanel();
        searchButton = new JButton("Search");
        updateButton = new JButton("Update");
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);

        mainPanel.add(labelPanel, BorderLayout.WEST);
        mainPanel.add(textFieldPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Button Actions
        searchButton.addActionListener(e -> searchStudent());
        updateButton.addActionListener(e -> updateStudent());
    }

    // Search Student from DB
    public void searchStudent() {
    	int rollno = Integer.parseInt(rollnoTextField.getText());
        try{
        	Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook","shraddha","");
        	String sql = "select * from students where roll_no = ?";
        	PreparedStatement ps = con.prepareStatement(sql);
        	ps.setInt(1,rollno);
        	ResultSet rs = ps.executeQuery();
        	
     		if(rs.next()){
     			rollnoTextField.setText(rs.getString(1));
     			nameTextField.setText(rs.getString(2));
     			phoneTextField.setText(rs.getString(3));
     			emailTextField.setText(rs.getString(4));
     			addressTextField.setText(rs.getString(5));
     		}
     		else{
     			JOptionPane.showMessageDialog(this,"Student Not Fond");
     		}
     		rs.close();
     		con.close();
     		ps.close();
        	
        }
        catch(Exception e){
        	JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update Student in DB
    public void updateStudent() {
        int rollno = Integer.parseInt(rollnoTextField.getText());
        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();
        String address = addressTextField.getText();
        
          if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled!", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validation: Phone number (exactly 10 digits)
    if (!phone.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(this, "Invalid phone number! Must be exactly 10 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validation: Email format (must contain '@' and '.')
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
    JOptionPane.showMessageDialog(this, "Invalid email format! Must contain '@' and '.' with a valid domain.", "Validation Error", JOptionPane.ERROR_MESSAGE);
    return;
}

        
        try{
        	Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook","shraddha","");
        	String sql = "update students set name=?,phone=?,email=?,address=? where roll_no = ?";
        	PreparedStatement ps = con.prepareStatement(sql);
        	ps.setString(1,name);
        	ps.setString(2,phone);
        	ps.setString(3,email);
        	ps.setString(4,address);
        	ps.setInt(5,rollno);
        	
        	int result = ps.executeUpdate();
        	if(result>0){
        		JOptionPane.showMessageDialog(this,"Student Updated Successfully");
        		dispose();
        		new AdminDashboard();
        	}
        	else{
        		JOptionPane.showMessageDialog(this,"Failed Update! Student Not Found");
        	}
        
            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new UpdateStudent();
    }
}

