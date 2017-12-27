package App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

public class VTquerier {
	private String key = null;
	private final String reportURL = "https://www.virustotal.com/vtapi/v2/file/report?apikey=";
	private final String resource = "&resource=";
	private final String scanURL = "https://www.virustotal.com/vtapi/v2/file/scan";
	
	
	@SuppressWarnings("resource")
	public VTquerier(){
		String apikey = null;
		try {
			apikey = new BufferedReader(new FileReader("apikey.txt")).readLine();
		} catch (IOException e) {
			SafeKey.logger.severe("Unable to open apikey file - key was not assigned!");
		}
		if (apikey != null)
			key = apikey;
		else{
			SafeKey.logger.info("SafeKey needs a valid api key to work, please provide one and restart the program\nExiting...");
			UI.warningDialog("<center>Unable to retrieve your API key!</center><br><center>Put your key into 'apikey.txt' and restart the program</center>");
			UI.warning.setVisible(true);
			SafeKey.logger.info("Exiting...");
			SafeKey.closeLogger();
			System.exit(0);
		}
		
	}
	
	public String getReport(String hash){
		URLConnection con;
		BufferedReader rd = null;
		try {
			con = new URL(reportURL+key+resource+hash).openConnection();
			rd = new BufferedReader (new InputStreamReader (con.getInputStream()));
		} catch (IOException e) {
			SafeKey.logger.severe("Connection failed! Exiting...");
			UI.warningDialog("<center>Connection to remote server failed!</center><br><center>Check your internet connection and restart the program</center>");
			UI.warning.setVisible(true);
			SafeKey.closeLogger();
			System.exit(0);
		}
		
		StringBuilder response = new StringBuilder();
		String line;
		
		try {
			while ((line = rd.readLine()) != null)
				response.append(line);
		} catch (IOException e) {
			SafeKey.logger.severe("Failed to read the report from the server! Exiting...");
			UI.warningDialog("<center>Connection with remote server was terminated!</center><br><center>Check your internet connection and restart the program</center>");
			UI.warning.setVisible(true);
			SafeKey.closeLogger();
			System.exit(0);
		}
		
		return response.toString();
	}
	
	public String scanFile(File file){
		FileBody filebody = new FileBody(file, ContentType.DEFAULT_BINARY);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", filebody);
		builder.addTextBody("apikey", key);
		
		HttpPost request = new HttpPost(scanURL);
		request.setEntity(builder.build());
		HttpClient client = HttpClientBuilder.create().build();
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = null;
		try {
			response = client.execute(request, responseHandler);
		} catch (IOException e) {
			SafeKey.logger.warning("Failed to send " + file + " to emulation!");
		}
		
		return response;
		
	}

}
