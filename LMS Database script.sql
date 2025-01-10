CREATE DATABASE LMS;
USE LMS;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Instructor', 'Student') NOT NULL
);

create TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
    );

 create TABLE instructor (
    instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
    );


CREATE TABLE Courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    instructor_id INT NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES Users(user_id) ON DELETE CASCADE
);


INSERT INTO Users (name, email, password, role) VALUES
('Admin User', 'admin@lms.com', 'admin123', 'Admin'),
('Kanye West', 'kanye@lms.com', 'kanye123', 'Instructor'),
('Brody Walls', 'brody@lms.com', 'brody123', 'Student'),
('Dash Kelly', 'dash@lms.com', 'dash123', 'Student');

INSERT INTO Courses (course_name, instructor_id) VALUES
('Java Programming', 2),
('Web Development', 2);










