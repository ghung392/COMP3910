package com.corejsf.Services;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.EmployeeSession;
import com.corejsf.Access.EmployeeManager;
import com.corejsf.Model.Employee;

/**
 * Resource responsible for employee tasks such as authentication and
 * CRUD operations.
 * @author Gabriel
 *
 */
@RequestScoped
@Path("/employees")
public class EmployeeResource {
	/** Employee Data Access Object. */
    @EJB private EmployeeManager employeeList;
    /** Session object. */
    @Inject private EmployeeSession employeeSession;

    /**
     * Create employee function.
     * @param token of employee requesting
     * @param username of employee to add
     * @param password of employee to add
     * @param confirmPassword of employee to add
     * @param firstName of employee to add
     * @param lastName of employee to add
     * @return OK if successful,
     * BAD_REQUEST if there is an empty form parameter or passwords don't match
     * UNAUTHORIZED if invalid token,
     * CONFLICT if adding a duplicate username,
     * FORBIDDEN if trying to create as non-admin
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/xml")
    public Response createEmployee(@HeaderParam("token") final String token,
    		@FormParam("username") final String username,
    		@FormParam("password") final String password,
    		@FormParam("confirmpassword") final String confirmPassword,
    		@FormParam("firstname") final String firstName,
    		@FormParam("lastname") final String lastName) {

    	employeeSession.timeoutToken();

    	if (token == null) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	if (!password.equals(confirmPassword)) {
    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
    	}

    	if (username == null || password == null || confirmPassword == null
    			|| firstName == null || lastName == null) {
    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
    	}

    	int loggedInEmployee = employeeSession.getEmployeeId(token);

    	if (!employeeList.find(loggedInEmployee).getAdmin()) {
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}

    	Employee[] employees = employeeList.getEmployees();

    	for (Employee employee : employees) {
    		if (employee.getUserName().equals(username)) {
    			throw new
    			WebApplicationException(Response.Status.CONFLICT);
    		}
    	}

    	Employee employee = new Employee(firstName, lastName, username,
    			false, password);

    	employeeList.persist(employee);

    	return Response.ok(employee).build();

    }

    /**
     * Get a single employee.
     * @param token
     * @param id of employee to view
     * @return UNAUTHORIZED if invalid token,
     * FORBIDDEN if viewing other employees when not an admin,
     * NOT_FOUND if trying to view non-existent employee,
     * OK when successful
     */
    @GET
    @Path("/{id}")
    @Produces("application/xml")
    public Response getEmployee(@HeaderParam("token") final String token,
    		@PathParam("id") final int id) {
    	employeeSession.timeoutToken();

    	if (token == null) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	int loggedInEmployee = employeeSession.getEmployeeId(token);
    	int employeeToView = id;

    	if (!employeeList.find(loggedInEmployee).getAdmin() &&
    			loggedInEmployee != employeeToView) {
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}

    	Employee employee = employeeList.find(employeeToView);

    	if (employee == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}

    	return Response.ok(employee).build();


    }

    /**
     * Get list of employees.
     * @param token of requesting employee
     * @return FORBIDDEN status if not admin,
     * OK status if successful,
     * UNAUTHORIZED if invalid token
     */
    @GET
    @Produces("application/xml")
    public Response getEmployees(@HeaderParam("token") final String token) {
    	employeeSession.timeoutToken();

    	if (token == null) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	int loggedInEmployee = employeeSession.getEmployeeId(token);

    	if (!employeeList.find(loggedInEmployee).getAdmin()) {
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}

    	return Response.ok(employeeList.getEmployees()).build();
    }

    /**
     * Updates an employee.
     * @param token of requesting employee
     * @param id of employee to update
     * @param username of employee to update
     * @param password of employee to update
     * @param firstName of employee to update
     * @param lastName of employee to update
     * @return FORBIDDEN status if trying to update an employee that is
     * not itself if non-admin,
     * NOT FOUND status if trying to update non-existent employee
     * OK status & Xml if successful,
     * UNAUTHORIZED if invalid token
     */
    @PUT
    @Path("/{id}")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/xml")
    public Response updateEmployee(@HeaderParam("token") final String token,
    		@PathParam("id") final int id,
    		@FormParam("username") final String username,
    		@FormParam("password") final String password,
    		@FormParam("firstname") final String firstName,
    		@FormParam("lastname") final String lastName) {

    	employeeSession.timeoutToken();

    	int loggedInEmployee = employeeSession.getEmployeeId(token);
    	int employeeToView = id;

    	if (!employeeList.find(loggedInEmployee).getAdmin() &&
    			loggedInEmployee != employeeToView) {
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}

    	Employee employeeToUpdate = employeeList.find(employeeToView);

    	if (employeeToUpdate == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}

    	if (username != null) {
    		employeeToUpdate.setUserName(username);
    	}

    	if (password != null) {
    		employeeToUpdate.setPassword(password);
    	}

    	if (firstName != null) {
    		employeeToUpdate.setFirstName(firstName);
    	}

    	if (lastName != null) {
    		employeeToUpdate.setLastName(lastName);
    	}

    	employeeList.merge(employeeToUpdate);

    	return Response.ok(employeeToUpdate).build();

    }

    /**
     * Delete an employee.
     * @param token of requesting employee
     * @param id of employee to delete
     * @return FORBIDDEN if non-admin or admin deleting themselves,
     * NOT_FOUND if employee id to delete is non-existent,
     * NO_CONTENT if successful,
     * UNAUTHORIZED if invalid token
     */
    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@HeaderParam("token") final String token,
    		@PathParam("id") final int id) {

    	employeeSession.timeoutToken();
    	int loggedInEmployee = employeeSession.getEmployeeId(token);

    	if (!employeeList.find(loggedInEmployee).getAdmin()) {
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}

    	Employee employeeToDelete = employeeList.find(id);

    	if (employeeToDelete == null) {
    		throw new WebApplicationException(Response.Status.NOT_FOUND);
    	}

    	if (employeeToDelete.equals(employeeList.find(loggedInEmployee))) {
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}

    	employeeList.remove(employeeToDelete);

    	return Response.noContent().build();
    }

}
