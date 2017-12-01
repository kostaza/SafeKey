package App;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VTparser {
	
	public int parseReport(String response) throws ParseException{
		JSONObject json = (JSONObject) new JSONParser().parse(response);
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

}
