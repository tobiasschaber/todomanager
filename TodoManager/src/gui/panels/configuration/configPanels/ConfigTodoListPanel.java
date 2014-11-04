package gui.panels.configuration.configPanels;

import gui.panels.configuration.AbstractConfigurationPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.ConfigurationHandlerImpl;

public class ConfigTodoListPanel extends AbstractConfigurationPanel implements ChangeListener, KeyListener {
	
	private final JCheckBox 	chkbx_alarm = new JCheckBox("ALARM");
	private final JCheckBox 	chkbx_done = new JCheckBox("DONE");
	private final JCheckBox 	chkbx_pending = new JCheckBox("PENDING");
	private final JCheckBox 	chkbx_todo = new JCheckBox("TODO");
	private final JCheckBox 	chkbx_wait = new JCheckBox("WAIT");
	private static final JLabel labDescDateFormat = new JLabel("Standard Datums-Format:");
	private static final JLabel labDescStatus = new JLabel("Standardmäßig angezeigte Status:");
	private static final JLabel	labDescSec = new JLabel("Sekunden");
	private static final JLabel	labDescReminderDelaySec = new JLabel("Zeit nach der eine Erinnerung erneut erscheint");
	private final JTextField 	txtDefaultDateFormat = new JTextField();
	private final JTextField	txtReminderDelaySec = new JTextField();
	
	public ConfigTodoListPanel() {

		initComponents();
		initListeners();
		initLayout();
	}
	
	@Override
	protected void initComponents() {


		txtDefaultDateFormat.setText(ConfigurationHandlerImpl.getInstance().getProperty("DefaultDateFormat"));
		txtReminderDelaySec.setText(ConfigurationHandlerImpl.getInstance().getProperty("reminderDelayTimeSec"));
		
		
		String defaultSelection = ConfigurationHandlerImpl.getInstance().getProperty("DefaultSelectionMode");
		
		if(defaultSelection.contains("TODO")) 		chkbx_todo.setSelected(true);
		if(defaultSelection.contains("PENDING"))	chkbx_pending.setSelected(true);
		if(defaultSelection.contains("WAIT")) 		chkbx_wait.setSelected(true);
		if(defaultSelection.contains("ALARM"))		chkbx_alarm.setSelected(true);
		if(defaultSelection.contains("DONE")) 		chkbx_done.setSelected(true);
		
		
	
	}
	
		
	private void initListeners() {
		
		chkbx_todo.addChangeListener(this);
		chkbx_pending.addChangeListener(this);
		chkbx_wait.addChangeListener(this);
		chkbx_alarm.addChangeListener(this);
		chkbx_done.addChangeListener(this);
		txtDefaultDateFormat.addKeyListener(this);
		txtReminderDelaySec.addKeyListener(this);
		
	}
	
	
	private void initLayout() {
		
		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labDescStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDescDateFormat)
                    .addComponent(labDescReminderDelaySec, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkbx_todo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkbx_wait)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkbx_pending)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkbx_alarm)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkbx_done))
                            .addComponent(txtDefaultDateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtReminderDelaySec, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labDescSec)))))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labDescStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkbx_todo)
                    .addComponent(chkbx_wait)
                    .addComponent(chkbx_pending)
                    .addComponent(chkbx_alarm)
                    .addComponent(chkbx_done))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labDescDateFormat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDefaultDateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labDescReminderDelaySec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtReminderDelaySec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labDescSec))
                .addContainerGap(152, Short.MAX_VALUE))
        );
	}
	
	
	@Override
	public String toString() {
		return "Todo-Listen";
	}
	
	@Override
	public String getHeadline() {
		return "Konfiguration der Todo-Listen";
	}
	
	
	
	public void stateChanged(ChangeEvent e) {
		String newMode = "";
		
		if(chkbx_todo.isSelected()) 	newMode += " TODO";
		if(chkbx_pending.isSelected()) 	newMode += " PENDING";
		if(chkbx_wait.isSelected()) 	newMode += " WAIT";
		if(chkbx_alarm.isSelected()) 	newMode += " ALARM";
		if(chkbx_done.isSelected()) 	newMode += " DONE";
		
		updateProperty("DefaultSelectionMode", newMode);
	}
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {

		if(e.getSource() == txtDefaultDateFormat) {
			updateProperty("DefaultDateFormat", txtDefaultDateFormat.getText());
		}
		
		if(e.getSource() == txtReminderDelaySec) {
			updateProperty("reminderDelayTimeSec", txtReminderDelaySec.getText());
		}
		
	}
	public void keyTyped(KeyEvent e) {}


	

}
