package com.corejsf.Access;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.corejsf.Model.EmployeeModel;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

@Named("timesheetManager")
@ApplicationScoped
public class TimesheetManager implements TimesheetCollection{
	
	private List<Timesheet> timesheetCollection;
	private EmployeeTracker employeeManager = new EmployeeTracker();
	
	public TimesheetManager() {
		timesheetCollection = new LinkedList<Timesheet>();
		populateTimesheetCollection();
	}

	@Override
	public List<Timesheet> getTimesheets() {
		return timesheetCollection;
	}

	@Override
	public List<Timesheet> getTimesheets(Employee e) {
		if (e == null)	return null;

		List <Timesheet> employeeTimesheets = new LinkedList<Timesheet>();
		for (Timesheet t : timesheetCollection) {
			if (e.equals(t.getEmployee())) {
				employeeTimesheets.add(t);
			}
		}
		return employeeTimesheets;
	}

	@Override
	public Timesheet getCurrentTimesheet(Employee e) {
		Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        Date currEndWeek = c.getTime();

		Timesheet currWeek = getTimesheet(e, currEndWeek);
		if (currWeek == null) {
			currWeek = new Timesheet();
		}
		
		return currWeek;
	}
	
	public Timesheet getTimesheet(final Employee e, final Date weekEnd) {
		List<Timesheet> employeeTimesheets = getTimesheets(e);
		Timesheet timesheet = null;

		for (Timesheet t : employeeTimesheets) {
			if (t.getEndWeek() == weekEnd) {
				timesheet = t;
			}
		}
		if (timesheet == null) {
			timesheet = new Timesheet();
		}
		
		return timesheet;
	}

	@Override
	public String addTimesheet() {
		
		return ""; // TODO Add navigation to the new timesheet page
	}

	private void populateTimesheetCollection() {
		final int P1 = 132;

		final List<TimesheetRow> e1Rows = new LinkedList<TimesheetRow>();

		TimesheetRow row;
		BigDecimal[] h1 = {null, null, null, new BigDecimal(4), null, null, null};
		row = new TimesheetRow(P1, "AA123", h1, "");
		e1Rows.add(row);
		
		BigDecimal[] h2 = {null, null, new BigDecimal(8), null, new BigDecimal(4), null, null};		
		row = new TimesheetRow(P1, "AB112", h2, "");
		e1Rows.add(row);
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date end = sdf.parse("10/10/2014");

			final List<EmployeeModel> elist = employeeManager.getEmployees();
			if (elist != null && elist.size() >= 2) {
				final Employee e1 = elist.get(0);
				final Timesheet t1 = new Timesheet(e1, end, e1Rows);
				timesheetCollection.add(t1);

				final Employee e2 = elist.get(1);
				final Timesheet t2 = new Timesheet(e2, end, e1Rows);
				timesheetCollection.add(t2);
			}
			else {
				System.out.println("no user!");
			}

		} catch (ParseException e) {
			System.out.println("ERROR: cannot parse strng date");
			e.printStackTrace();
		}
	}

}
