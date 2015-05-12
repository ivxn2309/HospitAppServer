package multixsoft.hospitapp.management;

import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.webservice.DoctorFacadeREST;
import multixsoft.hospitapp.webservice.PatientFacadeREST;

/**
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
public class PatientMapper {
    public boolean mapPatient(Patient patient, Doctor doctor) {
        patient = new PatientFacadeREST().find(patient.getNss());
        doctor = new DoctorFacadeREST().find(doctor.getUsername());
        if (patient == null) {
            return false;
        }
        if (doctor == null) {
            return false;
        }
        patient.setDoctorUsername(doctor);
        return true;
    }
}
