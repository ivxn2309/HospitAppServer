package multixsoft.hospitapp.unittest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import multixsoft.hospitapp.management.PatientManager;
import multixsoft.hospitapp.entities.Patient;

/**
 *
 * @author manuelmartinez
 */
public class PatientManagerTest {
    
    @Test
    public void test_saveNewPatientDone() {
        Patient patient = new Patient();
        patient.setNss("123456789");
        patient.setPassword("password");
        patient.setIsActive(true);
        PatientManager pm = new PatientManager();
        assertEquals(123L, pm.saveNewPatient(patient));
    }
    
    @Test
    public void test_saveNewPatientFail() {
        Patient patient = new Patient();
        patient.setNss("123456789");
        patient.setPassword("password");
        patient.setIsActive(true);
        PatientManager pm = new PatientManager();
        assertEquals(0, pm.saveNewPatient(patient));
    }
}
