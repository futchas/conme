/**
 * 
 */
package de.htw.conme.client;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * @author Iyad Al-Sahwi
 *
 */
public class ConnectionInfo implements Serializable{

	private static final long serialVersionUID = 7392125719654130170L;
	private String id;
	private DateTime startDate;
	private DateTime endDate;
	private String duration; // hh:mm:ss
	private long startReceivedData;
	private long startTransmittedData;
	private long startTotalDataUsage;
	private long receivedData;
	private long transmittedData;
	private long totalDataUsage;
	private String device;
	
	public ConnectionInfo(String id, String device, long startTransmittedData, long startReceivedData) {
		this.id = id;
		this.device = device;
		this.startDate = new DateTime();
		this.startTransmittedData = startTransmittedData;
		this.startReceivedData = startReceivedData;
		this.transmittedData = startTransmittedData;
		this.receivedData = startReceivedData;
		this.startTotalDataUsage = startTransmittedData + startReceivedData;
		this.totalDataUsage= startTotalDataUsage; 
	}

	public String getId() {
		return id;
	}


	public String getDevice() {
		return device;
	}
	
	private void setDuration(DateTime endDate) {
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
		} else
			this.duration = "00:00:00";
	}

	public String getDuration() {
		return duration;
	}


	public DateTime getStartDate() {
		return startDate;
	}


	public DateTime getEndDate() {
		return endDate;
	}
	
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
		setDuration(endDate);
	}

//	public void setStartDate(DateTime startDate) {
//		this.startDate = startDate;
//	}

	public long getTotalDataUsage() {
		return totalDataUsage - startTotalDataUsage;
	}

	public long getTotalDataUsageInMB() {
		return getTotalDataUsage() / 1024 / 1024;
	}

	public void setTotalDataUsage(long transmitted, long received) {
		this.transmittedData = transmitted;
		this.receivedData = received;
		this.totalDataUsage = received + transmitted;
	}


	@Override
	public String toString() {
		StringBuilder message = new StringBuilder("ID: " + id + ", startDate: " + startDate);
		if(endDate != null)
			message.append(", endDate: " + endDate);
		if(startTotalDataUsage != 0)
			message.append(", Start Data Usage: " + startTotalDataUsage);
		if(totalDataUsage != 0)
			message.append(", End Data Usage: " + totalDataUsage);
		
		return message.toString();
	}
	
	

}
