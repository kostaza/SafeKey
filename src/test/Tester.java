package test;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import App.VTquerier;
import App.driveHandler;
//import App.Constants;
import App.VTparser;

public class Tester {

	public static void main(String[] args) {
		
		
		driveHandler x = new driveHandler();
		x.listen();
		
		
		
		
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
