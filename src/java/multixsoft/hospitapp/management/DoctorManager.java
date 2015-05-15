/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multixsoft.hospitapp.management;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;

/**
 * REST Web Service
 * @author Yonathan Martínez
 * @version 1.0
 * 
 */
@Path("doctormanager")
public class DoctorManager {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DoctorManager
     */
    public DoctorManager() {
    }
    
    /**
     * Retrieves representation of an instance of multixsoft.hospitapp.management.DoctorManager
     * @return an instance of java.lang.String
     *    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of DoctorManager
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    * */
    @PUT
    @PathParam("/savenewdoctor")
    @Produces ("aplication/json")
    public String putSaveNewDoctor(
        @QueryParam("username") String usrname){
        AdapterRest adapter = new AdapterRest();
        String path = "doctor/id=" + usrname;
        JSONArray array = new (JSONArray) adapter.get(path);
        
        
        return array.toJSONString();
    }
}
