package com.corejsf.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Wrapper class to wrap list of timesheets to proper xml element.
 */
@XmlRootElement(name = "timesheets")
@XmlSeeAlso(Timesheet.class)
public class Timesheets extends ArrayList<Timesheet> {

    /** Serial version number. */
    private static final long serialVersionUID = 1L;

    /**
     * Zero-argument constructor.
     */
    public Timesheets() {
        super();
    }

    /**
     * Constructor taking a list of timesheet.
     * @param t list of timesheet.
     */
    public Timesheets(final Collection<? extends Timesheet> t) {
        super(t);
    }

    /**
     * @return list of timesheets.
     */
    @XmlElement(name = "timesheet")
    public List<Timesheet> getTimesheets() {
        return this;
    }

    /**
     * @param timesheets list of timesheets.
     */
    public void setTimesheets(List<Timesheet> timesheets) {
        this.addAll(timesheets);
    }
}
