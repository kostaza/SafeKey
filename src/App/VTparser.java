package App;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * VTparser object is responsible to parse the report that is fetched in JSON format from VT
 * 
 * 
 * @author Kosta
 *
 */
public class VTparser {
	
	/**
	 * Parses a JSON report form VT for a specific file
	 * 
	 * @param response - The report that was fetched from VT
	 * @return An indicator that states the verdict
	 */
	public int parseReport(String response){
		JSONObject json;
		try {
			json = (JSONObject) new JSONParser().parse(response);
		} catch (ParseException e) {
			SafeKey.logger.warning("Report parsing failed!");
			return Constants.UNAVAILABLE;
		}
		int code = Integer.parseInt(json.get("response_code").toString());
		
		switch (code){
		case 0:	
			return Constants.UNAVAILABLE;
		
		case 1:
			int positives = Integer.parseInt(json.get("positives").toString());
			if (positives >= Constants.THRESHOLD)
				return Constants.MALICIOUS;
			else
				return Constants.BENIGN;
			
		default: 
			return Constants.UNAVAILABLE;
		}
	}
	
	/**
	 * Retrieves the response code from a given JSON response
	 * @param response - A given JSON response
	 * @return The response code, represented as integer
	 */
	public int responseCode(String response){
		JSONObject json;
		try {
			json = (JSONObject) new JSONParser().parse(response);
		} catch (ParseException e) {
			SafeKey.logger.warning("Response parsing failed!");
			return -1;
		}
		int code = Integer.parseInt(json.get("response_code").toString());
		
		return code;
	}

}
