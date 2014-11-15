package com.corejsf.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
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

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * Extend Timesheet class, representing a single timesheet. Adds more methods
 * for convenient access and validation.
 */
@Entity
@Table(name = "Timesheets")
public class TimesheetModel extends Timesheet {

    /** Timesheet's id */
    private int id;

    /** Maximum number of hours in a day.*/
    private final int maxHours = 24;

    public TimesheetModel() {
        this(null); // calling public TimesheetModel(final Employee e)
    }

    /**
     * Constructor for TimesheetModel. Initialize a Timesheet with 5 empty
     * TimesheetRowModel rather than TimesheetRow, and setEmployee
     *
     * @param e owner of timesheet
     */
    public TimesheetModel(final Employee e) {
        super();

        ArrayList<TimesheetRow> newDetails = new ArrayList<TimesheetRow>();
        newDetails.add(new TimesheetRowModel(this));
        newDetails.add(new TimesheetRowModel(this));
        newDetails.add(new TimesheetRowModel(this));
        newDetails.add(new TimesheetRowModel(this));
        newDetails.add(new TimesheetRowModel(this));

        setDetails(newDetails);
        setEmployee(e);
    }

    /**
     * Create a TimesheetModel, passing all parameters to parent constructor.
     *
     * @param user The owner of the timesheet
     * @param end The date of the end of the week for the timesheet (Friday)
     * @param charges he detailed hours charged for the week for this timesheet
     */
    public TimesheetModel(final Employee user, final Date end,
            final List<TimesheetRow> charges) {
        super(user, end, charges);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TimesheetID")
    public int getId() {
        return id;
    }

    public void setId(final int n) {
        id = n;
    }

    @ManyToOne(targetEntity = EmployeeModel.class)
    @JoinColumn(name = "EmpID", nullable = false)
    public Employee getEmp() {
        return getEmployee();
    }

    public void setEmp(final Employee e) {
        setEmployee(e);
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "EndWeek", nullable = false)
    public Date getWeekEnd() {
        return getEndWeek();
    }

    public void setWeekEnd(final Date end) {
        setEndWeek(end);
    }

    @Column(name = "OverTime")
    public BigDecimal getOtime() {
        return getOvertime();
    }

    public void setOtime(final BigDecimal otime) {
        setOvertime(otime);
    }

    @Column(name = "FlexTime")
    public BigDecimal getFtime() {
        return getFlextime();
    }

    public void setFtime(final BigDecimal ftime) {
        setFlextime(ftime);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "timesheet", targetEntity = TimesheetRowModel.class)
    public List<TimesheetRow> getTimesheetRows() {
        return getDetails();
    }

    public void setTimesheetRows(final List<TimesheetRow> rows) {
        setDetails(rows);
    }

    @Override
    public final void addRow() {
        getDetails().add(new TimesheetRowModel(this));
    }

    /**
     * @return the weeknumber of the timesheet
     */
    @Transient
    public int getWeekNum() {
        return getWeekNumber();
    }

    /**
     * @return current weeks End of Week day (Friday)
     */
    @Transient
    public static Date getCurrDate() {
        Calendar c = new GregorianCalendar();
        int currentDay = c.get(Calendar.DAY_OF_WEEK);
        int leftDays = Calendar.FRIDAY - currentDay;
        c.add(Calendar.DATE, leftDays);

        return c.getTime();
    }

    /**
     * @return total hours on Saturday
     */
    @Transient
    public BigDecimal getSatHours() {
        return getDailyHours()[TimesheetRow.SAT];
    }

    /**
     * @return total hours on Sunday
     */
    @Transient
    public BigDecimal getSunHours() {
        return getDailyHours()[TimesheetRow.SUN];
    }

    /**
     * @return total hours on Monday
     */
    @Transient
    public BigDecimal getMonHours() {
        return getDailyHours()[TimesheetRow.MON];
    }

    /**
     * @return total hours on Tuesday
     */
    @Transient
    public BigDecimal getTueHours() {
        return getDailyHours()[TimesheetRow.TUE];
    }

    /**
     * @return total hours on Wednesday
     */
    @Transient
    public BigDecimal getWedHours() {
        return getDailyHours()[TimesheetRow.WED];
    }

    /**
     * @return total hours on Thursday
     */
    @Transient
    public BigDecimal getThuHours() {
        return getDailyHours()[TimesheetRow.THU];
    }

    /**
     * @return total hours on Friday
     */
    @Transient
    public BigDecimal getFriHours() {
        return getDailyHours()[TimesheetRow.FRI];
    }

    /**
     * Remove empty TimesheetRows.
     */
    public final void trimTimesheetRows() {
        Iterator<TimesheetRow> iterator = getDetails().iterator();
        while (iterator.hasNext()) {
            TimesheetRowModel row = (TimesheetRowModel) iterator.next();
            if (row.getSum().compareTo(BigDecimal.ZERO) == 0
                    && !row.isIdEmpty()) {
                iterator.remove();
            }
        }
        if (getDetails().size() == 0) {
            addRow();
        }
    }

    /**
     * return a list of 0-hour TimesheetRows.
     */
    @Transient
    private final List<TimesheetRow> getEffectiveRows() {
        final List<TimesheetRow> rows = getDetails();
        List<TimesheetRow> trimmed = new ArrayList<TimesheetRow>();

        for (TimesheetRow row : rows) {
            if (row.getSum().compareTo(BigDecimal.ZERO) != 0) {
                trimmed.add(row);
            }
        }

        return trimmed;
    }

    /**
     * Check if Timesheet's rows all have unique combination of Project ID &
     * WokrPackage. Note 0 is considered valid project id number, and
     * WorkPackage must not be empty.
     *
     * @return whether timesheet's rows all have valid projectID+workPackage.
     */
    @Transient
    public final boolean areRowsValid() {
        boolean rowsValid = true;

        final List<TimesheetRow> rows = getEffectiveRows();
        final int size = rows.size();

        // if only 1 row is filled, then check it has workPackage
        // else check project id + WP is unique
        if (size == 1) {
            TimesheetRowModel row = (TimesheetRowModel) rows.get(0);
            rowsValid = row.isIdEmpty();
            System.out.println("projectId and work package must be filled.");
        } else {
            rowsValid = hasCollision(rows);
        }

        return rowsValid;
    }

    /**
     * Check if given TimesheetRows' combination of Project ID & WokrPackage has
     * any duplicates.
     *
     * @param rows TimesheetRows to check
     * @return whether given TimesheetRows has duplicates.
     */
    @Transient
    private boolean hasCollision(final List<TimesheetRow> rows) {
        TimesheetRowModel row1, row2;
        final int size = rows.size();

        for (int i = 0; i < size - 1; i++) {
            row1 = (TimesheetRowModel) rows.get(i);
            if (!row1.isIdEmpty()) {
                System.out
                        .println("1: projectId and work package must be filled.");
                return false;
            }
            for (int j = i + 1; j < size; j++) {
                row2 = (TimesheetRowModel) rows.get(j);
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
     * Checks to see if hours in a day add up to 24 hours or less.
     * @return true if correctly summed
     */
    @Transient
    public boolean correctHoursInDay() {
        if (getMonHours() != null) {
            if ((getMonHours().intValue()) > maxHours) {
                return false;
            }
        }
        if (getTueHours() != null) {
            if ((getTueHours().intValue()) > maxHours) {
                return false;
            }
        }
        if (getWedHours() != null) {
            if ((getWedHours().intValue()) > maxHours) {
                return false;
            }
        }
        if (getThuHours() != null) {
            if ((getThuHours().intValue()) > maxHours) {
                return false;
            }
        }
        if (getFriHours() != null) {
            if ((getFriHours().intValue()) > maxHours) {
                return false;
            }
        }
        if (getSatHours() != null) {
            if ((getSatHours().intValue()) > maxHours) {
                return false;
            }
        }
        if (getSunHours() != null) {
            if ((getSunHours().intValue()) > maxHours) {
                return false;
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
        final EmployeeModel testEmp = (EmployeeModel) this.getEmployee();

        final boolean isSameUser = testEmp.equals(referenceSheet.getEmployee());
        final boolean isSameWeek = isSameWeekEnd(referenceSheet.getEndWeek());

        return isSameUser && isSameWeek;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Week of ").append(getWeekEnding()).append("\n");
        sb.append("EMP#: ").append(getEmployee().getUserName()).append("\n");

        List<TimesheetRow> rows = getDetails();
        for (TimesheetRow r : rows) {
            sb.append(r.toString());
        }

        sb.append("Overtime: ").append(getOvertime()).append("\n");
        sb.append("FlexTime: ").append(getFlextime()).append("\n");

        return sb.toString();
    }
}
