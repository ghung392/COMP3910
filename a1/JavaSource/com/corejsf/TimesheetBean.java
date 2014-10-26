package com.corejsf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Access.TimesheetManager;
import com.corejsf.Model.EmployeeModel;
import com.corejsf.Model.TimesheetModel;

@Named("timesheetData")
@SessionScoped
public class TimesheetBean implements Serializable {

	@Inject
	private TimesheetManager timesheetManager;
	@Inject
	private EmployeeInfo employeeSession;

	private List<TimesheetModel> allTimesheets;
	private TimesheetModel timesheet;
	private Date currEndWeek;

	public List<TimesheetModel> getAllTimesheets() {
		// refresh timesheetList only if not initialized, empty or current
		// employee changed.
		if (allTimesheets == null) {
			refreshTimesheetList();
		} else {
			if (allTimesheets.size() == 0) {
				refreshTimesheetList();
			} else {
				EmployeeModel reference = (EmployeeModel) allTimesheets.get(0).getEmployee();
				if (!reference.equals(employeeSession.getCurrentEmployee())) {
					refreshTimesheetList();
				}

			}
		}

		return allTimesheets;
	}

	public TimesheetModel getTimesheet() {
		// refresh timesheet only if not initialized or currEndWeek changed
		final boolean isSameCurrEndWeek = timesheet != null && timesheet.isSameWeekEnd(currEndWeek);
		if (timesheet == null || !isSameCurrEndWeek) {
			refreshTimesheet();
		}

		return timesheet;
	}

	private void refreshTimesheetList() {
		System.out.println("Refreshing employee's TimesheetList");
		Employee currEmployee = employeeSession.getCurrentEmployee();
		allTimesheets = timesheetManager.getTimesheets(currEmployee);
	}

	private void refreshTimesheet() {
		System.out.println("Refreshing current timesheet.");
		Employee currEmployee = employeeSession.getCurrentEmployee();
		TimesheetModel newSheet = timesheetManager.getTimesheet(currEmployee, currEndWeek);
		timesheet = newSheet;
	}

	public String addTimesheet() {
		boolean isValid = true;

		if (!timesheet.isValid()) {
			isValid = false;
			System.out.println("Work hours must add up to 40."); // show error
		}
		if (!timesheet.isRowsValid()) {
			isValid = false;
			System.out.println("A combination of project id + workpage must be unique for each row."); // show error			
		}
		if (isValid) {
			timesheetManager.saveTimesheet(timesheet);
			System.out.println("Saving timesheet");
		}

		return null;
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
