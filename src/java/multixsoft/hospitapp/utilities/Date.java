package multixsoft.hospitapp.utilities;

import java.util.Calendar;

/**
 * @author Ivan Tovar
 * @version 1.0
 * @date 12/May/2015
 */
public class Date {

    private int dia;
    private int mes;
    private int year;

    public Date(Calendar calendar) {
        dia = calendar.get(Calendar.DATE);
        mes = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    public Date(int dia, int mes, int year) {
        this.dia = dia;
        this.mes = mes;
        this.year = year;
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

    @Override
    public String toString() {
        return dia + "/" + getSpanishMonth(mes) + "/" + year;
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

}
