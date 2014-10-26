package com.corejsf.Model;

import java.math.BigDecimal;
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
	
	public BigDecimal getSatHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.SAT];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public BigDecimal getSunHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.SUN];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public BigDecimal getMonHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.MON];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public BigDecimal getTueHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.TUE];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public BigDecimal getWedHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.WED];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public BigDecimal getThuHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.THU];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public BigDecimal getFriHours() {
		BigDecimal hours = getDailyHours()[TimesheetRow.FRI];
		if (hours == null) {
			hours = new BigDecimal(0.0);
		}
		return hours;
	}
	
	public boolean isSameWeekEnd(final Date reference) {
		final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		final String date1 = fmt.format(this.getEndWeek());
		final String date2 = fmt.format(reference);
		
		return date1.equals(date2);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null && !(obj instanceof Timesheet)) {
			return false;
		}
		
		final Timesheet referenceSheet = (Timesheet) obj;
		final EmployeeModel testEmp = (EmployeeModel) this.getEmployee();

		final boolean isSameUser = testEmp.equals(referenceSheet.getEmployee());
		final boolean isSameWeek = isSameWeekEnd(referenceSheet.getEndWeek());
		
		return isSameUser && isSameWeek;
	}
}
