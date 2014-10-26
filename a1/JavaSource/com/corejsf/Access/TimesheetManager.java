package com.corejsf.Access;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.corejsf.Model.EmployeeModel;
import com.corejsf.Model.TimesheetModel;
import com.corejsf.Model.TimesheetRowModel;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named("timesheetManager")
@ApplicationScoped
public class TimesheetManager {

	private List<TimesheetModel> timesheetCollection;
	private EmployeeTracker employeeManager = new EmployeeTracker();

	public TimesheetManager() {
		timesheetCollection = new LinkedList<TimesheetModel>();
		populateTimesheetCollection();
	}

	public List<TimesheetModel> getTimesheets() {
		return timesheetCollection;
	}

	public List<TimesheetModel> getTimesheets(Employee e) {
		if (e == null)
			return null;

		List<TimesheetModel> employeeTimesheets = new LinkedList<TimesheetModel>();
		for (TimesheetModel t : timesheetCollection) {
			if (e.equals(t.getEmployee())) {
				employeeTimesheets.add(t);
			}
		}
		return employeeTimesheets;
	}

	public TimesheetModel getTimesheet(final Employee e, final Date weekEnd) {
		List<TimesheetModel> employeeTimesheets = getTimesheets(e);
		TimesheetModel timesheet = null;

		for (TimesheetModel t : employeeTimesheets) {
			if (t.isSameWeekEnd(weekEnd)) {
				timesheet = t;
				System.out.println("Found timesheet for " + weekEnd.toString());
			}
		}
		if (timesheet == null) {
			timesheet = new TimesheetModel(e);
			System.out.println("Create timesheet for " + timesheet.getEndWeek().toString());
		}

		return timesheet;
	}

	public boolean saveTimesheet(final TimesheetModel timesheet) {
		if (timesheet == null)
			return false;

		boolean saved = false;

		final int size = timesheetCollection.size();
		for (int i = 0; i < size; i++) {
			// if timesheet found in timesheetCollection, replace with new
			if (timesheet.equals(timesheetCollection.get(i))) {
				saved = true;
				timesheetCollection.set(i, timesheet);
				System.out.println("Timesheet replaced.");
				break;
			}
		}
		// add timesheet only if it is not found in collection
		if (!saved) {
			timesheetCollection.add(timesheet);
			System.out.println("Timesheet added.");
		}

		return saved;
	}

	private void populateTimesheetCollection() {
		final int P1 = 132;

		final List<TimesheetRow> e1Rows = new LinkedList<TimesheetRow>();

		TimesheetRowModel row;
		BigDecimal[] h1 = { null, null, new BigDecimal(8), new BigDecimal(6), null,
				new BigDecimal(8), new BigDecimal(8) };
		row = new TimesheetRowModel(P1, "AA123", h1, "");
		e1Rows.add(row);

		BigDecimal[] h2 = { null, null, null, null, new BigDecimal(2), null, new BigDecimal(8) };
		row = new TimesheetRowModel(P1, "AA122", h2, "");
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
