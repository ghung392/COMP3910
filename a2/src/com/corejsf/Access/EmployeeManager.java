package com.corejsf.Access;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.corejsf.Model.Employee;

/**
 * Database CRUD operations for employees.
 * @author Gabriel
 *
 */
@Stateless
public class EmployeeManager {
	/** Entity Manager. */
	@PersistenceContext(unitName="a3") EntityManager em;

	/**
     * Method to find a employee.
     * @param id of employee to find
     * @return employee if found or null
     */
    public Employee find(final int id) {
        return em.find(Employee.class, id);
    }
    /**
     * Remove an employee from the list.
     * @param employee to remove
     */
    public void remove(final Employee employee) {
        Employee emp = find(employee.getId());
        em.remove(emp);
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
    public List<Employee> getEmployees() {
        TypedQuery<Employee> query = em.createQuery("select e "
                + "from Employee e", Employee.class);
        List<Employee> employees = query.getResultList();
        return employees;
    }
    /**
     * Authenticates a user.
     * @param username username input
     * @param password password input
     * @return employee if correct validation or else null
     */
    public Employee auth(final String username,
            final String password) {
        List<Employee> employeeList = getEmployees();
        for (Employee employee : employeeList) {
            if (username.compareTo(employee.getUserName()) == 0
                    && password.compareTo(employee.getPassword()) == 0) {
                return employee;
            }
        }

        return null;
    }
}
