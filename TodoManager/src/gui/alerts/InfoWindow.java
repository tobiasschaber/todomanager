package gui.alerts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import main.Start;
import controller.ImageController;


/**
 * Das Info-Window, das Informationen über das Programm anzeigt
 * (Version, Release-Datum etc).
 * @author Tobias Schaber
 *
 */
public class InfoWindow extends JDialog implements ActionListener {

	
	
	private final JScrollPane 	jScrollPane1 	= new JScrollPane();
	private final JLabel 		labHeadline 	= new JLabel("TodoManager " + Start.version);
	private final JLabel 		labText 		= new JLabel("Release-Datum " + Start.releaseDate);
	private final JTextArea 	txtFieldInfo 	= new JTextArea("Copyright (c) 2012, 2013 by Tobias Schaber.\nDieser Todo-Manager ist kostenlos und\ndarf weitergegeben werden, solange\ndies kostenlos erfolgt.\n\nViel Spass damit wünscht\n\nTobias Schaber\n\nTobias.Schaber@gmx.de");
	private final JButton 		butClose 		= new JButton("Gelesen");
	private final JLabel 		labLogo 		= new JLabel();
		
	public InfoWindow() {
		
		
		initComponents();
		initLayout();
		initListeners();
        
		Dimension d = this.getToolkit().getScreenSize(); 
        this.setLocation((int) ((d.getWidth()/2 - this.getWidth()/2)), (int) ((d.getHeight() - this.getHeight()) / 2));      	
        
        setVisible(true);
	}
	
	
	
	
	public void initComponents() {
		getRootPane().setBorder(new LineBorder(Color.darkGray));
		
		this.setModal(true);
		labLogo.setIcon(ImageController.iconLogo);
        txtFieldInfo.setColumns(20);
        txtFieldInfo.setRows(5);
        jScrollPane1.setViewportView(txtFieldInfo);
        labHeadline.setFont(new java.awt.Font("Tahoma", 1, 14));  
	}


	
	public void initLayout() {
		setUndecorated(true);
		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(butClose))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(labLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labHeadline, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                            .addComponent(labText, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labHeadline)
                        .addGap(18, 18, 18)
                        .addComponent(labText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(butClose))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );


        pack();
	}
	
	
	public void initListeners() {
		butClose.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == butClose) {
			this.dispose();
		}
		
}

}
