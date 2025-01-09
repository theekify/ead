CREATE DATABASE LMS;
USE LMS;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Instructor', 'Student') NOT NULL
);

CREATE TABLE Courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    instructor_id INT NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE CASCADE
);

CREATE TABLE Materials (
    material_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    material_name VARCHAR(200) NOT NULL,
    file_path VARCHAR(300) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE CASCADE
);

CREATE TABLE Assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    description TEXT NOT NULL,
    due_date DATE NOT NULL,
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE CASCADE
);

CREATE TABLE Grades (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT NOT NULL,
    student_id INT NOT NULL,
    grade FLOAT NOT NULL,
    FOREIGN KEY (assignment_id) REFERENCES Assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

INSERT INTO Users (name, email, password, role) VALUES
('Admin User', 'admin@lms.com', 'admin123', 'Admin'),
('Kanye West', 'kanye@lms.com', 'kanye123', 'Instructor'),
('Brody Walls', 'brody@lms.com', 'brody123', 'Student'),
('Dash Kelly', 'dash@lms.com', 'dash123', 'Student');

INSERT INTO Courses (course_name, instructor_id) VALUES
('Java Programming', 2),
('Web Development', 2);

INSERT INTO Enrollments (student_id, course_id) VALUES
(3, 1),
(3, 2),
(4, 1);

INSERT INTO Materials (course_id, material_name, file_path) VALUES
(1, 'Introduction to Java', '/materials/java_intro.pdf'),
(1, 'Java Basics', '/materials/java_basics.pdf'),
(2, 'HTML Basics', '/materials/html_basics.pdf');

INSERT INTO Assignments (course_id, description, due_date) VALUES
(1, 'Complete Java Assignment', '2024-12-31'),
(2, 'Create a Responsive Web Page', '2024-12-28');

INSERT INTO Grades (assignment_id, student_id, grade) VALUES
(1, 3, 85.5),
(1, 4, 90.0),
(2, 3, 78.0);










