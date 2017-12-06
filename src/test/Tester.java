package test;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import App.VTquerier;
import App.driveHandler;
import App.UI;
//import App.Constants;
import App.VTparser;

public class Tester {

	public static void main(String[] args) {
		
		
		/***************************************
		File file = new File ("C:/Users/Kosta/Desktop/RESOURCES.txt");
		try {
			System.out.println(new VTquerier().scanFile(file));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		***************************************/
		
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				UI.welcome();
				UI.welcomeFrame.setVisible(true);
			}
		});
		
		
		
		/**************************************
		driveHandler x = new driveHandler();
		x.listen();
		**************************************/
		
		
		
		
		/**************************************
		VTquerier vt = new VTquerier();
		String md5 = "b7a5337ae06f5259601572af5b8efa5a";
		try {
			System.out.println(((JSONObject) new JSONParser().parse(vt.getReport(md5))).get("response_code").toString().equalsIgnoreCase("1"));
		} catch (IOException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("JSON parsing failed!");
			e.printStackTrace();
		}
		**************************************/
		
		
		
		/**************************************
		String md5 = "b7a5337ae06f5259601572af5b8efa5a";
		VTquerier q = new VTquerier();
		VTparser p = new VTparser();
		try {
			System.out.println(p.parseReport(q.getReport(md5)) == Constants.BENIGN);
		} catch (ParseException e) {
			System.out.println("Parsing Failed!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
		}
		***************************************/
		
	}

}
