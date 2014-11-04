package gui.panels.configuration.configPanels.subPanels;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class BoxColorModel implements ComboBoxModel<String> {
	
	private HashMap<String, String> colKeys;
	private String selectedKey;
	
	
	public BoxColorModel(HashMap<String, String> colKeys) {
		this.colKeys = colKeys;
		
	}
	
	
	public String getElementAt(int index) {
		Iterator<String> it = colKeys.keySet().iterator();
		int ct = 0;
		
		while(it.hasNext()) {
			
			if(index == ct) {
				return colKeys.get(it.next());
			}
			it.next();
			ct++;
			
		}
		return null;
	}
	
	
	@Override
	public void addListDataListener(ListDataListener l) {}

	
	
	@Override
	public Object getSelectedItem() {
		return selectedKey;
		
	}
	@Override
	public int getSize() {

		return colKeys.size();
	}
	@Override
	public void removeListDataListener(ListDataListener l) {}
	
	@Override
	public void setSelectedItem(Object anItem) {
		selectedKey = (String)anItem;
		

	}
	
}
