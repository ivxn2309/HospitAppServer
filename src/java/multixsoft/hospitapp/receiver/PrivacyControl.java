package multixsoft.hospitapp.receiver;

import multixsoft.hospitapp.entities.Admin;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.webservice.AdminFacadeREST;
import multixsoft.hospitapp.webservice.DoctorFacadeREST;
import multixsoft.hospitapp.webservice.PatientFacadeREST;

/**
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
public class PrivacyControl {
    public PrivacyControl(){
        
    }
    
    public int accessAsPatient(String nss, String password) {
        if(!isValid(nss)){
            return -1;
        }
        if(!isValid(decrypt(password, "key", "256"))) {
            return -1;
        }
        Patient patient = new PatientFacadeREST().find(nss);
        if(patient == null) {
            return -1;
        }
        if(!patient.getPassword().equals(password)){
            return -1;
        }
        return 1;
    }
    
    public int accessAsAdminDoctor(String username, String password) {
        if(!isValid(username)){
            return -1;
        }
        if(!isValid(decrypt(password, "key", "256"))) {
            return -1;
        }
        Doctor doctor = new DoctorFacadeREST().find(username);
        Admin admin = new AdminFacadeREST().find(username);
        if(doctor == null) {
            if(admin == null) {
                return -1;
            }
            else if(admin.getPassword().equals(password)){
                return 2;
            }
        }
        else if(doctor.getPassword().equals(password)){
            return 1;
        }
        return -1;
    }
    
    public String encrypt(String text, String key, String bits) {
        return "";
    }
    
    public String decrypt(String text, String key, String bits) {
        return "";
    }
    
    private boolean isValid(String string) {
        return false;
    }
}
