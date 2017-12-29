package App;

import java.io.File;
import java.io.IOException;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

/**
 * Scanner object is responsible to scan a given drive's files and to act accordingly.
 * Every valid file is sent to the querier object, which returns the report for the file.
 * The report is then being sent to the parser object, which returns a verdict indicator for this file.
 * 
 * @author Kosta
 *
 */
public class Scanner implements Runnable {
	public static boolean emulation = false;
	
	private File drive;
	
	Scanner (File root){
		drive = root;
	}

	@Override
	public void run() {
		scanDir(drive);
		
	}

	/**
	 * Scans a given directory's file and it's directories recursively.
	 * @param dir - The given directory to scan
	 */
	private void scanDir(File dir){
		File[] files = dir.listFiles();
		final VTquerier querier = new VTquerier();
		final VTparser parser = new VTparser();
		
		for (int i=0; i<files.length; i++){
			if (files[i].isFile()	&&	files[i].length() < Constants.MAX_SIZE){
				HashCode md5 = null;
				try {
					md5 = Files.hash(files[i], Hashing.md5());
				} catch (IOException e1) {
					SafeKey.logger.severe(files[i] + " IS UNAVAILABLE");
					SafeKey.logger.warning("Unable to calculate hash for: " + files[i] + "\nSkipping...");
					UI.warningDialog("<center>Unable to calculate hash for the following file:</center><br><center>" + files[i] + "</center>");
					UI.warning.setVisible(true);
					continue;
				}
				int verdict = parser.parseReport(querier.getReport(md5.toString()));
				
				switch (verdict){
				case Constants.BENIGN:
					SafeKey.logger.info(files[i] + " IS BENIGN");
					break;
					
				case Constants.UNAVAILABLE:
					SafeKey.logger.severe(files[i] + " IS UNAVAILABLE");
					if (emulation && parser.responseCode(querier.scanFile(files[i]))==1){
						SafeKey.logger.info("Sending to emulation...");
						final int index = i;
						final HashCode hash = md5;
						new Thread(new Runnable(){

							@Override
							public void run() {
								while (parser.responseCode(querier.getReport(hash.toString())) != 1) {
									if (parser.responseCode(querier.getReport(hash.toString())) == -1){
										SafeKey.logger.severe("Emulation for: "+ files[index] + " failed");
										UI.warningDialog("<center>Emulation failed for the following file:</center><br><center>" + files[index] + "</center>");
										UI.warning.setVisible(true);
										return;
									}
								}
								int verdict = parser.parseReport(querier.getReport(hash.toString()));
								if (verdict == Constants.MALICIOUS){
									SafeKey.logger.severe("Emulation result: "+ files[index] + " IS MALICOUS");
									maliciousHandler(files[index]);
								}
								else if (verdict == Constants.BENIGN) SafeKey.logger.info("Emulation result: "+ files[index] + " IS BENIGN");
									else{
										SafeKey.logger.severe("Emulation result: "+ files[index] + " IS UNAVAILABLE"); 
										UI.warningDialog("<center>Emulation failed for the following file:</center><br><center>" + files[index] + "</center>");
										UI.warning.setVisible(true);
									}
								
							}
							
						}).start();
					}
					else SafeKey.logger.info("Emulation was not performed");
					break;
					
				case Constants.MALICIOUS:
					SafeKey.logger.severe(files[i] + " IS MALICOUS");
					final int index = i;
					new Thread(new Runnable(){

						@Override
						public void run() {
							maliciousHandler(files[index]);
						}
						
					}).start();
					
				}
			}
			else if (files[i].isDirectory()) scanDir(files[i]);
		}
	
	}
	
	
	/**
	 * A handler function that responsible to call the appropriate UI window if a malicious file was found,
	 * and make it visible.
	 * 
	 * @param file - the given malicious file
	 */
	private synchronized void maliciousHandler(File file){
		UI.foundMalicious(file);
		UI.maliciousNotification.setVisible(true);
	}

}
