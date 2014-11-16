package com.corejsf.Model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.bcit.infosys.employee.Employee;

/**
 * Employee Model that extends Employee to add password and admin status
 * attributes.
 *
 * @author Gabriel
 * @version 1.0
 */
@Entity
@Table(name = "Employees")
public class EmployeeModel extends Employee implements Serializable {
    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;
    /** Employee password. */
    private String password;
    /** Employee admin status. */
    private boolean admin;

    /**
     * Default constructor.
     */
    public EmployeeModel() {

    }

    /**
     * Constructor to instantiate an employee.
     *
     * @param empName employee name
     * @param id employee username
     * @param isSuper boolean to determine whether user has admin status
     * @param pass employee password
     */
    public EmployeeModel(final String empName, final String id,
            final boolean isSuper, final String pass) {

        super(empName, id);
        admin = isSuper;
        password = pass;
    }

    /**
     * Getter for employee name.
     *
     * @return employee name
     */
    @Column(name = "EmpName")
    public String getEmpName() {
        return getName();
    }

    /**
     * Setter for employee name.
     *
     * @param empName new employee name
     */
    public void setEmpName(final String empName) {
        setName(empName);
    }

    /**
     * Getter for employee number.
     *
     * @return employee number
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmpID")
    public int getId() {
        return getEmpNumber();
    }

    /**
     * Setter for employee number.
     *
     * @param number new employee number
     */
    public void setId(final int number) {
        setEmpNumber(number);
    }

    /**
     * Getter for employee username.
     *
     * @return employee username
     */
    @Column(name = "UserName")
    public String getUserId() {
        return getUserName();
    }

    /**
     * Setter for employee username.
     *
     * @param id new employee username
     */
    public void setUserId(final String id) {
        setUserName(id);
    }

    /**
     * Getter for password.
     *
     * @return employee password
     */
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
    @Column(name = "IsAdmin")
    public boolean getAdmin() {
        return admin;
    }

    @Override
    public final boolean equals(final Object o) {
        if (o == null && !(o instanceof EmployeeModel)) {
            return false;
        }

        EmployeeModel emp = (EmployeeModel) o;
        return emp.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getEmpNumber();
        result = prime * result
                + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result
                + ((getUserName() == null) ? 0 : getUserName().hashCode());
        return result;
    }

}
