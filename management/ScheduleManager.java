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
    

    /* Función para calendarizar cita */
    public long scheduleAppointment(Appointment appointment){
        appointment = appointmentManagement.find(appointment.getIdAppointment());
        
        Patient patient = patientManagement.find(appointment.getPatientNss());
        Doctor doctor = doctorManagement.find(appointment.getDoctorUsername());

        //se incializa con la fecha actual
        Date date = new Date();
        if(date.isBefore(appointment.getDate()) || patient == null || doctor == null 
            || compareAppointmentByPatientAndDate(appointment)){
            return -1;
        }else{
            appointmentManagement.create(appointment);
            return appointment.getIdAppointment();
        }
    }


    /* Función para marcar una cita como concluida */
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

    /* Obtener todas las citas para un doctor y fecha en especifico */

    public List<Appointment> getAllAppointmentsFor(Doctor doctor, Date date){
        doctor = doctorManagement.find(doctor.getUsername());

        if(doctor == null){
            return null;
        }

        List<Appointment> currentAppointments = appointmentManagement.findAll();
        List<Appointment> doctorAppointments = new ArrayList<Appointment>();

        for(Appointment appointment : currentAppointments){
            /* si tienen el mismo doctor y fecha agregarlas a la lista */
            if(appointment.getDoctorUsername() == doctor.getUsername() && date.equals(appointment.getDate()) && appointment.setIsFinished(false)){
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }




    /* Conseguir todas las citas para un paciente dado */
    public Appointment getNextAppointment(Patient patient){
        patient = new PatientFacadeREST.find(patient.getPatientNss());

        List<Appointment> currentAppointments = appointmentManagement.findAll();
        List<Appointment> patientAppointments = new ArrayList<Appointment>();

        for(Appointment appointment : currentAppointments){
            if(appointment.getPatientNss() == patient.getNss() && appointment.getIsFinished() == false){
                patientAppointments.add(appointment);
            }
        }

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

        List<Schedule> actualSchedules = new ScheduleFacadeREST().findAll();
        Schedule doctorSchedule = null;

        for(Schedule schedule : actualSchedules){
            if(schedule.getDoctorUsername().getUsername() == doctor.getUsername()){
                doctorSchedule = schedule;
            }
        }

        if(original){
            return doctorSchedule;
        }

        List<Appointment> actualAppointments = appointmentManagement.findAll();
        List<Appointment> doctorAppointments = new ArrayList<Appointment>();

        for(Appointment appointment : actualAppointments){
            if(appointment.getDoctorUsername().getUsername() == doctor.getUsername() && appointment.getIsFinished() == false){
                doctorAppointments.add(appointment);
            }
        }

        for(Appointment appointment : doctorAppointments){
            if(appointment.getDate().belongsThisWeek()){
                /* remover intervalo de tiempo de appointment a Schedule de acuerdo al día  */
            }
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
