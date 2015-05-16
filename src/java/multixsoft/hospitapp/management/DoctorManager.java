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
import org.json.simple.JSONObject;

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
     * POST para crear una instancia de Doctor Manager
     * @param doc corresponde a la representacion del Doctor Manager que se creará
     * @return un String que contiene el nombre de usuario del doctor que se ha creado
    **/
    @POST
    @Path("/savenewdoctor")
    @Consumes ("aplication/json")
    public String postSaveNewDoctor(
        @QueryParam("doctor") String doc){
        AdapterRest adapter = new AdapterRest();
        try{
        	JSONObject doctorObject = (JSONObject)new JSONParser().parse(doc);
        	String path = "doctor/id=" + doctorObject.get("username");
        	JSONObject doctorRequest = (JSONObject) adapter.get(path);
        	if(doctorRequest.isEmpty()){
            	path = "doctor/create";
            	adapter.post(path,doctorObject.toJSONString());
            	return doctorObject.get("username");
        	}else{
            	return null;
        	}
    	}catch(ParseException io){
    		return null;
    	}
    }
    
    /**
    * POST metodo para crear una instancia de Schedule Manager
    * @param sched es la representacion del Schedule Manager que se creará
    * @return Un String que contiene el id del Schedule Manager que se creó
    **/
    @POST
    @Path("/setschedule")
    @consumes("application/json")
    public String putSetSchedule(
        @QueryParam("schedule") String sched){
        AdapterRest adapter = new AdapterRest();
        try{
        	JSONObject scheduleObject = new JSONObject(sched);
        	String path = "multixsoft.hospitapp.entities.schedule/id=" + scheduleObject.get("idSchedule");
        	JSONObject scheduleRequest = (JSONObject) adapter.get(path);
        	if(scheduleRequest.isEmpty()){
            	path = "multixsoft.hospitapp.entities.schedule/create";
            	adapter.put(path, scheduleRequest.toJSONString());
            	return scheduleObject.get("idSchedule");
        	}else{
            	return null;
        	}
        }catch(ParseException e){
        	return null;
        }
    }
}
