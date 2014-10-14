package com.corejsf.Model;

import java.io.Serializable;

import ca.bcit.infosys.employee.Employee;
/**
 * Employee Model
 * @author Gabriel
 * @version 1.0
 */
public class EmployeeModel extends Employee implements Serializable {
    /** Employee password */
    private String password;
    /** Employee admin status */
    private boolean admin;
    /**
     * Constructor to instantiate an employee
     * @param empName employee name
     * @param number employee number
     * @param id employee username
     * @param isSuper boolean to determine whether user has admin status
     * @param pass employee password
     */
    public EmployeeModel(final String empName, final int number, final String id, final boolean isSuper, final String pass) {
        super(empName, number, id);
        admin = isSuper;
        password = pass;
    }
    /**
     * Getter for password
     * @return employee password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Setter for password
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Setter for admin status
     * @param newValue new admin status
     */
    public void setAdmin(boolean newValue) {
        admin = newValue;
    }
    /**
     * Getter for admin status
     * @return current admin status
     */
    public boolean isAdmin() {
        return admin;
    }
    
    @Override
    public boolean equals(Object o) {
    	Employee emp = (Employee) o;
    	return emp.getEmpNumber() == this.getEmpNumber();
    }
}
