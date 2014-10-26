package com.corejsf.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
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

	@Override
	public void addRow() {
		getDetails().add(new TimesheetRowModel());
	}

	public int getWeekNum() {
		return getWeekNumber();
	}

	public static Date getCurrDate() {
		Calendar c = new GregorianCalendar();
		int currentDay = c.get(Calendar.DAY_OF_WEEK);
		int leftDays = Calendar.FRIDAY - currentDay;
		c.add(Calendar.DATE, leftDays);

		return c.getTime();
	}

	private BigDecimal getHourOn(final int day) {
		BigDecimal scaledHour = BigDecimal.ZERO.setScale(1, BigDecimal.ROUND_HALF_UP);
		BigDecimal hour = getDailyHours()[day];
		if (hour != null) {
			scaledHour = scaledHour.add(hour);
		}

		return scaledHour;
	}

	public BigDecimal getSatHours() {
		return getHourOn(TimesheetRow.SAT);
	}

	public BigDecimal getSunHours() {
		return getHourOn(TimesheetRow.SUN);
	}

	public BigDecimal getMonHours() {
		return getHourOn(TimesheetRow.MON);
	}

	public BigDecimal getTueHours() {
		return getHourOn(TimesheetRow.TUE);
	}

	public BigDecimal getWedHours() {
		return getHourOn(TimesheetRow.WED);
	}

	public BigDecimal getThuHours() {
		return getHourOn(TimesheetRow.THU);
	}

	public BigDecimal getFriHours() {
		return getHourOn(TimesheetRow.FRI);
	}

	public void trimmedDetails() {
		Iterator<TimesheetRow> iterator = getDetails().iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getSum().compareTo(BigDecimal.ZERO) == 0) {
				iterator.remove();
			}
		}

		if (getDetails().size() == 0) {
			addRow();
		}
	}

	public boolean isRowsValid() {
		boolean rowsValid = true;

		final List<TimesheetRow> rows = getDetails();
		final int size = rows.size();

		// if only 1 row is filled, then check it has workPackage
		// else check project id + WP is unique
		// note 0 is considered valid project id number
		if (size == 1) {
			final String wp = rows.get(0).getWorkPackage();
			rowsValid = !(wp == null || (wp != null && wp.length() == 0));
		} else {
			rowsValid = hasCollision(rows);
		}

		return rowsValid;
	}

	private boolean hasCollision(final List<TimesheetRow> rows) {
		TimesheetRowModel row1, row2;
		final int size = rows.size();

		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				row1 = (TimesheetRowModel) rows.get(i);
				row2 = (TimesheetRowModel) rows.get(j);
				if (row1.isDuplicate(row2)) {
					return false;
				}
			}
		}

		return true;
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
