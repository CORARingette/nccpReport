package com.coach.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Module {
	
	String moduleRequirement;
	String moduleName;
	String points;
	public String getModuleRequirement() {
		return moduleRequirement;
	}
	public void setModuleRequirement(String moduleRequirement) {
		this.moduleRequirement = moduleRequirement;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}



}
