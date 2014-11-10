package com.corejsf.Access;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Model.EmployeeModel;
/**
 * Class that tracks and manipulates the employee list.
 * Also provides an authentication.
 * method.
 * @author Gabriel
 * @version 1.0
 */
@Dependent
@Stateless
public class EmployeeTracker implements Serializable {
    /** Entity Manager. */
    @PersistenceContext(unitName = "a1") EntityManager em;
    /**Counter to keep track which ID number to add to a new employee to
     * prevent overlap. */
    private int counter = 4;
    /**
     * Method to find a employee.
     * @param username of employee to find
     * @return employee if found or null
     */
    public EmployeeModel find(final String username) {
        return em.find(EmployeeModel.class, username);
    }
    /**
     * Remove an employee from the list.
     * @param employee to remove
     */
    public void remove(Employee employee) {
        employee = find(employee.getUserName());
        em.remove(employee);
    }
    /**
     * Merge an employee for updating values.
     * @param employee to merge
     */
    public void merge(final Employee employee) {
        em.merge(employee);
    }
    /**
     * Add an employee to the list.
     * @param newEmployee new employee model
     */
    public void persist(final EmployeeModel newEmployee) {
        em.persist(newEmployee);
        counter++;
    }
    /**
     * Gets the whole list of employees.
     * @return list of employees
     */
    public EmployeeModel[] getEmployees() {
        TypedQuery<EmployeeModel> query = em.createQuery("select e "
                + "from EmployeeModel e", EmployeeModel.class);
        java.util.List<EmployeeModel> employees = query.getResultList();
        EmployeeModel[] emparray = new EmployeeModel[employees.size()];
        for (int i = 0; i < emparray.length; i++) {
            emparray[i] = employees.get(i);
        }
        return emparray;
    }
    /**
     * Authenticates a user.
     * @param username username input
     * @param password password input
     * @return employee if correct validation or else null
     */
    public EmployeeModel auth(final String username,
            final String password) {
        for (int i = 0; i < getEmployees().length; i++) {
            if ((getEmployees()[i].getUserName().compareTo(username) == 0)
                    && (getEmployees()[i].getPassword().compareTo(password)
                            == 0)) {
                return getEmployees()[i];
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
    public int getCounter() {
        return counter;
    }

    /**
     * Set the id counter.
     * @param number to be set
     */
    public void setCounter(final int number) {
        counter = number;
    }

}
