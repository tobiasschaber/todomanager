package gui.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import data.Attachment;

/**
 * Auswahl-Dialog (modal) um den Speicher-Modus für ein Attachment auszuwählen.
 * Der Speicher-Modus wird über eine Radio-Group ausgewählt, es wird an der Seite
 * eine ausführliche Beschreibung angezeigt.
 * @author Tobias Schaber
 */
public class SelectAttachmentStoreModeDialog  extends JComponent implements ActionListener {
	

	
    private final JDialog dialog = new JDialog();
    
    private static int result = -1;
	private static String title;
	
	private static final JLabel labHeadline = new JLabel("Wie sollen die hinzugefügten Dateien gespeichert werden?");
	private static final JTextArea labLongDesc = new JTextArea();
	
	
	
	private static final ButtonGroup  radioSaveGroup  = new ButtonGroup();
	private static final JRadioButton radioSaveIntern = new JRadioButton("In die Todo-Liste aufnehmen (Datei wird kopiert)");
	private static final JRadioButton radioSaveLocal  = new JRadioButton("Datei nur verlinken (auf diesem PC belassen)");
	
	private static final JButton butSave = new JButton("Speichern");
	private static final JButton butCancel = new JButton("Abbrechen");
	
	private static final String longDescIntern = 
			"Das Attachment wird im Todo-Projekt\n" +
			"gespeichert. Dadurch wird die\n" +
			"Projekt-Datei größer, jedoch kann\n" +
			"das Attachment so auf andere\n" +
			"Computer mitgenommen werden und ist\n" +
			"immer verfügbar.";
	
	private static final String longDescLocal =
			"Das Attachment verbleibt an dem\n" +
			"Platz, an dem es sich befindet.\n" +
			"Das Todo-Projekt bleibt kleiner,\n" +
			"wird die Datei jedoch vom Computer\n" +
			"gelöscht so ist sie auch im\n" + 
			"Projekt nicht mehr verfügbar.";
	
	
	public SelectAttachmentStoreModeDialog(String title) {
		SelectAttachmentStoreModeDialog.title = title;

		initComponents();
		initLayout();
		initListeners();
		
		dialog.setVisible(true);
	}


	/**
	 * Achtung: "this" ersetzen durch "dialog.getContentPane()"
	 */
	private void initLayout() {


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labHeadline, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(radioSaveLocal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(butSave)
                                .addGap(18, 18, 18)
                                .addComponent(butCancel))
                            .addComponent(radioSaveIntern, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(labLongDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labHeadline)
                        .addGap(18, 18, 18)
                        .addComponent(radioSaveIntern)
                        .addGap(8, 8, 8)
                        .addComponent(radioSaveLocal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(labLongDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(butSave)
                    .addComponent(butCancel))
                .addContainerGap())
        );
	}


	private void initListeners() {
		butSave.addActionListener(this);
		butCancel.addActionListener(this);
		
		radioSaveIntern.addActionListener(this);		
		radioSaveLocal.addActionListener(this);
	}

	
	
	private void initComponents() {
		
		labLongDesc.setBackground(getBackground());
		labLongDesc.setEnabled(false);
        labLongDesc.setColumns(20);
        labLongDesc.setRows(5);
        labLongDesc.setText(longDescLocal);
        labLongDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        
		radioSaveGroup.add(radioSaveIntern);
		radioSaveGroup.add(radioSaveLocal);
		radioSaveLocal.setSelected(true);

		dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(640, 240);
        dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 125, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 75);
        
        final FocusTraversalPolicy orgFocusPolicy = dialog.getFocusTraversalPolicy();
        
        /* diese focus traversal policy wird benötigt, um dem Speichern-Button den fokus zu geben */
        dialog.setFocusTraversalPolicy(new FocusTraversalPolicy() {
        
           @Override
           public Component getDefaultComponent(Container container) {
             return butSave;
           }
        
           @Override
           public Component getFirstComponent(Container container) {
             return orgFocusPolicy.getFirstComponent(container);
           }
        
           @Override
           public Component getLastComponent(Container container) {
             return orgFocusPolicy.getLastComponent(container);
           }
        
           @Override
           public Component getComponentBefore(
                 Container container,
                 Component component) {
             return orgFocusPolicy.getComponentBefore(container, component);
           }
        
           @Override
           public Component getComponentAfter(
                 Container container,
                 Component component) {
             return orgFocusPolicy.getComponentAfter(container, component);
           }
         });
		
	}
	
	
	
    public static int showAttachmentStoreModeDialog(String title) {
        
        new SelectAttachmentStoreModeDialog(title);
        
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    	
    	if(arg0.getSource() == butSave) {
    		if(radioSaveIntern.isSelected()) result = Attachment.STORE_MODE_INTERN;
    		if(radioSaveLocal.isSelected())  result = Attachment.STORE_MODE_LOCAL;
    	
    		dialog.setVisible(false);
    		dialog.dispose();
    		return;
    	}
    	
    	
    	
    	if(arg0.getSource() == butCancel){
    		result = Attachment.STORE_MODE_NOSELECTION;
    		dialog.setVisible(false);
    		dialog.dispose();
    		return;
    	}
    	
    	
    	if(arg0.getSource() == radioSaveIntern) {
			labLongDesc.setText(longDescIntern);
    	}
    	
    	if(arg0.getSource() == radioSaveLocal) {
			labLongDesc.setText(longDescLocal);
    	}
    	

    }

}
