package com.coach.certs;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.coach.entities.CoachRecord;
import com.mashape.unirest.http.HttpResponse;

public class CoachInfo implements Comparable<CoachInfo> {
	private String nccpId;
	private String lastName;
	private String firstName;
	private String currentYear;
	private boolean found = false;
	CoachRecord coachRecord;

	public CoachRecord getCoachRecord() {
		return coachRecord;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private Map<String, CourseInfo> courseList = new HashMap<String, CourseInfo>();

	public String getNccpId() {
		return nccpId;
	}

	public void setNccpId(String nccpId) {
		this.nccpId = nccpId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public void addCourse(CourseInfo courseInfo) {
		courseList.put(courseInfo.getName(), courseInfo);
	}

	public void updateFromResponse(HttpResponse<String> jsonResponse) {

		String body = jsonResponse.getBody();

		int index = body.indexOf("model:");
		if (index >= 0) {
			found = true;
			// parts model data from response
			body = body.substring(index + 6);
			index = body.indexOf("};");
			body = body.substring(0, index + 1);
//
//			// get account from JSON
//			JSONObject jsonObj = new JSONObject(body);
			// System.err.println(body);
			ObjectMapper mapper = new ObjectMapper();

			try {
				coachRecord = mapper.readValue(body, CoachRecord.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isQueryInfoComplete() {
		return lastName != null && !lastName.isEmpty() && nccpId != null
				&& !nccpId.isEmpty();
	}

	public boolean isResponseInfoComplete() {
		return lastName != null && !lastName.isEmpty() && nccpId != null
				&& !nccpId.isEmpty() && found;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoachInfo other = (CoachInfo) obj;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public int compareTo(CoachInfo coachInfo) {
		// TODO Auto-generated method stub
		if (coachInfo instanceof CoachInfo) {
			CoachInfo ci = (CoachInfo) coachInfo;
			if (!this.getLastName().equals(ci.getLastName())) {
				return this.getLastName().compareTo(ci.getLastName());
			} else {
				return this.getFirstName().compareTo(ci.getFirstName());
			}
		} else {
			return 1;
		}
	}

	public static class CompareByName implements Comparable<CoachInfo> {

		@Override
		public int compareTo(CoachInfo o) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

}
