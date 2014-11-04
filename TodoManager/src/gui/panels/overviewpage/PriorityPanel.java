package gui.panels.overviewpage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.*;

import javax.swing.JPanel;

import main.BaseController;
import main.annotations.OverriddenByConfiguration;
import controller.ConfigurationHandlerImpl;
import data.*;



/**
 * Dieses Priority Panel dient der Anzeige der Top 10 der Todo Items aller Todo
 * Listen, nach Priorität der Todos.
 * @author Tobias Schaber
 */
public class PriorityPanel extends JPanel {
	
	// parameter zum Zeichnen des Prioritäts-Strahls
	private static final int DISTANCE 		= 1;	// Abstand zwischen den Kästchen
	private static final int WHITEBORDER 	= 3;	// Rahmen oberhalb und unterhalb
	private static final int INTERVAL		= 5;	// In welches Intervall sollen 100% aufgeteilt werden
	private static final int DISTANCE_START = 5;	// Abstand zum Rand links
	private static final int WIDTH 			= 10;	// Breite der einzelnen Kästchen
	
	@OverriddenByConfiguration
	public static 		 int PRIORITY_COUNT	= 10;	// Default-Anzahl von Todos die in der High Priority Liste angezeigt werden
	
	private static final int todoHeight 	= 30;
	
	private BaseController 	controller;
	
	// hält die todoitemstacks mit den n höchsten prioritäten
	private TodoItemStack[] highPriorityTis = new TodoItemStack[PRIORITY_COUNT];
	
	
	/**
	 * Default Constructor
	 * @param controller
	 */
	public PriorityPanel(BaseController controller) {
		
		// muss gesetzt sein damit der tool tip registriert wird 
		setToolTipText("");
		
		this.controller = controller;
		
		// Lese Konfiguration ein
		PRIORITY_COUNT = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("highPrioCount"));
		
		initListeners();

	}
	
	
	
	/**
	 * Initialisiert alle Listeners
	 * 
	 */
	private void initListeners() {
		this.addMouseListener(new MouseAdapter() {
			/**
			 * mouse Listener der den Doppelklick auf ein Todo abfängt
		 	 * um diesen in seinem Tab anzuzeigen
			 */
			public void mouseClicked(MouseEvent e) {
				
				if(e.getClickCount() < 2) return;
				
				int hClick = e.getPoint().y;
				
				int indx = (hClick/todoHeight);

				if(indx < highPriorityTis.length) {
					TodoItemStack tisClicked = highPriorityTis[indx];
				
					if(tisClicked != null) {
						
						controller.setSelectedTabByTodoItemStack(highPriorityTis[indx]);
					}
				}
				
			}
		});
	}
	
	
	/**
	 * Zeichnet am rechten Rand eine Uhr, die angibt, wie alt das Todo ist
	 * @param g
	 * @param x Horizontale Position
	 * @param y Vertikale Position
	 * @param tis Der TodoItemStack, zu dem die Uhr gehört
	 */
	public void paintClock(Graphics g, int x, int y, TodoItemStack tis) {
		
		long distHours = tis.getNotModifiedDurationHours();
		
		final long inDays = distHours/24;
		final long inHours = distHours % 24;

		int addSpace = 0;
		
		/* Berechne, wie weit der Startpunkt nach links/rechts verschoben werden muss,
		 * falls nur eine oder 3 Stellen bei den Stunden angezeigt werden müssen */
		if(inDays < 10) addSpace += 6;
		if(inDays >= 100) addSpace -= 6;
		
		if(inDays < 1) 					g.setColor(Color.gray);
		if(inDays >= 1 && inDays < 3) 	g.setColor(Color.green);
		if(inDays >= 3 && inDays < 6)	g.setColor(Color.orange);
		if(inDays >= 6)					g.setColor(Color.red);
		
	
		g.drawString(inDays + "d:" + inHours + "h", x+addSpace,  y+18);
	}

	
	
	/**
	 * Sammelt alle anzuzeigenden TodoItemStacks und kopiert sie in highPriorityTis[] 
	 */
	private void setPaintableTodoItemStacks() {

		// liste zurücksetzen, ansonsten enthält sie evtl. zu viele einträge
		highPriorityTis = new TodoItemStack[PRIORITY_COUNT];
		
		// in dieser Liste werden alle relevanten Todos gesammelt
		List<TodoItemStack> allTodos = new ArrayList<TodoItemStack>();
		
		// in dieser Liste werden alle relevanten Listen gesammelt
		ArrayList<TodoList> allLists = new ArrayList<TodoList>();
		
		if(controller.getBaseFrame().getSelectedTodoList() != null) {
		// Nur die aktuelle TodoListe holen
			allLists.add(controller.getBaseFrame().getSelectedTodoList());
		} else {
		// 	alle Todolisten holen
			allLists = controller.getCurrentTodoProject().getLists();
		}
				
		// alle Todolisten holen
		Iterator<TodoList> itTP = allLists.iterator();
		
		
		
		// baue Liste aller TodoItemStacks die nicht DONE sind auf
		while(itTP.hasNext()) {
			allTodos.addAll(itTP.next().getUnDoneTodoList());
		}
		
		
		// sortieren nach Priorität via Comparable
		Collections.sort(allTodos);
		
		
		/* Liste kürzen wenn zu viele Einträge enthalten sind */
		int indx = PRIORITY_COUNT;
		if(allTodos.size() < indx) indx = allTodos.size();
		
		highPriorityTis = allTodos.subList(0, indx).toArray(highPriorityTis);

		
		
	}
	
	
	
	/**
	 * übernimmt das Zeichnen der gesamten High Priority List.
	 * Hierfür werden zunächst alle Todos in einer Liste gesammelt,
	 * nach Priorität sortiert, die "DONE" Elemente entfernt, und dann
	 * die n Elemente mit höchster Priorität verwendet.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Liste aller anzuzeigenden TodoItemStacks setzen
		setPaintableTodoItemStacks();
		
		
		for(int i=0; i<highPriorityTis.length; i++) {
			
			int startY = (i) * todoHeight;


			if(highPriorityTis[i] != null) {
				
				g.setColor(Color.lightGray);
				g.drawRect(DISTANCE_START, startY, getWidth()-11, 25);
				g.setColor(Color.white);

				if(highPriorityTis[i].getLatestTodo().getStatus().equals(TodoItem.STATUS_ALARM)) {
					g.setColor(new Color(255, 120, 120));
				}
				g.fillRect(DISTANCE_START+1, startY+1, getWidth()-12, 24);
				
				// Namen des Todos ausgeben
				g.setColor(Color.black);
				
				if(highPriorityTis[i].getLatestTodo().getStatus().equals(TodoItem.STATUS_PENDING) || highPriorityTis[i].getLatestTodo().getStatus().equals(TodoItem.STATUS_WAIT)) {
					g.setColor(Color.GRAY);
				}
				
				// der clip wird gesetzt um den bereich einzugrenzen nach rechts, in dem der text steht
				g.setClip(DISTANCE_START, startY, getWidth()-60, 30);
				g.drawString(highPriorityTis[i].getName(), DISTANCE_START + 5, startY + 20);
				
				// clip zurücksetzen damit er nicht für die nächste iteration gilt
				g.setClip(null);
		
				/* Zeichnet eine Uhr am rechten Rand, je nach alter des Todos */
				this.paintClock(g, getWidth()-50, startY, highPriorityTis[i]);
				
				int prio = highPriorityTis[i].getPriority();
				int ct = 0;	
				
				// Prio-Strahlen zeichnen
				for(int i2=0; i2<prio; i2 = i2+INTERVAL) {
				
					g.setColor(new Color(10+(ct*12), 255-(ct*12), 0));
					
					// einzelne Kästchen des Prio-Strahls zeichnen
					g.fillRect(
							DISTANCE_START + DISTANCE + (WIDTH*ct)+(ct*DISTANCE),
							startY + WHITEBORDER-1,
							WIDTH, 
							9-(2*WHITEBORDER));
					++ct;
				}			
			}
		}
	}
	
	
	

	/**
	 * leert die High Priority Liste
	 */
	public void clearHighPriorityList() {
		for(int i=0; i<highPriorityTis.length; i++) {
			highPriorityTis[i] = null;
		}
		
	}

	


	
	/**
	 * zeigt den Tool Tip je nach Box an
	 */
	@Override
	public String getToolTipText(MouseEvent e) {
	
		int hClick = e.getPoint().y;
		
		int indx = (hClick/todoHeight);

		if(indx < highPriorityTis.length) {
			TodoItemStack tisClicked = highPriorityTis[indx];
			if(tisClicked != null) {
				
				if(tisClicked.getLatestTodo().getDescription().equals(tisClicked.getName())) {
					if(tisClicked.getLatestTodo().getText().equals("")) {
						return "-";
					} else {
						return tisClicked.getLatestTodo().getText();
					}
					
				}
				
			}
				
			if(tisClicked == null) return null;
			return tisClicked.getLatestTodo().getDescription();
		}
		return null;
	}

}
