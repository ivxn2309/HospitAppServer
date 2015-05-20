package multixsoft.hospitapp.management;

import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
@Path("patientmapper")
public class PatientMapper {

    @Context
    private UriInfo context;

    public PatientMapper() {
    }

    @PUT
    @Path("/mappatient")
    @Produces("text/plain")
    public boolean mapPatient(
            @QueryParam("nss") String nss, @QueryParam("username") String usrn) {
        AdapterRest adapter = new AdapterRest();
        JSONObject jObj = (JSONObject)adapter.get("patient/"+nss);
        if (jObj.isEmpty()) {
            return false;
        }
        
        jObj.put("doctorUsername", usrn);        
        return adapter.put("patient", jObj.toJSONString());
    }
}
