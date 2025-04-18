import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class UniversityDatabaseApp {
    static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    static final String USER = "root";
    static final String PASSWORD = "medhaBest17";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            createDepartmentTable(conn);
            createInstructorsTable(conn);
            createStudentTable(conn);
            createCoursesTable(conn);
            createFeesTable(conn);
            createExamTable(conn);
            createTakesTable(conn);
            createTeachesTable(conn);
            createMakesTable(conn);

            insertDepartment(conn, "Computer Science", "Dr. Smith", "CS101, CS102, CS103", "Dr. Smith");
            insertInstructor(conn, 1, "Dr. John", "Computer Science", 75000.00, "CS101", "CS101, CS102");
            insertStudent(conn, 1, "Alice", 2, "CS101", "A", 12);
            insertStudent(conn, 3, "Max", 2, "CS101", "C", 10);
            insertCourse(conn, "CS101", "Introduction to Programming", null, "Computer Science", "A1");
            insertFee(conn, 1500.00, 1, "P001", 1500.00, 100.00);
            insertExam(conn, 1, "CS101", 1, Date.valueOf("2025-05-01"), "A");
            insertExam(conn, 2, "CS101", 3, Date.valueOf("2025-05-04"), "C");
            insertTakes(conn, 1, "CS101");
            insertTeaches(conn, 1, "CS101");
            insertMakes(conn, 1, 1);

            insertDepartment(conn, "Mathematics", "Dr. Euler", "MA101, MA102", "Dr. Euler");
            insertInstructor(conn, 2, "Dr. Newton", "Mathematics", 80000.00, "MA101", "MA101, MA102");
            insertStudent(conn, 2, "Bob", 3, "MA101", "B", 15);
            insertCourse(conn, "MA101", "Calculus I", null, "Mathematics", "A1");
            insertFee(conn, 1800.00, 2, "P002", 1800.00, 120.00);
            insertExam(conn, 3, "MA101", 2, Date.valueOf("2025-05-08"), "B");
            insertTakes(conn, 2, "MA101");
            insertTeaches(conn, 2, "MA101");
            insertMakes(conn, 2, 3);

            insertDepartment(conn, "Physics", "Dr. Feynman", "PH101", "Dr. Feynman");
            insertInstructor(conn, 3, "Dr. Einstein", "Physics", 90000.00, "PH101", "PH101");
            insertStudent(conn, 4, "Eve", 4, "PH101", "A", 18);
            insertCourse(conn, "PH101", "Classical Mechanics", null, "Physics", "A1");
            insertFee(conn, 2000.00, 4, "P004", 2000.00, 150.00);
            insertExam(conn, 4, "PH101", 4, Date.valueOf("2025-05-10"), "A");
            insertTakes(conn, 4, "PH101");
            insertTeaches(conn, 3, "PH101");
            insertMakes(conn, 3, 4);

            readStudents(conn);
            readDepartments(conn);
            readInstructors(conn);
            
            printInstructorTax(conn);
            printHighestGradeStudentPerSemester(conn);


            deleteMakes(conn, 1);
            deleteExam(conn, 1);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void createDepartmentTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Department (" +
                "dept_name VARCHAR(100) PRIMARY KEY, " +
                "dept_head VARCHAR(100), " +
                "courses_offered TEXT, " +
                "instructor_name VARCHAR(100))";
        try {
            executeSQL(conn, sql, "Department");
        } catch (SQLException e) {
            System.err.println("Error creating Department table: " + e.getMessage());
        }
    }

    public static void createInstructorsTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Instructors (" +
                "InstructorID INT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "dept_name VARCHAR(100), " +
                "salary DECIMAL(10,2), " +
                "CoursesID VARCHAR(100), " +
                "courses_taught TEXT, " +
                "FOREIGN KEY (dept_name) REFERENCES Department(dept_name))";
        try {
            executeSQL(conn, sql, "Instructors");
        } catch (SQLException e) {
            System.err.println("Error creating Instructors table: " + e.getMessage());
        }
    }

    public static void createStudentTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Student (" +
                "StudentID INT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "semester INT, " +
                "CourseID VARCHAR(100), " +
                "grades VARCHAR(10), " +
                "tot_cred INT)";
        try {
            executeSQL(conn, sql, "Student");
        } catch (SQLException e) {
            System.err.println("Error creating Student table: " + e.getMessage());
        }
    }

    public static void createCoursesTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (" +
                "CourseID VARCHAR(100) PRIMARY KEY, " +
                "course_name VARCHAR(100), " +
                "pre_req VARCHAR(100), " +
                "dept_name VARCHAR(100), " +
                "section VARCHAR(50), " +
                "FOREIGN KEY (dept_name) REFERENCES Department(dept_name))";
        try {
            executeSQL(conn, sql, "Courses");
        } catch (SQLException e) {
            System.err.println("Error creating Courses table: " + e.getMessage());
        }
    }

    public static void createFeesTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Fees (" +
                "program_tuition DECIMAL(10,2), " +
                "StudentID INT, " +
                "payment_ID VARCHAR(50), " +
                "amount_paid DECIMAL(10,2), " +
                "fees_fee DECIMAL(10,2), " +
                "FOREIGN KEY (StudentID) REFERENCES Student(StudentID))";
        try {
            executeSQL(conn, sql, "Fees");
        } catch (SQLException e) {
            System.err.println("Error creating Fees table: " + e.getMessage());
        }
    }

    public static void createExamTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Exam (" +
                "ExamID INT PRIMARY KEY, " +
                "Course_ID VARCHAR(100), " +
                "Student_ID INT, " +
                "Exam_date DATE, " +
                "grade VARCHAR(5), " +
                "FOREIGN KEY (Course_ID) REFERENCES Courses(CourseID), " +
                "FOREIGN KEY (Student_ID) REFERENCES Student(StudentID))";
        try {
            executeSQL(conn, sql, "Exam");
        } catch (SQLException e) {
            System.err.println("Error creating Exam table: " + e.getMessage());
        }
    }

    public static void createTakesTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Takes (" +
                "StudentID INT, " +
                "CourseID VARCHAR(100), " +
                "PRIMARY KEY (StudentID, CourseID), " +
                "FOREIGN KEY (StudentID) REFERENCES Student(StudentID), " +
                "FOREIGN KEY (CourseID) REFERENCES Courses(CourseID))";
        try {
            executeSQL(conn, sql, "Takes");
        } catch (SQLException e) {
            System.err.println("Error creating Takes table: " + e.getMessage());
        }
    }

    public static void createTeachesTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Teaches (" +
                "InstructorID INT, " +
                "CourseID VARCHAR(100), " +
                "PRIMARY KEY (InstructorID, CourseID), " +
                "FOREIGN KEY (InstructorID) REFERENCES Instructors(InstructorID), " +
                "FOREIGN KEY (CourseID) REFERENCES Courses(CourseID))";
        try {
            executeSQL(conn, sql, "Teaches");
        } catch (SQLException e) {
            System.err.println("Error creating Teaches table: " + e.getMessage());
        }
    }

    public static void createMakesTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Makes (" +
                "InstructorID INT, " +
                "ExamID INT, " +
                "PRIMARY KEY (InstructorID, ExamID), " +
                "FOREIGN KEY (InstructorID) REFERENCES Instructors(InstructorID), " +
                "FOREIGN KEY (ExamID) REFERENCES Exam(ExamID))";
        try {
            executeSQL(conn, sql, "Makes");
        } catch (SQLException e) {
            System.err.println("Error creating Makes table: " + e.getMessage());
        }
    }

    private static void executeSQL(Connection conn, String sql, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table '" + tableName + "' created successfully.");
        }
    }

    private static void insertDepartment(Connection conn, String deptName, String deptHead, String coursesOffered, String instructorName) throws SQLException {
        String sql = "INSERT INTO Department (dept_name, dept_head, courses_offered, instructor_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, deptName);
            statement.setString(2, deptHead);
            statement.setString(3, coursesOffered);
            statement.setString(4, instructorName);
            statement.executeUpdate();
            System.out.println("New department inserted successfully.");
        }
    }

    private static void insertInstructor(Connection conn, int instructorId, String name, String deptName, double salary, String coursesId, String coursesTaught) throws SQLException {
        String sql = "INSERT INTO Instructors (InstructorID, name, dept_name, salary, CoursesID, courses_taught) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, instructorId);
            statement.setString(2, name);
            statement.setString(3, deptName);
            statement.setDouble(4, salary);
            statement.setString(5, coursesId);
            statement.setString(6, coursesTaught);
            statement.executeUpdate();
            System.out.println("New instructor inserted successfully.");
        }
    }

    private static void insertStudent(Connection conn, int studentId, String name, int semester, String courseId, String grades, int totCred) throws SQLException {
        String sql = "INSERT INTO Student (StudentID, name, semester, CourseID, grades, tot_cred) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setString(2, name);
            statement.setInt(3, semester);
            statement.setString(4, courseId);
            statement.setString(5, grades);
            statement.setInt(6, totCred);
            statement.executeUpdate();
            System.out.println("New student inserted successfully.");
        }
    }

    private static void insertCourse(Connection conn, String courseId, String courseName, String preReq, String deptName, String section) throws SQLException {
        String sql = "INSERT INTO Courses (CourseID, course_name, pre_req, dept_name, section) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, courseId);
            statement.setString(2, courseName);
            statement.setString(3, preReq);
            statement.setString(4, deptName);
            statement.setString(5, section);
            statement.executeUpdate();
            System.out.println("New course inserted successfully.");
        }
    }

    private static void insertFee(Connection conn, double programTuition, int studentId, String paymentId, double amountPaid, double feesFee) throws SQLException {
        String sql = "INSERT INTO Fees (program_tuition, StudentID, payment_ID, amount_paid, fees_fee) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setDouble(1, programTuition);
            statement.setInt(2, studentId);
            statement.setString(3, paymentId);
            statement.setDouble(4, amountPaid);
            statement.setDouble(5, feesFee);
            statement.executeUpdate();
            System.out.println("New fee record inserted successfully.");
        }
    }

    private static void insertExam(Connection conn, int examId, String courseId, int studentId, Date examDate, String grade) throws SQLException {
        String sql = "INSERT INTO Exam (ExamID, Course_ID, Student_ID, Exam_date, grade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, examId);
            statement.setString(2, courseId);
            statement.setInt(3, studentId);
            statement.setDate(4, examDate);
            statement.setString(5, grade);
            statement.executeUpdate();
            System.out.println("New exam record inserted successfully.");
        }
    }

    private static void insertTakes(Connection conn, int studentId, String courseId) throws SQLException {
        String sql = "INSERT INTO Takes (StudentID, CourseID) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setString(2, courseId);
            statement.executeUpdate();
            System.out.println("New takes record inserted successfully.");
        }
    }

    private static void insertTeaches(Connection conn, int instructorId, String courseId) throws SQLException {
        String sql = "INSERT INTO Teaches (InstructorID, CourseID) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, instructorId);
            statement.setString(2, courseId);
            statement.executeUpdate();
            System.out.println("New teaches record inserted successfully.");
        }
    }

    private static void insertMakes(Connection conn, int instructorId, int examId) throws SQLException {
        String sql = "INSERT INTO Makes (InstructorID, ExamID) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, instructorId);
            statement.setInt(2, examId);
            statement.executeUpdate();
            System.out.println("New makes record inserted successfully.");
        }
    }

    private static void readStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Student";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int studentId = resultSet.getInt("StudentID");
                String name = resultSet.getString("name");
                int semester = resultSet.getInt("semester");
                String courseID = resultSet.getString("CourseID");
                String grades = resultSet.getString("grades");
                int totCred = resultSet.getInt("tot_cred");
                System.out.println("Student ID: " + studentId + ", Name: " + name + ", Semester: " + semester + ", Course ID: " + courseID + ", Grades: " + grades + ", Total Credits: " + totCred);
            }
        }
    }

    private static void readDepartments(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Department";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String deptName = resultSet.getString("dept_name");
                String deptHead = resultSet.getString("dept_head");
                String coursesOffered = resultSet.getString("courses_offered");
                String instructorName = resultSet.getString("instructor_name");
                System.out.println("Department Name: " + deptName + ", Head: " + deptHead + ", Courses Offered: " + coursesOffered + ", Instructor Name: " + instructorName);
            }
        }
    }

    private static void readInstructors(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Instructors";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int instructorID = resultSet.getInt("InstructorID");
                String name = resultSet.getString("name");
                String deptName = resultSet.getString("dept_name");
                double salary = resultSet.getDouble("salary");
                String coursesTaught = resultSet.getString("courses_taught");
                System.out.println("Instructor ID: " + instructorID + ", Name: " + name + ", Department: " + deptName + ", Salary: " + salary + ", Courses Taught: " + coursesTaught);
            }
        }
    }

    private static void deleteMakes(Connection conn, int ExamID) throws SQLException {
        String sql = "DELETE FROM Makes WHERE ExamID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ExamID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Makes deleted successfully");
            }
        }
    }

    private static void deleteExam(Connection conn, int studentId) throws SQLException {
        String sql = "DELETE FROM Exam WHERE Student_ID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Exam deleted successfully");
            }
        }
    }
    
    private static void printInstructorTax(Connection conn) throws SQLException {
        String sql = "SELECT name, salary FROM Instructors";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("\nInstructor Tax Amounts:");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                double tax = salary * 0.10;
                System.out.println("Instructor: " + name + ", Tax: " + tax);
            }
        }
    }
    
     private static void printHighestGradeStudentPerSemester(Connection conn) throws SQLException {
        String sql = "SELECT semester, name, grades FROM Student WHERE (semester, grades) IN (SELECT semester, MAX(grades) FROM Student GROUP BY semester)";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            System.out.println("\nStudents with Highest Grades per Semester:");
            while (resultSet.next()) {
                int semester = resultSet.getInt("semester");
                String name = resultSet.getString("name");
                String grades = resultSet.getString("grades");
                System.out.println("Semester: " + semester + ", Student: " + name + ", Grade: " + grades);
            }
        }
    }
}