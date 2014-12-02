package com.corejsf.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement (name="timesheets")
@XmlSeeAlso(Timesheet.class)
public class Timesheets extends ArrayList<Timesheet>{

    private static final long serialVersionUID = 1L;
    
    public Timesheets() {
        super();
    }
    
    public Timesheets(Collection<? extends Timesheet> t) {
        super(t);
    }
    
    @XmlElement( name = "timesheet" )
    public List<Timesheet> getTimesheets () {
        return this;
    }
    
    public void setTimesheets(List<Timesheet> timesheets) {
        this.addAll(timesheets);
    }
}
