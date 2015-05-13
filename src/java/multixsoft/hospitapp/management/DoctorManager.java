package multixsoft.hospitapp.managment;

/**
 *
 * @author Yonathan Alexander Martínez Padilla
 */
public class DoctorManager {
    
    /**
     * Este metodo permite guardar un nuevo doctor en la base de datos
     * primero verifica que la entidad doctor no se encuentre en la base de datos
     * @param doctor corresponde a la entidad que será almacenada en la base de datos
     * @return username que corresponde al nombre de usuario del doctor que se almaceno
     * o un valor nulo si no se pudo guardar la entidad.
     */
    public String saveNewDoctor(Doctor doctor){
        DoctorFacadeREST dc = new DoctorFacadeREST();
        if(dc.find(doctor.username) =! null){
            return null;
        }
        dc.create(doctor);
        return doctor.username;
    }
    /**
     * Este metodo permite asignar un horario a un doctor, primero verifica que
     * el doctor no tenga ese horario asignado.
     * @param schedule corresponde al horario que se asignara a una entidad de tipo Doctor
     * @return un valor entero que corresponde a -1 si no se pudo completar la
     * operacion o schedule.idSchedule si la operacion se realizo correctamente.
     */
    public int setSchedule(Scheudle schedule){
        ScheduleFacadeREST sf = new ScheduleFacadeREST();
        if(sf.find(schedule.idSchedule) =! null){
            return -1;
        }
        sf.create(schedule);
        return schedule.idSchedule;
    }
}
