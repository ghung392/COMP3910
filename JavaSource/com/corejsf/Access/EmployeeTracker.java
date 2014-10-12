package com.corejsf.Access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Model.EmployeeModel;
/**
 * Class that tracks and manipulates the employee list. Also provides an authentication
 * method.
 * @author Gabriel
 * @version 1.0
 */
public class EmployeeTracker implements Serializable{
    /**ArrayList holding employees. Initially defined with a superuser and a normal user. */
    private ArrayList<EmployeeModel> employees = new ArrayList<EmployeeModel>(Arrays.asList(
            new EmployeeModel("Gabriel", 1, "ghung392", true, "toohardtoguess"),
            new EmployeeModel("Angela", 2, "starangelma", false, "qwerty")
            ));
    /**
     * Method to find a employee
     * @param id employee number
     * @return employee if find or null
     */
    public EmployeeModel find(final String name) {
        for(int i = 0; i < employees.size(); i++) {
            if(employees.get(i).getName().compareTo(name) == 0) {
                return employees.get(i);
            }
        }
        return null;
    }
    /**
     * Remove an employee from the list
     * @param id employee number
     */
    public void remove(Employee employee) {
        for(int i = 0; i < employees.size(); i++) {
            if(employees.get(i).getEmpNumber() == employee.getEmpNumber()) {
                employees.remove(i);
            }
        }
        return;
    }
    /**
     * Add an employee to the list.
     * @param newEmployee new employee model
     */
    public void add(EmployeeModel newEmployee) {
        employees.add(newEmployee);
        
        return;
    }
    /**
     * Gets the whole list of employees.
     * @return list of employees
     */
    public ArrayList<EmployeeModel> getEmployees() {
        return employees;
    }
    /**
     * Authenticates a user.
     * @param username username input
     * @param password password input
     * @return employee if correct validation or else null
     */
    public EmployeeModel auth(final String username, final String password) {
        for(int i = 0; i < employees.size(); i++) {
            if((employees.get(i).getUserName().compareTo(username) == 0)
                    && (employees.get(i).getPassword().compareTo(password) == 0)) {
                return employees.get(i);
            }
        }
        
        return null;
    }
}
