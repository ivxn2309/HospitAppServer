package multixsoft.hospitapp.receiver;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
    @GET
    @Path("accessaspatient")
    @Produces("text/plain")
    public int accessAsPatient( @QueryParam("nss") String nss, 
            @QueryParam("password") String password) {
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
    
    @GET
    @Path("accessasadmindoctor")
    @Produces("text/plain")
    public int accessAsAdminDoctor(@QueryParam("username") String username, 
            @QueryParam("password") String password) {
        if(!isValid(username)){
            return -1;
        }
        if(!isValid(decrypt(password, "key", "256"))) {
            return -1;
        }
        JSONObject doctor = (JSONObject)adapter.get("doctor/"+username);
        JSONObject admin = (JSONObject)adapter.get("admin/"+username);
        if(doctor==null || doctor.isEmpty()) {
            if(admin == null || admin.isEmpty()) {
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
        return text;
    }
    
    public String decrypt(String text, String key, String bits) {
        return text;
    }
    
    private boolean isValid(String string) {
        return true;
    }
}
