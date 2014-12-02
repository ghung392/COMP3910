package com.corejsf.Services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.EmployeeSession;
import com.corejsf.Access.EmployeeManager;
import com.corejsf.Access.TimesheetManager;
import com.corejsf.Model.Employee;
import com.corejsf.Model.Timesheet;
import com.corejsf.Model.Timesheets;

@Dependent
@Stateless
@Path("/timesheets")
public class TimesheetResource {
    /** Employee Data Access Object. */
    @EJB
    private EmployeeManager employeeManager;
    /** Timesheet Data Access Object. */
    @EJB
    private TimesheetManager timesheetManager;
    /** Session object. */
    @Inject
    private EmployeeSession employeeSession;

    @GET
    @Path("/{id}")
    @Produces("application/xml")
    public Response getTimesheet(@HeaderParam("token") final String token,
            @PathParam("id") final int id) {
        if (token == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        final int employeeId;
        final Employee owner;
        final Timesheet timesheet;

        employeeId = employeeSession.getEmployeeId(token);
        owner = employeeManager.find(employeeId);

        timesheet = timesheetManager.find(id);
        if (timesheet == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            if (!timesheet.getEmployee().equals(owner)) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }

        System.out.println("returning timesheet: " + timesheet);
        return Response.ok(timesheet).build();
    }

    @GET
    @Produces("application/xml")
    public Timesheets getTimesheets(@HeaderParam("token") final String token) {
        if (token == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        final int employeeId;
        final Employee owner;
        final List<Timesheet> timesheets;

        employeeId = employeeSession.getEmployeeId(token);
        owner = employeeManager.find(employeeId);
        timesheets = timesheetManager.find(owner);

        return new Timesheets(timesheets);
    }
}
