package App;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.UIManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.awt.Toolkit;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;


/**
 * An user interface class, responsible for all the on-screen messages and alerts, the system tray icon functionality and the welcome screen.
 * 
 * @author Kosta
 *
 */
public class UI {
	private static driveHandler handler = new driveHandler();
	public static JFrame welcomeFrame;
	public static JDialog maliciousNotification;
	public static JDialog warning;
	
	/**
	 * Constructs the welcome screen of the application
	 */
	public static void welcome(){
		welcomeFrame = new JFrame();
		welcomeFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\icon.jpg"));
		welcomeFrame.setBackground(Color.LIGHT_GRAY);
		welcomeFrame.setTitle("SafeKey - USB Drives Scanner");
		welcomeFrame.setBounds(400, 200, 450, 300);
		welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		welcomeFrame.getContentPane().setLayout(null);
		
		JLabel welcomeMessage = new JLabel("Welcome to SafeKey - an automated USB drives scanner");
		welcomeMessage.setFont(new Font("Baskerville Old Face", Font.BOLD, 14));
		welcomeMessage.setBounds(32, 33, 370, 40);
		welcomeFrame.getContentPane().add(welcomeMessage);
		
		JLabel startMessage = new JLabel("To start monitoring - press START");
		startMessage.setForeground(Color.RED);
		startMessage.setFont(new Font("Berlin Sans FB", Font.ITALIC, 12));
		startMessage.setBounds(132, 96, 173, 26);
		welcomeFrame.getContentPane().add(startMessage);
		
		JButton startBtn = new JButton("START");
		startBtn.setContentAreaFilled(false);
		startBtn.setFocusPainted(false);
		startBtn.setOpaque(false);
		startBtn.setForeground(Color.BLUE);
		startBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
		startBtn.setBackground(UIManager.getColor("Button.background"));
		startBtn.setBounds(174, 159, 89, 23);
		startBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				SafeKey.logger.info("SafeKey monitoring started! Emulation Off.");
				handler.listen();
				addTray();
				welcomeFrame.dispose();
			}
			
		});
		welcomeFrame.getContentPane().add(startBtn);
		
	}
	
	/**
	 * Constructs the tray icon and it's functionality
	 */
	private static void addTray(){
		if (SystemTray.isSupported()){
			TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("resources\\icon.png"), "SafeKey");
			
			PopupMenu menu = new PopupMenu();
			MenuItem stopItem = new MenuItem("Stop SafeKey");
			stopItem.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					handler.stop();
					SystemTray.getSystemTray().remove(trayIcon);
					SafeKey.logger.info("SafeKey Stopped! Exiting...");
					SafeKey.closeLogger();
					System.exit(0);
				}
				
			});
			CheckboxMenuItem emul = new CheckboxMenuItem("Enable Emulation");
			emul.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					Scanner.emulation = !Scanner.emulation;
					SafeKey.logger.info("Emulation is ON!");
				}
				
			});
			
			menu.add(emul);
			menu.add(stopItem);
			trayIcon.setPopupMenu(menu);
			
			try {
				SystemTray.getSystemTray().add(trayIcon);
			} catch (AWTException e1) {
				SafeKey.logger.severe("Failed to add a tray - desktop system tray is missing");
				warningDialog("<center>Failed to add tray - system tray is missing!</center><br><center>Emulation and graceful stop won't be available</center>");
				warning.setVisible(true);
				e1.printStackTrace();
			}
		}
		else {
			SafeKey.logger.severe("Failed to add a tray - system tray is not supported");
			warningDialog("<center>Failed to add tray - system tray is not supported!</center><br><center>Emulation and graceful stop won't be available</center>");
			warning.setVisible(true);
		}
	}
	
	
	/**
	 * Constructs the alert window the appears when a malicious file is found
	 * @param fileName - The specific file that triggered the alert
	 */
	public static void foundMalicious(File fileName){
		maliciousNotification = new JDialog();
		maliciousNotification.setAlwaysOnTop(true);
		maliciousNotification.setModal(true);
		maliciousNotification.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\icon.jpg"));
		maliciousNotification.setBackground(Color.LIGHT_GRAY);
		maliciousNotification.setTitle("SafeKey - Malicious file found!");
		maliciousNotification.setBounds(400, 200, 400, 200);
		maliciousNotification.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		maliciousNotification.getContentPane().setLayout(null);
		
		JTextPane maliciousMessage = new JTextPane();
		maliciousMessage.setEditable(false);
		maliciousMessage.setForeground(Color.RED);
		maliciousMessage.setFont(new Font("Castellar", Font.BOLD, 11));
		maliciousMessage.setText("Malicous file found inside USB drive!!!");
		maliciousMessage.setBounds(10, 11, 364, 20);
		maliciousMessage.setBackground(null);
		maliciousNotification.getContentPane().add(maliciousMessage);
		
		JTextPane maliciousFile = new JTextPane();
		maliciousFile.setFont(new Font("Century", Font.ITALIC, 11));
		maliciousFile.setEditable(false);
		maliciousFile.setText(fileName.toString());
		maliciousFile.setBounds(10, 46, 364, 20);
		maliciousFile.setBackground(null);
		maliciousNotification.getContentPane().add(maliciousFile);
		
		JLabel cautiousMessage = new JLabel("*This file may harm your computer - open with cautious.");
		cautiousMessage.setForeground(new Color(0, 0, 0));
		cautiousMessage.setFont(new Font("Bookman Old Style", Font.BOLD, 10));
		cautiousMessage.setBounds(10, 112, 364, 20);
		maliciousNotification.getContentPane().add(cautiousMessage);
	}
	
	
	/**
	 * Constructs the warning window that appears when there was any malfunction in the program.
	 * @param message - indicates the message that will be displayed within the warning window
	 */
	public static void warningDialog (String message){
		warning = new JDialog();
		warning.setAlwaysOnTop(true);
		warning.setModal(true);
		warning.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\icon.jpg"));
		warning.setTitle("SafeKey - Error!");
		warning.setBounds(400, 200, 300, 200);
		warning.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		warning.getContentPane().setLayout(null);
		
		JLabel warningMessage = new JLabel("<html>"+ message + "</html>");
		warningMessage.setFont(new Font("Tahoma", Font.PLAIN, 11));
		warningMessage.setHorizontalAlignment(SwingConstants.CENTER);
		warningMessage.setBounds(10, 90, 264, 61);
		warning.getContentPane().add(warningMessage);
		
		JLabel warningSign = new JLabel("");
		warningSign.setIcon(new ImageIcon("resources\\warning.jpg"));
		warningSign.setHorizontalAlignment(SwingConstants.CENTER);
		warningSign.setBounds(10, 11, 264, 68);
		warning.getContentPane().add(warningSign);
	}
}
