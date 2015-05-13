/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multixsoft.hospitapp.webservice;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import multixsoft.hospitapp.entities.Patient;

/**
 *
 * @author Ivan Tovar
 */
@Stateless
@Path("multixsoft.hospitapp.entities.patient")
public class PatientFacadeREST extends AbstractFacade<Patient> {
    @PersistenceContext(unitName = "HospitAppServerPU")
    private EntityManager em;

    public PatientFacadeREST() {
        super(Patient.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Patient entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Patient entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Patient find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Patient> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Patient> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    /* Retrieve all the appointments unfinished associated with the patient*/
    public List<Appointment> getUnfinishedAppointments(Patient patient){
        String sql = "SELECT * FROM Appointment WHERE patientNss = :pnss AND"
                + " AND isFinished is false";
        Query query = getEntityManager().createQuery(sql).setParameter("pnss", patient.getNss());

        List<Appointment> appointments = query.getResultList();
        return appointments;
    }

}
