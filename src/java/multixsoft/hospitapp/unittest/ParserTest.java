package multixsoft.hospitapp.unittest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import multixsoft.hospitapp.utilities.Parser;

/**
 *
 * @author Yonathan Alexander Martínez Padilla
 */

public class parserTest {
    private Parser parser;

    @Before
    public void setUp(){
        parser = new Parser();
    }
    
    @Test
    public void testScheduleComposition(){
        String [] hours = new String[] {"8", "12"};
        parser.setSchedule(hours);
        assertEquals(parser.getInterval(), "08:00-12:00" );
    }
    @Test
    public void testScheduleComposition2(){
       String [] hours = new String[] {"8", "9"};
       parser.setSchedule(hours);
       assertEquals(parser.getInterval(), "08:00-09:00");
    }
    @Test
    public void testScheduleComposition3(){
        String [] hours = new String[] {"10", "15"};
        parser.setSchedule(hours);
        assertEquals(parser.getInterval(), "10:00-03:00");
    }
    @Test
    public void testScheduleComposition4(){
        String [] hours = new String[] {"10", "12"};
        parser.setSchedule(hours);
        assertEquals(parser.getInterval(), "10:00-12:00");
    }
    @Test
    public void testTimelineToSchedule(){
        parser.setTimeline(new int[] {8, 9, 10});
        assertEquals(parser.getMinHour(), 8);
        assertEquals(parser.getMaxHour(), 10);
    }
    @Test
    public void testFollowUpTimeline(){
        parser.followUpTimeline(8, 10);
        assertEquals(parser.getHoursAvailable(), new int[] {8, 9, 10});
    }
    @Test
    public void testScheduleToTimeline(){
        parser.setSchedule(new String[] {"08:00-09:00"});
        assertEquals(parser.getMinHour(), 8);
        assertEquals(parser.getMaxHour(), 9);
    }
    @Test
    public void testparser(){
        assertEquals(parser.getPoints(), ":");
        assertEquals(parser.getZero(), "0");
        assertEquals(parser.getDash(), "-");
        assertEquals(parser.getTimeline(), new int[] {});
        assertEquals(parser.getSchedule(), new String[] {});
        assertEquals(parser.getSizeTimeline(), 0);
        assertEquals(parser.getSizeSchedule(), 0);
    }
    @Test
    public void testAddSchedule(){
        parser.addSchedule("08:00-09:00");
        assertEquals(parser.getSchedule()[parser.getSizeSchedule()], "08:00-09:00");
    }
    @Test
    public void testAddTimeline(){
        parser.addTimeline(new int[]{8, 9, 10});
        assertEquals(parser.getTimeline()[parser.getSizeTimeLine()], 10);
    }
}
