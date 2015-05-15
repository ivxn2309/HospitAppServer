package multixsoft.hospitapp.receiver;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
@Path("privacycontrol")
public class PrivacyControl {

    @Context
    private UriInfo context;
    private AdapterRest adapter;

    public PrivacyControl() {
        adapter = new AdapterRest();
    }

    public int accessAsPatient(String nss, String password) {
        if(!isValid(nss)){
            return -1;
        }
        if(!isValid(decrypt(password, "key", "256"))) {
            return -1;
        }        
        
        JSONObject jObj = (JSONObject)adapter.get("patient/"+nss);
        
        if(jObj.isEmpty()) {
            return -1;
        }
        if(!jObj.get("password").equals(password)){
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
        JSONObject doctor = (JSONObject)adapter.get("doctor/"+username);
        JSONObject admin = (JSONObject)adapter.get("admin/"+username);
        if(doctor.isEmpty()) {
            if(admin.isEmpty()) {
                return -1;
            }
            else if(admin.get("password").equals(password)){
                return 2;
            }
        }
        else if(doctor.get("password").equals(password)){
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
        return true;
    }
}