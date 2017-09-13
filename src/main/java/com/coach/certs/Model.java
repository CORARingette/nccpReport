package com.coach.certs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coach.entities.Achievement;
import com.coach.entities.CoachRecord;
import com.coach.entities.Module;
import com.coach.entities.Transcript;

public class Model {
	private static String CURRENT_YEAR = "2016";

	private Map<String, CoachInfo> coachList = new HashMap<String, CoachInfo>();

	public Map<String, CoachInfo> getCoachList() {
		return coachList;
	}

	public void addCoach(CoachInfo coach) {
		coachList.put(coach.getNccpId(), coach);
	}

	public void loadFromCSVFile(String filename) {
		String line;

		try {
			InputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				String[] splitArray = line.split(",");
				CoachInfo coach = new CoachInfo();
				coach.setLastName(splitArray[0]);
				if (splitArray.length > 1) {
					coach.setFirstName(splitArray[1]);
				}
				if (splitArray.length > 2) {
					coach.setNccpId(splitArray[2]);
				}
				if (splitArray.length > 3) {
					coach.setCurrentYear(splitArray[3]);
				}
				addCoach(coach);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void report() {
		List<CoachInfo> errors = new ArrayList<CoachInfo>();
		List<CoachInfo> coaches = new ArrayList<CoachInfo>(coachList.values());
		Collections.<CoachInfo> sort(coaches);
		for (CoachInfo coach : coaches) {
			if (coach.isResponseInfoComplete()) {
				printCoach(coach);
			} else {
				if (coach.getCurrentYear() != null
						&& coach.getCurrentYear().equals(CURRENT_YEAR)) {
					errors.add(coach);
				}
			}
		}

		for (CoachInfo coach : errors) {
			System.out.println("Error:" + coach.getLastName() + ":"
					+ coach.getFirstName() + ":" + coach.getNccpId());
		}
	}

	private void printCoach(CoachInfo coach) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(coach.getLastName() + "," + coach.getFirstName()
				+ ":" + coach.getNccpId());
		CoachRecord record = coach.getCoachRecord();
		Transcript transcript = record.getTranscript();

		for (Achievement achievement : transcript.getAchievements()) {
			for (Module module : achievement.getModules()) {
				System.out.println(String.format("- %-75s : %-15s : %s", module
						.getModuleName(),
						module.getModuleRequirement().equals("E") ? "CERTIFIED"
								: "TRAINED", df.format(achievement
								.getDateValue())));
			}
		}
		System.out.println();
	}

}
