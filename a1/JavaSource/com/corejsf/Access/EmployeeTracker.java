package com.corejsf.Access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Model.EmployeeModel;
/**
 * Class that tracks and manipulates the employee list.
 * Also provides an authentication.
 * method.
 * @author Gabriel
 * @version 1.0
 */
@SessionScoped
public class EmployeeTracker implements Serializable {
    /**ArrayList holding employees. Initially defined with a superuser and a
     * normal user. */
    private ArrayList<EmployeeModel> employees =
            new ArrayList<EmployeeModel>(Arrays.asList(
            new EmployeeModel("Johne Doe", 1, "test", false, "test"),
            new EmployeeModel("Bob Smith", 2, "testadmin", true, "testadmin"),
            new EmployeeModel("Gabriel", 3, "ghung392", true, "toohardtoguess"),
            new EmployeeModel("Angela", 4, "starangelam", false, "qwerty")
            ));
    /**Counter to keep track which ID number to add to a new employee to
     * prevent overlap. */
    private int counter = 4;
    /**
     * Method to find a employee.
     * @param username of employee to find
     * @return employee if found or null
     */
    public final EmployeeModel find(final String username) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getUserName().compareTo(username) == 0) {
                return employees.get(i);
            }
        }
        return null;
    }
    /**
     * Remove an employee from the list.
     * @param employee to remove
     */
    public final void remove(final Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmpNumber() == employee.getEmpNumber()) {
                employees.remove(i);
            }
        }
        return;
    }
    /**
     * Add an employee to the list.
     * @param newEmployee new employee model
     */
    public final void add(final EmployeeModel newEmployee) {
        employees.add(newEmployee);

        return;
    }
    /**
     * Gets the whole list of employees.
     * @return list of employees
     */
    public final ArrayList<EmployeeModel> getEmployees() {
        return employees;
    }
    /**
     * Authenticates a user.
     * @param username username input
     * @param password password input
     * @return employee if correct validation or else null
     */
    public final EmployeeModel auth(final String username,
            final String password) {
        for (int i = 0; i < employees.size(); i++) {
            if ((employees.get(i).getUserName().compareTo(username) == 0)
                    && (employees.get(i).getPassword().compareTo(password)
                            == 0)) {
                return employees.get(i);
            }
        }
        FacesContext.getCurrentInstance().addMessage("loginform:password",
                new FacesMessage("Username and/or password does not match! "
                        + "Please try again."));
        return null;
    }

    /**
     * Get the id counter.
     * @return the counter
     */
    public final int getCounter() {
        return counter;
    }

    /**
     * Set the id counter.
     * @param number to be set
     */
    public final void setCounter(final int number) {
        counter = number;
    }

}
