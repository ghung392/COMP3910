package com.corejsf.Model;

import java.math.BigDecimal;

import ca.bcit.infosys.timesheet.TimesheetRow;

public class TimesheetRowModel extends TimesheetRow {
	
	public TimesheetRowModel() {
		super();
	}
	
	public TimesheetRowModel(final int id, final String wp,
            final BigDecimal[] hours, final String comments) {
		super(id, wp, hours, comments);
	}

	public BigDecimal getHourMon() {
		return getHour(TimesheetRow.MON);
	}

	public BigDecimal getHourTue() {
		return getHour(TimesheetRow.TUE);
	}

	public BigDecimal getHourWed() {
		return getHour(TimesheetRow.WED);
	}

	public BigDecimal getHourThur() {
		return getHour(TimesheetRow.THU);
	}

	public BigDecimal getHourFri() {
		return getHour(TimesheetRow.FRI);
	}

	public BigDecimal getHourSat() {
		return getHour(TimesheetRow.SAT);
	}

	public BigDecimal getHourSun() {
		return getHour(TimesheetRow.SUN);
	}

	public void setHourMon(final BigDecimal hour) {
		setHour(TimesheetRow.MON, hour);
	}

	public void setHourTue(final BigDecimal hour) {
		setHour(TimesheetRow.TUE, hour);
	}

	public void setHourWed(final BigDecimal hour) {
		setHour(TimesheetRow.WED, hour);
	}

	public void setHourThur(final BigDecimal hour) {
		setHour(TimesheetRow.THU, hour);
	}

	public void setHourFri(final BigDecimal hour) {
		setHour(TimesheetRow.FRI, hour);
	}

	public void setHourSat(final BigDecimal hour) {
		setHour(TimesheetRow.SAT, hour);
	}

	public void setHourSun(final BigDecimal hour) {
		setHour(TimesheetRow.SUN, hour);
	}
	
	public boolean isDuplicate(final TimesheetRowModel reference) {
		boolean isDuplicate = true;

		final String wp1 = getWorkPackage();
		final String wp2 = reference.getWorkPackage();

		if ((wp1 != null && wp1.length() > 0) && (wp2 != null && wp2.length() > 0)) {
			final String id1 = getProjectID() + wp1;
			final String id2 = reference.getProjectID() + wp2;
			if (!id1.equals(id2)) {
				isDuplicate = false;
			}
		}

		return isDuplicate;
	}

}
