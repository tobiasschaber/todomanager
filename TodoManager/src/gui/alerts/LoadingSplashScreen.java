package gui.alerts;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.LineBorder;

import controller.ImageController;

/**
 * Splash Screen wenn das Programm lädt.
 * @author Tobias Schaber
 */
public class LoadingSplashScreen extends JDialog {

	private static final LoadingSplashScreen INSTANCE = new LoadingSplashScreen();
	
	private final JLabel 		labMessage 	= new JLabel();
	private final JProgressBar	loadBar 	= new JProgressBar();
	private final JLabel 		bgImage 	= new JLabel();
	

	
	/**
	 * returns a singleton instance of the splash screen
	 * @return
	 */
	public static LoadingSplashScreen getInstance() {
		return INSTANCE;
	}
	
	
	
	/**
	 * private constructor
	 */
	private LoadingSplashScreen() {
		
		initComponents();
		initLayout();
        
		Dimension d = this.getToolkit().getScreenSize(); 
        this.setLocation((int) ((d.getWidth()/2 - this.getWidth()/2)), (int) ((d.getHeight() - this.getHeight()) / 2));      	
        
	}

	
	
	public void initComponents() {
		getRootPane().setBorder(new LineBorder(Color.darkGray));
		
		bgImage.setIcon(ImageController.iconSplashBg);
		
	}


	
	public void initLayout() {
		
		setAlwaysOnTop(true);
		setUndecorated(true);

        bgImage.setMaximumSize(new Dimension(480, 256));
        bgImage.setMinimumSize(new Dimension(480, 256));
        bgImage.setPreferredSize(new Dimension(480, 256));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(loadBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(bgImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(bgImage, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labMessage)
                    .addComponent(loadBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
	}
	

	
	/**
	 * zeigt das splash fenster an mit einem fortschritt von 0%
	 * @param toShow der Text der angezeigt werden soll
	 */
	public void showSplash(String toShow) {

		loadBar.setValue(0);
		labMessage.setText(toShow);
		setVisible(true);

	}
	
	
	/**
	 * aktualisiert den prozentualen fortschritt des splash screens
	 * @param newVal
	 */
	public void setPercentage(int newVal) {
		loadBar.setValue(newVal);
		
	}
	
	
	/**
	 * versteckt den splash screen wieder
	 */
	public void hideSplash() {
		setVisible(false);

	}

}