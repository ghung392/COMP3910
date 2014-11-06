package com.corejsf.Model;

import java.math.BigDecimal;

import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Extending TimesheetRow class, representing a single row on a timesheet
 * containing identifier and hours per day.
 */
public class TimesheetRowModel extends TimesheetRow {

    /**
     * Creates a TimesheetRowModel object.
     */
    public TimesheetRowModel() {
        super();
    }

    /**
     * Creates a TimesheetRowModel object with all fields set. Used to create
     * sample data.
     * @param id project id
     * @param wp work package number (alphanumeric)
     * @param hours number of hours charged for each day of week.
     *      null represents ZERO
     * @param comments any notes with respect to this work package charges
     */
    public TimesheetRowModel(final int id, final String wp,
            final BigDecimal[] hours, final String comments) {
        super(id, wp, hours, comments);
    }

    /**
     * @return Monday's hour on this row.
     */
    public BigDecimal getHourMon() {
        return getHour(TimesheetRow.MON);
    }

    /**
     * @return Tuesday's hour on this row.
     */
    public BigDecimal getHourTue() {
        return getHour(TimesheetRow.TUE);
    }

    /**
     * @return Wednesday's hour on this row.
     */
    public BigDecimal getHourWed() {
        return getHour(TimesheetRow.WED);
    }

    /**
     * @return Thursday's hour on this row.
     */
    public BigDecimal getHourThur() {
        return getHour(TimesheetRow.THU);
    }

    /**
     * @return Friday's hour on this row.
     */
    public BigDecimal getHourFri() {
        return getHour(TimesheetRow.FRI);
    }

    /**
     * @return Saturday's hour on this row.
     */
    public BigDecimal getHourSat() {
        return getHour(TimesheetRow.SAT);
    }

    /**
     * @return Sunday's hour on this row.
     */
    public BigDecimal getHourSun() {
        return getHour(TimesheetRow.SUN);
    }

    /**
     * Set hour on Monday.
     * @param hour number of hours worked
     */
    public void setHourMon(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.MON, hour);
    }

    /**
     * Set hour on Tuesday.
     * @param hour number of hours worked
     */
    public void setHourTue(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.TUE, hour);
    }

    /**
     * Set hour on Wednesday.
     * @param hour number of hours worked
     */
    public void setHourWed(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.WED, hour);
    }

    /**
     * Set hour on Thursday.
     * @param hour number of hours worked
     */
    public void setHourThur(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.THU, hour);
    }

    /**
     * Set hour on Friday.
     * @param hour number of hours worked
     */
    public void setHourFri(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.FRI, hour);
    }

    /**
     * Set hour on Saturday.
     * @param hour number of hours worked
     */
    public void setHourSat(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.SAT, hour);
    }

    /**
     * Set hour on Sunday.
     * @param hour number of hours worked
     */
    public void setHourSun(final BigDecimal hour) {
        setHourOnDay(TimesheetRow.SUN, hour);
    }

    /**
     * Set hour on a given day.
     * @param day day to set hours for
     * @param hour hour number of hours worked
     */
    private void setHourOnDay(final int day, final BigDecimal hour) {
        try {
            setHour(day, hour);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to set hour.");
        }
    }

    /**
     * Check if row is a duplicate of given row. Definition of duplicate is
     * when two rows has exact same combination of projectID + WorkPackage.
     * @param reference row to be checked against.
     * @return whether given row is a duplicate of current row
     */
    public final boolean isDuplicate(final TimesheetRowModel reference) {
        boolean isDuplicate = true;

        final String wp1 = getWorkPackage();
        final String wp2 = reference.getWorkPackage();

        if ((wp1 != null && wp1.length() > 0)
                && (wp2 != null && wp2.length() > 0)) {
            final String id1 = getProjectID() + wp1;
            final String id2 = reference.getProjectID() + wp2;
            if (!id1.equals(id2)) {
                isDuplicate = false;
            }
        }

        return isDuplicate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(getProjectID()).append(getWorkPackage());
        sb.append(": MON: ").append(getHourMon());
        sb.append(", TUE: ").append(getHourTue());
        sb.append(", WED: ").append(getHourWed());
        sb.append(", THU: ").append(getHourThur());
        sb.append(", FRI: ").append(getHourFri());
        sb.append(", SAT: ").append(getHourSat());
        sb.append(", SUN: ").append(getHourSun());
        sb.append("\n");
        
        return sb.toString();
    }
}
