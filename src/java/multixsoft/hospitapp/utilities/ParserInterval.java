package multixsoft.hospitapp.utilities;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ParserInterval{


	public List<Integer> removeUnavailableScheduleIntervals(String scheduleInterval, 
		String appointmentInterval, int day){
		
		String weekScheduleInterval = extractIntervalsWithDate(scheduleInterval, day);
		int[] hourSchedule = extractHoursInterval(weekScheduleInterval);
		int[] hourAppointment = extractHoursInterval(appointmentInterval);
		
		List<Integer> listSchedule = convertHourArray(hourSchedule);
		List<Integer> listAppointment = convertHourArray(hourAppointment);
		listSchedule.removeAll(listAppointment);
		return listSchedule;
	}


	private String[] extractIntervals(String intervalo){
		String[] daysIntervals = intervalo.split(";");
		return daysIntervals;
	}

	private String extractIntervalsWithDate(String intervalo, int day){
		String[] dayIntervals = extractIntervals(intervalo);
		int weekDay = day-2;
		return dayIntervals[weekDay];
	}

	private List<Integer> convertHourArray(int[] interval){
		List<Integer> intList = new ArrayList<Integer>();
		for (int index = 0; index < interval.length; index++){
	        intList.add(interval[index]);
	    }
		return intList;
	}

	private int[] extractHoursInterval(String interval){
		String[] hourCharInterval = interval.split(",");
		int[] hourInterval = new int[hourCharInterval.length];
		
		for(int i=0; i<hourCharInterval.length; i++){
			hourInterval[i]=Integer.parseInt(hourCharInterval[i]);
		}
		return hourInterval;
	}
}