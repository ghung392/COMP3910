package com.corejsf.Model;

import ca.bcit.infosys.employee.Employee;
/**
 * Employee Model
 * @author Gabriel
 * @version 1.0
 */
public class EmployeeModel extends Employee {
    private String password;
    private boolean admin;
    
    public EmployeeModel(final String empName, final int number, final String id, final boolean isSuper, final String pass) {
        super(empName, number, id);
        admin = isSuper;
        password = pass;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean newValue) {
        admin = newValue;
    }
    
    public boolean isAdmin() {
        return admin;
    }
}
