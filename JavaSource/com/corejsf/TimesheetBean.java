package com.corejsf;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetCollection;
import ca.bcit.infosys.timesheet.TimesheetRow;

import com.corejsf.Access.TimesheetManager;

@Named("timesheetData")
@SessionScoped
public class TimesheetBean implements Serializable {

	@Inject
	private TimesheetManager timesheetManager;
	@Inject
	private EmployeeInfo employeeSession;

	private Date currEndWeek;

	public Timesheet getTimesheet() {
		Employee currEmployee = employeeSession.getCurrentEmployee();
		Timesheet timesheet = timesheetManager.getTimesheet(currEmployee, currEndWeek);
		return timesheet;
	}
	
	public List<Timesheet> getAllTimesheets() {
		Employee currEmployee = employeeSession.getCurrentEmployee();
		return timesheetManager.getTimesheets(currEmployee);
	}
	
	public String currTimesheet() {
		Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);

        currEndWeek = c.getTime();

		return "createTimesheet";
	}
	
	public String viewTimesheet(final Date endWeek) {
		currEndWeek = endWeek;
		
		// TODO return proper view page of timesheet
		return "createTimesheet";
	}
}
