/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multixsoft.hospitapp.management;

import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 *
 * @author manuelmartinez
 */
@Path("patientmanager")
public class PatientManager{
    
    @Context
    private UriInfo context;

    public PatientManager() {
    }
    
    @GET
    @Path("/savepatient")
    @Produces("text/plain")
    public String saveNewPatient( @QueryParam("patient") String patient) {
        AdapterRest adapter = new AdapterRest();
        try{
            JSONObject patientObj = (JSONObject) new JSONParser().parse(patient);
            String path = "patient/id="+patientObj.get("nss");
            JSONObject doctorRequest = (JSONObject) adapter.get(path);
            if(doctorRequest.isEmpty()){
                path = "patien/create";
                adapter.post(path, patientObj.toJSONString());
                return patientObj.get("nss");
            }else{
                return null;
            }
        }catch(ParseException e){
            return null;
        }
    }

}
