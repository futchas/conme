/**
 * 
 */
package de.htw.conme.client;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * @author Iyad Al-Sahwi
 *
 */

public class ConnectionInfo implements Serializable {

	private static final long serialVersionUID = 7392125719654130170L;
	private String androidId;
	private DateTime startDate;
	private DateTime endDate;
	private String duration; // hh:mm:ss
	private long startReceivedData, receivedData;
	private long startTransmittedData, transmittedData;
	private long startTotalDataUsage, totalDataUsage;
	private String device;
	
	public ConnectionInfo(String id, String device, long startTransmittedData, long startReceivedData) {
		this.androidId = id;
		this.device = device;
		this.startDate = new DateTime();
		this.duration = "00:00:00";
		this.startTransmittedData = startTransmittedData;
		this.startReceivedData = startReceivedData;
		this.transmittedData = startTransmittedData;
		this.receivedData = startReceivedData;
		this.startTotalDataUsage = startTransmittedData + startReceivedData;
		this.totalDataUsage= startTotalDataUsage; 
	}

	public String getId() {
		return androidId;
	}


	public String getDevice() {
		return device;
	}
	
	
	public String getStartDate() {
		return getStringDate(startDate);
	}

	public String getEndDate() {
		return getStringDate(endDate);
	}
	
	private String getStringDate(DateTime date) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
		return dtf.print(date);
	}
	
	
	private void setDuration() {
		if(startDate != null && endDate != null) {
			Period period = new Period(startDate, endDate);
			
			PeriodFormatter formatter = new PeriodFormatterBuilder()
			.printZeroAlways()
	        .minimumPrintedDigits(2)
	        .appendHours()
	        .appendSeparator(":")
	        .printZeroAlways()
	        .minimumPrintedDigits(2)
	        .appendMinutes()
	        .appendSeparator(":")
	        .printZeroAlways()
	        .minimumPrintedDigits(2)
	        .appendSeconds()
	        .toFormatter();
			 
			this.duration = formatter.print(period);
		}
	}

	public String getDuration() {
		return duration;
	}
	
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
		setDuration();
	}
	
	public long getTotalDataUsage() {
		return totalDataUsage - startTotalDataUsage;
	}

//	public float getTotalDataUsageInMB() {
//		return getTotalDataUsage() / 1024f / 1024f;
//	}
	
	public String getTotalDataUsageInKB() {
		return String.valueOf(getTotalDataUsage() / 1024f);
	}
	
	public String getReceivedData() {
		return String.valueOf((receivedData - startReceivedData)/1024f);
	}

	public String getTransmittedData() {
		return String.valueOf((transmittedData - startTransmittedData)/1024f);
	}

	public void setTotalDataUsage(long transmitted, long received) {
		this.transmittedData = transmitted;
		this.receivedData = received;
		this.totalDataUsage = received + transmitted;
	}


	@Override
	public String toString() {
		StringBuilder message = new StringBuilder("ID: " + androidId + ", startDate: " + startDate + ", Duration: " + duration);
		if(endDate != null)
			message.append(", endDate: " + endDate);
		if(startTotalDataUsage != 0)
			message.append(", Start Data Usage: " + startTotalDataUsage);
		if(totalDataUsage != 0)
			message.append(", End Data Usage: " + totalDataUsage);
		
		return message.toString();
	}
	
	

}
