package com.corejsf.Services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.corejsf.EmployeeSession;
import com.corejsf.Access.EmployeeManager;
import com.corejsf.Access.TimesheetManager;
import com.corejsf.Model.Employee;
import com.corejsf.Model.Timesheet;
import com.corejsf.Model.TimesheetRow;
import com.corejsf.Model.Timesheets;

/**
 * Resource responsible for create, update and get timesheet(s).
 */
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

    /**
     * Get a specified timesheet.
     * 
     * @param token of employee requesting
     * @param id of timesheet
     * @return 200 OK and timesheet requested on successful, NOT_FOUND if
     *         timesheet with specified id not found UNAUTHORIZED if invalid
     *         token.
     */
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

    /**
     * Get all timesheets for this employee.
     * 
     * @param token of employee requesting
     * @return 200 OK and timesheets requested on successful, UNAUTHORIZED if
     *         invalid token.
     */
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

    /**
     * Create (if non-exist) or update current week's timesheet for this
     * employee.
     * 
     * @param token of employee requesting
     * @param sheet timesheet to save
     * @return 200 OK with updated timesheet on success, UNAUTHORIZED if invalid
     *         token, BAD_REQUEST if timesheet is invalid.
     */
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public Response updateCurrentWeekTimesheet(
            @HeaderParam("token") final String token, Timesheet sheet) {
        if (token == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        if (!sheet.checkTotalHours()) {
            throw new WebApplicationException(Response
                    .status(Status.BAD_REQUEST)
                    .entity("Timesheet must total nets to 40 hours").build());

        } else if (!sheet.checkHoursInDay()) {
            throw new WebApplicationException(Response
                    .status(Status.BAD_REQUEST)
                    .entity("Hours on any day must sum to no more "
                            + "than 24 hours across timesheet rows").build());

        } else if (!sheet.checkRowsIdUnique()) {
            throw new WebApplicationException(Response
                    .status(Status.BAD_REQUEST)
                    .entity("Each timesheet row must have a valid and uqnie "
                            + "combination of project-num and work-package.")
                    .build());

        }

        final int employeeId;
        final Employee owner;
        Timesheet stored;

        employeeId = employeeSession.getEmployeeId(token);
        owner = employeeManager.find(employeeId);
        stored = timesheetManager.find(owner, Timesheet.getCurrEndWeek());

        sheet.setEmployee(owner);
        // if there's existing timesheet, setId so JPA perform merge
        if (stored != null) {
            sheet.setId(stored.getId());
        }

        // to compensate JAXB infinite cycle error, whose fix cause
        // timesheetRows don't have proper relationship set for one-to-many
        for (TimesheetRow row : sheet.getTimesheetRows()) {
            row.setTimesheet(sheet);
        }

        stored = timesheetManager.merge(sheet);
        return Response.ok(stored).build();
    }
}
