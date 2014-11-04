package gui.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;

import controller.ConfigurationHandlerImpl;
import data.TodoItemStack;
import data.TodoList;


/**
 * Dieses Model wird für die Haupt-Todo-Liste verwendet, in der alle
 * TodoItemStacks eines Projektes aufgelistet sind. Es übernimmt auch
 * die Anwendung der verschiedenen Filter.
 * @author Tobias Schaber
 */
public class TodoListModel extends DefaultTableModel {

	/* this element contains the disblayable data */
	private TodoList tp;
	
	/* this array contains every mode which shall be displayed */
	private ArrayList<String> selectionModes = new ArrayList<String>();
	
	/* if true, the selected modes will be negotiated */
	private boolean negotiateSelection = false;

	
	private String filterCriteria = "";
	private String filterCategory = "";
	
	/**
	 * constructor
	 * @param tp
	 */
	public TodoListModel(TodoList tp) {
		this.tp = tp;

	}
	
	/**
	 * constructor
	 * @return
	 */
	public TodoList getTodoProject() {
		return tp;
	}

	
	/**
	 * switches the selection status of the delivered mode
	 * @param modeToSwitch
	 */
	public void switchSelectionMode(String modeToSwitch) {
		if (selectionModes.contains(modeToSwitch)) {
			selectionModes.remove(modeToSwitch);

		} else {
			selectionModes.add(modeToSwitch);
		}
		
		// the items to display change if the selection criteria change
		fireTableDataChanged();
	}

	
	/**
	 * switches the negotiation selection mode
	 */
	public void switchNegotiationMode() {
		if (negotiateSelection)
			negotiateSelection = false;
		else
			negotiateSelection = true;

		fireTableDataChanged();

	}

	/**
	 * returns the column description
	 */
	public String getColumnName(int col) {

		if (col == 0)
			return "ID";
		if (col == 1)
			return "Status";
		if (col == 2)
			return "Beschreibung";
		if (col == 3)
			return "Datum";
		if (col == 4)
			return "Priorität";

		return "";
	}

	/**
	 * Returns the current number of items to show in the table. This depends on
	 * the selection (and negotiation) settings.
	 */
	public int getRowCount() {

		/* in case of initializing, this method is called before tp is initialized */
		if (tp != null) {

			int count = 0;

			Iterator<TodoItemStack> it = tp.getTodoList().iterator();

			while (it.hasNext()) {
				TodoItemStack tis = it.next();

				if ((!negotiateSelection
						&& selectionModes.contains(tis.getLatestTodo().getStatus()) ||
						  (negotiateSelection && !(selectionModes.contains(tis.getLatestTodo().getStatus()))))) {
					
					
					if(filterCriteria.equals("") && filterCategory.equals("")) {

						count++;
						
					} else {
						
						if(filterCategory.equals("")) {
							if(tis.contains(filterCriteria)) {
								count++;
							}
						} else {
							
							if(tis.contains(filterCriteria) && tis.getCategory() != null && tis.getCategory().equals(filterCategory)) {
								count++;
							}
						}
						
						
					}
				}

			}

			return count;
		} else {
			return 0;
		}
	}

	/**
	 * delivers the static integer 4, because there are 4 columns for id, description, date and status
	 */
	public int getColumnCount() {
		return 5;
	}
	
	
	
	/**
	 * returns the content of the row. this content is calculated by using the selection (and negotiation) settings
	 */
	public Object getValueAt(int row, int col) {

		ArrayList<TodoItemStack> containedElements = new ArrayList<TodoItemStack>();

		Iterator<TodoItemStack> it = tp.getTodoList().iterator();

		while (it.hasNext()) {

			TodoItemStack tis = it.next();

			if ((!negotiateSelection
					&& selectionModes.contains(tis.getLatestTodo().getStatus()) || (negotiateSelection && !(selectionModes
					.contains(tis.getLatestTodo().getStatus()))))) {

				if(filterCriteria.equals("") && filterCategory.equals("")) {
					
					containedElements.add(tis);
					
				} else {
					
					if(filterCategory.equals("")) {
					
						if(tis.contains(filterCriteria)) {
							containedElements.add(tis);
						}
					} else {
						if(tis.contains(filterCriteria) && tis.getCategory() != null &&  tis.getCategory().equals(filterCategory)) {
							containedElements.add(tis);
						}
					}
				}

			}
		}

		if (col == 0)
			return (containedElements.get(row)).getTodoId();
		if (col == 1)
			return (containedElements.get(row)).getLatestTodo().getStatus();
		
		if (col == 2)
			return (containedElements.get(row)).getName();
		if (col == 3)
			return new SimpleDateFormat(
					ConfigurationHandlerImpl
					.getInstance()
					.getProperty("DefaultDateFormat"))
					.format(containedElements.get(row)
					.getLatestTodo()
					.getModificationDate());
		
		if (col == 4){
			return (containedElements.get(row)).getPriority();
		}
		

		return "";
	}

	
	/**
	 * all cells are not editable
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	
	public void setValueAt(Object value, int row, int col) {

		fireTableCellUpdated(row, col);
	}
	
	
	/**
	 * aktualisiert den Filter auf neue Werte. Ignoriert den Standard "Filtern..." für Textsuche
	 * @param newFilter neuer Textfilter
	 * @param newCategory neuer Kategorie-Filter
	 */
	public void setFilter(String newFilter, String newCategory) {
		 
		
		if(newFilter != null) {
			if(newFilter.equals("Filtern..."))  filterCriteria = "";
			else								filterCriteria = newFilter;
		}
		if(newCategory != null) filterCategory = newCategory;
		
		fireTableDataChanged();
	}
	
	
	
	

}