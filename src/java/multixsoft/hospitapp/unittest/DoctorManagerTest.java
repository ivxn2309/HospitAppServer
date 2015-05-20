package multixsoft.hospitapp.unittest;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Schedule;
import multixsoft.hospitapp.management.DoctorManager;
import multixsoft.hospitapp.webservice.ScheduleFacade;

/**
 *
 * @author Yonathan Alexander Martínez Padilla
 */

public class DoctorManagerTest {
     private Doctor pepe;
     private Schedule schedule;
     private DoctorManager doctorManager;

     @Before
     public void setUp() {
          pepe = new Doctor();
          pepe.setUsername("pepillo");
          pepe.setPassword("una_password");
          pepe.setFirstName("Pepe");
          pepe.setLastName("Perez");
          pepe.setLicense("12345678");

          schedule = new Schedule();
          schedule.setIdSchedule(3);
          doctorManager = new DoctorManager();
     }

     @Test
     public void testSaveNewDoctor(){
          assertEquals(doctorManager.saveNewDoctor(pepe), "pepillo");
     }

     @Test
     public void testFailSaveNewDoctor(){
          assertEquals(doctorManager.saveNewDoctor(pepe), null);
     }

     @Test
     public void testSetSchedule(){
     	schedule.setDoctorUsername(pepe.getUsername());
          assertEquals(new ScheduleFacade().create(schedule), 3);
     }

     @Test
     public void testFailSetSchedule(){
          assertEquals(new ScheduleFacade().create(schedule), -1);
     }

     @AfterClass
     public void tearDownClass() {
        new ScheduleFacade().remove(schedule);
     }
}
