package com.coach.certs;

import java.util.logging.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

public class Main {

	public static void main(String[] args) {

		final Logger logger = Logger.getLogger("Main");
		logger.info("Starting");

		// open data file and load coach list
		Model model = new Model();
		model.loadFromCSVFile("Coaches.csv");

		// fill in data

		int count = 1;
		int total = model.getCoachList().values().size();
		
		for (final CoachInfo coach : model.getCoachList().values()) {
			//if (count > 3) break;
			if (coach.isQueryInfoComplete()) {
				try {
					HttpRequestWithBody request = Unirest
							.post("https://thelocker.coach.ca/Account/Public");
					request.body("accountNumber="
							+ coach.getNccpId()
							+ "&lastName="
							+ coach.getLastName().trim().replace(" ", "%20")
									.replace("'", "%2C"));
					request.queryString("accountNumber", coach.getNccpId());
					request.queryString("lastName", coach.getLastName());

					HttpResponse<String> jsonResponse = request.asString();
					System.err.println("Processing (" + (count++) + "/" + total +")-> " + coach.getLastName() + " : " + coach.getNccpId());
					coach.updateFromResponse(jsonResponse);
				} catch (UnirestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		// output
		model.report();
		logger.info("Done");
	}

}
