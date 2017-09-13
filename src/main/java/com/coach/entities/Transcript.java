package com.coach.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transcript {
	Achievement [] achievements;

	public Achievement[] getAchievements() {
		return achievements;
	}

	public void setAchievements(Achievement[] achievements) {
		this.achievements = achievements;
	}


}
