package multixsoft.hospitapp.management;

import java.util.ArrayList;
import java.util.List;
import multixsoft.hospitapp.entities.Appointment;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.entities.Patient;
import multixsoft.hospitapp.entities.Schedule;
import multixsoft.hospitapp.webservice.AppointmentFacadeREST;
import multixsoft.hospitapp.webservice.DoctorFacadeREST;
import multixsoft.hospitapp.webservice.PatientFacadeREST;
import multixsoft.hospitapp.webservice.ScheduleFacadeREST;

/**
 *
 * @author maritza
 */
public class ScheduleManager {

    AppointmentFacadeREST appointmentManagement;
    PatientFacadeREST patientManagement;
    DoctorFacadeREST doctorManagement;
    
    public ScheduleManager(){
        appointmentManagement = new AppointmentFacadeREST();
        patientManagement = new PatientFacadeREST();
        doctorManagement = new DoctorFacadeREST();
    }
    
    /* Función para cancelar citas */
    public boolean cancelAppointment(Appointment appointment){
        try{
            appointment = appointmentManagement.find(appointment.getIdAppointment()); 
            appointment.setIscanceled(true);
            appointmentManagement.edit(appointment.getIdAppointment(), appointment);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    

    public long scheduleAppointment(Appointment appointment){
        appointment = appointmentManagement.find(appointment.getIdAppointment());
        Patient patient = patientManagement.find(appointment.getPatientNss());
        Doctor doctor = doctorManagement.find(appointment.getDoctorUsername());

        Date date = new Date();
        if(date.isBefore(appointment.getDate()) || patient == null || doctor == null 
            || compareAppointmentByPatientAndDate(appointment)){
            return -1;
        }else{
            appointmentManagement.create(appointment);
            return appointment.getIdAppointment();
        }
    }


    public boolean setAppointmentFinish(Appointment appointment){
        boolean isCompleteOperation=false;

        try{
            appointmet = appointmentManagement.find(appointment.getIdAppointment());
            appointment.setIsFinished(true);
            appointmentManagement.edit(appointment.getIdAppointment(), appointment);
            isCompleteOperation = true;
        }catch(Exception e){
            
        }
        return isCompleteOperation;
    }


    /* Conseguir todas las citas para un paciente dado */
    public Appointment getNextAppointment(Patient patient){
        patient = new PatientFacadeREST.find(patient.getPatientNss());
        
        List<Appointment> patientAppointments = 
        patientManagement.getUnfinishedAppointments(patient);

        if(patientAppointments == null || patientAppointments.isEmpty()){
            return null;
        }
        Appointment nextAppointment = patientAppointments.get(0);

        for(Appointment appointment : patientAppointments){
            if(appointment.getDate().isBefore(nextAppointment.getDate())){
                nextAppointment = appointment;
            }
        }
        return nextAppointment;
    }


    public Schedule getAvailableSchedule(Doctor doctor, boolean original){
        doctor = new DoctorFacadeREST.find(doctor.getUsername());
        if(doctor == null){
            return null;
        }

        Schedule doctorSchedule = doctorManagement.getDoctorSchedule();

        if(original){
            return doctorSchedule;
        }

        List<Appointment> doctorAppointments = 
        doctorManagement.getUnfinishedAppointments(doctor);

        IntervalFilter intervalFilter = new IntervalFilter();
        for(Appointment appointment : doctorAppointments){
            if(appointment.getDate().belongsThisWeek()){
                
                int day = appointment.getDate().getDayOfWeek();
                int appointmentTime = Integer.parseInt(appointment.getTime());
                String scheduleTime = scheduleIntervalByDay(doctorSchedule, day);
                intervalFilter.removeInterval(appointmentTime, scheduleTime);
                
            }
        }
    }

    private String scheduleIntervalByDay(Schedule schedule, int day){
        if(day == 2){
            return schedule.getMonday();
        }else if(day == 3){
            return schedule.getTuesday();
        }else if(day == 4){
            return schedule.getWednesday();
        }else if(day == 5){
            return schedule.getThursday();
        }else if(day == 6){
            return schedule.getFriday();
        }else{
            return -1;
        }
    }

    /* compara si dos appointments tienen el mismo paciente y fecha */
    private boolean compareAppointmentByPatientAndDate(Appointment appointment){
         List<Appointment> appointments = appointmentManagement.findAll();
         for (Appointment app: appointments ){
            if(app.getPatientNss() == appointment.getPatientNss() && app.getDate() == appointment.getDate()){
                return true;
            }
         }
         return false;
    }
}
