package com.corejsf.Model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Employee Model that extends Employee to add password and admin status
 * attributes.
 *
 * @author Gabriel
 * @version 1.0
 */
@XmlRootElement(name = "employee")
@Entity
@Table(name = "Employees")
public class Employee implements Serializable {
    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;
    /** Employee password. */
    private String password;
    /** Employee admin status. */
    private boolean admin;
    /** Employee first name. */
    private String firstName;
    /** Employee last name. */
    private String lastName;
    /** Employee username. */
    private String userName;
    /** Employee ID. */
    private int id;

    /**
     * Default constructor.
     */
    public Employee() {

    }

    /**
     * Constructor to instantiate an employee.
     *
     * @param first employee first name
     * @param last employee last name
     * @param user employee username
     * @param isSuper boolean to determine whether user has admin status
     * @param pass employee password
     */
    public Employee(final String first, final String last, final String user,
            final boolean isSuper, final String pass) {

        firstName = first;
        lastName = last;
        userName = user;
        admin = isSuper;
        password = pass;
    }

    /**
     * Getter for employee first name.
     *
     * @return employee first name
     */
    @XmlElement
    @Column(name = "EmpFirstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for employee first name.
     *
     * @param empFirstName new employee first name
     */
    public void setFirstName(final String empFirstName) {
        firstName = empFirstName;
    }

    /**
     * Getter for employee first name.
     *
     * @return employee first name
     */
    @XmlElement
    @Column(name = "EmpLastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for employee first name.
     *
     * @param empLastName new employee first name
     */
    public void setLastName(final String empLastName) {
        lastName = empLastName;
    }

    /**
     * Getter for employee number.
     *
     * @return employee number
     */
    @XmlAttribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmpID")
    public int getId() {
        return id;
    }

    /**
     * Setter for employee number.
     *
     * @param number new employee number
     */
    public void setId(final int number) {
        id = number;
    }

    /**
     * Getter for employee username.
     *
     * @return employee username
     */
    @XmlElement
    @Column(name = "UserName")
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for employee username.
     *
     * @param identification new employee username
     */
    public void setUserName(final String identification) {
        userName = identification;
    }

    /**
     * Getter for password.
     *
     * @return employee password
     */
    @XmlElement
    @Column(name = "Pass")
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password.
     *
     * @param pass new password
     */
    public void setPassword(final String pass) {
        password = pass;
    }

    /**
     * Setter for admin status.
     *
     * @param newValue new admin status
     */
    public void setAdmin(final boolean newValue) {
        admin = newValue;
    }

    /**
     * Getter for admin status.
     *
     * @return current admin status
     */
    @XmlElement
    @Column(name = "IsAdmin")
    public boolean getAdmin() {
        return admin;
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null && !(o instanceof Employee)) {
            return false;
        }

        Employee emp = (Employee) o;
        return emp.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId();
        result = prime * result
                + ((getFirstName() == null) ? 0 : getFirstName().hashCode());
        result = prime * result
                + ((getUserName() == null) ? 0 : getUserName().hashCode());
        return result;
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Employee{\n");
        sb.append("id=").append(id).append(",\n");
        sb.append("firstName=").append(firstName).append(",\n");
        sb.append("lastName=").append(lastName).append(",\n");
        sb.append("userName=").append(userName).append(",\n");
        sb.append("isAdmin=").append(admin).append(",\n");
        sb.append("password=").append(password).append("\n");
        sb.append("}\n");

        return sb.toString();
    }

}
