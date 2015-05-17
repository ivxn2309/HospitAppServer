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
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.utilities.Date;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import multixsoft.hospitapp.utilities.*;

/**
 * REST Web Service
 *
 * @author maritza
 */
@Path("schedulemanager")
public class ScheduleManager {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ScheduleManager
     */
    public ScheduleManager() {
    }

    @PUT
    @Path("/cancelappointment")
    @Produces("application/json")
    public boolean cancelAppointment(@QueryParam("idAppointment") String id){
        AdapterRest adapter = new AdapterRest();
        String path="appointment/"+id;
        JSONObject appointment = (JSONObject) adapter.get(path);
        
        if(appointment.isEmpty()){
            return false;
        }
        
        appointment.put("iscanceled", true);
        return adapter.put("appointment",appointment.toJSONString());
    }
    
    @POST
    @Path("/scheduleappointment")
    @Produces("application/json")
    public long scheduleAppointment(@QueryParam("Appointment") String appointment){
        AdapterRest adapter = new AdapterRest();
        JSONObject appointmentToSchedule = (JSONObject) JSONValue.parse(appointment);
        
        String doctorPath = "doctor/"+appointmentToSchedule.get("username");
        JSONObject doctor = (JSONObject) adapter.get(doctorPath);
        
        String patientPath = "patient/"+appointmentToSchedule.get("nss");
        JSONObject patient = (JSONObject) adapter.get(patientPath);
        
        Date actualDate = new Date();
       
        Date appointmentDate = (Date)appointmentToSchedule.get("date");
        boolean appointmentAlreadyExists = 
                comparePatientAndDate(appointmentToSchedule.get("idAppointment").toString());
        
        if(doctor.isEmpty() || patient.isEmpty() || actualDate.isBefore(appointmentDate)
                || appointmentAlreadyExists ){
            return -1;
            
        }else{
            
            adapter.put("appointment/", appointmentToSchedule.toJSONString());
            return (Long)appointmentToSchedule.get("idAppointment");
        }
        
    }
    
    @GET
    
     /* compara si dos appointments tienen el mismo paciente y fecha */
    private boolean comparePatientAndDate(@QueryParam("idAppointment") String id){
         AdapterRest adapter = new AdapterRest();
         JSONObject appointment =  (JSONObject) adapter.get("appointment/"+id);
         JSONArray appointments = (JSONArray)adapter.get("appointment");
         
         for (Object app: appointments ){
             if(((JSONObject)app).get("nss") == appointment.get("nss") 
                     && appointment.get("date") == appointment.get("date")){
                 return true;
             }
         }
         return false;
    }
             
    @PUT
    @Path("/finishappointment")
    @Produces("application/json")
    public boolean setAppointmentFinish( @QueryParam("idAppointment") String id){
        AdapterRest adapter = new AdapterRest();
        String path="appointment/"+id;
        JSONObject appointment = (JSONObject) adapter.get(path);
        
        if(appointment.isEmpty()){
            return false;
        }
        
        appointment.put("isFinished", true);
        return adapter.put("appointment", appointment.toJSONString());
       
    }
    
    @GET
    @Path("/nextappointment")
    @Produces("application/json")
    public String getNextAppointment(@QueryParam("nss") String nss){
        AdapterRest adapter = new AdapterRest();
        String path="patient/"+nss;
        JSONObject patient = (JSONObject) adapter.get(path);
        
        String pathApps="patient/unfinishedappointments?nss="+nss;
        JSONArray patientAppointments = (JSONArray) adapter.get(pathApps);
        
        if(patientAppointments.isEmpty()){
            return null;
        }
        
        JSONObject nextAppointment = (JSONObject) patientAppointments.get(0);
        JSONObject appointment;
   
        for(Object app: patientAppointments){
           if(((JSONObject)app).get("date").isBefore(((JSONObject)nextAppointment).get("date"))){
               nextAppointment = (JSONObject)app;
           }
           
        }
        return nextAppointment.toJSONString();
    }
    
    
    @GET
    @Path("/availableschedule")
    @Produces("application/json")
    public String getAvailableSchedule(@QueryParam("username") String usr, boolean original){
        AdapterRest adapter = new AdapterRest();
        JSONObject doctor = (JSONObject) adapter.get("doctor/"+usr);
        
        if(doctor.isEmpty()){
            return null;
        }
        JSONObject doctorSchedule = (JSONObject) adapter.get("doctor/doctorschedule?username="+usr);
        
        if(original){
            return doctorSchedule.toJSONString();
        }

        JSONArray doctorAppointments = (JSONArray) adapter.get("doctor/unfinishedappointments?username="+usr);
  
        IntervalFilter intervalFilter = new IntervalFilter();
        
        for(Object appointment : doctorAppointments){
            Date appDate = (Date) ((JSONObject)appointment).get("date");
            if(appDate.belongsThisWeek()){
                int day = appDate.getDayOfWeek();
                int appointmentTime = (Integer) ((JSONObject)appointment).get("time");
                String scheduleTime = scheduleIntervalByDay(doctorSchedule.get("idSchedule").toString(), day);
                //obtenemos una cadena con los intervalos para un día dado
                String newInterval = intervalFilter.removeInterval(appointmentTime, scheduleTime);
                
                if(day == 2){
                    doctorSchedule.put("monday", newInterval);
                }else if(day==3){
                    doctorSchedule.put("tuesday", newInterval);
                }else if(day==4){
                    doctorSchedule.put("wednesday", newInterval);
                }else if(day==5){
                    doctorSchedule.put("thursday", newInterval);
                }else if(day==6){
                    doctorSchedule.put("friday", newInterval);
                }
            }
        }
        adapter.put("schedule", doctorSchedule.toJSONString());
        return doctorSchedule.toJSONString();
    }
    
    private String scheduleIntervalByDay(@QueryParam("idSchedule") String idSchedule, int day){
        AdapterRest adapter = new AdapterRest();
        JSONObject schedule = (JSONObject) adapter.get("schedule/"+idSchedule);
     
        if(day == 2){
            return schedule.get("monday").toString();
        }else if(day == 3){
            return schedule.get("tuesday").toString();
        }else if(day == 4){
            return schedule.get("wednesday").toString();
        }else if(day == 5){
            return schedule.get("thursday").toString();
        }else if(day == 6){
            return schedule.get("Friday").toString();
        }
        return null;
    }

}
