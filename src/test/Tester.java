package test;

import App.VTparser;
import App.VTquerier;
import App.Constants;

public class Tester {

	public static void main(String[] args) {
			
		
		/**************************************
		driveHandler x = new driveHandler();
		x.listen();
		**************************************/
		
		
		String md5 = "b7a5337ae06f5259601572af5b8efa5a";
		VTquerier q = new VTquerier();
		VTparser p = new VTparser();
		System.out.println(p.parseReport(q.getReport(md5)) == Constants.BENIGN);
		
	}

}
