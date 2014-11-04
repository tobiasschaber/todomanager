package gui.components;

import gui.panels.todoeditor.TodoListBasePanel;

import java.awt.Component;
import java.awt.Font;

import javax.swing.*;


/**
 * Dieses Panel stellt einen Editor dar, der angezeigt werden kann, wenn
 * das kleine Editor-Feld bei Todos nicht mehr ausreicht.
 * @author Tobias Schaber
 */
public class TodoTextEditor extends JPanel {
	
	
	private final Component parent;
	
	private final JTextArea 	textField 		= new JTextArea();
	private final JButton	  	butClose  		= new JButton("Fertig");
	private final JScrollPane 	jScrollPane1 	= new JScrollPane();
	private final JLabel		labDesc 		= new JLabel(" Beschreibungs-Text anpassen und dann auf \"Fertig\" drücken.");
	
	
	/**
	 * constructor
	 * @param parent
	 * @param sourceText default text der initial in den editor eingefügt wird
	 */
	public TodoTextEditor(Component parent, String sourceText) {
		
		
		this.parent = parent;
		
		textField.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
		textField.setText(sourceText);
		
		initComponents();
		initLayout();
		initListeners();
        
        setVisible(true);
        
        
	}

	
	
	/**
	 * liefert den text aus dem editor zurück
	 * @return der text des editors
	 */
	public String getText() {
		return textField.getText();
	}
	
	
	
	private void initListeners() {
	
		// listener zum Schließen des editors
		butClose.addMouseListener((TodoListBasePanel)parent);
	}
	
	
	
	private void initComponents() {	
		jScrollPane1.setViewportView(textField); 
	}

	
	private void initLayout() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(butClose))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(butClose)
                    .addComponent(labDesc)))
        );
	}
	
	
}
