package gui.alerts;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import main.BaseController;
import main.Logger;
import controller.*;
import data.TodoItemStack;


/**
 * Der tray icon handler implementiert einen reminder listener, wodurch er
 * beim reminder handler registriert werden kann. im alert-fall öffnet er eine
 * windows-bubble im taskbereich.
 * @author Tobias Schaber
 */
public class TrayIconWindowHandler implements ActionListener, ReminderListenerInterface {
	
	private final BaseController 	controller;
	
	private final TrayIcon		trayIcon 		= new TrayIcon(ImageController.imgTrayIcon, "Todo Manager - Reminder aktiv");
	private final SystemTray	tray 			= SystemTray.getSystemTray();
	
	private final MenuItem	itemExit 			= new MenuItem("Beenden");
	private final MenuItem	itemOpenWindow 		= new MenuItem("TodoManager anzeigen");
	private final MenuItem	itemDisableReminder = new MenuItem("Reminder deaktivieren");
	private final PopupMenu	popupMenu 			= new PopupMenu();
	
	private final NotificationWindow notifyWindow;

	private TodoItemStack lastStack;
	public MouseListener mouseListener;
	
	
	/**
	 * constructor
	 * @param bf
	 */
	public TrayIconWindowHandler(BaseController controller) {
		this.controller = controller;
		
		 notifyWindow = new NotificationWindow(this, controller);
		
		initComponents();
		initListeners();
		

	}
	
	
	/**
	 * schließt aktive reminder fenster
	 */
	public void closeActiveWindows() {
		notifyWindow.hideNotificationWindow();
	}
	
	
	
	
	private void initComponents() {
		
		mouseListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {

	
				// bei doppelklick das fensterchen verschwinden lassen und das hauptfenster zeigen
				if(e.getClickCount() == 2) {
					controller.showWindow();
					controller.setSelectedTabByTodoItemStack(lastStack);
					notifyWindow.hideNotificationWindow();
				}
			}
			
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}			
			public void mouseClicked(MouseEvent e) {}
		};

		
		
		notifyWindow.addMouseListener(mouseListener);

		itemDisableReminder.setName("enabled");
		
		
		
		
		popupMenu.add(itemDisableReminder);		
		popupMenu.add(itemOpenWindow);
		popupMenu.addSeparator();
		popupMenu.add(itemExit);
		
		trayIcon.setImageAutoSize(true);
		
		trayIcon.setPopupMenu(popupMenu);
		

		
		try {
			tray.add( trayIcon);
		}
		catch(Exception e) { e.printStackTrace(); }		
				
	}
	
	private void initListeners() {
		
		itemExit.addActionListener(this);
		itemOpenWindow.addActionListener(this);
		trayIcon.addActionListener(this);
		itemDisableReminder.addActionListener(this);
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == itemExit) {
			controller.exit();
		}
		
		if(e.getSource() == itemOpenWindow) {
			controller.showWindow();
		}
		
		// wechselt den status des reminders
		if(e.getSource() == itemDisableReminder) {
			System.out.println("ALT : " + ConfigurationHandlerImpl.reminderIsActive);

			
			if(itemDisableReminder.getName().equals("enabled")) {
				closeActiveWindows();
				itemDisableReminder.setName("disabled");
				itemDisableReminder.setLabel("Reminder aktivieren");
				trayIcon.setImage(ImageController.imgTrayIconDisabled);
				ConfigurationHandlerImpl.reminderIsActive = false;
				trayIcon.setToolTip("Todo Manager - Reminder nicht aktiv");
				
			} else {
				
				itemDisableReminder.setName("enabled");
				itemDisableReminder.setLabel("Reminder deaktivieren");
				trayIcon.setImage(ImageController.imgTrayIcon);
				ConfigurationHandlerImpl.reminderIsActive = true;
				trayIcon.setToolTip("Todo Manager - Reminder aktiv");
			}
			
			System.out.println("NEU : " + ConfigurationHandlerImpl.reminderIsActive);
		}
		
		if(e.getSource() == trayIcon) {
			controller.showWindow();
		}
		
		
	}

	@Override
	public void showAlert(TodoItemStack tis) {
		lastStack = tis;
		
		notifyWindow.showNotificationWindow(tis.getLatestTodo().getDescription(), tis.getName());

		
	}
	
	/**
	 * liefert den zuletzt im reminder gehandelten todo item stack zurück
	 * @return
	 */
	public TodoItemStack getLastStack() {
		return lastStack;
	}

	public void removeReminders() {

		closeActiveWindows();
		lastStack = null;
		notifyWindow.hideNotificationWindow();
		
	}

}



class BasePanel extends JPanel {
	
	private JLabel 				labText = new JLabel();
	private JLabel 				headline = new JLabel();
	private JButton 			closBut;
	private JLabel				labDeactivate;
	
	private NotificationWindow	parent;
	
	
	/**
	 * constructor
	 * @param parent window to be closed
	 */
	public BasePanel(NotificationWindow parent) {
		
		this.parent = parent;
		
		initComponents();
		initLayout();
		
	}
	
	
	public void initComponents() {
		
		closBut = new CloseButton();
		labDeactivate = new JLabel();
		labDeactivate.setIcon(ImageController.iconNoAlarm);
		labDeactivate.setToolTipText("Reminder deaktivieren");
		labDeactivate.addMouseListener(parent);
		
		labDeactivate.setToolTipText("Reminder deaktivieren (nicht mehr erinnern)");
		closBut.setToolTipText("Später erneut erinnern");
		
		headline.setIcon(ImageController.iconAlarm);
		
        closBut.setMaximumSize(new java.awt.Dimension(20, 20));
        closBut.setMinimumSize(new java.awt.Dimension(20, 20));
        closBut.setPreferredSize(new java.awt.Dimension(20, 20));

        headline.setFont(new java.awt.Font("Tahoma", 1, 12));
        
	}
	
	public void initLayout() {
	
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(headline, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addComponent(closBut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labText, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labDeactivate))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closBut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labText)
                .addGap(31, 31, 31)
                .addComponent(labDeactivate)
                .addContainerGap())
        );
		
	}

	
	public void setText(String msg, String hdl) {
		labText.setText(msg);
		headline.setText(hdl);
	}
	
	private class CloseButton extends JButton {
		
		
		/**
		 * constructor
		 */
		public CloseButton() {
			
			setToolTipText("schließen");         
			setUI(new BasicButtonUI());            
			setContentAreaFilled(false);         
			setFocusable(false);            
			setBorder(BorderFactory.createEtchedBorder()); 
			setBorderPainted(false);             
			addMouseListener(closeButtenHoverListener);      
			setRolloverEnabled(true);
			
			addActionListener(parent);  
		}      
		
		
		/* die grafik muss nicht neu gezeichnet werden */
		public void updateUI() {}
		
		
		/**
		 * zeichnet das kreuz zum schließen
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g); 
			Graphics2D g2 = (Graphics2D) g.create();  
			
			if (getModel().isPressed()) {
				g2.translate(1, 1);   
			}
			
			g2.setStroke(new BasicStroke(2)); 
			g2.setColor(Color.gray);  
			if (getModel().isRollover()) { 
				g2.setColor(Color.red);     
			}
			
			int delta = 6;   
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);    
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();   
		}  
	}

	/* eigener MouseListener für Hover-Effekte */
	private final static MouseListener closeButtenHoverListener = new MouseAdapter() {   
		
		/**
		 * für hover-effekte über dem button zu verwenden
		 */
		public void mouseEntered(MouseEvent e) { 
	
			Component component = e.getComponent();  
			
			if (component instanceof AbstractButton) { 
				AbstractButton button = (AbstractButton) component; 

				button.setBorderPainted(true);
			}   
		}        
			 
		/**
		 * für hover-effekte über dem button zu verwenden
		 */
		public void mouseExited(MouseEvent e) { 
			Component component = e.getComponent();       
			if (component instanceof AbstractButton) {   
				AbstractButton button = (AbstractButton) component;  
				button.setBorderPainted(false);    
			}      
		} 
	};

}



/**
 * Hauptfenster welches als Notification angezeigt wird
 * @author SCHABT
 *
 */
class NotificationWindow extends JWindow implements ActionListener, MouseListener {
	
	private BasePanel basePanel;
	private TrayIconWindowHandler tiwh;
	private BaseController parent;
	
	public NotificationWindow(TrayIconWindowHandler tiwh, BaseController bf) {
		this.tiwh = tiwh;
		this.parent = bf;

		initComponents();		
	}
	
	

	
	@Override
	public void mouseClicked(MouseEvent e) {
		Logger.getInstance().log("Deaktiviere Reminder", Logger.LOGLEVEL_INFO);
		
		tiwh.getLastStack().setReminderActivationStatus(false);
		
		// aktualisiere das hauptfenster
		parent.setSelectedTabByTodoItemStack(tiwh.getLastStack());
		
		hideNotificationWindow();
	}
	

	public void mousePressed(MouseEvent e)  {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e)  {
		
		Component component = e.getComponent();  
		
		if (component instanceof JLabel) { 
			((JLabel)component).setBorder(new EtchedBorder()); 
		}   
	}        
		 
	/**
	 * für hover-effekte über dem reminder deaktivieren button zu verwenden
	 */
	public void mouseExited(MouseEvent e) { 

		Component component = e.getComponent(); 
		
		if (component instanceof JLabel) { 
			((JLabel)component).setBorder(null); 
		}     
	
	}

	public void initComponents() {
		
		// sichtbares rechteck (berücksichtigt taskleiste) für unten rechts
		Rectangle d = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		basePanel = new BasePanel(this);
		
		this.setContentPane(basePanel);
		
		getRootPane().setBorder(new LineBorder(Color.black));
		this.setAlwaysOnTop(true);
		this.setSize(250, 100);
		this.setLocation((int)(d.getWidth()-this.getWidth()), (int)(d.getHeight()-this.getHeight()));
//		this.setUndecorated(true);
	}
	
	
	public void showNotificationWindow(String message, String headline) {
		if(ConfigurationHandlerImpl.reminderIsActive) {
			basePanel.setText(message, headline);
			this.setVisible(true);
		}
	}
	
	public void hideNotificationWindow() {
		this.setVisible(false);
	}
	
	
	/* action listener für den schließen-button */
	public void actionPerformed(ActionEvent e) {   
		// schließe das notification window
		hideNotificationWindow();
		
		// setze den reminder-zeitpunkt um eine gewisse zeit nach hinten
		
		final Date newCalcedDate = new Date(new Date().getTime() + Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("reminderDelayTimeSec"))*1000);
		
		System.out.println("DAT: " + tiwh.getLastStack().getReminderDate());
		
		if(tiwh.getLastStack().getReminderDate().before(newCalcedDate)) {
			
			tiwh.getLastStack()
				.getReminderDate()
				.setTime(newCalcedDate.getTime());
		}
		
//		parent.setSelectedTabByTodoItemStack(tiwh.getLastStack());

		
	}  
	
}



