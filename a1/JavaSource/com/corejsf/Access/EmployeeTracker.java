package com.corejsf.Access;

import java.io.Serializable;
import java.util.List;

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

    /**
     * Method to find a employee.
     * @param id of employee to find
     * @return employee if found or null
     */
    public EmployeeModel find(final int id) {
        return em.find(EmployeeModel.class, id);
    }
    /**
     * Remove an employee from the list.
     * @param employee to remove
     */
    public void remove(Employee employee) {
        employee = find(employee.getEmpNumber());
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
    public void persist(final Employee newEmployee) {
        em.persist(newEmployee);
    }
    /**
     * Gets the whole list of employees.
     * @return list of employees
     */
    public List<EmployeeModel> getEmployees() {
        TypedQuery<EmployeeModel> query = em.createQuery("select e "
                + "from EmployeeModel e", EmployeeModel.class);
        List<EmployeeModel> employees = query.getResultList();
        return employees;
    }
    /**
     * Authenticates a user.
     * @param username username input
     * @param password password input
     * @return employee if correct validation or else null
     */
    public EmployeeModel auth(final String username,
            final String password) {
        List<EmployeeModel> employeeList = getEmployees();
        for (EmployeeModel employee : employeeList) {
            if (username.compareTo(employee.getUserName()) == 0
                    && password.compareTo(employee.getPassword()) == 0) {
                return employee;
            }
        }
        FacesContext.getCurrentInstance().addMessage("loginform:password",
                new FacesMessage("Username and/or password does not match! "
                        + "Please try again."));
        return null;
    }

}
