package gui.panels.overviewpage;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.JPanel;

import main.BaseController;
import utils.DateUtils;
import controller.ConfigurationHandlerImpl;
import controller.ImageController;
import data.*;

/**
 * Das Panel, welches den Zeitstrahl enthält.
 * @author Tobias Schaber
 */
public class TimePanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
		
	private final BaseController controller;
	private final OverviewTabPanel parent;
	
	/* Enthält eine Liste von Korrelationen zwischen TodoItemStacks und (grafischen) Bounds,
	 * um Klick-Positionen im Panel inem TodoItemStack zuzuordnen bzw. Tool-Tips zu zeigen */
	private ArrayList<BoundsToItem>	bounds2item;
	
	/* Maße des Panels */
	private int width	= 0;
	private int height	= getHeight();
	
	private final int 			gridHorFrontSpace	= 150;	// Space ganz links
	private int 				gridIntervalDays 	= 7;	// interval in tagen zwischen zwei lines
	private int 				gridSpace			= 100;	// Space zwischen zwei Lines
	private final static int 	defaultGridSpace	= 100;	// Standard-Breite eines Grids
	
	private static final int 	gradientStrength 	= 100;	// Stärke des Farbverlaufs der Timeline
	
	private static final Color	COLOR_WEEKEND		= Color.LIGHT_GRAY;	// Farbe der Hintergrund-Schraffur bei Wochenenden
	
	private boolean menuVisible		= false;	// Gibt an ob das kleine Menü in der Ecke sichtbar ist
	private boolean showLeftArrow 	= false;	// Gibt an ob der Indikator "Links gibt es ungesehene Items" angezeigt wird
	
	/* initiales Start-Datum, das den linken Anfang des Panels definiert */
	private Date initialRangeStart  = DateUtils.removeTimeFromDate(DateUtils.getPreMontag(new Date()));
	
	/* Aktuelles Start-Datum für den linken Anfang. Wird zurückgesetzt durch setzen auf initialRangeStart */
	private Date rangeStart			= initialRangeStart;
	
	
	// Koordinaten-Eigenschaften des Einblend-Menüs rechts unten
	private final int menuWidth = 94;	// breite
	private final int menuHeight = 18;	// höhe
	private int menuX = 0; 				// position hor	
	private int menuY = 0;				// position ver
	
	
	
	/**
	 * Default Constructor
	 * @param ct
	 * @param parent
	 */
	public TimePanel(BaseController ct, OverviewTabPanel parent) {
		
		this.parent = parent;
		this.controller = ct;
		this.setBackground(new Color(247, 225, 167));
		
		/* In config schauen ob in Tages- oder Wochenansicht gestartet wird */
		final String startupMode = ConfigurationHandlerImpl.getInstance().getProperty("timepanel.startup.default.view");
		if(startupMode.equals("week")) 	gridIntervalDays = 7;
		else							gridIntervalDays = 1;
		
		setToolTipText("");
		
		initListeners();
		
		
		
	}

	
	/**
	 * Initialisiert alle Listener
	 */
	private void initListeners() {
		
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				/* wenn sich die höhe des Panels verändert, muss die Position des Menüs angepasst werden */
//				menuY = getHeight() - menuHeight;
			}
		});
		
	}
	
	

	/**
	 * kann von außen aufgerufen werden, um die größe des panels zu aktualisieren
	 */
	public void resize() {
		
		setSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
	}
	
	
	
	
	/**
	 * gibt an ob ein Punkt p im Menü rechts unten liegt
	 * @param p
	 * @return
	 */
	private boolean isInMenu(Point p) {
		
		if(p.x > menuX && p.y > menuY) return true;
		
		return false;
		
	}


	
	/**
	 * zeichnet das Zeitstrahl-Panel
	 */
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		showLeftArrow = false;
		
		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial", Font.PLAIN, 8));
		
		// Intervall-Anzeige links unten
		String interval;
		
		if(gridIntervalDays >= 7) interval = "Wochenansicht (" + gridIntervalDays + " Tage)";
		else					  interval = "Tagesansicht";
		
		g.drawString(interval, 5, 7);
		
		paintGrid(g);
		
		bounds2item = new ArrayList<BoundsToItem>();
		
		
		String showListName;
		
		// Liste die in der Timeline angezeigt werden soll
		ArrayList<TodoList> toShowList = null;
		
		// wenn keine Liste ausgewählt ist -> alle nehmen
		if(parent.getSelectedTodoList() == null) {
			
			toShowList = controller.getCurrentTodoProject().getLists();
			showListName = "Alle Listen";
			
		} else {
			
			// Liste erzeugen mit nur der ausgewählten Liste
			toShowList = new ArrayList<TodoList>();
			toShowList.add(parent.getSelectedTodoList());
			showListName = parent.getSelectedTodoList().getListName();
			
		}
		
		g.setColor(Color.darkGray);
		g.drawString("Ausgewählt: " + showListName, 5, getHeight()-5);
		
		// Alle Bars zeichnen
		paintAllBars(toShowList, g);
			
		
		// Zeichne den Pfeil links bei Bedarf
		if(showLeftArrow) {
			paintLeftArrow(g);
		}
		
		// Menü darüberzeichnen
		paintMenu(g);


	}
	
	
	/**
	 * Berechnet die Höhe im Panel, an der etwas gezeichent werden soll,
	 * anhand der Reihe
	 * @param row
	 * @return
	 */
	private int calculateHeightByRow(int row) {
		return row*20;
	}
	
	

	/**
	 * Zeichnet alle Bars für eine ganze TodoListe
	 * @param toShowList
	 * @param g
	 */
	private void paintAllBars(ArrayList<TodoList> toShowList, Graphics g) {
		
		int row = 0;

		for(TodoList tl : toShowList) {
			
			List<TodoItemStack> tmpList = tl.getTodoList();
			
			/* sort by start date */
			Collections.sort(tmpList, new TodoItemStackByStartTimeComparator());
		
			boolean nameIsShown = false;
			
			for(TodoItemStack tis : tmpList) {
				
				/* Die Pfeil-Berechnung muss nur ausgeführt werden, wenn bisher
				 * noch kein Grund für einen Pfeil gefunden wurde */
				if(!showLeftArrow) {
					checkShowLeftArrow(tis);	
				}
				
				
				/* Filtere definitiv nicht zu zeichnende TIS aus */
				if(mustBeShown(tis)) {
					
					boolean painted = false;
					
					
					
					// Start und Ende existieren -> Normale Bar
					if(tis.getPlanStart() != null && tis.getPlanEnd() != null) {
						paintBar(g, row, tis);
						painted = true;
					}
					
					// Nur Ende existiert -> End Bar
					if(tis.getPlanStart() == null && tis.getPlanEnd() != null) {
						paintEndBar(g, row, tis);
						painted = true;
					}
				
					// Nur Start existiert -> Start Bar
					if(tis.getPlanStart() != null && tis.getPlanEnd() == null) {
						paintStartBar(g, row, tis);
						painted = true;
					}
				
					// Reminder Icon
					if(tis.getReminderActivationStatus()) {
						paintReminderIcon(g, row, tis);
						painted = true;
					}
				
					// Wenn etwas gezeichnet wurde
					if(painted) {
						if(!nameIsShown) {
							paintListName(g, tl, row, nameIsShown);
							nameIsShown = true;
							painted = true;
						}
						
						height = calculateHeightByRow(row) + 40;
						row++;
					}
			
				}
			}
		}
	}
	


	/**
	 * Berechnet für einen TodoItemStack, ob er gezeichnet werden muss oder nicht.
	 * Nicht gezeichnet werden z.B. DONEs und zu alte Todos
	 * @param tis
	 * @return
	 */
	private boolean mustBeShown(TodoItemStack tis) {
		
		/* Zeige keine DONEs */
		if(tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
			return false;
		}
		
		/* Kein Reminder aktiv */
		if(!tis.getReminderActivationStatus()) {
			
			/* Das Ende darf nicht vor dem Anfang der Skala liegen */
			if(tis.getPlanEnd() != null && tis.getPlanEnd().before(rangeStart)) return false;
			
		} else {
			/* Wenn Reminder aktiv sind, aber sowohl reminder als auch normale Bar zu alt sind, nicht zeichnen */
			if(tis.getPlanEnd() != null && tis.getPlanEnd().before(rangeStart) && tis.getReminderDate().before(rangeStart)) return false;
		}
		
		/* ansonsten -> vorerst kein grund nicht zu zeichnen */	
		return true;
	}


	
	/**
	 * Berechnet, ob für ein TodoItemStack der linke Pfeil gezeichnet werden muss,
	 * und setzt ggf. den Wert neu, so dass der Pfeil später gezeichnet wird.
	 * @param tis
	 */
	private void checkShowLeftArrow(TodoItemStack tis) {
		
		Date start = null;
		Date end   = null;
		
		if(tis.getPlanEnd() != null) 	end   = tis.getPlanEnd();
		if(tis.getPlanStart() != null)	start = tis.getPlanStart();
		
						
		if(start == null && end != null && end.compareTo(rangeStart) < 0 && !tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
			showLeftArrow = true;
			return;
		}
			
		if(start != null && end == null && start.compareTo(rangeStart) < 0 && !tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
			showLeftArrow = true;
			return;
		}
			
		if(start != null && end != null && end.compareTo(rangeStart) < 0 && !tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
			showLeftArrow = true;
			return;
		}
		
		Date reminder = null;
		if(tis.getReminderActivationStatus() && tis.getReminderDate() != null) reminder = tis.getReminderDate();

		
		if(reminder != null && reminder.compareTo(rangeStart) < 0 && !tis.getLatestTodo().getStatus().equals(TodoItem.STATUS_DONE)) {
			showLeftArrow = true;
			return;
		}
		
	}


	/**
	 * zeichnet einen Pfeil nach Links der anzeigt, dass in nicht angezeigter Vergangenheit noch Todos liegen
	 * @param g
	 */
	private void paintLeftArrow(Graphics g) {
		
		g.drawImage(ImageController.imgArrowLeft, gridHorFrontSpace-20, 2, null);
		
	}
	
	
	

	/**
	 * zeichnet das menü, wenn die maus über dem panel ist
	 * @param g
	 */
	private void paintMenu(Graphics g) {
		
		if(menuVisible) {
			menuY = getVisibleRect().height - menuHeight;
			menuX = getWidth()-menuWidth-18;
			
			g.setColor(Color.white);
			g.fillRect(menuX-2, menuY-2, menuWidth-2, menuHeight);
			g.setColor(Color.black);
			g.drawRect(menuX-2, menuY-2, menuWidth-2, menuHeight);
			
			g.drawImage(ImageController.iconReset.getImage(),	menuX, menuY, null);
			g.drawImage(ImageController.iconPlus.getImage(),	menuX+18, menuY, null);
			g.drawImage(ImageController.iconMinus.getImage(),	menuX+36, menuY, null);
			g.drawImage(ImageController.iconSwitch.getImage(),	menuX+54, menuY, null);
			g.drawImage(ImageController.iconHelp.getImage(),	menuX+72, menuY, null);
		}
		
	}
	

	/**
	 * zeichnet eine Standard-Bar, die einen Anfangs- und einen End-Zeitpunkt hat.
	 * die bar hat einen Farbverlauf
	 * @param g
	 * @param y
	 * @param tis
	 */
	private void paintBar(Graphics g, int y, TodoItemStack tis) {
		
		int height = 15;
		int horStart = getHorPositionOfDate(tis.getPlanStart());
		int horEnd	 = getHorPositionOfDate(tis.getPlanEnd());
		
		// ein gridspace dazu = 1 "tag" mehr, da das item bis zum abend geht
		int boxWidth = horEnd-horStart+(gridSpace/this.gridIntervalDays);
		
		/* Wandert der Startpunkt links aus dem Anzeigebereich, muss
		 * die Breite der Box gekürzt werden und der Startpunkt auf die linke
		 * Grenze gesetzt werden 
		 */
		if(horStart < gridHorFrontSpace) {
			boxWidth = boxWidth - (gridHorFrontSpace-horStart);
			// start in der vergangenheit auf heute setzen
			horStart = gridHorFrontSpace;
		}

		
		y = calculateHeightByRow(y);
		
		// bounds an das todoitemstack registrieren
				bounds2item.add(new BoundsToItem(tis, new Rectangle(horStart, y+28, boxWidth, 15)));

				
		
		// Farbe der standard-bar
		g.setColor(Color.white);
		
		// Standard-Farbe festlegen
		Color e1 = new Color(255, 226, 125);
		
		// Farbe für die Kategorie auslesen
		e1 = ConfigurationHandlerImpl.getInstance().getCategoryColor(tis.getCategory());
		
		/* Farbtöne für die Sekundär-Farbe für den Farbverlauf bestimmen. Dieser weicht in allen Farbtönen um "gradientStrength" ab. */
		int cr = 0;
		int cg = 0;
		int cb = 0;
		
		// Die Farbwerte dürfen nicht < 0 sein
		if(e1.getRed() > gradientStrength) 		cr = e1.getRed()-gradientStrength;
		if(e1.getGreen() > gradientStrength) 	cg = e1.getGreen()-gradientStrength;
		if(e1.getBlue() > gradientStrength) 	cb = e1.getBlue()-gradientStrength;
		
		final Color e2 = new Color(cr, cg, cb);
		
		/* berechnet ca. die Helligkeit der Farbe */
		double lightIndicator = (0.2126 * cr) + (0.7152 * cg) + (0.0722 * cb);
		

		 
		// mit farbverlauf
		GradientPaint gradient1 = new GradientPaint(100, 100, e2, 200, 200, e1, true);
		((Graphics2D)g).setPaint(gradient1);
		((Graphics2D)g).fillRect(horStart,y+28,boxWidth,height);
		g.setColor(Color.GRAY);
		((Graphics2D)g).drawRect(horStart,y+28,boxWidth,height);
		

		/* wenn die Farbe als "hell" eingestuft wird (Wert > 100), Textfarbe weiß, sonst schwarz */
		if(lightIndicator < 100) {
			g.setColor(Color.white);
		} else {

			g.setColor(Color.black);
		}
		
		g.setFont(new Font("Arial", Font.PLAIN, 9));
		g.drawString(tis.getName(), horStart+2, y + 40);
		
		
	}
	
	
	
	/**
	 * zeichnet eine "End-Bar", die keinen Anfangszeitpunkt hat
	 * @param g
	 * @param y
	 * @param tis
	 */
	private void paintEndBar(Graphics g, int y, TodoItemStack tis) {
		
		final int height = 15;
		
		int horStart = getHorPositionOfDate(tis.getPlanEnd());

		
		if(horStart < gridHorFrontSpace) {
			// start in der vergangenheit auf heute setzen
			horStart = gridHorFrontSpace;
		}
		
		y = calculateHeightByRow(y);
		
		// bounds an das todoitemstack registrieren
		bounds2item.add(new BoundsToItem(tis, new Rectangle(horStart-10, y+28, 15, 15)));

		
		// Farbe der End-Bar
		g.setColor(Color.red);
		
		g.fillRect(horStart, y+28, 2, height);
		g.drawLine(horStart, y+28+(height/2), horStart-10, y+28+(height/2));
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.PLAIN, 9));
		g.drawString(tis.getName(), horStart+7, y + 40);
		
	}
	
	
	/**
	 * zeichnet ein "Alarm-Bildchen"
	 * @param g
	 * @param y
	 * @param tis
	 */
	private void paintReminderIcon(Graphics g, int y, TodoItemStack tis) {
		
		y = calculateHeightByRow(y);
		
		int horStart = getHorPositionOfDate(tis.getReminderDate());
		
		if(horStart < gridHorFrontSpace) {
			/* nicht anzeigen da es in der Vergangenheit liegt */
			return;	
		}
		
		
		/* Horizontale Position des Icons = X + einen halben Tag - die Hälfte der Icon-Breite,
		 * dann ist das Icon perfekt mittig im Tag zentriert */
		
		int dayWidth = gridSpace;
		if(gridIntervalDays != 1) dayWidth = gridSpace/gridIntervalDays;
		
		int horPositionOfIcon = horStart+(dayWidth/2)-(ImageController.iconReminder.getIconWidth()/2);
		
		bounds2item.add(new BoundsToItem(tis, new Rectangle(horPositionOfIcon, y+28, 15, 15)));
		
		
		g.drawImage(ImageController.iconReminder.getImage(), horPositionOfIcon, y+28, null);
		
		
	}
	
	/**
	 * zeichnet eine "Start-Bar", die kein End-Datum besitzt
	 * @param g
	 * @param y
	 * @param tis
	 */
	private void paintStartBar(Graphics g, int y, TodoItemStack tis) {

		final int height = 15;
		

		int horStart = getHorPositionOfDate(tis.getPlanStart());
		
		if(horStart < gridHorFrontSpace) {
			// start in der vergangenheit auf heute setzen
			horStart = gridHorFrontSpace;
		}			
		
		y = calculateHeightByRow(y);

		// bounds an das todoitemstack registrieren
		bounds2item.add(new BoundsToItem(tis, new Rectangle(horStart, y+28, 15, 15)));
		
		//Farbe der start-bar
		g.setColor(Color.green);
		
		g.fillRect(horStart, y+28, 2, height);
		g.drawLine(horStart, y+28+(height/2), horStart+10, y+28+(height/2));
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.PLAIN, 9));
		g.drawString(tis.getName(), horStart+15, y + 40);
		
	}

	
	
	
	/**
	 * Liefert die horizontale Position, an der ein Datum d eingezeichnet werden muss
	 * @param d das Datum das gezeichnet werden soll
	 * @return Anzahl in Pixeln vom linken Rand 
	 */
	private int getHorPositionOfDate(Date d) {

		// zeit in sekunden die seit dem angezeigten startpunkt vergangen sind
		long difSecFromStart = (d.getTime() - (rangeStart.getTime()))/1000;
		
		// wg. rundung erhöhen
		difSecFromStart++;
		
		// umrechnung der sekunden in tage
		int difDaysFromStart = (int)(difSecFromStart/(60*60*24));
		
		// die rundung bei int berücksichtigt kleiner 0 und größer -0,5 nicht richtig
		float f = (float)difSecFromStart/(60*60*24);

		if(f < 0) difDaysFromStart--;
		
		// position berechnen
		int spacePerDay = ((difDaysFromStart/gridIntervalDays)*gridSpace) + ((difDaysFromStart%gridIntervalDays)*(gridSpace/gridIntervalDays));
		
		// leerer bereich links addieren
		int pos = gridHorFrontSpace + spacePerDay;
		
		
		return pos;
	}
	
	
	
	/**
	 * Zeichnet im linken Bereich den Namen der Todo-Liste, falls diese für diese Liste noch nicht gezeichnet wurde
	 * @param g
	 * @param name Der zu zeichnende Name
	 * @param height Vertikale Position an der der Name stehen soll
	 * @param isShown gibt an, ob der Listen-Name bereits gezeichnet wurde oder nicht
	 * @return true wenn der Name neu gezeichnet wurde, false wenn er vorher schon gezeichnet wurde
	 */
	private void paintListName(Graphics g, TodoList tl, int y, boolean isShown) {
		
		 y = calculateHeightByRow(y);
		 
		if(!isShown) {
			
			/* Zeichne Schloss beim gelockten Listen */
			if(tl.isLocked()) {
				g.drawImage(ImageController.iconLockMini.getImage(), 4, y+31, null);
			}

			g.setColor(Color.gray);
			g.setFont(new Font("Arial", Font.BOLD, 10));
			
			// Zeichenbereich begrenzen, damit zu lange Namen abgeschnitten werden
			g.setClip(10, y+20, 200, 30);
			g.drawString(tl.getListName(), 15, y+40);
			
			// Sperre zurücksetzen
			g.setClip(null);
		}
	}
	
	
	
	/**
	 * zeichnet das Hintergrund-Grid, also die Datums-Striche
	 * @param g
	 */
	private void paintGrid(Graphics g) {

		// Anzahl gestrichelter Tages-Trenner
		int ctLines = (getWidth()-gridHorFrontSpace) / gridSpace;
		
		
		for(int i=0; i<=ctLines; i++) {
				
			// anzuzeigendes datum für die linie berechnen
			Date dShow = new Date(rangeStart.getTime() + (i * (long)(gridIntervalDays*1000*24*60*60)));
			Calendar c = Calendar.getInstance();			
			c.setTime(dShow);
			
			
			// aktuellen tag in der woche
			int day = c.get(Calendar.DAY_OF_WEEK);

			
			// Kalkuliere "heute"
			Date dTod = DateUtils.removeTimeFromDate(new Date());
			
			// Wenn "heute" dann rot zeichnen 
			if((dShow.getTime()/1000 == dTod.getTime()/1000)) 	g.setColor(Color.RED);
			else		g.setColor(Color.LIGHT_GRAY);
			
			int h = getHeight();	
			int space = 10;			// abstand zwischen zwei strichen
			int part = 3;			// länge eines striches
			

			/* Wenn wir beim Zeichnen noch ganz links sind und ein Sonntag ist, muss dieser gezeichnet werden */
			if(i == 0 && day == 1) {
				
				// Wochenende füllen
				g.setColor(COLOR_WEEKEND);
				fillWeekend(g, gridHorFrontSpace, 0, (gridSpace/gridIntervalDays), getHeight());
			}
			
			// berechne position von dem auf diese woche folgenden wochenende
			int nextWeekendStartPosition = (gridHorFrontSpace+(i*gridSpace)) + (7-day)*(gridSpace/gridIntervalDays);

			// Wochenende füllen
			g.setColor(COLOR_WEEKEND);
			
			fillWeekend(g, nextWeekendStartPosition, 0, (2*(gridSpace/gridIntervalDays)), getHeight());

			
			// Die gestrichelten Trennstriche nur in der Tagesansicht anzeigen
			if(gridIntervalDays != 7) {
				for(int k=0; k<h; k = k + space + part) {
					g.drawLine((gridHorFrontSpace+(i*gridSpace)), (k), (gridHorFrontSpace+(i*gridSpace)), (k + part));				
				}
			}

			
			/* Wenn die einzelnen Spalten zu schmal werden, wird das Jahr
			 * am Ende entfernt */		
			String date = "";
			if(gridSpace >= 90)		date = new SimpleDateFormat("dd.MM.yyyy").format(dShow);
			else					date = new SimpleDateFormat("dd.MM").format(dShow);
			

			g.setColor(Color.gray);
			g.setFont(new Font("Arial", Font.PLAIN, 10));
			g.drawString(date, (gridHorFrontSpace+(i*gridSpace)) + 5, 15);
			
			
		}
		
		g.setColor(Color.red);
		g.drawLine(getHorPositionOfDate(new Date()), 5, getHorPositionOfDate(new Date()), getHeight());
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		
		g.drawString("Heute", getHorPositionOfDate(new Date())+5, 25);
		
	}

	
	

	/**
	 * füllt die übergebene fläche mit der weekend-füllung
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void fillWeekend(Graphics g, int x, int y, int width, int height) {
		
		int dist = 2;
		
		for(int i=-width; i<height; i = i+dist) {
			g.drawLine(x, i, x+width, i+(width));
		}
		
	}

	
	
	
	/**
	 * liefert den TodoItemStack, der an einem Klickpunkt angezeigt wird
	 * @param p der klickpunkt
	 * @return
	 */
	private TodoItemStack getTodoItemStackAtPosition(Point p) {
		for (BoundsToItem bi : bounds2item) {
						
			if(bi.bounds.contains(p)) {
				return bi.tis;
			}
		}
		
		return null;
	}
	
	
	private void scrollLeft() {
		
		Date d = new Date(rangeStart.getTime());
		rangeStart = new Date(d.getTime() + (24*60*60*1000));
		resize();
		repaint();
	}
	
	private void scrollRight() {
		Date d = new Date(rangeStart.getTime());
		rangeStart = new Date(d.getTime() - (24*60*60*1000));
		resize();
		repaint();
	}

	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		// Links scrollen
		if(e.getWheelRotation() == 1) {
			scrollLeft();
		}
		
		// Rechts scrollen
		if(e.getWheelRotation() == -1) {
			scrollRight();
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//TODO: EXPERIMENTAL FUNCTION SET
		getDayAtPoint(e.getPoint());
		
		
		if(e.getClickCount() == 2) {

			TodoItemStack tis = getTodoItemStackAtPosition(e.getPoint());

			if(tis != null) controller.setSelectedTabByTodoItemStack(tis);

			
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		setMenuVisible(true);
	}
	public void mouseExited(MouseEvent e) {
		setMenuVisible(false);	
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {
		// linke maustaste

		if(isInMenu(e.getPoint())) {
			
			int buttonIndex = ((e.getPoint().x - menuX) / 18);

			switch(buttonIndex) {
			
				// Reset Button
				case 0:
					
					gridSpace = defaultGridSpace;
					
					rangeStart = initialRangeStart;

				break;
				
				
				// Rauszoomen Button
				case 1:
					
					gridSpace = gridSpace + 20;
					
					repaint();
				break;
					
				// Ranzoomen Button
				case 2:
					
					if(gridSpace > 20) gridSpace = gridSpace - 20;
					
					repaint();
				break;
				
				
				// Tages- und Wochenansicht switchen
				case 3:
					
					if(gridIntervalDays == 7) gridIntervalDays = 1;
					else gridIntervalDays = 7;
				break;
				
				
				// Hilfe-Hover
				case 4:
					return;
					
			}
			
			repaint();			

		}
	}
	
	
	
	
	/**
	 * blendet das menü zum verändern des charts ein oder aus
	 * @param visStatus
	 */
	public void setMenuVisible(boolean visStatus) {
		menuVisible = visStatus;
		repaint();
	}
	
	
	
	
	/**
	 * Zeigt die Tool-Tips sowohl für die Buttons als auch für die einzelnen angezeigten
	 * Elemente im Panel an. Je nach dem ob ein Text zu einem Item existiert wird dieser
	 * angezeigt, wenn kein Text existiert, dann wird nur die Beschreibung als Tool-Tip
	 * genommen.
	 */
	@Override
	public String getToolTipText(MouseEvent e) {
	
		if(isInMenu(e.getPoint())) {
		

			int buttonIndex = ((e.getPoint().x - menuX) / 18);
			
			switch(buttonIndex) {
			
				case 0:
					return "Zurücksetzen";
				case 1:
					return "Intervall erhöhen";
				case 2:
					return "Intervall verkleinern";
				case 3:
					return "Tages/Wochenansicht wechseln";
				case 4:
					return "Scrollen: Sichtbereich verschieben.";
			}
			
		} else {
			
			
			if(e.getPoint().x > gridHorFrontSpace-20 
					&& e.getPoint().x < gridHorFrontSpace
					&& e.getPoint().y < 20) {
				
				if(showLeftArrow) return "Es gibt nicht angezeigte Todos zu früherem Zeitpunkt";
				
			} else {
				
				/* Prüfe für alle im Panel enthaltenen Grafiken, ob wir gerade über
				 * einem Element sind. Wenn ja -> Zeige entsprechenden ToolTip
				 */
				for(BoundsToItem item : bounds2item) {
					
					if(item.bounds.contains(e.getPoint())) {
						
						String text = item.tis.getLatestTodo().getText();
						
						String category = "";
						if(item.tis.getCategory() != null) category = " (" + item.tis.getCategory() + ")";
						
						/* Wenn kein Text existiert, zeige den Namen */
						if(text == null || text.equals("")) {
							return item.tis.getLatestTodo().getDescription() + category;
						} else {
							return item.tis.getLatestTodo().getDescription() + ": " + item.tis.getLatestTodo().getText() + category;
						}
					}		
				}
			}
		}
		
		return null;
	}
	
	
	
	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e)   {}
	
	
	//TODO: EXPERIMENTAL FUNCTION SET
	/**
	 * Liefert für einen gegebenen Punkt auf dem TimePanel den an der
	 * Position des Punktes liegende Tag als Date zurück
	 * @param p Ein Punkt auf dem TimePanel
	 * @return Das Datum welches unter dem Punkt liegt
	 */
	private Date getDayAtPoint(Point p) {
		
		long horPos = (long)p.getX();
		
		// Breite die in echte Tage unterteilt werden muss
		long horDif = horPos - gridHorFrontSpace;
		
		long restDays = horDif/((gridSpace/gridIntervalDays));
		
		Date realDate = new Date(rangeStart.getTime() + (restDays*24*60*60*1000));
		System.out.println(realDate);
		return realDate;
	}
}






/**
 * beinhaltet eine Korrelation zwischen den Bounds(grafische Grenzen)
 * eines Items, sowie dessen TodoItemStacks
 * @author Tobias Schaber
 *
 */
class BoundsToItem {
	
	// der TodoItemStack
	public TodoItemStack tis;
	
	// die für den TIS genutzten Bounds
	public Rectangle bounds;
	
	public BoundsToItem(TodoItemStack tis, Rectangle bounds) {
		
		this.tis = tis;
		this.bounds = bounds;
		
	}
	

}



