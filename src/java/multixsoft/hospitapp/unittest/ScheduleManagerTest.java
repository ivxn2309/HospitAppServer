package multixsoft.hospitapp.unittest;

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import multixsoft.hospitapp.management.ScheduleManager;
import multixsoft.hospitapp.entities.Appointment;
import multixsoft.hospitapp.utilities.Date;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author maritza
 */
public class ScheduleManagerTest {
    
    private ScheduleManager scheduleManager;
    private JSONObject requestAppointment;
    private JSONObject doctorAvailableSchedule;
    private JSONObject doctorSchedule;
    private AdapterRest adapter;
    private JSONObject doctor;
    private JSONObject patient;
    
    @BeforeClass
    public void setUpClass(){
        adapter = new AdapterRest();
        scheduleManager = new ScheduleManager();
        
        doctor = new JSONObject();
        doctor.put("username", "jose");
        doctor.put("password", "pass");
        doctor.put("firstName", "Jose");
        doctor.put("lastName", "Dominguez");
        doctor.put("license", "34O34KDSFJ");
        doctor.put("specialty", "Cardiologia");

        patient = new JSONObject();
        patient.put("nss", "123456789");
        patient.put("password", "password");
        patient.put("isActive", true);
        patient.put("doctorUsername", doctor.toJSONString());
                
        requestAppointment = new JSONObject();
        requestAppointment.put("idAppointment",(long) 123445565);
        requestAppointment.put("patientNss", patient.toJSONString());
        requestAppointment.put("doctorUsername", doctor.toJSONString());
        requestAppointment.put("date", new Date(25,5,2015));
        requestAppointment.put("isFinished", false);
        requestAppointment.put("iscanceled", false);
        requestAppointment.put("time","11");
        
        doctorSchedule = new JSONObject();
        doctorSchedule.put("idSchedule",23345L);
        doctorSchedule.put("monday","9-15");
        doctorSchedule.put("tuesday","9-13");
        doctorSchedule.put("wednesday","13-17");
        doctorSchedule.put("thursday","8-12");
        doctorSchedule.put("friday","10-15");
        doctorSchedule.put("doctorUsername", doctor.toJSONString());
        
        doctorAvailableSchedule = new JSONObject();
        doctorAvailableSchedule.put("idSchedule",23345L);
        doctorAvailableSchedule.put("monday","9-11,12-15");
        doctorAvailableSchedule.put("tuesday","9-13");
        doctorAvailableSchedule.put("wednesday","13-17");
        doctorAvailableSchedule.put("thursday","8-12");
        doctorAvailableSchedule.put("friday","10-15");
        doctorAvailableSchedule.put("doctorUsername", doctor.toJSONString());
        
        // Duda al crear doctor y patient, aquí si se necesitaría facade?
        adapter.post("doctor", doctor.toJSONString());
        adapter.post("patient", patient.toJSONString());
        
        adapter.post("appointment", requestAppointment.toJSONString());
        adapter.post("schedule", doctorSchedule.toJSONString());
        adapter.post("schedule", doctorAvailableSchedule.toJSONString());
        
    }
    
     @Test
    public void testCancelAppointment() {
        boolean dateIsCanceled = scheduleManager.cancelAppointment(
            (String) requestAppointment.get("idAppointment")
        );
        assertEquals(dateIsCanceled, true);
    }
    
     @Test
    public void testSetAppointmentFinish(){
        Date requestDate = new Date(22,5,2015);
        boolean dateIsFinished = scheduleManager.setAppointmentFinish(
            (String) requestAppointment.get("idAppointment")
        );
        
        assertEquals(dateIsFinished, true);
    }

     @Test 
    public void testGetAvailableScheduleaFalse(){
        boolean scheduleIsOriginal = false;
       
        String actualAvailableSchedule = scheduleManager.getAvailableSchedule(
                "jose", scheduleIsOriginal);
        
        assertEquals(doctorAvailableSchedule.toJSONString(), actualAvailableSchedule);       
    }
    
    @Test 
    public void testGetAvailableScheduleaTrue(){
        boolean scheduleIsOriginal = true;
       
        String actualAvailableSchedule = scheduleManager.getAvailableSchedule(
                "jose", scheduleIsOriginal);
        
        assertEquals(doctorSchedule.toJSONString(), actualAvailableSchedule);       
    }
    
    @Test
    public void testObtainAllPatientsDated() {
        JSONArray array = (JSONArray) JSONValue.parse(scheduleManager.getAllAppointmentsFor("jose", "25-5-2015"));
        //List<Appointment> apps = new ArrayList<>();
        assertEquals(array.size(), 1);
    }
    
    @Test
    public void testScheduleAppointment(){
        long appointmentId = scheduleManager.scheduleAppointment(
            (String)requestAppointment.get("idAppointment")
        );
        
        assertEquals(appointmentId, requestAppointment.get("idAppointment"));
    }
    
    @Test
    public void testGetNextAppointment(){
        Date dateExpectedAppointment = new Date(26,5,2015);
        JSONObject expectedAppointment = new JSONObject();
        expectedAppointment.put("idAppointment", (long) 344532342);
        expectedAppointment.put("patientNss", patient.toJSONString());
        expectedAppointment.put("doctorUsername", doctor.toJSONString());
        expectedAppointment.put("date", dateExpectedAppointment);
        expectedAppointment.put("isFinished", false);
        
        
        String actualAppointment = scheduleManager.getNextAppointment(
                "123456789");
        
        assertEquals(actualAppointment, expectedAppointment.toJSONString());
    }
    
     @AfterClass
     public void tearDownClass() {
        // Duda al eliminar doctor y patient, aquí si se necesitaría facade?
        adapter.delete("appointment/"+(String)requestAppointment.get("idAppointment"));
        adapter.delete("schedule"+(String)doctorSchedule.get("idSchedule"));
        adapter.delete("schedule"+(String)doctorAvailableSchedule.get("idSchedule"));
        adapter.delete("patient/"+(String)patient.get("nss"));
        adapter.delete("doctor/"+(String)doctor.get("username"));
     }
  
}
