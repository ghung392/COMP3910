package com.corejsf.Access;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ca.bcit.infosys.employee.Employee;

import com.corejsf.Model.TimesheetModel;

/**
 * Manages the access of timesheet data in the data layer.
 */
@Dependent
@Stateless
public class TimesheetManager {
    /** Entity Manager. */
    @PersistenceContext(unitName = "a1")
    private EntityManager em;

    /**
     * @return all timesheets stored.
     */
    public List<TimesheetModel> getTimesheets() {
        TypedQuery<TimesheetModel> query = em.createQuery("select t "
                + "from TimesheetModel t ORDER BY t.weekEnd DESC",
                TimesheetModel.class);
        return query.getResultList();
    }

    /**
     * Get all timesheet belonging to given employee.
     *
     * @param employeeID employeeID of owner of timesheet.
     * @return all timesheet belonging to given employee
     */
    public List<TimesheetModel> find(final Employee e) {
        TypedQuery<TimesheetModel> query = em.createQuery(
                "SELECT t FROM TimesheetModel t WHERE t.emp = :employee"
                        + " ORDER BY t.weekEnd DESC", TimesheetModel.class);
        query.setParameter("employee", e);
        return query.getResultList();
    }

    /**
     * Get timesheet belong to a given employee and given week.
     *
     * @param employeeID timesheet's owner's employeeID
     * @param weekEnd timesheet's week end day
     * @return a timesheet of a given employee and given week.
     */
    public TimesheetModel find(final Employee e, final Date weekEnd) {
        TypedQuery<TimesheetModel> query = em.createQuery(
                "SELECT t FROM TimesheetModel t WHERE t.emp = :employee "
                        + "AND t.weekEnd = :endWeek", TimesheetModel.class);
        query.setParameter("employee", e);
        query.setParameter("endWeek", weekEnd);

        TimesheetModel result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException nre) {
            result = new TimesheetModel(e);
        }

        return result;
    }

    /**
     * Updates a timesheet.
     *
     * @param timesheet timesheet to store
     */
    public void merge(final TimesheetModel timesheet) {
        em.merge(timesheet);
    }

}
