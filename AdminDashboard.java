import java.awt.*;
import javax.swing.*;

class AdminDashboard extends JFrame{
	JPanel adminPanel;
	JButton viewStudents,addStudent,updateStudent,deleteStudent,searchStudent,checkExamStatus,logout;
	
	AdminDashboard(){
		setTitle("Admin Dashboard");
		setSize(500,300);
		setLocationRelativeTo(null);
		adminPanel = new JPanel();
		adminPanel.setLayout(new GridLayout(5,1,10,10));
		
		viewStudents = new JButton("View Students List");
		updateStudent = new JButton("Update Student Details");
		deleteStudent = new JButton("Delete Student Details");
		searchStudent = new JButton("Search Student By Roll No.");
		checkExamStatus = new JButton("See Exam Form Status");
		addStudent = new JButton("Add New Student");
		
		
		logout = new JButton("Logout");
		//adding buttons to the panel
		adminPanel.add(viewStudents);
		adminPanel.add(addStudent);
		adminPanel.add(updateStudent);
		adminPanel.add(deleteStudent);
		adminPanel.add(searchStudent);
		adminPanel.add(checkExamStatus);
		adminPanel.add(logout);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(adminPanel);
		setVisible(true);
		
		logout.addActionListener(e ->{
			JOptionPane.showMessageDialog(this,"Logging Out ...");
			dispose();
			new LogIn();
		});
		//adding action listeners to each button 
		
		viewStudents.addActionListener(e -> new StudentList());
		updateStudent.addActionListener(e -> new UpdateStudent());
		deleteStudent.addActionListener(e -> new DeleteStudent());
		checkExamStatus.addActionListener(e -> new ExamFormStatus());
		addStudent.addActionListener(e -> new AddStudent());
		}
		
		public static void main(String[] args){
			new AdminDashboard();
		}
}


