package com.corejsf;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

import com.corejsf.Access.TimesheetManager;
import com.corejsf.Model.TimesheetModel;
import com.corejsf.Model.TimesheetRowModel;

@Named("timesheetData")
@SessionScoped
public class TimesheetBean implements Serializable {

	@Inject
	private TimesheetManager timesheetManager;
	@Inject
	private EmployeeInfo employeeSession;

	private Date currEndWeek;

	public TimesheetModel getTimesheet() {
		Employee currEmployee = employeeSession.getCurrentEmployee();
		TimesheetModel timesheet = timesheetManager.getTimesheet(currEmployee, currEndWeek);
		return timesheet;
	}
	
	public List<TimesheetModel> getAllTimesheets() {
		Employee currEmployee = employeeSession.getCurrentEmployee();
		return timesheetManager.getTimesheets(currEmployee);
	}
	
	public String currTimesheet() {
        currEndWeek = TimesheetModel.getCurrDate();
		return "createTimesheet";
	}
	
	public String viewTimesheet(final Date endWeek) {
		currEndWeek = endWeek;
		
		// TODO return proper view page of timesheet
		return "createTimesheet";
	}
}
