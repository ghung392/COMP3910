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
public class EmployeeInfo implements Serializable {
   
    @Inject private EmployeeTracker employeeList;
    private EmployeeModel currentEmployee;   
    private EmployeeModel focusedEmployee;

    /**
     * Method to get the list of employees
     * @return arraylist of employees
     */
    public ArrayList<EmployeeModel> getEmployeeList() {
        return employeeList.getEmployees();
    }

    /**
     * @param name the name of the employee
     * @return the employee
     */
    public EmployeeModel getEmployee(String username) {
        return employeeList.find(username);     
    }

    /**
     * @return the Map containing the valid (userName, password) combinations.
     */
    public Map<String, String> getLoginCombos() {
        return null;
    }

    /**
     * Method to return the current employee signed in
     * @return current employee
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    
    public Employee getFocusedEmployee() {
        return focusedEmployee;
    }
    
    public boolean getAdmin() {
        return currentEmployee.isAdmin();
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

    /**
     * Logs out the current employee.
     * @return logout for navigation
     */
    public String employeeLogout() {
        currentEmployee = null;
        focusedEmployee = null;
        return "logout";
    }

    public String deleteEmployee(EmployeeModel employee) {
    	if(employee == currentEmployee) {
    		FacesContext.getCurrentInstance().addMessage("profileForm", 
                    new FacesMessage("You cannot delete yourself. You are the admin."));
            return "deletefail";
    	}
        employeeList.remove(employee); 
        
        return "deletesuccess";
    }

    public String createEmployee(final String username, final String name,
            final String newPassword, final String confirmPassword) {
    	int counter = employeeList.getCounter();
    	EmployeeModel newEmployee = new EmployeeModel(name, counter + 1, username, false, newPassword);
    	addEmployeeToList(newEmployee);
    	employeeList.setCounter(counter + 1);
    	
    	return "createsuccess";
    }
    
    public void addEmployeeToList(EmployeeModel newEmployee) {
        employeeList.add(newEmployee);
    }
    
    public String updateEmployee(final String oldPassword,
            final String newPassword, final String confirmPassword ) {
        
        if((oldPassword.compareTo(currentEmployee.getPassword())) != 0) {
            FacesContext.getCurrentInstance().addMessage("passwordForm:old_password", 
                    new FacesMessage("You did not enter a match with your old password. Try again."));
            return "updatefail";
        } else if((newPassword.compareTo(confirmPassword)) != 0) {
            FacesContext.getCurrentInstance().addMessage("passwordForm:confirm_password", 
                    new FacesMessage("Your password confirmation did not match. Try again."));
            return "updatefail";
        } else if((oldPassword.compareTo(newPassword)) == 0) {
            FacesContext.getCurrentInstance().addMessage("passwordForm:new_password", 
                    new FacesMessage("Your new password did not change. Try again."));
            return "updatefail";
        }
        
        currentEmployee.setPassword(newPassword);
        
        return "updatesuccess";
    }
    
    public String updateInfo(final String username, final String name) {
    	if((username.compareTo("")) != 0)
        {
            currentEmployee.setUserName(username);
        }
    	
    	if((name.compareTo("")) != 0)
        {
            currentEmployee.setName(name);
        }
    	
    	return "updatesuccess";
    }
    
    public String updateFocusedEmployee(final String newPassword, final String confirmPassword ) {
      
        if((newPassword.compareTo(confirmPassword)) != 0) {
            FacesContext.getCurrentInstance().addMessage("passwordForm:confirm_password", 
                    new FacesMessage("Your password confirmation did not match. Try again."));
            return "updatefail";
        } 
               
        focusedEmployee.setPassword(newPassword);
        
        return "updatesuccess";
    }
    
    public String updateFocusedEmployeeInfo(final String username, final String name) {
    	if((username.compareTo("")) != 0)
        {
            focusedEmployee.setUserName(username);
        }
    	
    	if((username.compareTo("")) != 0)
        {
            focusedEmployee.setName(name);
        }
    	
    	return "updatesuccess";
    }
    
    public String changeEmployee(final String username) {
        focusedEmployee = getEmployee(username);
        
        return "viewprofile";
    }
    
}
