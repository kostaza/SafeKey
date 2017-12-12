package App;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	
	public static void closeLogger(){
		logger.removeHandler(fh);
		fh.close();
	}

}
