package gui.dialogs;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.*;
 
 
 
/**
 * Input dialog to choose a color.
 * @author Tobias Schaber
 */
public class ChooseColorInputDialog extends JComponent {
	
    private static Color 				result;
    private static JDialog 				dialog = new JDialog();
    
    private static JColorChooser		jcc;
    
    private static final JButton		butSubmit		= new JButton("Auswählen");
    private static final JButton		butCancel		= new JButton("Abbrechen");
    
    private static String				title;
    
    
    /**
     * Default constructor
     * @param title
     * @param initColor
     */
    public ChooseColorInputDialog(String title, Color initColor) {
    	
    	
    	ChooseColorInputDialog.title = title;

    	initComponents(initColor);
    	initLayout();
    	initListeners();
    	
    	dialog.setVisible(true);

    }
    
    
    private void initComponents(Color initColor) {
        	
    	
    	
    	jcc = new JColorChooser(initColor);
    	dialog = new JDialog();
    	
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(610, 410);
        dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 125, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 75);
         
    }
    
    public void initLayout() {
    	
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcc, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(butSubmit)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(butCancel)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jcc, javax.swing.GroupLayout.PREFERRED_SIZE, 334, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(butSubmit)
                        .addComponent(butCancel))
                    .addContainerGap())
            );

    }
    
    
    
    private void initListeners() {
    	
    	dialog.addComponentListener(new ComponentAdapter() {
    		
    	    @Override
    	    public void componentShown(ComponentEvent e) {
    	    	// Ergebnis leeren, wenn das Fenster neu angezeigt wird
    	    	result = null;
    	    }
		});
    	
        butSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        });
        
        butCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelPressed();
				
			}
		});
        
    }
    
    public static Color showInputDialog(String title, Color initColor) {
    	jcc = null;
    	
    	new ChooseColorInputDialog(title, initColor);
        return result;
    }
 
    
    
    private void OKPressed() {
        result = jcc.getColor();

        dialog.setVisible(false);
        dialog.dispose();
    }
    
    private void cancelPressed() {
    	result = null;
    	
    	dialog.setVisible(false);
    	dialog.dispose();
    	
    }
    
    
}