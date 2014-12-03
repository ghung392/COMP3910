package com.corejsf.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Class which represents a list of employees to display in XML.
 * @author Gabriel
 *
 */
@XmlRootElement (name = "employees")
@XmlSeeAlso(Employee.class)
public class Employees extends ArrayList<Employee> {
    /**
     * Constructor.
     */
	public Employees() {
    	super();
    }

	/**
	 * Constructor taking a employee collection paramter.
	 * @param e employee collection
	 */
    public Employees(final Collection<? extends Employee> e) {
        super(e);
    }

    /**
     * Getter.
     * @return this object
     */
    @XmlElement( name = "employee" )
    public List<Employee> getEmployees() {
        return this;
    }

    /**
     * Setter.
     * @param employees to add
     */
    public void setEmployees(List<Employee> employees) {
        this.addAll(employees);
    }
}
