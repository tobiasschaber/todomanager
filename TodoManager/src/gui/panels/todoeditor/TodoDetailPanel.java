package gui.panels.todoeditor;

import gui.dialogs.SelectAttachmentStoreModeDialog;
import gui.menus.AttachmentContextMenu;
import gui.models.AttachmentListModel;
import gui.renderer.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Logger;

import org.apache.commons.io.FileUtils;

import com.toedter.calendar.JDateChooser;

import controller.ConfigurationHandlerImpl;
import controller.ImageController;
import data.*;

/**
 * Das Panel, welches die Detail-Informationen zu einem TodoItemStack anzeigt.
 * @author Tobias Schaber
 */
public class TodoDetailPanel extends JPanel implements ListSelectionListener, ActionListener, KeyListener {
	
	/* Pfad zum teporären Folder für Attachments. Backslash am Ende nicht vergessen! */
	public static final String ATTACHMENT_TEMP_FOLDER   = "attachments/"; 
	
	private final AttachmentContextMenu		attachmentContextMenu = new AttachmentContextMenu();
	
	private final JScrollPane scrollpaneText 			= new JScrollPane();
    private final JScrollPane scrollPaneTodoHistoryList	= new JScrollPane();
    private final JScrollPane scrollpaneAttachmentList  = new JScrollPane();
    
    private final JButton buttonSave 					= new JButton("Speichern");
    private final JCheckBox chkBoxReminderActive 		= new JCheckBox();
    private final JComboBox<String> comboBoxStatus 		= new JComboBox<String>();
    private final JComboBox<String>	comboBoxCategory	= new JComboBox<String>();
    
    private final JSlider sliderPriority				= new JSlider();
    
    private final JLabel labOpenTextEditor		= new JLabel();
    private final JLabel labAddEntry 			= new JLabel("Hinzufügen");
    private final JLabel labDescription 		= new JLabel("Beschreibung");
    private final JLabel labReminder 			= new JLabel("Erinnerung");
    private final JLabel labStatus 				= new JLabel("Status");
    private final JLabel labText 				= new JLabel("Text");
    private final JLabel labDatum				= new JLabel("Datum");
    private final JLabel labUhrzeit				= new JLabel("Uhrzeit");
    private final JLabel labTodoItem			= new JLabel("Todo-Item");
    private final JLabel labPriority			= new JLabel("Priorität");
    private final JLabel labTimePlan			= new JLabel("Zeitplan");
    private final JLabel labTimePlanStart		= new JLabel("Start");
    private final JLabel labTimePlanEnd			= new JLabel("Ende");
    private final JLabel labCategory			= new JLabel("Kategorie");
    private final JLabel labAttachments			= new JLabel("Attachments");
    private final JLabel labOpenAttachmentDir	= new JLabel();
    
    private final DefaultListModel<TodoItem> todoHistoryModel = new DefaultListModel<TodoItem>();
    private final AttachmentListModel attachmentModel = new AttachmentListModel(null); 
    
    
    private final JDateChooser 	txtFieldDate			= new JDateChooser();
    private final JDateChooser 	txtFieldDateStart		= new JDateChooser();
    private final JDateChooser 	txtFieldDateEnd			= new JDateChooser();
    private final JTextField 	txtFieldDescription 	= new JTextField();
    private final JTextField 	txtFieldTime 			= new JTextField();

    
    private final JTextArea textAreaText 				= new JTextArea();
    private JList<TodoItem> todoHistoryList 			= new JList<TodoItem>(todoHistoryModel);
    private JList<Attachment> listAttachments			= new JList<Attachment>();		
    
    private final JSeparator jSeparator1				= new JSeparator();
    
    private TodoListBasePanel parent;

	public TodoDetailPanel(TodoListBasePanel parent) {
		
        // drop target für file drag&drop hinzufügen
		listAttachments.setDropTarget(new DropTarget() {
			
			public synchronized void drop(DropTargetDropEvent evt) {
				
				if(TodoDetailPanel.this.parent.getTodoList().isLocked()) {
					return;
				}

				try { 
					int storeMode = SelectAttachmentStoreModeDialog.showAttachmentStoreModeDialog("Speichermodus auswählen");
					
					
					/* Dialog wurde abgebrochen -> Keine Verarbeitung der Dateien */
					if(storeMode == Attachment.STORE_MODE_NOSELECTION) {
						return;
					} 
					
		            evt.acceptDrop(DnDConstants.ACTION_COPY);
		            
		            boolean hasFolderWarnings = false;
		            
		            @SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor); 
		            for (File file : droppedFiles) {	
		            	
		            	if(file.isDirectory() && storeMode == Attachment.STORE_MODE_INTERN) {
		            		hasFolderWarnings = true;
		            		addFileToAttachments(file, Attachment.STORE_MODE_LOCAL);
		            	} else {
		            		addFileToAttachments(file, storeMode);
		            	}
		                
		            }
		            
		            if(hasFolderWarnings) {

		            	String showMessage = "Verzeichnisse können nicht intern gespeichert werden. \nFolgende Verzeichnisse wurden verlinkt: \n\n";
		            	
		            	for(File f : droppedFiles) {
		            		if(f.isDirectory()) {
		            			showMessage += f.getName() + "\n";
		            		}
		            	}
		            	
		            	JOptionPane.showMessageDialog(TodoDetailPanel.this.parent, showMessage, "Fehler beim hinzufügen eines Attachments", JOptionPane.WARNING_MESSAGE);
		            }
		            
		        } catch (Exception ex) {
		        	Logger.getInstance().logException("Fehler beim Einfügen eines Attachments", ex);

		        }} 
		    });
		
		this.parent = parent;
		initComponents();
		initListeners();
		initLayout();
		
		
	}
	

	
	
	private void initLayout() {


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labOpenTextEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollpaneText, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labPriority)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labTodoItem)
                    .addComponent(scrollPaneTodoHistoryList, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labReminder, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTimePlan, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labUhrzeit, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkBoxReminderActive, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFieldTime, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonSave)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(labDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtFieldDate))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labTimePlanEnd, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                                .addComponent(labTimePlanStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txtFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txtFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollpaneAttachmentList, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labAttachments, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labOpenAttachmentDir, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labTodoItem)
                            .addComponent(labReminder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labAttachments)
                            .addComponent(labOpenAttachmentDir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(scrollPaneTodoHistoryList, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labDescription)
                                            .addComponent(txtFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labDatum)
                                            .addComponent(txtFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labUhrzeit)
                                            .addComponent(txtFieldTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkBoxReminderActive)
                                        .addGap(18, 18, 18)
                                        .addComponent(labTimePlan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labTimePlanStart)
                                            .addComponent(txtFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(labStatus)
                                                .addComponent(comboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(labPriority))
                                            .addComponent(sliderPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(scrollpaneText)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(labText)
                                                .addGap(68, 68, 68)
                                                .addComponent(labOpenTextEditor)
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(labTimePlanEnd))
                                        .addGap(22, 22, 22)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(labCategory)
                                            .addComponent(comboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonSave))))
                            .addComponent(scrollpaneAttachmentList))))
                .addContainerGap())
        );
	}
	
	
	private void initToolTips() {
		
		labDescription.setToolTipText("Kurze Beschreibung des aktuellen Zustands");
		labStatus.setToolTipText("Status, in dem sich das Todo derzeit befindet");
		labText.setToolTipText("Ausführliche Beschreibung oder zusätzliche Details zum aktuellen Status");
		labPriority.setToolTipText("Priorität des Todos. Je weiter rechts der Reiter steht, desto höher ist die Priorität");
		labDatum.setToolTipText("An welchem Datum soll die Erinnerung angezeigt werden?");
		labUhrzeit.setToolTipText("Zu welcher Uhrzeit soll die Erinnerung angezeigt werden?");
		labReminder.setToolTipText("Hier kann eine Erinnerung konfiguriert werden, die zu gegebener Zeit eine Meldung im Tray anzeigt");
		labTimePlan.setToolTipText("Hier kann die Dauer des Todos eingetragen werden. Diese wird in der Übersicht im Zeitstrahl angezeigt");
		labCategory.setToolTipText("Gibt eine Kategorie für das Todo an. Kategorien können über das Menü \"Einstellungen\" konfiguriert werden");
		labAttachments.setToolTipText("Dem Todo können Dateien als Anhang hinzugefügt werden");
		labTimePlanStart.setToolTipText("Startzeitpunkt des Todos");
		labTimePlanEnd.setToolTipText("Endzeitpunkt des Todos");
		
	}

	
	
	private void initComponents() {
	

		
		labOpenAttachmentDir.setIcon(ImageController.iconDirectory);
		labOpenAttachmentDir.setToolTipText("Temporäres Verzeichnis für Attachments öffnen");
		
		listAttachments.setModel(attachmentModel);

		labTimePlan.setToolTipText("Diese Werte dienen der Anzeige im Zeitstrahl");
		labOpenTextEditor.setIcon(ImageController.iconOpenEditor);
		labOpenTextEditor.setToolTipText("Im Extra-Editor öffnen");
		
		labOpenTextEditor.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				labOpenTextEditor.setBorder(new LineBorder(Color.gray));
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				labOpenTextEditor.setBorder(new LineBorder(getBackground()));
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				labOpenTextEditor.setBorder(new LineBorder(getBackground()));
			}
		});

		
        comboBoxStatus.setRenderer(new ComboBoxStatusRenderer(comboBoxStatus));
        comboBoxStatus.setModel(new DefaultComboBoxModel<String>(new String[] {
        		TodoItem.STATUS_TODO, 
        		TodoItem.STATUS_WAIT, 
        		TodoItem.STATUS_PENDING,
        		TodoItem.STATUS_ALARM,
        		TodoItem.STATUS_DONE }));
        
        comboBoxCategory.setRenderer(new ComboBoxCategoryRenderer(comboBoxCategory));
        
        labAddEntry.setFont(new Font("Tahoma", 1, 11));
        labTodoItem.setFont(new Font("Tahoma", Font.BOLD, 11));
        labReminder.setFont(new Font("Tahoma", Font.BOLD, 11));
        labTimePlan.setFont(new Font("Tahoma", Font.BOLD, 11));
        labCategory.setFont(new Font("Tahoma", Font.BOLD, 11));
        labAttachments.setFont(new Font("Tahoma", Font.BOLD, 11));
        
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        textAreaText.setColumns(20);
        textAreaText.setRows(5);
        textAreaText.setFont(new Font("Tahoma", Font.PLAIN, 11));
        textAreaText.setLineWrap(true);
        scrollpaneText.setViewportView(textAreaText);
        scrollPaneTodoHistoryList.setViewportView(todoHistoryList);
        chkBoxReminderActive.setText("Erinnere mich");
        
        scrollpaneAttachmentList.setViewportView(listAttachments);

        txtFieldDescription.setPreferredSize(new Dimension(6, 20));

        todoHistoryList.setCellRenderer(new TodoHistoryRenderer());
        listAttachments.setCellRenderer(new AttachmentListRenderer());
        
        Color c = Color.blue;
		try { c = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("todoListColorSelection"))); }
		catch(Exception e) {}
        
		todoHistoryList.setSelectionBackground(c);
		todoHistoryList.setSelectionForeground(Color.black);
		
		setEditorEnabled(false);

		initToolTips();
	}
	
	
	
	/**
	 * initialisiert die listener
	 */
	private void initListeners() {
		
		labOpenAttachmentDir.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseEntered(MouseEvent e) {
				labOpenAttachmentDir.setIcon(ImageController.iconDirectoryOpen);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				labOpenAttachmentDir.setIcon(ImageController.iconDirectory);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + new File(ATTACHMENT_TEMP_FOLDER));
				} catch(Exception ex) {
					Logger.getInstance().logException("Fehler beim Öffnen des Attachmant Verzeichnisses", ex);
				}
				
			}
			
			
		});
		
		

		labOpenTextEditor.addMouseListener(parent);
		todoHistoryList.getSelectionModel().addListSelectionListener(this);
		
		buttonSave.addActionListener(this);
		comboBoxCategory.addKeyListener(this);
		
		listAttachments.addKeyListener(this);
		
		// Mouse-Listener für Doppelklick -> Datei öffnen
		listAttachments.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent me) {
				
				if(me.getClickCount() == 2) {
					
					/* Herausfinden, welche Datei in der Liste angeklickt wurde */
					Attachment att = attachmentModel.getElementAt(listAttachments.locationToIndex(me.getPoint()));
					
					if(att == null) return;
					
					
					
					/* f ist die Datei die gleich geöffnet wird */
					File f = att.getPathToFile();
					
					/* im Speichermodus "intern" -> Datei aus Speicher in tempporäre Datei kopieren */
					if(att.getStorageMode() == Attachment.STORE_MODE_INTERN) {
						Logger.getInstance().log("Lege temporäre Datei an für Attachment: " + f.getAbsolutePath(), Logger.LOGLEVEL_INFO);
						
						/* Temporäre Datei */
						f = new File(TodoDetailPanel.ATTACHMENT_TEMP_FOLDER + att.getTemporaryFileName());
						
						// Datei nur schreiben wenn es sie noch nicht gibt
						if(!f.exists()) {
						
							try {
								/* schreibe temporäre Datei in Unterverzeichnis */
								FileUtils.writeByteArrayToFile(f, att.getContent());
							} catch(Exception e) {
								Logger.getInstance().logException("Konnte temporäre Datei nicht speichern: " + f.getAbsolutePath(), e);
							}
						}
					}

					try {
						/* Datei nun in extermen Programm öffnen */
						Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + f);
					}
					catch(Exception ioe) {
						Logger.getInstance().logException("Kann die Datei nicht öffnen: " + f.getAbsolutePath(), ioe);
					}
					
					

					
				}
			}
			
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				if(SwingUtilities.isRightMouseButton(e)) {
					
					listAttachments.setSelectedIndex(listAttachments.locationToIndex(e.getPoint()));
					
					Attachment selectedAttachment = listAttachments.getSelectedValue();
					
					attachmentContextMenu.show(e.getComponent(), e.getX(), e.getY(), selectedAttachment);

				}
				
			}
		});
		
	}
	
	
	
	/**
	 * Aktiviert bzw. deaktiviert all im Todo Editor enthaltenen Felder und Beschreibungen
	 * @param b
	 */
	private void setEditorEnabled(boolean b) {
		
		String[] cats = new String[ConfigurationHandlerImpl.getInstance().getCategories().size()];
		

		ConfigurationHandlerImpl.getInstance().getCategories().keySet().toArray(cats);
		comboBoxCategory.setModel(new DefaultComboBoxModel<String>(cats));
		
		labOpenAttachmentDir.setEnabled(b);
		labCategory.setEnabled(b);
		comboBoxCategory.setEnabled(b);
		labText.setEnabled(b);
        txtFieldDescription.setEnabled(b);
        textAreaText.setEnabled(b);
        txtFieldDate.setEnabled(b);
        txtFieldTime.setEnabled(b);
        labOpenTextEditor.setEnabled(b);
        labAddEntry.setEnabled(b);
        labDatum.setEnabled(b);
        labUhrzeit.setEnabled(b);
        labReminder.setEnabled(b);
        chkBoxReminderActive.setEnabled(b);
        labDescription.setEnabled(b);
        labStatus.setEnabled(b);
        comboBoxStatus.setEnabled(b);
        labTodoItem.setEnabled(b);
        buttonSave.setEnabled(b);
        labPriority.setEnabled(b);
        sliderPriority.setEnabled(b);
        scrollpaneText.setEnabled(b);
        labAttachments.setEnabled(b);
        listAttachments.setEnabled(b);
        scrollpaneAttachmentList.setEnabled(b);
        
        labTimePlan.setEnabled(b);
        labTimePlanEnd.setEnabled(b);
        labTimePlanStart.setEnabled(b);
        txtFieldDateEnd.setEnabled(b);
        txtFieldDateStart.setEnabled(b);
		
	}
	
	
	
	/**
	 * zeigt die details eines todoitems in den einzelnen feldern an
	 */
	public void showTodoItemDetails() {
		
		// reset date fields
		txtFieldDate.setDate(null);
		txtFieldTime.setText("");
		
		labTodoItem.setText("Todo-Item");
		
		TodoItemStack currentTis = parent.getTodoListPanel().getSelectedTodoItemStack();
		int indexOfTodoItem = todoHistoryList.getSelectedIndex();
		
		TodoItem currentTi = currentTis.getTodoHistory().elementAt(indexOfTodoItem);
		
		
	    txtFieldDescription.setText(currentTi.getDescription());
	    txtFieldDate.setDate(currentTis.getReminderDate());
	    textAreaText.setText(currentTi.getText());
		
	    if(currentTi.getStatus().equals(TodoItem.STATUS_TODO))		comboBoxStatus.setSelectedIndex(0);
	    if(currentTi.getStatus().equals(TodoItem.STATUS_WAIT))		comboBoxStatus.setSelectedIndex(1);
	    if(currentTi.getStatus().equals(TodoItem.STATUS_PENDING))	comboBoxStatus.setSelectedIndex(2);
	    if(currentTi.getStatus().equals(TodoItem.STATUS_ALARM))		comboBoxStatus.setSelectedIndex(3);
	    if(currentTi.getStatus().equals(TodoItem.STATUS_DONE))		comboBoxStatus.setSelectedIndex(4);
	    
			
		if(currentTis.getReminderDate() != null) {
			
		    GregorianCalendar gc = new GregorianCalendar();
		    gc.setTimeZone(TimeZone.getTimeZone("CET") );
		    gc.setTime(currentTis.getReminderDate());
		    gc.set(GregorianCalendar.MINUTE, 0);
		    gc.set(GregorianCalendar.HOUR, 0);
		    gc.set(GregorianCalendar.SECOND, 0);
		    gc.set(GregorianCalendar.MILLISECOND, 0);
		    
		    txtFieldDate.setDate(gc.getTime());
		
		    SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm");
		    
		    if(currentTis.getReminderDate() != null) txtFieldTime.setText(sdf.format(currentTis.getReminderDate()));
		}
	
		sliderPriority.setValue(currentTis.getPriority());
		
		txtFieldDateStart.setDate(currentTis.getPlanStart());
		txtFieldDateEnd.setDate(currentTis.getPlanEnd());
		labTodoItem.setText("Todo-Item (" +currentTis.getNotModifiedDurationHours()/24 + " Tage ohne Änderung)");
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {

		/** ***************************************************************** BUTTON SAVE */
		if(e.getSource() == buttonSave) {
			
			Logger.getInstance().log("Starte Speichervorgang", Logger.LOGLEVEL_INFO);

			
			try {
				
				/* hole derzeit bearbeiteten todo item stack */
				TodoItemStack currentTis = parent.getTodoListPanel().getSelectedTodoItemStack();
				
				/* prüfen ob sich die inhalte der todo felder geändert haben */
				if(!currentTis.getLatestTodo().getDescription().equals(txtFieldDescription.getText()) ||
				   !currentTis.getLatestTodo().getText().equals(textAreaText.getText()) ||
				   !currentTis.getLatestTodo().getStatus().equals(comboBoxStatus.getSelectedItem())) {

					/* hänge das neue todo item dem todo item stack an */
					currentTis.updateTodo(comboBoxStatus.getSelectedItem().toString(), textAreaText.getText(), txtFieldDescription.getText());
				
				}
				
				

				
				long msecs = 0;

				/* wenn das Zeit-Fenster gesetzt und nicht leer ist, dann hole Reminder-Uhrzeit raus */
				if(!txtFieldTime.getText().equals("") && txtFieldTime.getText() != null) {
										
					SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm");
					Date d = sdf.parse(txtFieldTime.getText());
					
					
					msecs = d.getTime();
					
					GregorianCalendar gc2 = new GregorianCalendar();

					gc2.setTimeInMillis(msecs);
					
					msecs = gc2.getTimeInMillis();
					
				}
				
				

				// End-Zeitpunkt aktualisieren
				// --------------------------------------------------------------------------------------------
				// Änderung von wert auf null ("zurücksetzen")
				if(txtFieldDate.getDate() == null && currentTis.getReminderDate() != null) {
					currentTis.setReminderDate(null);
					if(currentTis.getReminderActivationStatus()) currentTis.setReminderActivationStatus(false);
					
				} else {
					// wenn nicht beide null sind
					if(!(txtFieldDate.getDate() == null && currentTis.getReminderDate() == null)) {

						GregorianCalendar gc = new GregorianCalendar();

						gc.setTime(txtFieldDate.getDate());
						gc.set(GregorianCalendar.HOUR, 0);
						gc.set(GregorianCalendar.MINUTE, 0);
						gc.set(GregorianCalendar.SECOND, 0);
				    
						/* manchmal liefert der JDateChooser Nachmittag, 12 Uhr zurück. das muss auf 0 gesetzt werden */
						if(gc.get(GregorianCalendar.HOUR_OF_DAY) == 12) {
							gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
						}
				    			    
						Date sumDate = new Date((gc.getTimeInMillis()+msecs + 3600000));

						// wenn nicht beide gleich sind
						if(!(sumDate.equals(currentTis.getReminderDate()))){
							

						
					    
							currentTis.setReminderDate(sumDate);
							
						}
					}
				
			
				}
				
				// wenn der reminder sich geändert hat
				if(currentTis.getReminderActivationStatus() != chkBoxReminderActive.isSelected()) {
					// wenn der reminder deaktiviert wird, oder wenn er aktiviert wird und das datum != null ist
					if(!chkBoxReminderActive.isSelected() || (chkBoxReminderActive.isSelected() && currentTis.getReminderDate() != null)) {
						currentTis.setReminderActivationStatus(chkBoxReminderActive.isSelected());
					}
				}

				
				// prüfen ob sich die priority geändert hat
				
				if(currentTis.getPriority() != sliderPriority.getValue()) {
					currentTis.setPriority(sliderPriority.getValue());
				}
				
	
				
				// Start-Zeitpunkt aktualisieren
				// --------------------------------------------------------------------------------------------
				// Änderung von wert auf null ("zurücksetzen")
				if(txtFieldDateStart.getDate() == null && currentTis.getPlanStart() != null) {
					currentTis.setPlanStart(null);
				} else {
					// wenn nicht beide null sind
					if(!(txtFieldDateStart.getDate() == null && currentTis.getPlanStart() == null)) {

						// wenn nicht beide gleich sind
						if(!(txtFieldDateStart.getDate().equals(currentTis.getPlanStart()))){
							
							currentTis.setPlanStart(txtFieldDateStart.getDate());
						}
					}
				}
				
				// End-Zeitpunkt aktualisieren
				// --------------------------------------------------------------------------------------------
				// Änderung von wert auf null ("zurücksetzen")
				if(txtFieldDateEnd.getDate() == null && currentTis.getPlanEnd() != null) {
					currentTis.setPlanEnd(null);
				} else {
					// wenn nicht beide null sind
					if(!(txtFieldDateEnd.getDate() == null && currentTis.getPlanEnd() == null)) {

						// wenn nicht beide gleich sind
						if(!(txtFieldDateEnd.getDate().equals(currentTis.getPlanEnd()))){
							
							currentTis.setPlanEnd(txtFieldDateEnd.getDate());
						}
					}
				}
				
				/* category aktualisieren */

				if(comboBoxCategory.getSelectedIndex() != -1) {
					String selectedCat = (String)comboBoxCategory.getSelectedItem();

					if(selectedCat != null && !selectedCat.equals(currentTis.getCategory())) {

						currentTis.setCategory(selectedCat);
					}
					
				} else {
					/* Category soll entfernt werden */ 
					currentTis.setCategory(null);
					
				}
				
				
				
				// #####################################################################################################################
				// #####################################################################################################################
				// #####################################################################################################################
				
				
				parent.getTodoListPanel().updateTodoList();
				
			} catch(Exception ex) {
				Logger.getInstance().logException("Fehler beim Speichern des aktuellen Todo Item Stacks", ex);
				ex.printStackTrace();
				
				JOptionPane.showMessageDialog(this, "Diese Änderung konnte nicht gespeichert werden.", "Fehler beim Speichern", JOptionPane.WARNING_MESSAGE);
			}	
						

		}

		
	}

	
	
	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getSource() == comboBoxCategory){
			
			/* ESC oder ENTF */
			if(event.getKeyCode() == 27 || event.getKeyCode() == 127) {
				comboBoxCategory.setSelectedIndex(-1);
				
				
			}
		}
		
		if(event.getSource() == listAttachments) {
			
			// ENTF-Taste
			if(event.getKeyCode() == 127) {
				attachmentModel.removeElementAt(listAttachments.getSelectedIndex());
				
			}
		
		}
		
	}
	
	@Override public void keyReleased(KeyEvent arg0) {}
	@Override public void keyTyped(KeyEvent arg0) {}
	
	
	
	/**
	 * Zeigt einen TodoItemStack in der detailansicht an
	 * 
	 * @param tisToShow
	 */
	public void showSingleStack(TodoItemStack tisToShow) {

		
		// clear
		if(tisToShow == null) {
			Logger.getInstance().log("Cleare Eingabefelder", Logger.LOGLEVEL_INFO);
			todoHistoryModel.clear();
			attachmentModel.setTodoItemStack(null);
			
			
			
			
			this.setEditorEnabled(false);
			
			comboBoxCategory.setSelectedIndex(-1);
			labCategory.setForeground(Color.black);
			labCategory.setToolTipText(null);
			sliderPriority.setValue(0);
			txtFieldDate.setDate(null);
			txtFieldDescription.setText("");
			txtFieldTime.setText("");
			textAreaText.setText("");
			txtFieldDateEnd.setDate(null);
			txtFieldDateStart.setDate(null);
			chkBoxReminderActive.setSelected(false);
			comboBoxStatus.setSelectedIndex(0);
			labTodoItem.setText("Todo-Item");
		}
		else {
			this.setEditorEnabled(true);
			todoHistoryModel.clear();

			attachmentModel.setTodoItemStack(tisToShow);
			listAttachments.repaint();
			
						
			chkBoxReminderActive.setSelected(tisToShow.getReminderActivationStatus());
			if(tisToShow.getReminderDate() != null) {
				txtFieldDate.setDate(tisToShow.getReminderDate());

			}
			
			for(TodoItem ti : tisToShow.getTodoHistory()) {
				
				todoHistoryModel.addElement(ti);
			}
			
			
			todoHistoryList.setSelectedIndex(todoHistoryList.getModel().getSize()-1);
			todoHistoryList.ensureIndexIsVisible(todoHistoryList.getSelectedIndex());
			
		}
		
		// Speichern-Button ausblenden, wenn Liste abgeschlossen ist
		if(tisToShow != null && tisToShow.getParent().isLocked()) {
			buttonSave.setEnabled(false);
			buttonSave.setToolTipText("Diese Liste ist abgeschlossen");
		}
		
		   if(tisToShow == null || tisToShow.getCategory() == null ) {
			   comboBoxCategory.setSelectedIndex(-1);
			   labCategory.setForeground(Color.darkGray);
			   labCategory.setToolTipText("Element besitzt keine Kategorie");
			   
		   } else {
			   if(ConfigurationHandlerImpl.getInstance().getCategories().keySet().contains(tisToShow.getCategory())) {
				   comboBoxCategory.setSelectedItem(tisToShow.getCategory());
				   labCategory.setForeground(Color.black);
				   
			   } else {
				      comboBoxCategory.setSelectedIndex(-1);
				      labCategory.setForeground(Color.red);
				      labCategory.setToolTipText("Kategorie nicht vorhanden: " + tisToShow.getCategory());
			   }
			   
		   }

	
	}

	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if(!e.getValueIsAdjusting() && todoHistoryList.getSelectedIndex() != -1) {
			
			showTodoItemDetails();
		}		
	}

	
	
	public String getTextAreaText() {
		return textAreaText.getText();
	}

	
	
	public void setTextAreaText(String text) {
		textAreaText.setText(text);
	}
	
	
	
	/**
	 * Fügt eine Datei den Attachments hinzu. Je nach Modus wird im TodoProjekt oder lokal
	 * auf der Platte gespeichert.
	 * @param f Die Datei, die hinzugefügt wird
	 * @param mode einer von {@link SelectAttachmentStoreModeDialog} STORE MODEs 
	 */
	private void addFileToAttachments(File f, int mode) {
		attachmentModel.addElement(new Attachment(mode, f));
		
	}
	
}


