package com.corejsf.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

public class TimesheetModel extends Timesheet {

	public TimesheetModel() {
		super();
		
		ArrayList<TimesheetRow> newDetails = new ArrayList<TimesheetRow>() {
            private static final long serialVersionUID = 1L;
            {
                add(new TimesheetRowModel());
                add(new TimesheetRowModel());
                add(new TimesheetRowModel());
                add(new TimesheetRowModel());
                add(new TimesheetRowModel());
            }
		};
		
		setDetails(newDetails);
	}

	public TimesheetModel(final Employee user, final Date end, final List<TimesheetRow> charges) {
		super(user, end, charges);
	}

	public static Date getCurrDate() {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);

		return c.getTime();
	}

	public int getWeekNum() {
		return getWeekNumber();
	}
}
