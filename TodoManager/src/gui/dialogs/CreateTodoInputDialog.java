package gui.dialogs;
import gui.renderer.ComboBoxStatusRenderer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import data.TodoItem;
 
 
 
/**
 * Input dialog for newly created todos.
 * @author Tobias Schaber
 */
 
public class CreateTodoInputDialog extends JComponent implements ActionListener, ComponentListener {
	
    private static String[] 			result 			= { null, null, null };
    private static JComboBox<String> 	comboBoxStatus;
    private static JTextField 			txtTodoName;
    
    private final JDialog				dialog 			= new JDialog();
    
    private static final JSlider		sliderPriority 	= new JSlider();
    
    private static final JLabel			labPriority 	= new JLabel("Priorität");
    private static final JLabel			labStatus 		= new JLabel("Status");
    private static final JLabel			labName 		= new JLabel("Name des Todos");
    
    private static final JButton		butSubmit		= new JButton("Anlegen");
    
    private static String				title;
    
    
    public CreateTodoInputDialog(String title) {
    	CreateTodoInputDialog.title = title;
    	initComponents();
    	initLayout();
    	initListeners();
    	
    	dialog.setVisible(true);

    }
    
    
    private void initComponents() {
    
    	sliderPriority.setMajorTickSpacing(0);
    	
    	txtTodoName = new JTextField();
        comboBoxStatus = new JComboBox<String>();
        comboBoxStatus.setRenderer(new ComboBoxStatusRenderer(comboBoxStatus));
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(600, 140);
        dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 125, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 75);
        comboBoxStatus.setModel(new DefaultComboBoxModel<String>(new String[] {
        		TodoItem.STATUS_TODO, 
         		TodoItem.STATUS_WAIT, 
         		TodoItem.STATUS_PENDING,
         		TodoItem.STATUS_ALARM,
         		TodoItem.STATUS_DONE }));
    	
         
        final FocusTraversalPolicy orgFocusPolicy = dialog.getFocusTraversalPolicy();
         
        /* diese focus traversal policy wird benötigt, um dem text-feld für das neue todo den fokus zu geben */
        dialog.setFocusTraversalPolicy(new FocusTraversalPolicy() {
        
           @Override
           public Component getDefaultComponent(Container container) {
             return txtTodoName;
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
    
    private void initLayout() {
    	
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(labName, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                        .addComponent(labStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtTodoName)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(butSubmit)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(comboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labPriority)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sliderPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 376, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labStatus)
                            .addComponent(comboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labPriority))
                        .addComponent(sliderPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labName)
                        .addComponent(txtTodoName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(butSubmit)
                    .addContainerGap(13, Short.MAX_VALUE))
            );

    }
    
    
    
    private void initListeners() {
    	
    	dialog.addComponentListener(this);
    	
    	txtTodoName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)
                        OKPressed();
                
                if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                	cancelPressed();
                }
            }
        });
        
        butSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        });
      
        comboBoxStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)
                        OKPressed();
            }
        }); 
        
    }
    
    public static String[] showInputDialog(String title) {
        
    	// default-wert des priority sliders setzen
        sliderPriority.setValue(20);
        new CreateTodoInputDialog(title);
        
        return result;
    }
 
    
    
    private void OKPressed() {
        result[0] = (String)comboBoxStatus.getSelectedItem();
        result[1] = String.valueOf(txtTodoName.getText());
        result[2] = ""+sliderPriority.getValue();
        
        dialog.setVisible(false);
        dialog.dispose();
    }
    
    private void cancelPressed() {
    	result[0] = null;
    	result[1] = null;
    	result[2] = null;
    	
    	dialog.setVisible(false);
    	dialog.dispose();
    	
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void componentMoved(ComponentEvent e) {}   
    @Override
    public void componentResized(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {
    	
    	// Ergebnis leeren, wenn das Fenster neu angezeigt wird
    	result[0] = null;
    	result[1] = null;
    	result[2] = null;
    	
    }
    
    
}