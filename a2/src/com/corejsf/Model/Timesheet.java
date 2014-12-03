package com.corejsf.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.corejsf.utils.DateAdapter;

/**
 * Extend Timesheet class, representing a single timesheet. Adds more methods
 * for convenient access and validation.
 */
@Entity
@Table(name = "Timesheets")
@XmlRootElement(name = "timesheet")
public class Timesheet implements java.io.Serializable {

    /** Serial version number. */
    private static final long serialVersionUID = 2L;
    /** Number of days in a week. */
    public static final int DAYS_IN_WEEK = 7;
    /** Number of hours in a day. */
    public static final BigDecimal HOURS_IN_DAY = new BigDecimal(24.0)
            .setScale(1, BigDecimal.ROUND_HALF_UP);
    /** Full work week. */
    public static final BigDecimal FULL_WORK_WEEK = new BigDecimal(40.0)
            .setScale(1, BigDecimal.ROUND_HALF_UP);

    /** Timesheet's id. */
    private int id;
    /** The user associated with this timesheet. */
    private Employee employee;
    /** The date of Friday for the week of the timesheet. */
    private Date endWeek;
    /** The ArrayList of all timesheetRows (i.e. rows) that the form contains. */
    private List<TimesheetRow> timesheetRows;
    /** The total number of overtime hours on the timesheet. */
    private BigDecimal overtime;
    /** The total number of flextime hours on the timesheet. */
    private BigDecimal flextime;

    /**
     * Zero Argument constructor used for jpa.
     */
    public Timesheet() {
        timesheetRows = new ArrayList<TimesheetRow>();
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);
        endWeek = c.getTime();
    }

    /**
     * Constructor for Timesheet. Initialize a Timesheet with 5 empty
     * TimesheetRow rather than TimesheetRow, and setEmployee
     *
     * @param e owner of timesheet
     */
    public Timesheet(final Employee e) {
        this();
        employee = e;
    }

    /**
     * Create a Timesheet, passing all parameters to parent constructor.
     *
     * @param user The owner of the timesheet
     * @param end The date of the end of the week for the timesheet (Friday)
     * @param charges he detailed hours charged for the week for this timesheet
     */
    public Timesheet(final Employee user, final Date end,
            final List<TimesheetRow> charges) {
        employee = user;
        checkFriday(end);
        endWeek = end;
        timesheetRows = charges;
    }

    /**
     * @return timesheet id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TimesheetID")
    @XmlAttribute
    public int getId() {
        return id;
    }

    /**
     * Set timesheet id.
     *
     * @param n timesheet id.
     */
    public void setId(final int n) {
        id = n;
    }

    /**
     * @return the employee.
     */
    @ManyToOne
    @JoinColumn(name = "EmpID", nullable = false)
    @XmlTransient
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param e the employee who has the timesheet
     */
    public void setEmployee(Employee e) {
        employee = e;
    }

    /**
     * @return the endWeek
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "EndWeek", nullable = false)
    @XmlElement(name = "week-end")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getEndWeek() {
        return endWeek;
    }

    /**
     * @param end the endWeek to set. Must be a Friday
     */
    public void setEndWeek(final Date end) {
        checkFriday(end);
        endWeek = end;
    }

    private void checkFriday(final Date end) {
        Calendar c = new GregorianCalendar();
        c.setTime(end);
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        if (currentDay != Calendar.FRIDAY) {
            throw new IllegalArgumentException("EndWeek must be a Friday");
        }

    }

    /**
     * @return the overtime
     */
    @Column(name = "OverTime")
    @XmlElement
    public BigDecimal getOvertime() {
        return overtime;
    }

    /**
     * @param ot the overtime to set
     */
    public void setOvertime(final BigDecimal ot) {
        if (ot == null) {
            overtime = null;
        } else {
            overtime = ot.setScale(1, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * @return the flextime
     */
    @Column(name = "FlexTime")
    @XmlElement
    public BigDecimal getFlextime() {
        return flextime;
    }

    /**
     * @param flex the flextime to set
     */
    public void setFlextime(final BigDecimal flex) {
        if (flex == null) {
            flextime = null;
        } else {
            flextime = flex.setScale(1, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * @return timesheet rows.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "timesheet")
    @XmlElement(name = "timesheet-row")
    @XmlElementWrapper(name = "timesheet-rows")
    public List<TimesheetRow> getTimesheetRows() {
        return timesheetRows;
    }

    /**
     * Set timesheet rows.
     *
     * @param rows timesheet rows
     */
    public void setTimesheetRows(final List<TimesheetRow> rows) {
        timesheetRows = rows;
    }

    /**
     * @return the weeknumber of the timesheet
     */
    @Transient
    @XmlElement(name = "week-number")
    public int getWeekNumber() {
        Calendar c = new GregorianCalendar();
        c.setTime(endWeek);
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Sets the end of week based on the week number.
     *
     * @param weekNo the week number of the timesheet week
     * @param weekYear the year of the timesheet
     */
    public void setWeekNumber(final int weekNo, final int weekYear) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SATURDAY);
        c.setTime(endWeek);
        c.setWeekDate(weekYear, weekNo, Calendar.FRIDAY);
        endWeek = c.getTime();
    }

    /**
     * Calculates the total hours.
     *
     * @return total hours for timesheet.
     */
    @Transient
    @XmlElement(name = "total-hours")
    public BigDecimal getTotalHours() {
        BigDecimal sum = BigDecimal.ZERO.setScale(1, BigDecimal.ROUND_HALF_UP);
        for (TimesheetRow row : timesheetRows) {
            sum = sum.add(row.getSum());
        }
        return sum;
    }

    /**
     * @return current weeks End of Week day (Friday)
     */
    @Transient
    public static Date getCurrEndWeek() {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);

        return c.getTime();
    }

    /**
     * Calculates the daily hours.
     *
     * @return array of total hours for each day of week for timesheet.
     */
    @Transient
    public BigDecimal[] getDailyHours() {
        BigDecimal[] sums = new BigDecimal[DAYS_IN_WEEK];
        for (TimesheetRow day : timesheetRows) {
            BigDecimal[] hours = day.getHoursForWeek();
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                if (hours[i] != null) {
                    if (sums[i] == null) {
                        sums[i] = hours[i];
                    } else {
                        sums[i] = sums[i].add(hours[i]);
                    }
                }
            }
        }
        return sums;
    }

    /**
     * Deletes the specified row from the timesheet.
     *
     * @param rowToRemove the row to remove from the timesheet.
     */
    public void deleteRow(final TimesheetRow rowToRemove) {
        timesheetRows.remove(rowToRemove);
    }

    /**
     * Add an empty to to the timesheet.
     */
    public void addRow() {
        timesheetRows.add(new TimesheetRow());
    }

    /**
     * Checks to see if timesheet total nets 40 hours.
     *
     * @return true if FULL_WORK_WEEK == hours -flextime - overtime
     */
    @Transient
    public boolean checkTotalHours() {
        BigDecimal net = getTotalHours();
        if (overtime != null) {
            net = net.subtract(overtime);
        }
        if (flextime != null) {
            net = net.subtract(flextime);
        }
        return net.equals(FULL_WORK_WEEK);
    }

    /**
     * Check if Timesheet's rows all have unique combination of Project ID &
     * WokrPackage. Note 0 is considered valid project id number, and
     * WorkPackage must not be empty.
     *
     * @return whether timesheet's rows all have valid projectID+workPackage.
     */
    @Transient
    public final boolean checkRowsIdUnique() {
        boolean rowsValid = true;

        final List<TimesheetRow> rows = getTimesheetRows();
        final int size = rows.size();

        // if only 1 row is filled, then check it has workPackage
        // else check project id + WP is unique
        if (size == 1) {
            TimesheetRow row = (TimesheetRow) rows.get(0);
            rowsValid = row.isIdEmpty();
            System.out.println("projectId and work package must be filled.");
        } else {
            rowsValid = hasCollision(rows);
        }

        return rowsValid;
    }

    /**
     * Checks to see if hours in a day add up to 24 hours or less.
     *
     * @return true if correctly summed
     */
    @Transient
    public boolean checkHoursInDay() {
        BigDecimal[] hoursInDay = getDailyHours();

        for (int i = 0; i < hoursInDay.length; i++) {
            BigDecimal hourInDay = hoursInDay[i];
            if (hourInDay != null && hourInDay.compareTo(HOURS_IN_DAY) == 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if given TimesheetRows' combination of Project ID & WokrPackage has
     * any duplicates.
     *
     * @param rows TimesheetRows to check
     * @return whether given TimesheetRows has duplicates.
     */
    private boolean hasCollision(final List<TimesheetRow> rows) {
        TimesheetRow row1, row2;
        final int size = rows.size();

        for (int i = 0; i < size - 1; i++) {
            row1 = (TimesheetRow) rows.get(i);
            if (!row1.isIdEmpty()) {
                System.out.println("projectId and workPackage must be filled.");
                return false;
            }
            for (int j = i + 1; j < size; j++) {
                row2 = (TimesheetRow) rows.get(j);
                if (!row2.isIdEmpty()) {
                    return false;
                }
                if (row1.isDuplicate(row2)) {
                    System.out
                            .println("A combination of project id & workpage "
                                    + "must be unique for each row.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check whether given Date is on same day as timesheet's endWeek.
     *
     * @param reference date to validate against.
     * @return whether given Date is on same day as timesheet's endWeek.
     */
    @Transient
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
        final Employee testEmp = this.getEmployee();

        final boolean isSameUser = testEmp.equals(referenceSheet.getEmployee());
        final boolean isSameWeek = isSameWeekEnd(referenceSheet.getEndWeek());

        return isSameUser && isSameWeek;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((getEmployee() == null) ? 0 : getEmployee().hashCode());
        result = prime * result
                + ((getEndWeek() == null) ? 0 : getEndWeek().hashCode());
        return result;
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Id ").append(getId()).append("\n");
        sb.append("Week of ").append(getEndWeek()).append("\n");
        final String username = (employee != null) ? employee.getUserName()
                : null;
        sb.append("EMP#: ").append(username).append("\n");

        List<TimesheetRow> rows = getTimesheetRows();
        for (TimesheetRow r : rows) {
            sb.append(r.toString());
        }

        sb.append("Overtime: ").append(getOvertime()).append("\n");
        sb.append("FlexTime: ").append(getFlextime()).append("\n");

        return sb.toString();
    }
}
