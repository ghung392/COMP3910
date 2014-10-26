package ca.bcit.infosys.test.timesheet;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import ca.bcit.infosys.employee.Employee;
import ca.bcit.infosys.timesheet.Timesheet;
import ca.bcit.infosys.timesheet.TimesheetRow;
/**
 * JUnit tests for the Timesheet.
 *
 * @author blink
 *
 */

public class TestTimesheet {
    final BigDecimal[] hours = {
            new BigDecimal(8).setScale(1, BigDecimal.ROUND_HALF_UP),
            new BigDecimal(2).setScale(1, BigDecimal.ROUND_HALF_UP),
            new BigDecimal(2.3).setScale(1, BigDecimal.ROUND_HALF_UP),
            new BigDecimal(2.7).setScale(1, BigDecimal.ROUND_HALF_UP),
            new BigDecimal(4).setScale(1, BigDecimal.ROUND_HALF_UP),
            new BigDecimal(4).setScale(1, BigDecimal.ROUND_HALF_UP),
            new BigDecimal(7).setScale(1, BigDecimal.ROUND_HALF_UP)
    };  //totals 30 hours

    final BigDecimal[] hours2 = {
            new BigDecimal(0),
            BigDecimal.ZERO,
            new BigDecimal(8),
            new BigDecimal(8),
            new BigDecimal(4),
            new BigDecimal(4),
            new BigDecimal(8)
    };  //totals 32 hours

    final BigDecimal[] hours3 = {
            null,
            null,
            new BigDecimal(8),
            new BigDecimal(8),
            new BigDecimal(4),
            new BigDecimal(4),
            new BigDecimal(8)
    };  //totals 32 hours
    
    final BigDecimal[] hours4 = {
            null,
            null,
            new BigDecimal(8),
            new BigDecimal(0),
            new BigDecimal(2),
            new BigDecimal(0),
            new BigDecimal(0)
    };  //totals 10 hours

    Employee e = new Employee();


    @Test
    public void testCreation() {
        Timesheet ts = new Timesheet();
        Date ew = ts.getEndWeek();
        Calendar c = new GregorianCalendar();
        c.setTime(ew);
        assertTrue(c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);
        assertTrue(ts.getEmployee() == null);

        List<TimesheetRow> tsrs = new ArrayList<TimesheetRow>();
        tsrs.add(new TimesheetRow());
        ts = new Timesheet(e, new Date(2014 - 1900, 10 - 1, 10), //Oct 10, 2014
                tsrs); 
        ew = ts.getEndWeek();
        c.setTime(ew);
        assertTrue(c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);
        assertTrue(ts.getEmployee() == e);
        assertTrue(ts.getDetails() == tsrs);
        
        //empty details
        tsrs = new ArrayList<TimesheetRow>();
        ts = new Timesheet(e, new Date(2014 - 1900, 10 - 1, 10), //Oct 10, 2014
                tsrs); 

    }
    
    @Test
    public void testWeekNumber() {
        //empty details
        Timesheet ts = new Timesheet(e, new Date(2014 - 1900, 0, 3), //Jan 3, 2014
                new ArrayList<TimesheetRow>()); 
        assertTrue(ts.getWeekNumber() == 1);
        
        ts.setWeekNumber(5, 2014);
        assertTrue(ts.getWeekNumber() == 5);
        Date ew = ts.getEndWeek();
        Calendar c = new GregorianCalendar();
        c.setTime(ew);
        assertTrue(c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);
        assertTrue(ew.equals(new Date(2014 - 1900, 0, 31))); //Jan 31, 2014

    }
    
    @Test
    public void testHours() {
        List<TimesheetRow> tsrs = new ArrayList<TimesheetRow>();
        tsrs.add(new TimesheetRow(123, "123", hours, "comment1"));
        tsrs.add(new TimesheetRow(123, "123", hours2, "comment2"));
        tsrs.add(new TimesheetRow(123, "123", hours3, "comment3"));
        Timesheet ts = new Timesheet(e, new Date(2014 - 1900, 10 - 1, 10), //Oct 10, 2014
                tsrs); 
        BigDecimal totalHours = ts.getTotalHours();
        BigDecimal[] dailyHours = ts.getDailyHours();
        assertTrue(totalHours.
                equals(new BigDecimal(94).setScale(1, BigDecimal.ROUND_HALF_UP)));
    }
    
    @Test
    public void testOT() {
        Timesheet ts = new Timesheet();
        assertTrue(ts.getOvertime() == null);
        ts.setOvertime(new BigDecimal(10.245));
        assertTrue(ts.getOvertime().
                equals(new BigDecimal(10.2).setScale(1, BigDecimal.ROUND_HALF_UP)));
        ts.setOvertime(new BigDecimal(10.25));
        assertTrue(ts.getOvertime().
                equals(new BigDecimal(10.3).setScale(1, BigDecimal.ROUND_HALF_UP)));
        ts.setOvertime(new BigDecimal(10.24999));
        assertTrue(ts.getOvertime().
                equals(new BigDecimal(10.2).setScale(1, BigDecimal.ROUND_HALF_UP)));
    }
    
    @Test
    public void testFlex() {
        Timesheet ts = new Timesheet();
        assertTrue(ts.getFlextime() == null);
        ts.setFlextime(new BigDecimal(10.245));
        assertTrue(ts.getFlextime().
                equals(new BigDecimal(10.2).setScale(1, BigDecimal.ROUND_HALF_UP)));
        ts.setFlextime(new BigDecimal(10.25));
        assertTrue(ts.getFlextime().
                equals(new BigDecimal(10.3).setScale(1, BigDecimal.ROUND_HALF_UP)));
        ts.setFlextime(new BigDecimal(10.24999));
        assertTrue(ts.getFlextime().
                equals(new BigDecimal(10.2).setScale(1, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void testIsValid() {
        List<TimesheetRow> tsrs = new ArrayList<TimesheetRow>();
        tsrs.add(new TimesheetRow(123, "123", hours, "comment1"));
        Timesheet ts = new Timesheet(e, new Date(2014 - 1900, 10 - 1, 10), //Oct 10, 2014
                tsrs);
        assertFalse(ts.isValid());

        tsrs.add(new TimesheetRow(123, "456", hours4, "comment1"));
        assertTrue(ts.isValid());

       tsrs.add(new TimesheetRow(123, "223", hours2, "comment1"));
       assertFalse(ts.isValid());
       ts.setOvertime(new BigDecimal(32));
       assertTrue(ts.isValid());

       ts.setOvertime(new BigDecimal(20));
       assertFalse(ts.isValid());
       ts.setFlextime(new BigDecimal(12));
       assertTrue(ts.isValid());

    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNotFridayException() {
        //fails because date not Friday
        List<TimesheetRow> tsrs = new ArrayList<TimesheetRow>();
        Timesheet ts = new Timesheet(e, new Date(2014 - 1900, 10 - 1, 9), //Oct 10, 2014
                tsrs); 

    }


}
