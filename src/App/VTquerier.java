package App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class VTquerier {
	private final String key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	private final String reportURL = "https://www.virustotal.com/vtapi/v2/file/report?apikey=";
	private final String resource = "&resource=";
	
	public String getReport(String hash) throws IOException{
		URLConnection con = new URL(reportURL+key+resource+hash).openConnection();
		BufferedReader rd = new BufferedReader (new InputStreamReader (con.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;
		
		while ((line = rd.readLine()) != null)
			response.append(line);
		
		return response.toString();
	}

}
