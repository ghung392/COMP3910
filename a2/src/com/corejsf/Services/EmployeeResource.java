package com.corejsf.Services;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.EmployeeSession;
import com.corejsf.Access.EmployeeManager;
import com.corejsf.Model.Employee;
import com.corejsf.Model.Token;

/**
 * Resource responsible for employee tasks such as authentication and
 * CRUD operations.
 * @author Gabriel
 *
 */
@RequestScoped
@Path("/employee")
public class EmployeeResource {
    @EJB private EmployeeManager employeeList;

    @Inject private EmployeeSession employeeSession;

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/xml")
    public Response login(@FormParam("username") String username,
    		@FormParam("password") String password) {
    	if( username == null || password == null) {
    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
    	}
    	Employee employee = employeeList.auth(username, password);
    	if (employee == null) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	Token token = employeeSession.generateToken(employee.getId());

    	return Response.ok(token).build();
    }

    @POST
    @Path("/createemployee")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/xml")
    public Response createEmployee(@HeaderParam("token") String token,
    		@FormParam("username") String username,
    		@FormParam("password") String password,
    		@FormParam("confirmpassword") String confirmPassword,
    		@FormParam("firstname") String firstName,
    		@FormParam("lastname") String lastName) {

    	if( username == null || password == null || confirmPassword == null
    			|| firstName == null || lastName == null ) {
    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
    	}

    	int loggedInEmployee = employeeSession.getEmployeeId(token);

    	if( !employeeList.find(loggedInEmployee).getAdmin() ) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	Employee employee = new Employee(firstName, lastName, username,
    			false, password);

    	employeeList.persist(employee);

    	return Response.ok(employee).build();

    }

}
