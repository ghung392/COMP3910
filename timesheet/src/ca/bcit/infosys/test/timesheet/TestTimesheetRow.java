package ca.bcit.infosys.test.timesheet;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import ca.bcit.infosys.timesheet.TimesheetRow;

/**
 * JUnit tests for TimesheetRow.
 *
 * @author blink
 *
 */
public class TestTimesheetRow {
    private TimesheetRow tr = new TimesheetRow();
    final BigDecimal bd = new BigDecimal(23).divide(new BigDecimal(10));
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

    @Before
    public void setup() {
        tr.setProjectID(123);
        tr.setWorkPackage("A123");
    }
    
    @Test
    public void testTimesheetRowCreation() {
        TimesheetRow tr = new TimesheetRow(1234, "1234", hours, "comments");
        assertTrue(tr.getProjectID() == 1234);
        assertTrue(tr.getWorkPackage().equals("1234"));
        assertTrue(tr.getNotes().equals("comments"));
        assertTrue(tr.getHoursForWeek() == hours);
        assertTrue(tr.getHour(TimesheetRow.SAT).
                equals(new BigDecimal(8).setScale(1, BigDecimal.ROUND_HALF_UP)));
        assertTrue(tr.getHour(TimesheetRow.SUN).
                equals(new BigDecimal(2).setScale(1, BigDecimal.ROUND_HALF_UP)));
        assertTrue(tr.getHour(TimesheetRow.MON).
                equals(new BigDecimal(2.3).setScale(1, BigDecimal.ROUND_HALF_UP)));
        assertTrue(tr.getHour(TimesheetRow.TUE).
                equals(new BigDecimal(2.7).setScale(1, BigDecimal.ROUND_HALF_UP)));
        assertTrue(tr.getHour(TimesheetRow.WED).
                equals(new BigDecimal(4).setScale(1, BigDecimal.ROUND_HALF_UP)));
        assertTrue(tr.getHour(TimesheetRow.THU).
                equals(new BigDecimal(4).setScale(1, BigDecimal.ROUND_HALF_UP)));
        assertTrue(tr.getHour(TimesheetRow.FRI).
                equals(new BigDecimal(7).setScale(1, BigDecimal.ROUND_HALF_UP)));
        
        tr = new TimesheetRow(1234, "1234", hours2, "comments");
        assertTrue(tr.getProjectID() == 1234);
        assertTrue(tr.getWorkPackage().equals("1234"));
        assertTrue(tr.getNotes().equals("comments"));
        assertTrue(tr.getHoursForWeek() == hours2);
        assertTrue(tr.getHour(TimesheetRow.SAT).
                equals(new BigDecimal(0)));
        assertTrue(tr.getHour(TimesheetRow.SUN).
                equals(BigDecimal.ZERO));
        assertTrue(tr.getHour(TimesheetRow.MON).
                equals(new BigDecimal(8)));
        assertTrue(tr.getHour(TimesheetRow.TUE).
                equals(new BigDecimal(8)));
        assertTrue(tr.getHour(TimesheetRow.WED).
                equals(new BigDecimal(4)));
        assertTrue(tr.getHour(TimesheetRow.THU).
                equals(new BigDecimal(4)));
        assertTrue(tr.getHour(TimesheetRow.FRI).
                equals(new BigDecimal(8)));

        tr = new TimesheetRow(1234, "1234", hours3, "comments");
        assertTrue(tr.getProjectID() == 1234);
        assertTrue(tr.getWorkPackage().equals("1234"));
        assertTrue(tr.getNotes().equals("comments"));
        assertTrue(tr.getHoursForWeek() == hours3);
        assertTrue(tr.getHour(TimesheetRow.SAT) == null);
        assertTrue(tr.getHour(TimesheetRow.SUN) == null);
        assertTrue(tr.getHour(TimesheetRow.MON).
                equals(new BigDecimal(8)));
        assertTrue(tr.getHour(TimesheetRow.TUE).
                equals(new BigDecimal(8)));
        assertTrue(tr.getHour(TimesheetRow.WED).
                equals(new BigDecimal(4)));
        assertTrue(tr.getHour(TimesheetRow.THU).
                equals(new BigDecimal(4)));
        assertTrue(tr.getHour(TimesheetRow.FRI).
                equals(new BigDecimal(8)));

    }
    
    @Test
    public void testGetSum() { 
       tr.setHour(TimesheetRow.MON, bd);
       tr.setHour(TimesheetRow.SAT, 0.0);
       assertTrue(bd.equals(tr.getSum()));
       tr.setHour(TimesheetRow.MON, 2.3);
       assertTrue(new BigDecimal(2.3).setScale(1, BigDecimal.ROUND_HALF_UP)
               .equals(tr.getSum()));
       tr.setHour(TimesheetRow.WED, null);  //same as zero
       assertTrue(bd.equals(tr.getSum()));

       tr.setHoursForWeek(hours);
       assertTrue(tr.getSum().equals(new BigDecimal(30.0).
               setScale(1, BigDecimal.ROUND_HALF_UP)));

       tr.setHoursForWeek(hours2);
       assertTrue(tr.getSum().equals(new BigDecimal(32)));

       tr.setHoursForWeek(hours3);
       assertTrue(tr.getSum().equals(new BigDecimal(32)));
    }
    
    @Test
    public void testSetHour() {
        tr.setHour(TimesheetRow.MON, bd);
        assertTrue(bd.equals(tr.getHour(TimesheetRow.MON)));

        tr.setHour(TimesheetRow.MON, 2.3);
        assertTrue(bd.equals(tr.getHour(TimesheetRow.MON)));

        tr.setHour(TimesheetRow.MON, 24);
        assertTrue(tr.getHour(TimesheetRow.MON).
                equals(new BigDecimal(24).setScale(1, BigDecimal.ROUND_HALF_UP)));
        tr.setHour(TimesheetRow.MON, new BigDecimal(24));
        assertTrue(tr.getHour(TimesheetRow.MON).equals(new BigDecimal(24)));
        tr.setHour(TimesheetRow.SAT, 0.0);
        assertTrue(tr.getHour(TimesheetRow.SAT) == null );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetHourException1() {
        //fails because scale > 1
        tr.setHour(TimesheetRow.MON, new BigDecimal(2.3));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetHourException2() {
        tr.setHour(TimesheetRow.MON, 25);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetHourException3() {
        tr.setHour(TimesheetRow.MON, -1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetHoursForWeekException() {
        final BigDecimal[] hours = {
                new BigDecimal(8),
                new BigDecimal(2),
                new BigDecimal(2.3),
                new BigDecimal(2.7),
                new BigDecimal(4),
                new BigDecimal(4),
                new BigDecimal(7)
        };  //totals 30 hours, but scale >1
        tr.setHoursForWeek(hours);

    }
}
