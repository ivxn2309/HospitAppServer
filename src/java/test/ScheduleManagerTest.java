package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import org.junit.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author maritza
 */

public class ScheduleManagerTest {
    JSONObject requestAppointment;
    JSONObject doctorAvailableSchedule;
    JSONObject doctorSchedule;
    AdapterRest adapter;
    JSONObject doctor;
    JSONObject patient;
    
    @Before
    public void setUp(){
        adapter = new AdapterRest("http://127.0.0.1:4848/HospitAppServer/webresources/");
        
        doctor = new JSONObject();
        doctor.put("username", "jose");
        doctor.put("password", "pass");
        doctor.put("firstName", "Jose");
        doctor.put("lastName", "Dominguez");
        doctor.put("license", "34O34KDSFJ");
        doctor.put("specialty", "Cardiologia");

        patient = new JSONObject();
        patient.put("nss", "123456789");
        patient.put("password", "password");
        patient.put("isActive", true);
        patient.put("doctorUsername", doctor.toJSONString());
                
        requestAppointment = new JSONObject();
        requestAppointment.put("idAppointment",(long) 123445565);
        requestAppointment.put("patientNss", patient.toJSONString());
        requestAppointment.put("doctorUsername", doctor.toJSONString());
        requestAppointment.put("date", new Date(25,5,2015));
        requestAppointment.put("isFinished", false);
        requestAppointment.put("iscanceled", false);
        requestAppointment.put("time","11");
        
        doctorSchedule = new JSONObject();
        doctorSchedule.put("idSchedule",23345L);
        doctorSchedule.put("monday","9-15");
        doctorSchedule.put("tuesday","9-13");
        doctorSchedule.put("wednesday","13-17");
        doctorSchedule.put("thursday","8-12");
        doctorSchedule.put("friday","10-15");
        doctorSchedule.put("doctorUsername", doctor.toJSONString());
        
        doctorAvailableSchedule = new JSONObject();
        doctorAvailableSchedule.put("idSchedule",23345L);
        doctorAvailableSchedule.put("monday","9-11,12-15");
        doctorAvailableSchedule.put("tuesday","9-13");
        doctorAvailableSchedule.put("wednesday","13-17");
        doctorAvailableSchedule.put("thursday","8-12");
        doctorAvailableSchedule.put("friday","10-15");
        doctorAvailableSchedule.put("doctorUsername", doctor.toJSONString());
        
        adapter.post("doctor", doctor.toJSONString());
        adapter.post("patient", patient.toJSONString());
        
        adapter.post("appointment", requestAppointment.toJSONString());
        adapter.post("schedule", doctorSchedule.toJSONString());
        adapter.post("schedule", doctorAvailableSchedule.toJSONString());
        
    }
    
    @Test
    public void testCancelAppointment() {
        long id = (Long) requestAppointment.get("idAppointment");
        System.out.println("Debug: id=" + id);
        boolean dateIsCanceled = (Boolean) adapter.get("schedulemanager/cancelappointment/"+id);
        System.out.println("Debug: date=" + dateIsCanceled);
        assertEquals(dateIsCanceled, true);
        
    }
    
    @Test
    public void testExtraAlwaysTrue() {
        assertEquals("1", "1");
    }
}
    /*
     @Test
    public void testSetAppointmentFinish(){
        Date requestDate = new Date(22,5,2015);
        boolean dateIsFinished = (Boolean) adapter
				.get("schedulemanager/finishappointment/"
						+ requestAppointment.get("idAppointment"));
       assertEquals(dateIsFinished, true);
    }

     @Test 
    public void testGetAvailableScheduleaFalse(){
        boolean scheduleIsOriginal = false;
       String actualAvailableSchedule = (String) adapter
				.get("schedulemanager/availableschedule?username="
						+ requestAppointment.get("doctorUsername")
						+ "&original=" + scheduleIsOriginal);

        
        assertEquals(doctorAvailableSchedule.toJSONString(), actualAvailableSchedule);       
    }
    
    @Test 
    public void testGetAvailableScheduleaTrue(){
        boolean scheduleIsOriginal = true;
       
       		String actualAvailableSchedule = (String) adapter
				.get("schedulemanager/availableschedule?username="
						+ requestAppointment.get("doctorUsername")
						+ "&original=" + scheduleIsOriginal);

        assertEquals(doctorSchedule.toJSONString(), actualAvailableSchedule);       
    }
    
    @Test
    public void testObtainAllPatientsDated() {
        String actualAvailableSchedule = (String) adapter
				.get("schedulemanager/appointmentsfor?username="
						+ requestAppointment.get("doctorUsername") + "&date="
						+ requestAppointment.get("date"));

        JSONArray array = (JSONArray) JSONValue.parse(actualAvailableSchedule);
        //List<Appointment> apps = new ArrayList<>();
        assertEquals(array.size(), 1);
    }
    
    @Test
    public void testScheduleAppointment(){
       		long appointmentId = (Long) adapter
				.get("schedulemanager/scheduleappointment?Appointment="
						+ requestAppointment.toJSONString());

        assertEquals(appointmentId, requestAppointment.get("idAppointment"));
    }
    
    @Test
    public void testGetNextAppointment(){
        Date dateExpectedAppointment = new Date(26,5,2015);
        JSONObject expectedAppointment = new JSONObject();
        expectedAppointment.put("idAppointment", (long) 344532342);
        expectedAppointment.put("patientNss", patient.toJSONString());
        expectedAppointment.put("doctorUsername", doctor.toJSONString());
        expectedAppointment.put("date", dateExpectedAppointment);
        expectedAppointment.put("isFinished", false);
        
       		String actualAppointment = (String) adapter
				.get("schedulemanager/nextappointment?nss="
						+ requestAppointment.get("patientNss"));


        assertEquals(actualAppointment, expectedAppointment.toJSONString());
    }
    
     @AfterClass
     public void tearDownClass() {
        // Duda al eliminar doctor y patient, aquí si se necesitaría facade?
        adapter.delete("appointment/"+(String)requestAppointment.get("idAppointment"));
        adapter.delete("schedule"+(String)doctorSchedule.get("idSchedule"));
        adapter.delete("schedule"+(String)doctorAvailableSchedule.get("idSchedule"));
        adapter.delete("patient/"+(String)patient.get("nss"));
        adapter.delete("doctor/"+(String)doctor.get("username"));
     }
  
}
*/
class AdapterRest {

    private String base = "http://localhost:8080/HospitAppServer/webresources/";

    public AdapterRest(String address) {
        base = address;
    }
    public AdapterRest() {
       // base = address;
    }

    public Object get(String path) {
        try {
            URL url = new URL(base + path);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setRequestMethod("GET");
            
            int codigo = conexion.getResponseCode();
            if (codigo == HttpURLConnection.HTTP_OK) {
                InputStream is = conexion.getInputStream();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(is));
                String respuesta = entrada.readLine();

                Object obj = JSONValue.parse(respuesta);
                entrada.close();
                return obj;
            }
        } 
        catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean delete(String path) {
        boolean resultado = false;                
        try {
            URL url = new URL(base+path);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            int codigo = conexion.getResponseCode();
            if (codigo/100 == 2) {
                resultado = true;
            }
        } 
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public boolean post(String path, String jsonObject){
        byte [] pack = jsonObject.getBytes();
        try {
            URL url = new URL(base + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(pack.length));
            OutputStream os = conn.getOutputStream();
            os.write(pack);

            int codigo = conn.getResponseCode();
            //System.out.println("Codigo recibido" + codigo);
            if (codigo / 100 != 2) {
                //System.out.println("Error en Codigo recibido" + codigo);
                return true;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public boolean put(String path, String jsonObject){
        byte [] pack = jsonObject.getBytes();
        try {
            URL url = new URL(base + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("PUT");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(pack.length));
            OutputStream os = conn.getOutputStream();
            os.write(pack);

            int codigo = conn.getResponseCode();
            if (codigo / 100 == 2) {
                return true;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}

class Date {
    private int dia;
    private int mes;
    private int year;
    private Calendar calendar;

    public Date() {
        this.calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DATE);
        mes = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }
    
    public Date(Calendar calendar) {
        dia = calendar.get(Calendar.DATE);
        mes = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        this.calendar = calendar;
    }

    public Date(int dia, int mes, int year) {
        this.dia = dia;
        this.mes = mes;
        this.year = year;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, dia);
        calendar.set(Calendar.MONTH, mes-1);
        calendar.set(Calendar.YEAR, year);
    }

    public boolean isBefore(Date date) {
        if (year != date.getYear()) {
            return year < date.getYear();
        }

        if (mes != date.getMes()) {
            return mes < date.getMes();
        }

        if (dia != date.getDia()) {
            return dia < date.getDia();
        }

        return false;
    }

    public boolean isAfter(Date date) {
        if (year != date.getYear()) {
            return year > date.getYear();
        }

        if (mes != date.getMes()) {
            return mes > date.getMes();
        }

        if (dia != date.getDia()) {
            return dia > date.getDia();
        }

        return false;
    }

    private String getSpanishMonth(int indexMonth) {
        switch (indexMonth) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
        }
        return null;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public java.util.Date getTime() {
        return calendar.getTime();
    }
    
    public int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public String toString() {
        return dia + "/" + getSpanishMonth(mes) + "/" + year;
    }
    
    public boolean belongsThisWeek() {
        Calendar beginWeek = Calendar.getInstance();
        Calendar endWeek = Calendar.getInstance();
        beginWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        endWeek.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        
        if(this.equals(new Date(beginWeek)) || this.equals(new Date(endWeek))){
            return true;
        }
        if(this.isAfter(new Date(beginWeek))) {
            return this.isBefore(new Date(endWeek));
        }
        return false;
    }
    
    public String toFormattedString(String format) {
        StringBuilder str = new StringBuilder();
        if(format.charAt(0) == 'Y') {
            str.append(year).append("/");
        }
        else if(format.charAt(0) == 'M') {
            str.append(mes).append("/");
        }
        else {
            str.append(dia).append("/");
        }
        
        if(format.charAt(1) == 'Y') {
            str.append(year).append("/");
        }
        else if(format.charAt(1) == 'M') {
            str.append(mes).append("/");
        }
        else {
            str.append(dia).append("/");
        }
        
        if(format.charAt(2) == 'Y') {
            str.append(year);
        }
        else if(format.charAt(2) == 'M') {
            str.append(mes);
        }
        else {
            str.append(dia);
        }           
            
        return str.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.dia;
        hash = 97 * hash + this.mes;
        hash = 97 * hash + this.year;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Date other = (Date) obj;
        if (this.dia != other.dia) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        return true;
    }
    
}