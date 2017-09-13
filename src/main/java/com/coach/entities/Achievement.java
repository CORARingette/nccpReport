package com.coach.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Achievement {

	String date;
	Module[] modules;

	private Date dateValue;

	public Date getDateValue() {
		return dateValue;
	}

	private static SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
		try {
			dateValue = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Module[] getModules() {
		return modules;
	}

	public void setModules(Module[] modules) {
		this.modules = modules;
	}

}
