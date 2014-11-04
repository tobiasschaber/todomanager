package gui.models;

import gui.panels.configuration.configPanels.*;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * Das Menü in Baum-Form, welches in den Einstellungen zur Navigation angezeigt wird.
 * @author Tobias Schaber
 */
public class ConfigTreeModel implements TreeModel {
	
	private final DefaultMutableTreeNode root;
	

	public ConfigTreeModel() {
		
		root = new DefaultMutableTreeNode("Einstellungen");
		
		final DefaultMutableTreeNode n1 = new DefaultMutableTreeNode("Oberfläche");
		final DefaultMutableTreeNode n12 = new DefaultMutableTreeNode(new ConfigBoxPanel());
		final DefaultMutableTreeNode n13 = new DefaultMutableTreeNode(new ConfigOverviewPanel());
		
		final DefaultMutableTreeNode n2 = new DefaultMutableTreeNode("Todo-Manager");
		final DefaultMutableTreeNode n21 = new DefaultMutableTreeNode(new ConfigTodoListPanel());
		final DefaultMutableTreeNode n22 = new DefaultMutableTreeNode(new ConfigSystemPanel());
		
		
		final DefaultMutableTreeNode n3 = new DefaultMutableTreeNode("Zeit-Strahl");
		final DefaultMutableTreeNode n31 = new DefaultMutableTreeNode(new ConfigTimeCategoryPanel());
		final DefaultMutableTreeNode n4 = new DefaultMutableTreeNode("High-Prio List");
		
		n1.add(n12);
		n1.add(n13);
		
		n2.add(n21);
		n2.add(n22);
		
		n3.add(n31);
		
		root.add(n1);
		root.add(n2);
		root.add(n3);
		root.add(n4);
		
	}
	
	 
	
	
	
	@Override
	public Object getChild(Object parent, int index) {
		
		DefaultMutableTreeNode def = (DefaultMutableTreeNode)parent;
		
		if(def.getUserObject() instanceof String) {
			return def.getChildAt(index);

		}
		
		return null;
	}
	
	@Override
	public int getChildCount(Object parent) {

		DefaultMutableTreeNode def = (DefaultMutableTreeNode)parent;
		return def.getChildCount();
		
		
	}

		
	
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}
	
	@Override
	public Object getRoot() {

		return root;
	}
	
	@Override
	public boolean isLeaf(Object node) {
		
		DefaultMutableTreeNode def = (DefaultMutableTreeNode)node;
		
		if(def.getUserObject() instanceof String) {
			return false;
		}

		return true;
	}
	
	
	@Override
	public void addTreeModelListener(TreeModelListener l) {

		
	}
	
	@Override
	public void removeTreeModelListener(TreeModelListener l) {

		
	}
	
	public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {};
	
	
	

}
