package App;

import java.io.File;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public class Scanner implements Runnable {
	
	private File drive;
	
	Scanner (File root){
		drive = root;
	}

	@Override
	public void run() {
		try {
			scanDir(drive);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void scanDir(File dir) throws IOException, ParseException, InterruptedException {
		File[] files = dir.listFiles();
		for (int i=0; i<files.length; i++){
			if (files[i].isFile()	&&	files[i].length() < Constants.MAX_SIZE){
				VTquerier querier = new VTquerier();
				VTparser parser = new VTparser();

				HashCode md5 = Files.hash(files[i], Hashing.md5());
				int verdict = parser.parseReport(querier.getReport(md5.toString()));
				
				switch (verdict){
				case Constants.BENIGN:
					System.out.println(files[i]+" IS BENIGN");
					break;
				case Constants.UNAVAILABLE:
					System.out.println(files[i]+" IS UNAVAILABLE");
					// TODO
					break;
				case Constants.MALICIOUS:
					System.out.println(files[i]+" IS MALICIOUS");
					File file = files[i];
					Thread notification = new Thread(new Runnable(){

						@Override
						public void run() {
							UI.foundMalicious(file);
							UI.maliciousNotification.setVisible(true);
						}
						
					});
					notification.start();
					notification.join();
					
				}
			}
			else if (files[i].isDirectory()) scanDir(files[i]);
		}
	
	}

}
