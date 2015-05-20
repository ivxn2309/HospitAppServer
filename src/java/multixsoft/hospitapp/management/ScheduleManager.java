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
 * REST Web Service
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
@Path("schedulemanager")
public class ScheduleManager {

    @Context
    private UriInfo context;

    public ScheduleManager() {
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
}
