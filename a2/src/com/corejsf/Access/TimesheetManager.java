package com.corejsf.Access;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.corejsf.Model.Employee;
import com.corejsf.Model.Timesheet;

/**
 * Manages the access of timesheet data in the data layer.
 */
@Dependent
@Stateless
public class TimesheetManager {
    /** Entity Manager. */
    @PersistenceContext(unitName = "a3")
    private EntityManager em;

    /**
     * @return all timesheets stored.
     */
    public List<Timesheet> getTimesheets() {
        TypedQuery<Timesheet> query = em.createQuery("select t "
                + "from Timesheet t ORDER BY t.endWeek DESC", Timesheet.class);
        return query.getResultList();
    }

    /**
     * Get all timesheet belonging to given employee.
     *
     * @param e owner of timesheet.
     * @return all timesheet belonging to given employee
     */
    public List<Timesheet> find(final Employee e) {
        TypedQuery<Timesheet> query = em.createQuery(
                "SELECT t FROM Timesheet t WHERE t.employee = :employee"
                        + " ORDER BY t.endWeek DESC", Timesheet.class);
        query.setParameter("employee", e);
        
        return query.getResultList();
    }

    /**
     * Get timesheet belong to a given employee and given week.
     *
     * @param e owner of timesheet.
     * @param weekEnd timesheet's week end day
     * @return a timesheet of a given employee and given week.
     */
    public Timesheet find(final Employee e, final Date weekEnd) {
        TypedQuery<Timesheet> query = em.createQuery(
                "SELECT t FROM Timesheet t WHERE t.employee = :employee "
                        + "AND t.endWeek = :endWeek", Timesheet.class);
        query.setParameter("employee", e);
        query.setParameter("endWeek", weekEnd);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Get timesheet belong to a given employee and given week.
     *
     * @param e owner of timesheet.
     * @param weekEnd timesheet's week end day
     * @return a timesheet of a given employee and given week.
     */
    public Timesheet find(final int id) {
        TypedQuery<Timesheet> query = em.createQuery(
                "SELECT t FROM Timesheet t WHERE t.id = :id", Timesheet.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Updates a timesheet.
     *
     * @param timesheet timesheet to store
     */
    public void merge(final Timesheet timesheet) {
        em.merge(timesheet);
    }

}
