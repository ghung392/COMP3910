package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.Access.EmployeeTracker;
import com.corejsf.Model.EmployeeModel;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * Backing bean to handle user authentication and to keep track of all the existing users
 * @author Gabriel
 *
 */
@Named("employee")
@SessionScoped
public class EmployeeInfo implements Serializable, EmployeeList {
   
    @Inject private EmployeeTracker employeeList;
    //@Inject private Employee employee;
    private EmployeeModel currentEmployee;    
    private boolean admin;

    @Override
    public List<Employee> getEmployees() {
        return null;
    }
    /**
     * Method to get the list of employees
     * @return arraylist of employees
     */
    public ArrayList<EmployeeModel> getEmployeeList() {
        return employeeList.getEmployees();
    }
    @Override
    /**
     * @param name the name of the employee
     * @return the employee
     */
    public Employee getEmployee(String name) {
        return employeeList.find(name);
        
    }

    @Override
    /**
     * @return the Map containing the valid (userName, password) combinations.
     */
    public Map<String, String> getLoginCombos() {
        return null;
    }

    @Override
    /**
     * Method to return the current employee signed in
     * @return current employee
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    @Override
    public Employee getAdministrator() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean getAdmin() {
        return currentEmployee.isAdmin();
    }

    @Override
    /**
     * Verifies that the loginID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return true if it is, false if it is not.
     */
    public boolean verifyUser(Credentials credential) {
        return false;
    }
    /**
     * Authenticates user and sets current employee depending on matching credentials.
     * @param username input username
     * @param password input password
     */
    public String verifyEmployee(final String username, final String password) {
        currentEmployee = employeeList.auth(username, password);
        
        if( currentEmployee == null ) {
            return "faillogin";
        } else {
            return "successlogin";
        }
    }

    @Override
    public String logout(Employee employee) {
        currentEmployee = null;
        return "logout";
    }
    /**
     * Logs out the current employee.
     * @return logout for navigation
     */
    public String employeeLogout() {
        currentEmployee = null;
        return "logout";
    }

    @Override
    public void deleteEmpoyee(Employee userToDelete) {
        employeeList.remove(userToDelete);
        
    }

    @Override
    public void addEmployee(Employee newEmployee) {
    }
    
    public void addEmployeeToList(EmployeeModel newEmployee) {
        employeeList.add(newEmployee);
    }
    
    public String updateEmployee(final String name, final String password) {
        currentEmployee.setName(name);
        currentEmployee.setPassword(password);
        
        return "updatesuccess";
    }
}
