package com.corejsf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Access.TimesheetManager;
import com.corejsf.Model.TimesheetModel;

/**
 * TimesheetBean is a CDI bean that holds relevant information and business
 * logic to interact with JSF XHTML presentation layer.
 *
 */
@Named("timesheetData")
@ConversationScoped
public class TimesheetBean implements Serializable {

    @Inject
    private Conversation conversation;
    /** Manages access and persistence of timesheets in data layer. */
    @Inject
    private TimesheetManager timesheetManager;
    /** CDI manages manages employee related data over a session. */
    @Inject
    private EmployeeInfo employeeSession;

    /** All timesheets belonging to current employee. */
    private List<TimesheetModel> allTimesheets;
    /** End of week of the timesheet to view or viewing. */
    private Date currEndWeek = TimesheetModel.getCurrDate(); // TODO producer
                                                             // inject
    /** A timesheet belonging to current employee, specified by currEndWeek. */
    private TimesheetModel timesheet;
    /** Flag indicate whether successfully saved timesheet. */
    private boolean saveSuccess = false;

    /**
     * Get saveSuccess flag.
     *
     * @return whether timesheet successfully saved.
     */
    public boolean getSaveSuccess() {
        return saveSuccess;
    }

    /**
     * Get all timesheets belonging to currently logged in employee.
     *
     * @return all timesheets belonging to currently logged in employee.
     */
    public List<TimesheetModel> getAllTimesheets() {
        if (allTimesheets == null) {
            refreshTimesheetList();
        }

        return allTimesheets;
    }

    /**
     * Get specified week's timesheet of current employee. Week dictated by
     * currEndWeek property.
     *
     * @return a timesheet.
     */
    public TimesheetModel getTimesheet() {
        if (timesheet == null) {
            refreshTimesheet();
        }

        return timesheet;
    }

    /**
     * Update current employee's timesheet list from data layer.
     */
    private void refreshTimesheetList() {
        System.out.println("Refreshing employee's TimesheetList");
        Employee currEmployee = employeeSession.getCurrentEmployee();
        allTimesheets = timesheetManager.find(currEmployee);
    }

    /**
     * Update current employee's timesheet from data layer.
     */
    private void refreshTimesheet() {
        System.out.println("Refreshing current timesheet.");
        Employee currEmployee = employeeSession.getCurrentEmployee();
        TimesheetModel newSheet = timesheetManager.find(currEmployee,
                currEndWeek);
        timesheet = newSheet;
    }

    /**
     * Validate and save timesheet to data layer.
     *
     * @return navigation outcome - return to same page.
     */
    public String saveTimesheet() {
        boolean isValid = true;
//        saveSuccess = false;

        timesheet.trimTimesheetRows();
        System.out.println("DEBUG: " + timesheet);

        if (!timesheet.isValid()) {
            isValid = false;
            FacesMessage message = new FacesMessage("Work hours must add "
                    + "up to 40. Please add "
                    + "additional hours into overtime or "
                    + "flexible hour field");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("timesheet_form",
                    message);
            System.out.println("Work hours must add up to 40.");
        }
        if (!timesheet.areRowsValid()) {
            isValid = false;
            FacesMessage message = new FacesMessage("A combination of project "
                    + "id & workpage must be filled and unique for each row.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("timesheet_form",
                    message);
        }
        if (!timesheet.correctHoursInDay()) {
            isValid = false;
            saveSuccess = false;
            FacesMessage message = new FacesMessage("Hours in a day must "
                    + "add up to 24 hours or less.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("timesheet_form",
                    message);
        }
        if (isValid) {
            timesheetManager.merge(timesheet);
            refreshTimesheetList();
            saveSuccess = true;
            closeNotification();
            System.out.println("Saving timesheet");
        }

        return null;
    }

    // reset saveSuccess after page render
    private void closeNotification() {
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        saveSuccess = false;
                    }
                }, 
                1500 
        );
    }

    /**
     * Add a new timesheetRow to timesheet table.
     *
     * @return navigation outcome - return to same page.
     */
    public String addRow() {
        timesheet.addRow();
        return null;
    }

    /**
     * Go to Fill timesheet page to create / edit current week's timesheet. Set
     * currEndWeek to current week's Friday.
     *
     * @return navigation outcome - createTimesheet page.
     */
    public String currTimesheet() {
        beginConversation();
        return "createTimesheet";
    }

    /**
     * Go to View a past timesheet page. Set currEndWeek to specified week's
     * Friday.
     *
     * @param endWeek specified week's Friday.
     * @return navigation outcome - viewTimesheet page.
     */
    public String viewTimesheet(final Date endWeek) {
        currEndWeek = endWeek;
        return "viewTimesheet";
    }

    public String viewTimesheetList() {
        endConversation();
        return "history";
    }

    private void beginConversation() {
        if (conversation.isTransient()) {
            System.out.println("conversation began.");
            conversation.begin();
        }
    }

    private void endConversation() {
        if (!conversation.isTransient()) {
            System.out.println("conversation end.");
            conversation.end();
        }
    }

}
