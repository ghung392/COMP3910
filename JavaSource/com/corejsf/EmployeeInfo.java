package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
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
    
    public String updateEmployee(final String name, final String oldPassword,
            final String newPassword, final String confirmPassword ) {
        if((oldPassword.compareTo(currentEmployee.getPassword())) != 0) {
            FacesContext.getCurrentInstance().addMessage("profileForm:old_password", 
                    new FacesMessage("You did not enter a match with your old password. Try again."));
            return "updatefail";
        } else if((newPassword.compareTo(confirmPassword)) != 0) {
            FacesContext.getCurrentInstance().addMessage("profileForm:confirm_password", 
                    new FacesMessage("Your password confirmation did not match. Try again."));
            return "updatefail";
        } else if((oldPassword.compareTo(newPassword)) == 0) {
            FacesContext.getCurrentInstance().addMessage("profileForm:new_password", 
                    new FacesMessage("Your new password did not change. Try again."));
            return "updatefail";
        }
        
        currentEmployee.setName(name);
        currentEmployee.setPassword(newPassword);
        
        return "updatesuccess";
    }
    
}
