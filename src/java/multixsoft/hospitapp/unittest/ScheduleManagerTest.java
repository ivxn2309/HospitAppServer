package multixsoft.hospitapp.unittest;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import multixsoft.hospitapp.management.ScheduleManager;
import multixsoft.hospitapp.entities.Appointment;
import multixsoft.hospitapp.utilities.Date;
import multixsoft.hospitapp.entities.Schedule;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.entities.Doctor;
import java.util.List;

/**
 *
 * @author maritza
 */
public class ScheduleManagerTest {
    
    private ScheduleManager scheduleManager;
    private Appointment requestAppointment;
    private Doctor requestDoctor;
    private Schedule doctorAvailableSchedule;
    
    @BeforeClass
    public void setUpClass(){
        scheduleManager = new ScheduleManager();
                
        requestAppointment = new Appointment();
        requestAppointment.setIdAppointment((long) 123445565);
        requestAppointment.setPatientNss("ABCDE12GEMSD");
        requestAppointment.setDoctorUsername("JoseManuel");
        requestAppointment.setDate(new Date(8,3,2015).getTime());
        requestAppointment.setIsFinished(false);
        
        requestDoctor = new Doctor();
        requestDoctor.setUsername("JoseManuel");
        requestDoctor.setPassword("medico120");
        requestDoctor.setFirstName("José");
        requestDoctor.setLastName("Domínguez");
        requestDoctor.setLicense("1618102");

        doctorAvailableSchedule = new Schedule();
        doctorAvailableSchedule.setIdSchedule(23834L);
        doctorAvailableSchedule.setMonday("9:00-15:00");
        doctorAvailableSchedule.setTuesday("9:00-13:00");
        doctorAvailableSchedule.setWednesday("13:00-17:00");
        doctorAvailableSchedule.setThursday("8:00-16:00");
        doctorAvailableSchedule.setFriday("10:00-14:00");

        Patient patient = new Patient();
        patient.setNss("123456789");
        patient.setPassword("password");
        patient.setIsActive(true);
        patient.setDocUsername("JoseManuel");
        //Cambiar esto por la nueva arquitectura
        new DoctorFacade().create(requestDoctor);
        new PatientFacade().create(patient);
        new ScheduleFacade().create(doctorAvailableSchedule);
        new AppointmentFacade().create(requestAppointment);
    }
    
    @Test
    public void testCancelAppointment() {
        boolean dateIsCanceled = scheduleManager.cancelAppointment(
                requestAppointment);
        assertEquals(dateIsCanceled, true);
    }
    
    @Test
    public void testSetAppointmentFinish(){
        Date requestDate = new Date(8,3,2015);
        
        boolean dateIsFinished = scheduleManager.setAppointmentFinish(
                requestDate, requestAppointment);
        
        assertEquals(dateIsFinished, true);
    }
    
    @Test
    public void testGetNextAppointment(){
        Appointment expectedAppointment = new Appointment();
        Date dateExpectedAppointment = new Date(9,3,2015);
        
        expectedAppointment.setIdAppointment((long) 344532342);
        expectedAppointment.setPatientNss("ABCDE12GEMSD");
        expectedAppointment.setDoctorUsername("JoseManuel");
        expectedAppointment.setDate(dateExpectedAppointment.getTime());
        expectedAppointment.setIsFinished(false);
        
        Appointment actualAppointment = scheduleManager.getNextAppointment(
                requestDoctor);
        
        assertEquals(actualAppointment, expectedAppointment);
    }
    
    @Test
    public void testScheduleAppointment(){
        long appointmentId = scheduleManager.scheduleAppointment(
                requestAppointment);
        
        assertEquals(appointmentId, requestAppointment.getIdAppointment());
    }
    
    @Test 
    public void testGetAvailableSchedule(){
        boolean scheduleIsOriginal = true;
       
        Schedule actualAvailableSchedule = scheduleManager.getAvailableSchedule(
                requestDoctor, scheduleIsOriginal);
        
        assertEquals(doctorAvailableSchedule, actualAvailableSchedule);
       
    }
    @Test 
    public void testGetAvailableScheduleaFalse(){
        boolean scheduleIsOriginal = false;
       
        Schedule actualAvailableSchedule = scheduleManager.getAvailableSchedule(
                requestDoctor, scheduleIsOriginal);
        
        assertEquals(doctorAvailableSchedule, actualAvailableSchedule);       
    }

/**
 *
 * @author manuelmartinez
 */
    
    @Test
    public void testCancelAppointmentFalse() {
        assertEquals(false, scheduleManager.cancelAppointment(requestAppointment));
    }

/**
 *
 * @author Ivan Tovar
 */

    @Test
    public void testObtainAllPatientsDated() {
        Appointment app1 = new Appointment();
        app1.setIdAppointment(99999999L);
        app1.setPatientNss("ABCDE12GEMSD");
        app1.setDoctorUsername("JoseManuel");
        app1.setDate(new Date().getTime());
        app1.setIsFinished(false);

        List<Appointment> apps = scheduleManager.getAllAppointmentsFor(requestDoctor, new Date());
        assertEquals(apps.size(), 1);
    }

    @AfterClass
     public void tearDownClass() {
        //Destruir de manera correcta
        new AppointmentFacade().remove(requestAppointment);
        new ScheduleFacade().remove(doctorAvailableSchedule);
        new PatientFacade().remove(patient);
        new DoctorFacade().remove(requestDoctor);
     }
    
}
