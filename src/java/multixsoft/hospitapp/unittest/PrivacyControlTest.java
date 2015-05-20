package multixsoft.hospitapp.unittest;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.management.PrivacyControl;

/**
 *
 * @author Ivan Tovar
 */

public class PrivacyControlTest {
     private Doctor doctor;
     private Patient patient;
     private PrivacyControl control;

     @BeforeClass
     public void setUpClass() {
          doctor = new Doctor();
          doctor.setUsername("pepillo");
          doctor.setPassword("una_password");
          doctor.setFirstName("Pepe");
          doctor.setLastName("Perez");
          doctor.setLicense("12345678");

          patient = new Patient();
          patient.setNss("123456789");
          patient.setPassword("password");
          patient.setIsActive(true);
          patient.setDocUsername("pepillo");
          
          //Cambiar esto por la nueva arquitectura 
          new DoctorFacade().create(doctor);
          new PatientFacade().create(patient);
     }

     @Test
     public void testAccessAsAdminOrDoctor(){
          int ans = control.accessAsAdminDoctor(doctor.getUsername(), doctor.getPassword());
          assertEquals(ans, 1);
     }

     @Test
     public void testAccessAsPatient(){
          int ans = control.accessAsPatient(patient.getNss(), patient.getPassword());
          assertEquals(ans, 1);
     }

     @AfterClass
     public void tearDownClass() {
         //Cambiar esto por la nueva arquitectura 
          new PatientFacade().remove(patient);
          new DoctorFacade().remove(doctor);
     }

}
