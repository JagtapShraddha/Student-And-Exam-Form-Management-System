import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

class StudentList extends JFrame{
	JTable studentTable;
	DefaultTableModel model;
	
	StudentList(){
		setTitle("Student List");
		setSize(600,400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		String[] columnNames = {"Roll No","Name","Phone","Email","Address"};
		model = new DefaultTableModel(columnNames,0);
		studentTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(studentTable);
		
		add(scrollPane,BorderLayout.CENTER);
		setVisible(true);
		
		fetchStudentData(); //load data from student table
	}
	
	public void fetchStudentData(){
		try{
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/contactbook","shraddha","");
			String sql = "select * from students";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				String rollno = rs.getString("roll_no");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				String address = rs.getString("address");
				
				model.addRow(new Object[]{rollno,name,phone,email,address});
				
			}
			rs.close();
			ps.close();
			con.close();
		}catch(Exception e){
			JOptionPane.showMessageDialog(this,"Database Error: "+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
		}
	}
	//public static void main(String[] args){
	//	new StudentList();
	//}
	
}
