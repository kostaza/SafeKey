package App;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * SafeKey application - determines when an USB drive is mounted and scans it's content to find malicious files.
 * 
 * If the file size is less then {@value} MAX_SIZE, the program calculates it's md5 and tries to pull the report for this file from VT.
 * 
 * The verdict of SafeKey for a specific file is determined based on the {@value} THRESHOLD and the report for this file, from VT.
 * 
 * If the report is not available, the file is being sent to VT active scan (emulation) or skipped, depending on the user decision,
 * which is reflected by {@value} emulation.
 * 
 * If emulation was perform, the verdict of SafeKey is then determined based on the newly generated report from VT for this file
 * 
 * {@value} logger - a simple logger. logs all the event of SafeKey into log.log file
 * 
 * 
 * @author Kosta
 *
 */
public class SafeKey {
	public static Logger logger = Logger.getLogger("SafeKey");
	private static FileHandler fh = null;

	public static void main(String[] args) {
		
		try {
			fh = new FileHandler("log.log");
			logger.addHandler(fh);
			fh.setFormatter(new SimpleFormatter());
		} catch (SecurityException | IOException e) {
			logger.warning("Failed to create a log file");
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				UI.welcome();
				UI.welcomeFrame.setVisible(true);
			}
		});
	}
	
	
	/**
	 * Gracefully stops the logger and closes the file handler of the logger
	 */
	public static void closeLogger(){
		logger.removeHandler(fh);
		fh.close();
	}

}
