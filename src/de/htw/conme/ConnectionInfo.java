/**
 * 
 */
package de.htw.conme;

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
	

	public ConnectionInfo(String id) {
		this.id = id;
		this.startDate = new DateTime();
//		this.totalDataUsage = transmittedData + receivedData;
	}
	
	public ConnectionInfo(String id, long startTransmittedData, long startReceivedData) {
		this.id = id;
		this.startDate = new DateTime();
		this.startTransmittedData = startTransmittedData;
		this.startReceivedData = startReceivedData;
		this.startTotalDataUsage = startTransmittedData + startReceivedData;
		
	}


	public String getId() {
		return id;
	}


	public String getDuration() {
		
		endDate = new DateTime();
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
		 
		duration = formatter.print(period);
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
	}


	public long getTotalDataUsage() {
		return totalDataUsage;
	}

	public long getTotalDataUsageInMB() {
		return totalDataUsage / 1024 / 1024;
	}

	public void setTotalDataUsage(long transmitted, long received) {
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
