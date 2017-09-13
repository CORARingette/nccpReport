package com.coach.certs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class CourseInfo {
	private String name;
	private CourseStatus status;
	private Date dateTaken;
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd-MMM-yyyy");
	private static SimpleDateFormat outFormatter = new SimpleDateFormat(
			"yyyy/MM/dd");

	public static Map<String, CourseInfo> createCoursesFromSports(
			JSONArray sports) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();
		JSONObject ringetteRecord = JSONTools.getObjectBySearchOfName(sports,
				"name", "Ringette");
		courses.putAll(createCoursesFromSport(ringetteRecord));

		JSONObject multisport = JSONTools.getObjectBySearchOfName(sports,
				"name", "Multi-Sport");
		mergeCourseToMostRecentStatus(courses,createCoursesFromSport(multisport));

		return courses;
	}

	private static Map<String, CourseInfo> createCoursesFromSport(
			JSONObject sport) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();

		if (sport != null) {
			// /audiences["Coach"]/context/history/evaluation/modules
			// /audiences["Coach"]/context/history/training/modules

			JSONArray audiences = sport.getJSONArray("audiences");
			JSONObject coachAudience = JSONTools.getObjectBySearchOfName(
					audiences, "name", "Coach");
			JSONArray contexts = coachAudience.getJSONArray("contexts");
			mergeCourseToMostRecentStatus(courses,createCoursesFromContexts(contexts));
		}

		return courses;
	}

	private static Map<String, CourseInfo> createCoursesFromContexts(
			JSONArray contexts) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();
		for (int i = 0; i < contexts.length(); i++) {
			JSONObject context = contexts.getJSONObject(i);
			mergeCourseToMostRecentStatus(courses,createCoursesFromContext(context));
		}

		return courses;
	}

	private static Map<String, CourseInfo> createCoursesFromContext(
			JSONObject context) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();
		if (context != null) {

			JSONObject history = context.getJSONObject("history");

			mergeCourseToMostRecentStatus(courses,createCoursesFromTraining(history,
					CourseStatus.TRAINED, "training"));
			mergeCourseToMostRecentStatus(courses,createCoursesFromTraining(history,
					CourseStatus.CERTIFIED, "evaluation"));

		}
		return courses;
	}

	private static Map<String, CourseInfo> createCoursesFromTraining(
			JSONObject history, CourseStatus courseStatus, String trainingType) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();
		if (history != null) {
			JSONArray trainingSessions = history.getJSONArray(trainingType);
			for (int i = 0; i < trainingSessions.length(); i++) {
				JSONObject trainingSession = trainingSessions.getJSONObject(i);
				mergeCourseToMostRecentStatus(courses,createCoursesFromTrainingSession(
						trainingSession, courseStatus));
			}
		}
		return courses;
	}

	private static Map<String, CourseInfo> createCoursesFromTrainingSession(
			JSONObject trainingSession, CourseStatus courseStatus) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();
		if (trainingSession != null) {
			for (int i = 0; i < trainingSession.length(); i++) {

				// get date
				String dateString = trainingSession.getString("date");

				Date date = new Date();
				try {
					date = formatter.parse(dateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// get modules
				JSONArray modules = trainingSession.getJSONArray("modules");
				mergeCourseToMostRecentStatus(courses,createCourseFromModules(date, courseStatus,
						modules));
			}
		}
		return courses;
	}

	private static Map<String, CourseInfo> createCourseFromModules(Date date,
			CourseStatus courseStatus, JSONArray modules) {
		Map<String, CourseInfo> courses = new HashMap<String, CourseInfo>();
		if (modules != null) {
			for (int i = 0; i < modules.length(); i++) {
				CourseInfo course = new CourseInfo();
				course.setDateTaken(date);

				JSONObject module = modules.getJSONObject(i);
				String name = module.getString("moduleName");
				course.setName(name);
				course.setStatus(courseStatus);
				courses.put(name, course);
			}
		}
		return courses;
	}

	private static void mergeCourseToMostRecentStatus(
			Map<String, CourseInfo> existingList,
			Map<String, CourseInfo> newList) {
		for (String key : newList.keySet()) {
			CourseInfo newCourse = newList.get(key);
			CourseInfo existingCourse = existingList.get(key);

			if (existingCourse != null
					&& newCourse.getDateTaken().getTime() > existingCourse
							.getDateTaken().getTime()) {
				existingList.remove(key);
			}

			existingList.put(key, newCourse);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

	public Date getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}

	public String Dump()
	{
		return String.format("%-70s: %-15s: %-10s", this.getName(),this.getStatus(),outFormatter.format(this.getDateTaken()));
	}
	
	public static class CompareByDate implements Comparator<CourseInfo>
	{

		@Override
		public int compare(CourseInfo o1, CourseInfo o2) {
			// TODO Auto-generated method stub
			return o1.getDateTaken().compareTo(o2.getDateTaken());
		}
		
	}
}
