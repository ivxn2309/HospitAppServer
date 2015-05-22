package multixsoft.hospitapp.management;

import java.util.Calendar;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import multixsoft.hospitapp.utilities.Date;
import multixsoft.hospitapp.utilities.IntervalFilter;
import multixsoft.hospitapp.webservice.AdapterRest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * REST Web Service
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
@Path("schedulemanager")
public class ScheduleManager {

    private AdapterRest adapter;
    private final String APPOINTMENT_PATH="appointment/";
    private final String PATIENT_PATH="patient/";
    private final String DOCTOR_PATH="doctor/";
    private final String DOCTOR_UNFINISHED_APPS="doctor/unfinishedappointments?username=";
    private final String PATIENT_UNIFINISHED_APPS="patient/unfinishedappointments?nss=";
    private final String DOCTOR_SCHEDULE_PATH="doctor/doctorschedule?username=";

    @Context
    private UriInfo context;

    public ScheduleManager() {
        adapter = new AdapterRest();
    }

    @GET
    @Path("/appointmentsfor")
    @Produces("application/json")
    public String getAllAppointmentsFor(
            @QueryParam("username") String usrn, @QueryParam("date") String date) {
        AdapterRest adapter = new AdapterRest();
        String path = "appointment/appointmentsfor?username=" + usrn + "&date=" + date;
        JSONArray array = (JSONArray)adapter.get(path);
        if (array.isEmpty()) {
            return null;
        }
        return array.toJSONString();
    }

    @GET
    @Path("/cancelappointment")
    @Produces("text/plain")
    public boolean cancelAppointment(@QueryParam("idAppointment") String id){
        JSONObject appointment = getAppointmentFromId(id);
         if(appointment.isEmpty()){
            return false;
        }
        appointment.put("iscanceled", true);
        return adapter.put("appointment/" + appointment.get("idAppointment"), appointment.toJSONString());
    }
    
    @GET
    @Path("/finishappointment")
    @Produces("text/plain")
    public boolean setAppointmentFinish(@QueryParam("idAppointment") String id){
        JSONObject appointment = getAppointmentFromId(id);
        if(appointment.isEmpty()){
            return false;
        }
        appointment.put("isFinished", true);
        return adapter.put("appointment/" + appointment.get("idAppointment"), appointment.toJSONString());
    }
    
    @GET
    @Path("/scheduleappointment")
    @Produces("text/plain")
    public long scheduleAppointment(@QueryParam("Appointment") String appointment){
        JSONObject appointmentToSchedule = (JSONObject) JSONValue.parse(appointment);
        if(isAppointmentValid(appointmentToSchedule)) {
            return -1;
        }
        else {
            adapter.post("appointment", appointmentToSchedule.toJSONString());
            return (Long) appointmentToSchedule.get("idAppointment");
        }
    }
             
    @GET
    @Path("/nextappointment")
    @Produces("application/json")
    public String getNextAppointment(@QueryParam("nss") String nss){
        JSONObject patient = getPatientFromNss(nss);
        JSONArray patientAppointments = getPatientAppointments(nss);
        
        if(patientAppointments.isEmpty()){
            return null;
        }
        JSONObject nextAppointment = compareAppointmentsDate(patientAppointments);
        return nextAppointment.toJSONString();
    }
    
    
    @GET
    @Path("/availableschedule")
    @Produces("application/json")
    public String getAvailableSchedule(@QueryParam("username") String usr, @QueryParam("original") boolean original){
        JSONObject doctor = getDoctorFromUsername(usr);
        if(doctor.isEmpty()){
            return null;
        }
        
        JSONObject doctorSchedule = getDoctorSchedule(usr);
        if(original){
            return doctorSchedule.toJSONString();
        }

        JSONArray doctorAppointments = getDoctorAppointments(usr);
  
        IntervalFilter intervalFilter = new IntervalFilter();
        
        for(Object appointment : doctorAppointments){
            Date appDate = getAppointmentDate((JSONObject)appointment);
            
            if(appDate.belongsThisWeek()){
                int day = appDate.getDayOfWeek();
                int appointmentTime = Integer.parseInt((String) ((JSONObject)appointment).get("time"));
                String scheduleTime = scheduleIntervalByDay(doctorSchedule.get("idSchedule").toString(), day);
                String newInterval = intervalFilter.removeInterval(appointmentTime, scheduleTime);
                putScheduleByDay(day, newInterval, doctorSchedule);
            }
        }
        return doctorSchedule.toJSONString();
    }
    
    
    private JSONObject compareAppointmentsDate(JSONArray appointments){
        JSONObject nextAppointment = (JSONObject) appointments.get(0);
        JSONObject actualApp;
        
        for(Object app: appointments){
            actualApp = (JSONObject) app;
            Date appDate = getAppointmentDate(actualApp);
            Date nextAppDate = getAppointmentDate(nextAppointment);
            
           if(appDate.isBefore(nextAppDate)){
               nextAppointment = actualApp;
           }
        }
        return nextAppointment;
    }
    
    private boolean putScheduleByDay(int day, String interval, JSONObject doctorSchedule){
        if (day == 2) {
            doctorSchedule.put("monday", interval);
        } else if (day == 3) {
            doctorSchedule.put("tuesday", interval);
        } else if (day == 4) {
            doctorSchedule.put("wednesday", interval);
        } else if (day == 5) {
            doctorSchedule.put("thursday", interval);
        } else if (day == 6) {
            doctorSchedule.put("friday", interval);
        }else{ 
            return false;
        }
        return true;
    }
    
    private String scheduleIntervalByDay(String idSchedule,  int day){
        JSONObject schedule = getSchedule(idSchedule);
     
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
    
    private boolean isAppointmentValid(JSONObject appointment){
        String idAppointment = appointment.get("idAppointment").toString();
        boolean appointmentAlreadyExists = comparePatientAndDate(idAppointment);
        if(!patientDoctorExists(appointment)) {
            return false;
        }
        return appointmentAlreadyExists;
    }
    
    private boolean patientDoctorExists(JSONObject appointment){
        /*
        String doctorUsername = appointment.get("doctorUsername").toString();
        JSONObject doctor = getDoctorFromUsername(doctorUsername);
        String patientNss = appointment.get("patientNss").toString();
        JSONObject patient = getPatientFromNss(patientNss);
        */
        JSONObject doctor = (JSONObject)appointment.get("doctorUsername");
        JSONObject patient = (JSONObject)appointment.get("patientNss");
        return !(doctor.isEmpty() || patient.isEmpty());
    }
    
    private JSONObject getAppointmentFromId(String id){
        return (JSONObject) adapter.get(APPOINTMENT_PATH+id);
    }
    
    private Date getAppointmentDate(JSONObject appointment){
        Calendar calendar = Calendar.getInstance();
        String [] strDate = ((String)appointment.get("date")).split("-");
        int year = Integer.parseInt(strDate[0]);
        int mes = Integer.parseInt(strDate[1]);
        int dia = Integer.parseInt(strDate[2].substring(0, 2));
        return new Date(dia, mes, year);
    }
    
    private JSONObject getPatientFromNss(String nss){
        return (JSONObject) adapter.get(PATIENT_PATH+nss);
    }
    
    private JSONObject getDoctorFromUsername(String usr){
        return (JSONObject) adapter.get(DOCTOR_PATH+usr);
    }
    
    private JSONArray getPatientAppointments(String nss){
        return (JSONArray) adapter.get(PATIENT_UNIFINISHED_APPS+nss);
    }
    
    private JSONObject getDoctorSchedule(String usr){
        return (JSONObject) adapter.get(DOCTOR_SCHEDULE_PATH+usr);
    }
    
    private JSONObject getSchedule(String idSchedule){
        return (JSONObject) adapter.get("schedule/"+idSchedule);
    }
    
    private JSONArray getDoctorAppointments(String usr){
        return (JSONArray) adapter.get(DOCTOR_UNFINISHED_APPS+usr);
    }
    
    
     /* compara si dos appointments tienen el mismo paciente y fecha */
    private boolean comparePatientAndDate(String id){
       JSONObject appointment =  getAppointmentFromId(id);
       if(appointment == null) {
           return false;
       }
       JSONArray appointments = (JSONArray)adapter.get("appointment");
       for (Object app: appointments ){
            if(((JSONObject)app).get("patientNss").equals(appointment.get("patientNss")) 
                     && ((JSONObject)app).get("date").equals(appointment.get("date"))){
                 return true;
            }
        }
        return false;
    }
}
