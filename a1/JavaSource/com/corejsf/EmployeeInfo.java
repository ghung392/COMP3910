package com.corejsf;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Access.EmployeeTracker;
import com.corejsf.Model.EmployeeModel;

/**
 * Backing bean to handle user authentication and to keep track of all the
 * existing users.
 * @author Gabriel
 *
 */
@Named("employee")
@SessionScoped
public class EmployeeInfo implements Serializable {

    /**EmployeeTracker object that holds all the sample database objects. */
    @Inject private EmployeeTracker employeeList;
    /**The current employee object using the session. */
    private EmployeeModel currentEmployee;
    /**The employee that the admin is focused on. */
    private EmployeeModel focusedEmployee;

    /**
     * Method to get the list of employees.
     * @return arraylist of employees
     */
    public List<EmployeeModel> getEmployeeList() {
        return employeeList.getEmployees();
    }

    /**
     * Method to get an employee.
     * @param username the name of the employee
     * @return the employee
     */
    public EmployeeModel getEmployee(final String username) {
        return employeeList.find(username);
    }

    /**
     * Method to return the current employee signed in.
     * @return current employee
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Method to return the focused employee.
     * @return focused employee
     */
    public Employee getFocusedEmployee() {
        return focusedEmployee;
    }

    /**
     * Checks whether the current employee is an admin or not.
     * @return employee's admin status
     */
    public boolean getAdmin() {
        return currentEmployee.isAdmin();
    }

    /**
     * Authenticates user and sets current employee depending on matching
     * credentials.
     * @param username input username
     * @param password input password
     * @return string for navigation
     */
    public String verifyEmployee(final String username,
            final String password) {
        currentEmployee = employeeList.auth(username, password);

        if (currentEmployee == null) {
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
        return "logout";
    }

    /**
     * Removes the employee from the employee list.
     * @param employee to be deleted
     * @return string depending on fail/success for navigation
     */
    public String deleteEmployee(final EmployeeModel employee) {
        if (employee == currentEmployee) {
            FacesContext.getCurrentInstance().addMessage("profileForm",
                    new FacesMessage("You cannot delete yourself. "
                            + "You are the admin."));
            return "deletefail";
        }
        employeeList.remove(employee);

        return "deletesuccess";
    }

    /**
     * Receives new employee info and creates it.
     * @param username of new employee
     * @param name of new employee
     * @param newPassword of new employee
     * @param confirmPassword of new employee
     * @return string for navigation
     */
    public String createEmployee(final String username, final String name,
            final String newPassword, final String confirmPassword) {
        int counter = employeeList.getCounter();
        EmployeeModel newEmployee = new EmployeeModel(name, counter + 1,
                username, false, newPassword);
        addEmployeeToList(newEmployee);
        employeeList.setCounter(counter + 1);

        return "createsuccess";
    }

    /**
     * Adds employee to the employee list.
     * @param newEmployee to add to list
     */
    public void addEmployeeToList(final EmployeeModel newEmployee) {
        employeeList.persist(newEmployee);
    }
    /**
     * Updates the employee's password.
     * @param oldPassword for update
     * @param newPassword for update
     * @param confirmPassword for update
     * @return sring for navigation
     */
    public String updateEmployee(final String oldPassword,
            final String newPassword, final String confirmPassword) {

        if ((oldPassword.compareTo(currentEmployee.getPassword())) != 0) {
            FacesContext.getCurrentInstance().addMessage(
                    "passwordForm:old_password",
                    new FacesMessage("You did not enter a match with your "
                            + "old password. Try again."));
            return "updatefail";
        } else if ((newPassword.compareTo(confirmPassword)) != 0) {
            FacesContext.getCurrentInstance().addMessage(
                    "passwordForm:confirm_password",
                    new FacesMessage("Your password confirmation did not "
                            + "match. Try again."));
            return "updatefail";
        } else if ((oldPassword.compareTo(newPassword)) == 0) {
            FacesContext.getCurrentInstance().addMessage(
                    "passwordForm:new_password",
                    new FacesMessage("Your new password did not "
                            + "change. Try again."));
            return "updatefail";
        }

        currentEmployee.setPassword(newPassword);
        employeeList.merge(currentEmployee);

        return "updatesuccess";
    }

    /**
     * Updates the employee's info.
     * @param username to update
     * @param name to update
     * @return string for navigation
     */
    public String updateInfo(final String username, final String name) {
        if ((username.compareTo("")) != 0) {
            currentEmployee.setUserName(username);
            employeeList.merge(currentEmployee);
        }

        if ((name.compareTo("")) != 0) {
            currentEmployee.setName(name);
            employeeList.merge(currentEmployee);
        }

        return "updatesuccess";
    }

    /**
     * Updates the focused employee's password.
     * @param newPassword to update
     * @param confirmPassword to update
     * @return string for navigation
     */
    public String updateFocusedEmployee(final String newPassword,
            final String confirmPassword) {

        if ((newPassword.compareTo(confirmPassword)) != 0) {
            FacesContext.getCurrentInstance().addMessage(
                    "passwordForm:confirm_password",
                    new FacesMessage("Your password confirmation did not "
                            + "match. Try again."));
            return "updatefail";
        }

        focusedEmployee.setPassword(newPassword);
        employeeList.merge(focusedEmployee);

        return "updatesuccess";
    }

    /**
     * Updates focused employee's info.
     * @param username to update
     * @param name to update
     * @return string for navigation
     */
    public String updateFocusedEmployeeInfo(final String username,
            final String name) {
        if ((username.compareTo("")) != 0) {
            focusedEmployee.setUserName(username);
            employeeList.merge(focusedEmployee);
        }

        if ((name.compareTo("")) != 0) {
            focusedEmployee.setName(name);
            employeeList.merge(focusedEmployee);
        }

        return "updatesuccess";
    }

    /**
     * Change to a different focused employee.
     * @param username of new focused employee
     * @return string for navigation
     */
    public String changeEmployee(final String username) {
        focusedEmployee = getEmployee(username);

        return "viewprofile";
    }

}
