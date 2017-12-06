package App;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
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
	private final String key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	private final String reportURL = "https://www.virustotal.com/vtapi/v2/file/report?apikey=";
	private final String resource = "&resource=";
	private final String scanURL = "https://www.virustotal.com/vtapi/v2/file/scan";
	
	public String getReport(String hash) throws IOException{
		URLConnection con = new URL(reportURL+key+resource+hash).openConnection();
		BufferedReader rd = new BufferedReader (new InputStreamReader (con.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line;
		
		while ((line = rd.readLine()) != null)
			response.append(line);
		
		return response.toString();
	}
	
	public String scanFile(File file) throws ClientProtocolException, IOException{
		FileBody filebody = new FileBody(file, ContentType.DEFAULT_BINARY);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", filebody);
		builder.addTextBody("apikey", key);
		
		HttpPost request = new HttpPost(scanURL);
		request.setEntity(builder.build());
		HttpClient client = HttpClientBuilder.create().build();
		
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = client.execute(request, responseHandler);
		
		return response;
		
	}

}
