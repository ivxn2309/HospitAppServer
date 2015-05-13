package multixsoft.hospitapp.management;

import java.util.List;
import multixsoft.hospitapp.entities.Appointment;
import multixsoft.hospitapp.entities.Doctor;
import multixsoft.hospitapp.utilities.Date;
import multixsoft.hospitapp.webservice.DoctorFacadeREST;

/**
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
public class ScheduleManager {
    public List<Appointment> getAllAppointmentsFor(Doctor doctor, Date date) {
        DoctorFacadeREST docFacade = new DoctorFacadeREST();
        Doctor dr = docFacade.find(doctor.getUsername());
        if(dr == null) {
            return null;
        }
        return docFacade.getAllApointmentsFor(doctor, date);
    }
}
