package App;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

/**
 * driveHandler object is responsible for actively monitoring and determining any newly connected or disconnected USB drives.
 * Whenever a valid USB drive is connected, it is sent to the drive scanner.
 * 
 * @author Kosta
 *
 */
public class driveHandler {
	private static File[] currentRoots = File.listRoots();
	private Thread listener;
	private boolean running;
	
	public void listen() {
		running = true;
		SafeKey.logger.info("Drives scanner started");
	    listener = new Thread(new Runnable() {
	        public void run() {
	            while (running) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                if (File.listRoots().length > currentRoots.length) {
	                	currentRoots = File.listRoots();
	                	File root = currentRoots[currentRoots.length-1];
	                	if (validDrive(root)){
	                		SafeKey.logger.severe("USB drive "+root+" inserted");
	                		new Thread(new Scanner(root)).start();
	                		//new Scanner(root).run();
	                	}
	                } 
	                else if (File.listRoots().length < currentRoots.length) {
	                	currentRoots = File.listRoots();
	                	SafeKey.logger.severe("USB drive was removed");
	                }

	            }
	        }
	    });
	    listener.start();
	}
	
	
	/**
	 * Stops the drives monitoring.
	 */
	public void stop(){
		running = false;
		SafeKey.logger.info("Drives scanner stopped");
	}
	
	
	/**
	 * A predicate function that checks if a specific drive is a removable drive.
	 * 
	 * @param f - The specific drive to be checked
	 * @return - 'true' if the given drive is a removable USB drive, else 'false'
	 */
	private static boolean validDrive(File f){
		FileSystemView fsv = FileSystemView.getFileSystemView();
		String type = fsv.getSystemTypeDescription(f);
		
		return type.equalsIgnoreCase("removable disk");
	}

}
