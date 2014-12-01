package com.corejsf.Services;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 * Authentication resource responsible for handling authentication and login
 * requests.
 * @author Gabriel
 *
 */
@RequestScoped
@Path("/authenticate")
public class AuthenticationResource {

	/** Employee Data Access Object. */
    @EJB private EmployeeManager employeeList;
    /** Session object. */
    @Inject private EmployeeSession employeeSession;

	/**
     * Login function that runs authorization on form parameters.
     * @param username from form
     * @param password from form
     * @return response code 200 if success,
     * response code 401 if unauthorized,
     * response code 400 if empty fields
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/xml")
    public Response login(@FormParam("username") final String username,
    		@FormParam("password") final String password) {
    	employeeSession.timeoutToken();

    	if (username == null || password == null) {
    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
    	}
    	Employee employee = employeeList.auth(username, password);
    	if (employee == null) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	Token token = employeeSession.generateToken(employee.getId());

    	return Response.ok(token).build();
    }

    /**
     * Logout and remove token tied to session.
     * @param token to be removed
     * @return response 204 NO_CONTENT if success,
     * response 401 if invalid token
     */
    @DELETE
    public Response logout(@HeaderParam("token") final String token) {
    	employeeSession.timeoutToken();

    	if (token == null) {
    		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    	}

    	employeeSession.getEmployeeId(token);

    	employeeSession.removeToken(token);

    	return Response.noContent().build();
    }
}
