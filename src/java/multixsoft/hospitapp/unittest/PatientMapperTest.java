package multixsoft.hospitapp.unittest;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.management.PatientMapper;

/**
 *
 * @author Ivan Tovar
 */

public class PatientMapperTest {
     private Doctor doctor;
     private Patient patient;
     private PatientMapper mapper;

     @Before
     public void setUp() {
          mapper = new PatientMapper();
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
          
          //Cambiar estos por el nuevo codigo
          new DoctorFacade().create(doctor);
          new PatientFacade().create(patient);
     }

     @Test
     public void testMapPatientDoctor(){
          mapper.mapPatient(patient.getNss(), doctor.getUsername());
          Doctor actualDoctor = mapper.getDoctorOf(patient);
          assertEquals(actualDoctor, doctor);
     }

     @AfterClass
     public void tearDownClass() {
         //Cambiar esto por la nueva arquitectura 
          new PatientFacade().remove(patient);
          new DoctorFacade().remove(doctor);
     }

}
