package App;

/**
 * An interface for all the project constants
 * and predefined.
 * 
 * {@value} THRESHOLD - defines the minimal number of vendors that  the file as malicious,
 * 				in order to consider it as malicious.
 * {@value} MALICIOUS - indicator for malicious file
 * {@value} BENIGN - indicator for clean file
 * {@value} UNAVAILABLE - indicator that a verdict for the file is unavailable
 * {@value} MAX_SIZE - defines the maximum file size that should be scanned
 * 
 * 
 * @author Kosta
 * 
 */
interface Constants {
	
	public static final int THRESHOLD = 3;
	public static final int MALICIOUS = 0;
	public static final int BENIGN = 1;
	public static final int UNAVAILABLE = 3;
	public static final long MAX_SIZE = 10000000;

}
