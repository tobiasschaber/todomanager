package gui.panels.overviewpage;

import gui.BaseFrame;
import gui.components.Box;
import gui.menus.OverviewBgContextMenu;
import gui.menus.OverviewBoxContextMenu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.BaseController;
import main.Logger;
import main.annotations.OverriddenByConfiguration;
import controller.ConfigurationHandlerImpl;
import controller.ImageController;
import data.TodoList;


/**
 * Dieses Panel zeichnet für jede Todo-Liste eine Box auf der Übersichts-Seite.
 * @author Tobias Schaber
 */
public class BoxSummaryPanel extends JPanel implements MouseListener {

	private ArrayList<Box>  				boxList = new ArrayList<Box>();	
	
	// wird nur eine box in den details angezeigt, so ist es diese
	private Box								sumShowBox		= null;
	
	// gibt an ob sich der zustand der boxen geändert hat, also der graph neu gezeichnet werden muss
	public boolean							sumStateChanged = true;
	
	private final OverviewBoxContextMenu 	boxContextMenu;
	private final OverviewBgContextMenu		bgContextMenu;
	
	// hält den Zustand, ob gesperrte listen angezeigt werden oder nicht
	@OverriddenByConfiguration
	public boolean							showLocked;
	
	private final BaseController			controller;
	private final OverviewTabPanel			parent;
	
	public BoxSummaryPanel(BaseController controller, OverviewTabPanel parent) {

		this.parent = parent;
		this.controller = controller;
		
		boxContextMenu = new OverviewBoxContextMenu(controller);
		bgContextMenu = new OverviewBgContextMenu(this);
		
		showLocked		= Boolean.valueOf(ConfigurationHandlerImpl.getInstance().getProperty("showLockedListsOnStartup"));
		
		this.setMinimumSize(new Dimension(100, 100));
		
		initListeners();
		
	}
	
	
	
	public void addProject(TodoList tl) {
		
		boxList.add(new Box(tl, this));
		sumStateChanged = true;
	}
	
	public void removeAllProjects() {
		boxList = null;
		boxList = new ArrayList<Box>();
		sumShowBox = null;
		sumStateChanged = true;
		
	}
	
	public void removeProject(TodoList tl) {
		
		Box remove = null;
		
		Iterator<Box> it = boxList.iterator();
		while(it.hasNext()) {
			Box b = it.next();
			
			if(b.tl.equals(tl)) {
				remove = b;

			}
		}
		
		boxList.remove(remove);		
		
		sumStateChanged = true;
		
	}
	
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int boxWidth  = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.width"));
		int boxHeight = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.height"));
		
		if(BaseFrame.menuItemEnableHints.isSelected()) {
		
			// Zeichne Hinweis, wenn das Panel ganz leer ist
			if(boxList.size() == 0) {
				g.drawImage(ImageController.imgHintOverview, 0, 0, this);	
			}
			
			// Zeichne Hinweis, dass Liste doppelt geklickt werden kann zum Öffnen
			if(boxList.size() == 1 && boxList.get(0).tl.getTodoList().size() == 0) {
				// zeichne mit leichter Überlappung (20px) unter die Box
				g.drawImage(ImageController.imgHintSingleList, boxWidth-20, boxHeight-20, this);
			}
		}

		int abstand_x = 10;
		int abstand_y = 8;
		
		int max_x = (this.getWidth() / (boxWidth+abstand_x + 10));
		if(max_x == 0) max_x = 1;
		
		int ct = 0;
		
		for (Box box: boxList){

			if(box.tl.isLocked() && !showLocked) {
				// Box nicht anzeigen, da sie gesperrt ist und gesperrte Listen ausgeblendet werden
			} else {
				
				box.posX = abstand_x + (ct%max_x) * (Box.sidebarWidth + boxWidth+abstand_x);
				box.posY = abstand_y + (ct/(max_x)) * (boxHeight+abstand_y);
				ct++;
				
				box.paintComponent(g);
			}
		}
		
	}
	

	
	/**
	 * liefert die box zurück, die an einem punkt p liegt
	 * @param p der punkt an den gesucht wird
	 * @return die box, oder null wenn an dem punkt keine box liegt
	 */
	private Box getBoxAtClickPosition(Point p) {
		
		Iterator<Box> it = boxList.iterator();
		while(it.hasNext()) {
			Box b = it.next();
			if(b.isInBox(p)) {
				return b;
			}
		}
		
		return null;
		
	}
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		Box b = getBoxAtClickPosition(e.getPoint());
		
		if(b != null) {
			
//			boxSummaryPosition = null;
			
			// nur neu zeichnen wenn eine neue box ausgewählt wurde
			if(!(b.equals(sumShowBox))) {
				Logger.getInstance().log("Setze neue selected Box", Logger.LOGLEVEL_INFO);
				sumStateChanged = true;
				sumShowBox = b;
				parent.repaint();
			}
		} else {
			
			// nur neu zeichnen wenn entweder vor oder nach dem klick eine box ausgewählt war
			if(sumShowBox != null) {
				Logger.getInstance().log("Entferne selected Box", Logger.LOGLEVEL_INFO);
				sumStateChanged = true;
				sumShowBox = null;
				parent.repaint();
			}
		}
		
		if(e.getClickCount() == 2) {
			sumStateChanged = true;
			if(b != null) {
				controller.showProjectInPanel(b.tl);
			}
			
		}

		
	}



	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(SwingUtilities.isRightMouseButton(e)) {
			
		
			/* popup menu anzeigen wenn es über einer box ist */
			Box b = getBoxAtClickPosition(e.getPoint());
			if(b == null) {
				bgContextMenu.show(e.getComponent(), e.getX(), e.getY(), parent);
				parent.repaint();
				
			} else {
				boxContextMenu.show(e.getComponent(), e.getX(), e.getY(), b.tl);
				parent.repaint();
			}
		}
		
	}
	
	
	public int countAllTodos() {
		
		int sum = 0;
		
		Iterator<Box> it = boxList.iterator();
		while(it.hasNext()) {
			Box b = it.next();
			sum = sum + b.tl.countUnsolvedTodos();
		}
		
		return sum;
	}
	
	
	/**
	 * gibt an, ob gesperrte listen angezeigt werden sollen oder nicht
	 * @param show
	 */
	public void showLocked(boolean show) {
		showLocked = show;
		repaint();
	}
	
	
	
	
	/**
	 * Liefert die derzeit selektierte TodoListe zurück. Ist keine selektiert, so wird null zurückgeliefert
	 * @return
	 */
	public TodoList getSelectedTodoList() {
		if(sumShowBox != null) return sumShowBox.tl;
		else {
			return null;
		}
		
	}
	
	
	
	private void initListeners() {
		this.addMouseListener(this);
		
	}
	
	public Box getSelectedBox() {
		return sumShowBox;
	}
	
	public BaseController getBaseController() {
		return controller;
	}
		
}

