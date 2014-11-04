package gui.panels.todoeditor;

import gui.BaseFrame;
import gui.alerts.FileChooserAlert;
import gui.dialogs.CreateTodoInputDialog;
import gui.dragdrop.TodoTableDragGestureListener;
import gui.dragdrop.TodoTableDragSourceListener;
import gui.models.TodoListModel;
import gui.renderer.ComboBoxCategoryRenderer;
import gui.renderer.TodoListRenderer;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

import main.Logger;
import main.annotations.OverriddenByConfiguration;
import controller.*;
import data.*;


/**
 * Panel för die Anzeige der kompletten Todo-Liste (als Tabelle)
 * sowie der dazugehörigen Auswahl-Buttons.
 * @author Tobias Schaber
 */
public class TodoListPanel extends JPanel implements ActionListener, ListSelectionListener, MouseListener, KeyListener {

	// das übergeordnete panel
	private final TodoListBasePanel parent;
	
	// das in der liste repräsentierte todoproject
	private TodoList tl;
	
	
	/* die checkboxen für die auswahl der angezeigten status */
	private final JCheckBox checkBoxSelectAlarm 	= new JCheckBox(TodoItem.STATUS_ALARM);
	private final JCheckBox checkBoxSelectDone 		= new JCheckBox(TodoItem.STATUS_DONE);
	private final JCheckBox checkBoxSelectNegotiate = new JCheckBox("Umkehren");
	private final JCheckBox checkBoxSelectPending 	= new JCheckBox(TodoItem.STATUS_PENDING);
	private final JCheckBox checkBoxSelectTodo 		= new JCheckBox(TodoItem.STATUS_TODO);
	private final JCheckBox checkBoxSelectWait 		= new JCheckBox(TodoItem.STATUS_WAIT);
	
	/* Felder für den Filter */
	private final JComboBox<String> comboBoxSelectCategory	= new JComboBox<String>();
	private JTextField				txtFieldFilter			= new JTextField("Filtern...");
	
	/* Labels, die als Button dienen und ein Icon bekommen */
	private final JLabel 		labAddTodo 			= new JLabel(" ");
	private final JLabel		labExportCsv		= new JLabel(" ");
	
	private final JScrollPane 	scrollpaneTodoList 	= new JScrollPane();
	private final JTable		tableTodoList 		= new JTable();
	
	// das model zum anzeige der tabelleninhalte */
	private final TodoListModel todoListModel;
	private final DefaultTableCellRenderer todoListRenderer = new TodoListRenderer();
	
	// das Hintergrundbild für noch nicht befüllte Tabellen
	
	
	/**
	 * constructor
	 * @param parent
	 * @param tp
	 */
	public TodoListPanel(TodoListBasePanel parent, TodoList tl) {

		this.parent = parent;
		this.tl = tl;
		
		Logger.getInstance().log("Erzeuge TodoListBasePanel für Projekt " + tl.getListName(), Logger.LOGLEVEL_INFO);
		todoListModel = new TodoListModel(tl);
		tableTodoList.setModel(todoListModel);
		
		
		// drag and drop handler zuweisen
		
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(tableTodoList, DnDConstants.ACTION_MOVE, new TodoTableDragGestureListener(ds, this));
		ds.addDragSourceListener(new TodoTableDragSourceListener(todoListModel));	
		
		
		final TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>( todoListModel );
		
		rowSorter.setComparator(0,
			new Comparator<Integer>() {
			
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 == o2) return 0;
				if(o1 > o2) return 1;
				if (o1 < o2) return -1;
				return 0;
			}
		});
		

		rowSorter.setComparator(3,
				new Comparator<String>() {
				
				@Override
				public int compare(String s1, String s2) {
					
					@OverriddenByConfiguration
					Date d1, d2;
					
					try {
						d1 = new SimpleDateFormat(ConfigurationHandlerImpl.getInstance().getProperty("DefaultDateFormat")).parse(s1);
						d2 = new SimpleDateFormat(ConfigurationHandlerImpl.getInstance().getProperty("DefaultDateFormat")).parse(s2);
					} catch(Exception e) {
						d1 = new Date();
						d2 = new Date();
					}
					
					return d1.compareTo(d2);
				}
			});
		
		rowSorter.setComparator(4,
				new Comparator<Integer>() {
				
				@Override
				public int compare(Integer s1, Integer s2) {
				
					return s1.compareTo(s2);
				}
			});
		
		/* sortieren einer spalte */
		rowSorter.setComparator( 1, 
			new Comparator<String>() { 
			
				@Override
				public int compare( String s1, String s2 )  {
					String[] reihenfolge = {"TODO", "PENDING", "WAIT", "ALARM", "DONE"};
					
					boolean found1 = false;
					boolean found2 = false;
					
					for(int i=0; i<reihenfolge.length; i++) {
						
						// der erste wurde gefunden
						if(s1.equals(reihenfolge[i])) {
							found1 = true;
							
						}
						
						if(s2.equals(reihenfolge[i])) {
							found2 = true;
						}
						
						if(found1) return 1;
						if(found2) return -1;
						
					}
					
					
					if(s2.equals("TODO")) {
						return 1;
					}
					return -1;


				}
			}
		);

		
		tableTodoList.setRowSorter(rowSorter);

		

		
		initComponents();
		initLayout();
		initListeners();
		
	}
	
	
	/**
	 * initialisieren der grafischen komponenten
	 */
	private void initComponents() {
		
		comboBoxSelectCategory.setRenderer(new ComboBoxCategoryRenderer(comboBoxSelectCategory));
        comboBoxSelectCategory.setMaximumSize(new java.awt.Dimension(32767, 14));
        comboBoxSelectCategory.setMinimumSize(new java.awt.Dimension(56, 14));
        comboBoxSelectCategory.setPreferredSize(new java.awt.Dimension(56, 14));
        
		txtFieldFilter.setVisible(false);
		comboBoxSelectCategory.setVisible(false);
		comboBoxSelectCategory.setToolTipText("Nach Kategorie filtern");
		
		
		txtFieldFilter.setBorder(new LineBorder(Color.orange, 1));
		
		checkBoxSelectNegotiate.setToolTipText("Auswahl umkehren");

		labAddTodo.setIcon(ImageController.iconAdd);
		labExportCsv.setIcon(ImageController.iconExportCsv);
		
		labAddTodo.setToolTipText("Neues Todo hinzufügen");
		labExportCsv.setToolTipText("Nach Excel exportieren");

		
		
		labAddTodo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				labAddTodo.setIcon(ImageController.iconAddHover);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				labAddTodo.setIcon(ImageController.iconAdd);
			}
			@Override
			public void mousePressed(MouseEvent e) {}
		});
		
		
		labExportCsv.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				labExportCsv.setIcon(ImageController.iconExportCsvHov);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				labExportCsv.setIcon(ImageController.iconExportCsv);
			}
			@Override
			public void mousePressed(MouseEvent e) {}
		});
		

		tableTodoList.getTableHeader().setReorderingAllowed(false);
		
		
		
		
		JViewport view = new JViewport() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				if(todoListModel.getTodoProject().getTodoList().size() == 0)
				
				if(tableTodoList.getModel().getRowCount() == 0 && BaseFrame.menuItemEnableHints.isSelected()) {
					g.drawImage(ImageController.imgHintTableBg, 0, 0, this);
				}
				
			}
		};
		
		view.add(tableTodoList);
		scrollpaneTodoList.setViewport(view);
		
		// tabellen sollen nicht in der größe veränderbar sein
		tableTodoList.getColumnModel().getColumn(0).setMinWidth(50);
		tableTodoList.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableTodoList.getColumnModel().getColumn(0).setMaxWidth(50);
		
		tableTodoList.getColumnModel().getColumn(1).setPreferredWidth(60);
		tableTodoList.getColumnModel().getColumn(1).setMaxWidth(60);
		tableTodoList.getColumnModel().getColumn(1).setMinWidth(60);
		
		// Das Beschreibungs-Feld ist die "dehnfuge" die sich beim resizen verändert
		tableTodoList.getColumnModel().getColumn(2).setPreferredWidth(250);
		
		tableTodoList.getColumnModel().getColumn(3).setMinWidth(120);
		tableTodoList.getColumnModel().getColumn(3).setPreferredWidth(120);
		tableTodoList.getColumnModel().getColumn(3).setMaxWidth(120);
		
		tableTodoList.getColumnModel().getColumn(4).setMinWidth(105);
		tableTodoList.getColumnModel().getColumn(4).setPreferredWidth(105);
		tableTodoList.getColumnModel().getColumn(4).setMaxWidth(105);
			
		tableTodoList.setColumnSelectionAllowed(false);
		tableTodoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	       
		labAddTodo.setFont(new java.awt.Font("Tahoma", 1, 11));
		labExportCsv.setFont(new java.awt.Font("Tahoma", 1, 11));
	        
		// default sortiere für alle spalten
//		tableTodoList.setAutoCreateRowSorter(true);
		
		// spezial
	    tableTodoList.setDefaultRenderer(Object.class, todoListRenderer);
	       
	    tableTodoList.setGridColor(new Color(232, 232, 232));
	    tableTodoList.setShowVerticalLines(false);
	        
	    /* depending on the config file, the default selection criteria are set */
		String selectionConfig = ConfigurationHandlerImpl.getInstance().getProperty("DefaultSelectionMode");
			
		if(selectionConfig.contains(TodoItem.STATUS_TODO)) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_TODO);
			checkBoxSelectTodo.setSelected(true);
		}
		if(selectionConfig.contains(TodoItem.STATUS_PENDING)) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_PENDING);
			checkBoxSelectPending.setSelected(true);
		}
		if(selectionConfig.contains(TodoItem.STATUS_WAIT)) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_WAIT);
			checkBoxSelectWait.setSelected(true);
		}
		if(selectionConfig.contains(TodoItem.STATUS_ALARM)) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_ALARM);
			checkBoxSelectAlarm.setSelected(true);
		}
		if(selectionConfig.contains(TodoItem.STATUS_DONE)) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_DONE);
		       checkBoxSelectDone.setSelected(true);
		}
		
		
		/* die auswahl-leiste farbig machen */
		checkBoxSelectAlarm.setBackground(new Color(255, 218, 74));
		checkBoxSelectDone.setBackground(new Color(255, 218, 74));
		checkBoxSelectNegotiate.setBackground(new Color(255, 218, 74));
		checkBoxSelectPending.setBackground(new Color(255, 218, 74));
		checkBoxSelectWait.setBackground(new Color(255, 218, 74));
		checkBoxSelectTodo.setBackground(new Color(255, 218, 74));
		this.setBackground(new Color(255, 218, 74));

	}
	
	
	/**
	 * initialisiert das layout
	 */
	private void initLayout() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(checkBoxSelectTodo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxSelectPending)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxSelectWait)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxSelectAlarm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxSelectDone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labAddTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFieldFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboBoxSelectCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labExportCsv, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxSelectNegotiate))
            .addComponent(scrollpaneTodoList)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxSelectTodo)
                    .addComponent(checkBoxSelectPending)
                    .addComponent(checkBoxSelectWait)
                    .addComponent(checkBoxSelectAlarm)
                    .addComponent(checkBoxSelectDone)
                    .addComponent(labAddTodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxSelectNegotiate)
                    .addComponent(labExportCsv)
                    .addComponent(txtFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxSelectCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollpaneTodoList, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
        );

        
	}
	
	
	
	/**
	 * initialisiert die listener 
	 */
	private void initListeners() {
		checkBoxSelectAlarm.addActionListener(this);
		checkBoxSelectDone.addActionListener(this);
		checkBoxSelectPending.addActionListener(this);
		checkBoxSelectTodo.addActionListener(this);
		checkBoxSelectWait.addActionListener(this);
		checkBoxSelectNegotiate.addActionListener(this);
		
		tableTodoList.getSelectionModel().addListSelectionListener(this);
		
		labAddTodo.addMouseListener(this);
		labExportCsv.addMouseListener(this);
		
		txtFieldFilter.setInputVerifier(null);
		txtFieldFilter.addKeyListener(this);
		txtFieldFilter.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "closeFilter");
		
		class LeftAction extends AbstractAction {
		    public LeftAction() {
		        
		        
		    }
		    public void actionPerformed(ActionEvent e) {
		        switchFilterVisible();
		    }
		}

		
		txtFieldFilter.getActionMap().put("closeFilter", new LeftAction());
		

		comboBoxSelectCategory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String newCatFilter = "";
				if(comboBoxSelectCategory.getSelectedItem() != null) {
					newCatFilter = comboBoxSelectCategory.getSelectedItem().toString();
				}
				
				todoListModel.setFilter(txtFieldFilter.getText(), newCatFilter);
			
			}
		});
		
		comboBoxSelectCategory.addKeyListener(new KeyListener() {
			
			@Override public void keyTyped(KeyEvent arg0) {}
			@Override public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {

				//ESC oder ENTF oder RÜCK
				if(arg0.getKeyCode() == 127 || arg0.getKeyCode() == 27 || arg0.getKeyCode() == 8) {
					comboBoxSelectCategory.setSelectedIndex(-1);
					todoListModel.setFilter(txtFieldFilter.getText(), "");
				}
			}
		});
		
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == checkBoxSelectAlarm) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_ALARM);
		}

		if(e.getSource() == checkBoxSelectDone) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_DONE);
		}
		
		if(e.getSource() == checkBoxSelectPending) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_PENDING);
		}
		
		if(e.getSource() == checkBoxSelectTodo) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_TODO);
		}

		if(e.getSource() == checkBoxSelectWait) {
			todoListModel.switchSelectionMode(TodoItem.STATUS_WAIT);
		}
		
		if(e.getSource() == checkBoxSelectNegotiate) {
			todoListModel.switchNegotiationMode();
		}
		
	}
	
	
	/**
	 * Exportiert die gerade angezeigte Todo-Liste in eine Excel-Datei. Hierzu wird ein 
	 * File-Chooser geöffnet, über den die Datei ausgewählt wird
	 */
	private void exportTodoListToExcelFile() {
		
		Logger.getInstance().log("Exporting TodoList to Excel", Logger.LOGLEVEL_INFO);

		
		final FileChooserAlert fca = new FileChooserAlert(FileChooserAlert.EXPORT_EXCEL_MODE);
		
		// JItemChooser starten und file wählen, und dann dort speichern
		int resp = fca.showOpenDialog(FileChooserAlert.EXPORT_EXCEL_MODE);
		
		// resp = 1 -> Abbruch des Speicherns
		if(resp != 1) {
		
			File targetFile = fca.getSelectedFile();
			
	
			/* wenn der dialog nicht abgebrochen wurde */
			if(targetFile != null) {

				/* gibt an ob das file geschrieben werden soll */
				boolean writeFile = true;
				
				// Nachfragen wenn das File schon existiert
				if(targetFile.exists()) {
					
					/* erst mal davon ausgehen, dass das file nicht geschrieben wird */
					writeFile = false;
					int c = JOptionPane.showConfirmDialog(parent, "Die Datei existiert bereits. überschreiben?", "Datei überschreiben?", JOptionPane.YES_NO_OPTION);
					
					// ja, überschreiben
					if(c == 0) {
						/* file nun doch schreiben */
						writeFile = true;
					}
				}	
				
				/* wenn das file über/geschrieben werden soll */
				if(writeFile) {
					
					try {
						ExportController.exportTodoListToExcel(todoListModel, targetFile);
					} catch(Exception e) {
						
						Logger.getInstance().logException("Fehler beim Excel Export", e);
						JOptionPane.showMessageDialog(parent, "Beim Speichern ist ein Fehler aufgetreten:\n" + e.getMessage(), "Fehler beim Speichern", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			
		}
		
	
	}
	
	
	/**
	 * Kopiert den Inhalt der aktuellen Todo-Liste im CSV-Format in die
	 * System-Zwischenablage
	 * 
	 */
	@Deprecated
	public void copyTodoListToClipboard() {
		
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		StringSelection strSel = new StringSelection(exportTodoListAsCSV());
		sysClip.setContents(strSel, null);
		
		JOptionPane.showMessageDialog(this, "Export im CSV-Format abgeschlossen.", "Export abgeschlossen.", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/**
	 * Konvertiert das gesamte Table Model in CSV Format, inklusive Kopfzeile
	 */
	@Deprecated
	private String exportTodoListAsCSV() {
		
		StringBuilder csvBuilder = new StringBuilder();
		

		for(int colCt = 1; colCt < todoListModel.getColumnCount(); colCt++) {
			
			csvBuilder.append(todoListModel.getColumnName(colCt) + ";");
		}
		/* remove last ";" */
		csvBuilder.deleteCharAt(csvBuilder.length()-1);
		
		csvBuilder.append("\n");
		
		for(int row = 0; row<todoListModel.getRowCount(); row++) {
			
			for(int col = 1; col<todoListModel.getColumnCount(); col++) {
				
				csvBuilder.append(todoListModel.getValueAt(row, col));
				csvBuilder.append(";");
			}
			
			csvBuilder.append("\n");
			
		}
		
		return csvBuilder.toString();
		
	}


	@Override
	/**
	 * listener wenn auf das anlegen-icon geklickt wurde
	 */
	public void mouseReleased(MouseEvent arg0) {
		
		if(tl.isLocked()) {
			JOptionPane.showMessageDialog(this, "Diese Liste ist abgeschlossen. Zum Bearbeiten muss sie zunächst über das Übersichts-Tab entsperrt werden.", "Liste geschlossen", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		
		if(arg0.getSource() == labExportCsv) {

			exportTodoListToExcelFile();
		}
		
		if(arg0.getSource() == labAddTodo) {

			// die nachricht wird in ein extra jlabel gepackt, damit das popup fenster breiter wird
			JLabel tx = new JLabel("Name des neuen Todos:");
			
			tx.setPreferredSize(new Dimension(500, 30));

			String[] repl = CreateTodoInputDialog.showInputDialog("Neues Todo anlegen");
			int newPrio = 0;
			String newStat = repl[0];
			String newName = repl[1];
			if(repl[2] != null) newPrio = Integer.parseInt(repl[2]);
						
			if(newName != null && !newName.equals("")) {
				tl.addTodoItemStack(newName, newStat, newPrio);
				todoListModel.fireTableDataChanged();
				
				/* unterste zeile selektieren */
				if(tableTodoList.getRowCount() != 0) tableTodoList.setRowSelectionInterval(tableTodoList.getRowCount()-1, tableTodoList.getRowCount()-1);
				scrollpaneTodoList.repaint();
			}
			
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {	}

	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		
		if(!e.getValueIsAdjusting()) {
			
			if(tableTodoList.getSelectedRow() != -1) {
			
				updateItemViewer(false);
				
			} else {
				
				updateItemViewer(true);	
			}	
		}
	}
	
	
	
	/**
	 * löst das aktualisieren der detail-anzeige im anderen panel aus
	 * @param clear bei true werden alle felder geleert, ansonsten werden die korrekten werte angezeigt
	 */
	private void updateItemViewer(boolean clear) {
		
		if(clear) {
			parent.showSingleStack(null);
				
		}
		else {
			
		
			if(tableTodoList.getSelectedRow() != -1) {
				int todoStackId = (Integer)tableTodoList.getValueAt(tableTodoList.getSelectedRow(), 0);
			
				TodoItemStack tisToShow = tl.getTodoItemStackById(todoStackId);
				parent.showSingleStack(tisToShow);
			}
		}

		
	}

	
	/**
	 * wird aufgerufen, wenn sich eine todoliste geändert hat (z.b. neues element, neuer status)
	 */
	public void updateTodoList() {
		
		// model aktualisieren
		todoListModel.fireTableDataChanged();
		
		
	}
	
	
	/**
	 * liefert den derzeit in der tabelle selektierten todoitemstack zurück
	 * @return
	 */
	public TodoItemStack getSelectedTodoItemStack() {
		
		int todoStackId = (Integer)tableTodoList.getValueAt(tableTodoList.getSelectedRow(), 0);
		
		return(tl.getTodoItemStackById(todoStackId));
		
	}


	public void switchFilterVisible() {
		
		
		/* aktualisiere den Kategorie-Filter mit allen derzeit existierenden Kategorien */
		if(!comboBoxSelectCategory.isVisible()) {
		
			String[] cats = new String[ConfigurationHandlerImpl.getInstance().getCategories().size()];
	
			ConfigurationHandlerImpl.getInstance().getCategories().keySet().toArray(cats);
			comboBoxSelectCategory.setModel(new DefaultComboBoxModel<String>(cats));
			comboBoxSelectCategory.setSelectedIndex(-1);
	
		}
		
		
		txtFieldFilter.setVisible(!txtFieldFilter.isVisible());
		comboBoxSelectCategory.setVisible(!comboBoxSelectCategory.isVisible());
		
		if(txtFieldFilter.isVisible()) {
			txtFieldFilter.setText("Filtern...");
			txtFieldFilter.setSelectionStart(0);
			txtFieldFilter.requestFocus();
		}
		
		if(!txtFieldFilter.isVisible()) {
			// Filter zurücksetzen beim ausblenden
			todoListModel.setFilter("", "");
			
		}
		
		validate();
		
	}
	
	
	public TodoListModel getTodoListModel() {
		return todoListModel;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {

		// Filter für Todos hat sich verändert
		if(e.getSource() == txtFieldFilter) {
			
			String newCatFilter = "";
			if(comboBoxSelectCategory.getSelectedItem() != null) {
				newCatFilter = comboBoxSelectCategory.getSelectedItem().toString();
			}
			todoListModel.setFilter(txtFieldFilter.getText(), newCatFilter);
		
		}
		
	}


	
	/**
	 * visiert in der todo liste den übergebenen TodoItemStack aus 
	 * @param tis
	 */
	public void setSelectedTodoItemStack(TodoItemStack tis) {
		
		int indx = tis.getTodoId();
		
		for(int i=0; i<tableTodoList.getRowCount(); i++) {
			if((Integer)tableTodoList.getValueAt(i, 0) == indx) {
				
				tableTodoList.setRowSelectionInterval(i, i);
			}
		}

		
		
		
	}

}