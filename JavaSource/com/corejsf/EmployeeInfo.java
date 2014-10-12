package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ca.bcit.infosys.employee.Credentials;
import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.employee.EmployeeList;

/**
 * Backing bean to handle user authentication and to keep track of all the existing users
 * @author Gabriel
 *
 */
public class EmployeeInfo implements Serializable, EmployeeList {
    @Inject private Credentials credentials;
    /**Current employee */
    @Inject private Employee employee = employees.get(0);
    /** ArrayList containing the employees */
    private static ArrayList<Employee> employees = new ArrayList<Employee>(Arrays.asList(
            new Employee("Gabriel", 1, "ghung392"),
            new Employee("Angela", 2, "starangelma")
            ));
    
    private static Map<String, String> loginCombos;
    static {
        loginCombos = new LinkedHashMap<String, String>();
        loginCombos.put(employees.get(0).getUserName(), "toohardtoguess");
        loginCombos.put(employees.get(1).getUserName(), "qwerty");
    }
   

    @Override
    /**
     * @return the employee list.
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    /**
     * @param name the name of the employee
     * @return the employee
     */
    public Employee getEmployee(String name) {
        for(int i = 0; i < employees.size(); i++ ) {
            if(employees.get(i).getName().compareToIgnoreCase(name) == 0) {
                return employees.get(i);
            }
            
        }
            
        return null;
    }

    @Override
    /**
     * @return the Map containing the valid (userName, password) combinations.
     */
    public Map<String, String> getLoginCombos() {
        return loginCombos;
    }

    @Override
    public Employee getCurrentEmployee() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getAdministrator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    /**
     * Verifies that the loginID and password is a valid combination.
     *
     * @param credential (userName, Password) pair
     * @return true if it is, false if it is not.
     */
    public boolean verifyUser(Credentials credential) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String logout(Employee employee) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteEmpoyee(Employee userToDelete) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addEmployee(Employee newEmployee) {
        // TODO Auto-generated method stub
        
    }
}
