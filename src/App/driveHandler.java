package App;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public class driveHandler {
	private static File[] currentRoots = File.listRoots();
	
	public void listen() {
	    Thread listener = new Thread(new Runnable() {
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                if (File.listRoots().length > currentRoots.length) {
	                	currentRoots = File.listRoots();
	                	File root = currentRoots[currentRoots.length-1];
	                	if (validDrive(root)){
	                		System.out.println("USB drive "+root+" inserted");
	                		new Scanner(root).run();
	                	}
	                } 
	                else if (File.listRoots().length < currentRoots.length) {
	                	currentRoots = File.listRoots();
	                	// TODO signal to scanner that drive was removed
	                }

	            }
	        }
	    });
	    listener.start();
	}
	
	private static boolean validDrive(File f){
		FileSystemView fsv = FileSystemView.getFileSystemView();
		String type = fsv.getSystemTypeDescription(f);
		if (type.equalsIgnoreCase("removable disk"))
			return true;
		else
			return false;
	}

}
