package com.coach.certs;

import org.json.JSONObject;
import org.json.JSONArray;

public class JSONTools {
	static JSONObject getObjectBySearchOfName(JSONArray array, String name,
			String value) {
		JSONObject returnValue = null;
		for (int i = 0; i < array.length(); i++) {
			JSONObject target = (JSONObject) array.get(i);
			Object namedObject =  target.get(name);
			if (namedObject instanceof String && namedObject != null && namedObject.equals(value))
			{
				returnValue = target;
				break;
			}
		}
		return returnValue;
	}
}
