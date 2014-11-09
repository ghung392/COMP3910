CREATE DATABASE timesheet;

GRANT ALL PRIVILEGES ON inventory.* TO stock@localhost IDENTIFIED BY 'check';
GRANT ALL PRIVILEGES ON inventory.* TO stock@"%" IDENTIFIED BY 'check';

USE timesheet;

DROP TABLE IF EXISTS Employees;
CREATE TABLE Employees(EmpID int, EmpName TINYTEXT, UserName TINYTEXT, IsAdmin tinyint, Pass TINYTEXT);

INSERT INTO Employees VALUES(1, "John Doe", "test", 0, "test");
INSERT INTO Employees VALUES(2, "Bob Smith", "testadmin", 1, "testadmin");
INSERT INTO Employees VALUES(3, "Gabriel Hung", "ghung392", 1, "toohardtoguess");
INSERT INTO Employees VALUES(4, "Angela Ma", "starangelam", 0, "qwerty");

DROP TABLE IF EXISTS Timetables;