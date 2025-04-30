# 🎓 Student Management System

A complete Java Swing application for managing student records and exam form submissions with PostgreSQL database integration.


## 📋 Features

- **🔐 Admin Authentication** - Secure login system for administrators
- **👥 Student Management**
  - View complete student list
  - Add new students with validation
  - Update existing student details
  - Search students by roll number
  - Delete student records
- **📝 Exam Form Management**
  - Track exam form submission status
  - Register new form submissions
  - View submission history
- **📧 Notification System**
  - Send email reminders to students with pending submissions
  - Automated messaging to multiple recipients



## 🛠️ Technologies Used

- **Frontend:** Java Swing
- **Backend:** Java
- **Database:** PostgreSQL
- **Additional Libraries:** JavaMail API 

## 📊 Database Structure

The application uses three main tables:

1. **admin** - Stores administrator credentials
2. **students** - Contains student personal information
3. **exam_form** - Tracks exam form submission status

## 🔧 Setup Instructions

### Prerequisites

- JDK 8 or higher
- PostgreSQL 10 or above
- PostgreSQL JDBC Driver

### Database Setup

1. Create a PostgreSQL database named `contactbook`:
   ```sql
   CREATE DATABASE contactbook;
   ```

2. Create the required tables:
   ```sql
   -- Admin table
   CREATE TABLE admin (
     username VARCHAR(50) PRIMARY KEY,
     password VARCHAR(50) NOT NULL
   );

   -- Students table
   CREATE TABLE students (
     roll_no INTEGER PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     phone VARCHAR(15) NOT NULL,
     email VARCHAR(100) NOT NULL,
     address VARCHAR(200) NOT NULL
   );

   -- Exam form table
   CREATE TABLE exam_form (
     roll_no INTEGER PRIMARY KEY REFERENCES students(roll_no),
     submitted VARCHAR(3) NOT NULL,
     submitted_on DATE
   );
   ```

3. Insert an admin account:
   ```sql
   INSERT INTO admin (username, password) VALUES ('admin', 'password');
   ```

### Application Setup

1. Clone the repository:
   ```
   git clone https://github.com/YourUsername/Student-Management-System.git
   ```

2. Navigate to the project directory:
   ```
   cd Student-Management-System
   ```

3. Compile the Java files:
   ```
   javac *.java
   ```

4. Run the application:
   ```
   java LogIn
   ```

5. For email functionality:
   - Update the `EmailSender.java` file with your email credentials
   - If using Gmail, you'll need to generate an App Password

## 🚀 Usage

1. **Login**: Use the admin credentials to access the system
2. **Dashboard**: Navigate through various functions using the dashboard
3. **Student Management**: Add, view, update, or delete student records
4. **Exam Forms**: Track and manage exam form submissions
5. **Reminders**: Send email notifications to students who haven't submitted forms

## 📁 Project Structure

```
StudentManagementSystem/
│
├── LogIn.java              # Admin authentication
├── AdminDashboard.java     # Main navigation hub
├── StudentList.java        # Displays all students
├── AddStudent.java         # Register new students
├── UpdateStudent.java      # Modify student details
├── DeleteStudent.java      # Remove student records
├── SearchStudent.java      # Find specific students
├── ExamFormStatus.java     # Track form submissions
├── AddExamForm.java        # Register new submissions
└── EmailSender.java        # Email notification system
```

## 🔐 Security Features

- Parameterized SQL queries to prevent injection attacks
- Password masking in UI
- Confirmation dialogs for destructive operations
- Input validation for all form fields

## 🔄 Future Enhancements

- Student login portal for self-service
- PDF report generation
- Data export functionality
- Dashboard analytics and statistics
- File attachments for student documents
- Mobile application integration

## 🤝 Contributing

Contributions to improve the Student Management System are welcome. Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## 👏 Acknowledgements

- [JavaMail API](https://javaee.github.io/javamail/) for email functionality
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) for database connectivity
- [Java Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/) for UI guidance