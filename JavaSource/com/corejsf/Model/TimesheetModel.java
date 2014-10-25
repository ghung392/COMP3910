package com.corejsf.Model;

import java.util.Date;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

public class TimesheetModel extends Timesheet {

	public TimesheetModel() {
		super();
	}

	public TimesheetModel(final Employee user, final Date end, final List<TimesheetRow> charges) {
		super(user, end, charges);
	}

	public int getWeekNum() {
		return getWeekNumber();
	}

}
