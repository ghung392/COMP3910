package com.corejsf;

import java.io.Serializable;
import java.util.ArrayList;
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
	private boolean saveSuccess = false;
	private boolean showMessages = false;
	private List<String> errorMessages = new ArrayList<String>();
	
	public boolean getSaveSuccess() {
		return saveSuccess;
	}

	public boolean getShowMessages() {
		return showMessages;
	}
	
	public List<String> getErrorMessages(){
		return errorMessages;
	}

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

		errorMessages.clear();
		showMessages = true;
		
		timesheet.trimmedDetails();
		
		if (!timesheet.isValid()) {
			isValid = false;
			saveSuccess = false;
			errorMessages.add("Work hours must add up to 40.");
			System.out.println("Work hours must add up to 40.");
		}
		if (!timesheet.isRowsValid()) {
			isValid = false;
			saveSuccess = false;
			errorMessages.add("A combination of project id + workpage must be filled and unique for each row.");
			System.out.println("A combination of project id + workpage must be filled and unique for each row.");			
		}
		if (isValid) {
			timesheetManager.saveTimesheet(timesheet);
			saveSuccess = true;
			System.out.println("Saving timesheet");
		}

		return null;
	}
	
	public String addRow() {
	    timesheet.addRow();
	    return null;
	}

	public String currTimesheet() {
		showMessages = false;
		currEndWeek = TimesheetModel.getCurrDate();
		return "createTimesheet";
	}

	public String viewTimesheet(final Date endWeek) {
		currEndWeek = endWeek;

		// TODO return proper view page of timesheet
		return "createTimesheet";
	}

}
