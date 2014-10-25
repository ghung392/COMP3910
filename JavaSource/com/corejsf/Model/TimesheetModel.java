package com.corejsf.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

public class TimesheetModel extends Timesheet {

	public TimesheetModel(final Employee e) {
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
		setEmployee(e);
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null && !(obj instanceof Timesheet)) {
			return false;
		}
		
		final Timesheet testSheet = (Timesheet) obj;
		final EmployeeModel testEmp = (EmployeeModel) this.getEmployee();
		final boolean isSameUser = testEmp.equals(testSheet.getEmployee());

		final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		final String date1 = fmt.format(this.getEndWeek());
		final String date2 = fmt.format(testSheet.getEndWeek());
		final boolean isSameWeek = date1.equals(date2);
		
		return isSameUser && isSameWeek;
	}
}
