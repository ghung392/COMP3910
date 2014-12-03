package com.corejsf.Model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Extending TimesheetRow class, representing a single row on a timesheet
 * containing identifier and hours per day.
 */
@Entity
@Table(name = "TimesheetRows")
@XmlRootElement(name = "timesheet-row")
public class TimesheetRow implements java.io.Serializable {

    /** Version number. */
    private static final long serialVersionUID = 2L;

    /** Timesheet row index for Saturday. */
    public static final int SAT = 0;
    /** Timesheet row index for Sunday. */
    public static final int SUN = 1;
    /** Timesheet row index for Monday. */
    public static final int MON = 2;
    /** Timesheet row index for Tuesday. */
    public static final int TUE = 3;
    /** Timesheet row index for Wednesday. */
    public static final int WED = 4;
    /** Timesheet row index for Thursday. */
    public static final int THU = 5;
    /** Timesheet row index for Friday. */
    public static final int FRI = 6;

    /** Unique id for TimesheetRow. */
    private int id;
    /** timesheet this timesheetrow belongs. */
    private Timesheet timesheet;
    /** The projectID. */
    private Integer projectID;
    /** The WorkPackage. Must be a unique for a given projectID. */
    private String workPackage;
    /** Any notes added to the end of a row. */
    private String notes;

    /**
     * An array holding all the hours charged for each day of the week. Day 0 is
     * Saturday, ... day 6 is Friday
     */
    private BigDecimal[] hoursForWeek = new BigDecimal[Timesheet.DAYS_IN_WEEK];

    /**
     * Creates a TimesheetRow object.
     */
    public TimesheetRow() {
    }

    /**
     * Onwer of timesheetrow.
     *
     * @param t timesheet this row belongs to.
     */
    public TimesheetRow(final Timesheet t) {
        timesheet = t;
    }

    /**
     * Creates a TimesheetRow object with all fields set. Used to create sample
     * data.
     *
     * @param projId project id
     * @param wp work package number (alphanumeric)
     * @param hours number of hours charged for each day of week. null
     *            represents ZERO
     * @param comments any notes with respect to this work package charges
     */
    public TimesheetRow(final int projId, final String wp,
            final BigDecimal[] hours, final String comments) {
        setProjectID(projId);
        setWorkPackage(wp);
        setHoursForWeek(hours);
        setNotes(comments);
    }

    /**
     * @return timesheetrow id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RowID")
    @XmlAttribute
    public int getId() {
        return id;
    }

    /**
     * Set timesheetrow id.
     *
     * @param n timesheetrow id
     */
    public void setId(final int n) {
        id = n;
    }

    /**
     * @return timesheet that owns this row.
     */
    @ManyToOne
    @JoinColumn(name = "TimesheetID")
    @XmlTransient
    public Timesheet getTimesheet() {
        return timesheet;
    }

    /**
     * Set timesheet that owns this row.
     *
     * @param t timesheet that owns this row
     */
    public void setTimesheet(final Timesheet t) {
        timesheet = t;
    }

    /**
     * @return the projectID
     */
    @Column(name = "ProjectID", nullable = false)
    @XmlElement(name = "project-num")
    public Integer getProjectID() {
        return projectID;
    }

    /**
     * @param identification the projectID to set
     */
    public void setProjectID(final Integer identification) {
        this.projectID = identification;
    }

    /**
     * @return the workPackage
     */
    @Column(name = "WorkPackage", nullable = false)
    @XmlElement(name = "work-package")
    public String getWorkPackage() {
        return workPackage;
    }

    /**
     * @param wp the workPackage to set
     */
    public void setWorkPackage(final String wp) {
        this.workPackage = wp;
    }

    /**
     * @return the notes
     */
    @Column(name = "Notes")
    @XmlElement(name = "note")
    public String getNotes() {
        return notes;
    }

    /**
     * @param comments the notes to set
     */
    public void setNotes(final String comments) {
        this.notes = comments;
    }

    /**
     * @return Monday's hour on this row.
     */
    @Column(name = "Mon")
    @XmlElement(name = "monday")
    public BigDecimal getHourMon() {
        return getHour(TimesheetRow.MON);
    }

    /**
     * Set hour on Monday.
     *
     * @param hour number of hours worked
     */
    public void setHourMon(final BigDecimal hour) {
        setHour(TimesheetRow.MON, hour);
    }

    /**
     * @return Tuesday's hour on this row.
     */
    @Column(name = "Tue")
    @XmlElement(name = "tuesday")
    public BigDecimal getHourTue() {
        return getHour(TimesheetRow.TUE);
    }

    /**
     * Set hour on Tuesday.
     *
     * @param hour number of hours worked
     */
    public void setHourTue(final BigDecimal hour) {
        setHour(TimesheetRow.TUE, hour);
    }

    /**
     * @return Wednesday's hour on this row.
     */
    @Column(name = "Wed")
    @XmlElement(name = "wednesday")
    public BigDecimal getHourWed() {
        return getHour(TimesheetRow.WED);
    }

    /**
     * Set hour on Wednesday.
     *
     * @param hour number of hours worked
     */
    public void setHourWed(final BigDecimal hour) {
        setHour(TimesheetRow.WED, hour);
    }

    /**
     * @return Thursday's hour on this row.
     */
    @Column(name = "Thu")
    @XmlElement(name = "thursday")
    public BigDecimal getHourThur() {
        return getHour(TimesheetRow.THU);
    }

    /**
     * Set hour on Thursday.
     *
     * @param hour number of hours worked
     */
    public void setHourThur(final BigDecimal hour) {
        setHour(TimesheetRow.THU, hour);
    }

    /**
     * @return Friday's hour on this row.
     */
    @Column(name = "Fri")
    @XmlElement(name = "friday")
    public BigDecimal getHourFri() {
        return getHour(TimesheetRow.FRI);
    }

    /**
     * Set hour on Friday.
     *
     * @param hour number of hours worked
     */
    public void setHourFri(final BigDecimal hour) {
        setHour(TimesheetRow.FRI, hour);
    }

    /**
     * @return Saturday's hour on this row.
     */
    @Column(name = "Sat")
    @XmlElement(name = "saturday")
    public BigDecimal getHourSat() {
        return getHour(TimesheetRow.SAT);
    }

    /**
     * Set hour on Saturday.
     *
     * @param hour number of hours worked
     */
    public void setHourSat(final BigDecimal hour) {
        setHour(TimesheetRow.SAT, hour);
    }

    /**
     * @return Sunday's hour on this row.
     */
    @Column(name = "Sun")
    @XmlElement(name = "sunday")
    public BigDecimal getHourSun() {
        return getHour(TimesheetRow.SUN);
    }

    /**
     * Set hour on Sunday.
     *
     * @param hour number of hours worked
     */
    public void setHourSun(final BigDecimal hour) {
        setHour(TimesheetRow.SUN, hour);
    }

    /**
     * @return the hours charged for each day
     */
    @Transient
    @XmlTransient
    public BigDecimal[] getHoursForWeek() {
        return hoursForWeek;
    }

    /**
     * @param hours the hours charged for each day
     */
    public void setHoursForWeek(final BigDecimal[] hours) {
        checkHoursForWeek(hours);
        this.hoursForWeek = hours;
    }

    /**
     * @param day The day of week to return charges for
     * @return charges in hours of specific day in week
     */
    @Transient
    private BigDecimal getHour(final int day) {
        return hoursForWeek[day];
    }

    /**
     * @param day The day of week to set the hour
     * @param hour The number of hours worked for that day
     */
    private void setHour(final int day, final BigDecimal hour) {
        if (hour != null) {
            hoursForWeek[day] = hour.setScale(1, BigDecimal.ROUND_HALF_UP);
        } else {
            hoursForWeek[day] = hour;
        }
    }

    /**
     * @param day The day of week to set the hour
     * @param hour The number of hours worked for that day
     */
    public void setHour(final int day, final double hour) {
        BigDecimal bdHour = null;
        if (hour != 0.0) {
            bdHour = new BigDecimal(hour).setScale(1, BigDecimal.ROUND_HALF_UP);
        }
        checkHour(bdHour);
        hoursForWeek[day] = bdHour;
    }

    /**
     * Checks if hour value is out of the valid bounds of 0.0 to 24.0, or has
     * more than one decimal digit.
     *
     * @param hour the value to check
     */
    private void checkHour(final BigDecimal hour) {
        if (hour != null) {
            if (hour.compareTo(Timesheet.HOURS_IN_DAY) > 0.0
                    || hour.compareTo(BigDecimal.ZERO) < 0.0) {
                throw new IllegalArgumentException(
                        "out of range: should be between 0 and 24");
            }
            if (hour.scale() > 1) {
                throw new IllegalArgumentException(
                        "too many decimal digits: should be at most 1");
            }
        }
    }

    /**
     * Checks if any hour value in any day of the week is out of the valid
     * bounds of 0.0 to 24.0, or has more than one decimal digit.
     *
     * @param hours array of hours charged for each day in a week
     */
    private void checkHoursForWeek(final BigDecimal[] hours) {
        if (hours.length != Timesheet.DAYS_IN_WEEK) {
            throw new IllegalArgumentException("wrong week length: need 7");
        }
        for (BigDecimal next : hours) {
            checkHour(next);
        }
    }

    /**
     * @return the weekly hours
     */
    @Transient
    @XmlElement(name = "total")
    public BigDecimal getSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal next : hoursForWeek) {
            if (next != null) {
                sum = sum.add(next);
            }
        }
        return sum;
    }

    /**
     * Check if row is a duplicate of given row. Definition of duplicate is when
     * two rows has exact same combination of projectID + WorkPackage.
     *
     * @param reference row to be checked against.
     * @return whether given row is a duplicate of current row
     */
    @Transient
    public final boolean isDuplicate(final TimesheetRow reference) {
        if (!this.isIdEmpty() || !reference.isIdEmpty()) {
            System.out.println("ERROR (timesheeRowModel.isDuplicate): "
                    + "projectId or workPackage cannot be null.");
            return false;
        }

        String id1 = getProjectID() + getWorkPackage();
        String id2 = reference.getProjectID() + reference.getWorkPackage();

        id1 = id1.toUpperCase();
        id2 = id2.toUpperCase();

        return id1.equals(id2);
    }

    /**
     * Validate the timesheetRow's projectId and Work package are not empty.
     *
     * @return if projectId and workpackage number are filled.
     */
    @Transient
    public final boolean isIdEmpty() {
        final Integer projId = getProjectID();
        final String workPk = getWorkPackage();

        return (projId != null) && (workPk != null) && (workPk.length() > 0);
    }

    @Override
    public final String toString() {
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
