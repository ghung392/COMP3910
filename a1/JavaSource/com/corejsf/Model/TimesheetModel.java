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

/**
 * Extend Timesheet class, representing a single timesheet. Adds more methods
 * for convenient access and validation.
 */
public class TimesheetModel extends Timesheet {

    /**
     * Constructor for TimesheetModel. Initialize a Timesheet with
     * 5 empty TimesheetRowModel rather than TimesheetRow, and setEmployee
     * @param e owner of timesheet
     */
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

    /**
     * Create a TimesheetModel, passing all parameters to parent constructor.
     * @param user  The owner of the timesheet
     * @param end   The date of the end of the week for the timesheet (Friday)
     * @param charges he detailed hours charged for the week for this timesheet
     */
    public TimesheetModel(final Employee user, final Date end,
            final List<TimesheetRow> charges) {
        super(user, end, charges);
    }

    @Override
    public final void addRow() {
        getDetails().add(new TimesheetRowModel());
    }

    /**
     * @return the weeknumber of the timesheet
     */
    public int getWeekNum() {
        return getWeekNumber();
    }

    /**
     * @return current weeks End of Week day (Friday)
     */
    public static Date getCurrDate() {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);

        return c.getTime();
    }

    /**
     * Get daily hours on a given day.
     * @param day day of week
     * @return total hours on the given day.
     */
    private BigDecimal getHourOn(final int day) {
        BigDecimal scaledHour = BigDecimal.ZERO.setScale(1,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal hour = getDailyHours()[day];
        if (hour != null) {
            scaledHour = scaledHour.add(hour);
        }

        return scaledHour;
    }

    /**
     * @return total hours on Saturday
     */
    public BigDecimal getSatHours() {
        return getHourOn(TimesheetRow.SAT);
    }

    /**
     * @return total hours on Sunday
     */
    public BigDecimal getSunHours() {
        return getHourOn(TimesheetRow.SUN);
    }

    /**
     * @return total hours on Monday
     */
    public BigDecimal getMonHours() {
        return getHourOn(TimesheetRow.MON);
    }

    /**
     * @return total hours on Tuesday
     */
    public BigDecimal getTueHours() {
        return getHourOn(TimesheetRow.TUE);
    }

    /**
     * @return total hours on Wednesday
     */
    public BigDecimal getWedHours() {
        return getHourOn(TimesheetRow.WED);
    }

    /**
     * @return total hours on Thursday
     */
    public BigDecimal getThuHours() {
        return getHourOn(TimesheetRow.THU);
    }

    /**
     * @return total hours on Friday
     */
    public BigDecimal getFriHours() {
        return getHourOn(TimesheetRow.FRI);
    }

    /**
     * Remove TimesheetRows with 0 hours entered.
     */
    public final void trimmedDetails() {
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

    /**
     * Check if Timesheet's rows all have unique combination of Project ID &
     * WokrPackage. Note 0 is considered valid project id number, and
     * WorkPackage must not be empty.
     * @return whether timesheet's rows all have valid projectID+workPackage.
     */
    public final boolean isRowsValid() {
        boolean rowsValid = true;

        final List<TimesheetRow> rows = getDetails();
        final int size = rows.size();

        // if only 1 row is filled, then check it has workPackage
        // else check project id + WP is unique
        if (size == 1) {
            final String wp = rows.get(0).getWorkPackage();
            rowsValid = !(wp == null || (wp != null && wp.length() == 0));
        } else {
            rowsValid = hasCollision(rows);
        }

        return rowsValid;
    }

    /**
     * Check if given TimesheeRows' combination of Project ID & WokrPackage
     * has any duplicates.
     * @param rows TimesheetRows to check
     * @return whether given TimesheetRows has duplicates.
     */
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

    /**
     * Check whether given Date is on same day as timesheet's endWeek.
     * @param reference date to validate against.
     * @return whether given Date is on same day as timesheet's endWeek.
     */
    public final boolean isSameWeekEnd(final Date reference) {
        final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        final String date1 = fmt.format(this.getEndWeek());
        final String date2 = fmt.format(reference);

        return date1.equals(date2);
    }

    @Override
    public final boolean equals(final Object obj) {
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
