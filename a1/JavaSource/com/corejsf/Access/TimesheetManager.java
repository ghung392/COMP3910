package com.corejsf.Access;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.TimesheetRow;

import com.corejsf.Model.EmployeeModel;
import com.corejsf.Model.TimesheetModel;
import com.corejsf.Model.TimesheetRowModel;

/**
 * Manages the access of timesheet data in the data layer.
 */
@Named("timesheetManager")
@ApplicationScoped
public class TimesheetManager {

    /** Replacement of database, holding all timesheets. */
    private List<TimesheetModel> timesheetCollection;
    /** Manager that control access of employee data from data layer. */
    private EmployeeTracker employeeManager = new EmployeeTracker();

    /**
     * Construction a TimesheetManager object. Initializing timesheetColleciton
     * list and populate it with some sample data.
     */
    public TimesheetManager() {
        timesheetCollection = new LinkedList<TimesheetModel>();
        populateTimesheetCollection();
    }

    /**
     * @return all timesheets stored.
     */
    public List<TimesheetModel> getTimesheets() {
        return timesheetCollection;
    }

    /**
     * Get all timesheet belonging to given employee.
     * @param e
     *            owner of timesheet.
     * @return all timesheet belonging to given employee
     */
    public List<TimesheetModel> getTimesheets(final Employee e) {
        if (e == null) {
            return null;
        }

        List<TimesheetModel> empTimesheets = new LinkedList<TimesheetModel>();
        for (TimesheetModel t : timesheetCollection) {
            if (e.equals(t.getEmployee())) {
                empTimesheets.add(t);
            }
        }
        return empTimesheets;
    }

    /**
     * Get timesheet belong to a given employee and given week.
     * @param e
     *            timesheet's owner
     * @param weekEnd
     *            timesheet's week end day
     * @return a timesheet of a given employee and given week.
     */
    public TimesheetModel getTimesheet(final Employee e, final Date weekEnd) {
        List<TimesheetModel> employeeTimesheets = getTimesheets(e);
        TimesheetModel timesheet = null;

        for (TimesheetModel t : employeeTimesheets) {
            if (t.isSameWeekEnd(weekEnd)) {
                timesheet = t;
                System.out.println("Found timesheet: \n" + timesheet);
            }
        }
        if (timesheet == null) {
            timesheet = new TimesheetModel(e);
            System.out.println("Create timesheet: \n" + timesheet);
        }

        return timesheet;
    }

    /**
     * Saves a timesheet - replace if exist in data storage, if no record found
     * create a new one.
     * @param timesheet
     *            timesheet to store
     */
    public void saveTimesheet(final TimesheetModel timesheet) {
        if (timesheet == null) {
            System.out.println("Error: null passed as timesheet to be saved.");
            return;
        }

        boolean saved = false;

        final int size = timesheetCollection.size();
        for (int i = 0; i < size; i++) {
            // if timesheet found in timesheetCollection, replace with new
            if (timesheet.equals(timesheetCollection.get(i))) {
                saved = true;
                timesheetCollection.set(i, timesheet);
                System.out.println("Timesheet replaced: \n" + timesheet);
                break;
            }
        }
        // add timesheet only if it is not found in collection
        if (!saved) {
            timesheetCollection.add(timesheet);
            System.out.println("Timesheet added: \n" + timesheet);
        }
    }

    /**
     * Populate data storage with sample Timesheets.
     */
    private void populateTimesheetCollection() {
        final int p1 = 132;
        final int h = 8;

        final List<TimesheetRow> e1Rows = new LinkedList<TimesheetRow>();

        TimesheetRowModel row;
        BigDecimal[] h1 = {null, null, new BigDecimal(h), null, null,
                new BigDecimal(h), new BigDecimal(h) };
        row = new TimesheetRowModel(p1, "AA123", h1, "");
        e1Rows.add(row);

        BigDecimal[] h2 = {null, null, null, null, new BigDecimal(h), null,
                new BigDecimal(h) };
        row = new TimesheetRowModel(p1, "AA122", h2, "");
        e1Rows.add(row);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date end = sdf.parse("10/10/2014");

            final List<EmployeeModel> elist = employeeManager.getEmployees();
            if (elist != null && elist.size() >= 2) {
                final Employee e1 = elist.get(0);
                final TimesheetModel t1 = new TimesheetModel(e1, end, e1Rows);
                saveTimesheet(t1);

                final Employee e2 = elist.get(1);
                final TimesheetModel t2 = new TimesheetModel(e2, end, e1Rows);
                saveTimesheet(t2);
            } else {
                System.out.println("no user!");
            }

        } catch (ParseException e) {
            System.out.println("ERROR: cannot parse strng date");
            e.printStackTrace();
        }
    }

}
