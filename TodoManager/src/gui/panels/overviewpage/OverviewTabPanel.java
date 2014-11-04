package gui.panels.overviewpage;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.*;

import main.BaseController;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.ConfigurationHandlerImpl;

import data.TodoItem;
import data.TodoList;

/**
 * das overview tab panel zeigt alle todo listen als grafische Blöcke an.
 * @author Tobias Schaber
 */
public class OverviewTabPanel extends JPanel implements MouseMotionListener {
	
	private final BaseController			controller;
	
	private final JPanel					summaryPanel = new JPanel();
	private final PriorityPanel				priorityPanel;
	private final TimePanel					timePanel;
	private final JScrollPane				scrollPaneTimePanel;
	private final BoxSummaryPanel			boxSummaryPanel;
	private final JSplitPane				splitPaneTimePanel;
	
	// hält den  Zustand, ob die summary im unteren fensterbereich eingeblendet wird oder nicht
	private boolean							summaryIsActive = true;

	
//	private Point							boxSummaryPosition;
	

	/**
	 * constructor
	 */
	public OverviewTabPanel(BaseController controller) {
		this.controller = controller;
		

		priorityPanel = new PriorityPanel(controller);
		timePanel = new TimePanel(controller, this);
		scrollPaneTimePanel = new JScrollPane(timePanel);
		boxSummaryPanel = new BoxSummaryPanel(controller, this);
		splitPaneTimePanel = new JSplitPane();
		
		
		
		initComponents();
		initLayout();
		initListeners();
	
		showPriorityList(Boolean.valueOf(ConfigurationHandlerImpl.getInstance().getProperty("showHighPrioListOnStartup")));
		showSummary(Boolean.valueOf(ConfigurationHandlerImpl.getInstance().getProperty("showSummaryDiagramOnStartup")));
		
	}
	
	
	
	private void initListeners() {
		this.addMouseMotionListener(this);
		
	}



	private void initLayout() {
        javax.swing.GroupLayout priorityPanelLayout = new javax.swing.GroupLayout(priorityPanel);
        priorityPanel.setLayout(priorityPanelLayout);
        priorityPanelLayout.setHorizontalGroup(
            priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        priorityPanelLayout.setVerticalGroup(
            priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        summaryPanel.setPreferredSize(new java.awt.Dimension(403, 0));

        javax.swing.GroupLayout summaryPanelLayout = new javax.swing.GroupLayout(summaryPanel);
        summaryPanel.setLayout(summaryPanelLayout);
        summaryPanelLayout.setHorizontalGroup(
            summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        summaryPanelLayout.setVerticalGroup(
            summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        splitPaneTimePanel.setDividerLocation(200);
        splitPaneTimePanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        javax.swing.GroupLayout timePanelLayout = new javax.swing.GroupLayout(timePanel);
        timePanel.setLayout(timePanelLayout);
        timePanelLayout.setHorizontalGroup(
            timePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1198, Short.MAX_VALUE)
        );
        timePanelLayout.setVerticalGroup(
            timePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout boxSummaryPanelLayout = new javax.swing.GroupLayout(boxSummaryPanel);
        boxSummaryPanel.setLayout(boxSummaryPanelLayout);
        boxSummaryPanelLayout.setHorizontalGroup(
            boxSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        boxSummaryPanelLayout.setVerticalGroup(
            boxSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );



        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(priorityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(splitPaneTimePanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(splitPaneTimePanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(priorityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(summaryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
        );
	}
	
	
	
	private void initComponents() {
		
		splitPaneTimePanel.setBorder(BorderFactory.createEmptyBorder());
        splitPaneTimePanel.setDividerLocation(150);
        splitPaneTimePanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        scrollPaneTimePanel.setViewportView(timePanel);		
        splitPaneTimePanel.setRightComponent(scrollPaneTimePanel);
        
		scrollPaneTimePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneTimePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		splitPaneTimePanel.setLeftComponent(boxSummaryPanel);
		
	}
	

	/**
	 * fügt eine TodoList als Grafik hinzu
	 * @param tl
	 */
	public void addProject(TodoList tl) {
		
		boxSummaryPanel.addProject(tl);
		timePanel.resize();
		this.repaint();
	}
	
	/**
	 * entfernt alle boxen und reinitialisiert die liste
	 */
	public void removeAllProjects() {
		boxSummaryPanel.removeAllProjects();
		priorityPanel.clearHighPriorityList();
		
		this.repaint();
		
		
	}
	
	
	/**
	 * entfernt die Grafik einer TodoList
	 * @param tl
	 */
	public void removeProject(TodoList tl) {
		
		boxSummaryPanel.removeProject(tl);
		

	}
	

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		
		
		if(summaryIsActive && boxSummaryPanel.sumStateChanged) {
			boxSummaryPanel.sumStateChanged = false;
			paintSummary(g);
			
		}
		
		timePanel.resize();
		
	}


	
	/**
	 * zeichnet die Übersicht im unteren bereich des panels
	 * @param g2
	 */
	private void paintSummary(Graphics g) {
		
		int todosPending = 0;
		int todosWait = 0;
		int todosTodo = 0;
		int todosAlarm = 0;
		
		
		
		DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
		
		
		if(boxSummaryPanel.getSelectedBox() == null) {
			// werte berechnen für alle todo liste
			
			Iterator<TodoList> it = controller.getCurrentTodoProject().getLists().iterator();
			
			while(it.hasNext()) {
				TodoList tl = it.next();
				todosPending = todosPending + tl.countTodos(TodoItem.STATUS_PENDING);
				todosWait = todosWait + tl.countTodos(TodoItem.STATUS_WAIT);
				todosTodo = todosTodo + tl.countTodos(TodoItem.STATUS_TODO);
				todosAlarm = todosAlarm + tl.countTodos(TodoItem.STATUS_ALARM);
			}

			
		} else {
			
			TodoList tmpList = boxSummaryPanel.getSelectedBox().tl;
			
			// werte berechnen für die eine box
			todosPending 	= tmpList.countTodos(TodoItem.STATUS_PENDING);
			todosWait 		= tmpList.countTodos(TodoItem.STATUS_WAIT);
			todosTodo 		= tmpList.countTodos(TodoItem.STATUS_TODO);
			todosAlarm 		= tmpList.countTodos(TodoItem.STATUS_ALARM);
			

			
			
		}

		localDefaultCategoryDataset.addValue(todosTodo, TodoItem.STATUS_TODO, TodoItem.STATUS_TODO);
		localDefaultCategoryDataset.addValue(todosPending, TodoItem.STATUS_PENDING, TodoItem.STATUS_PENDING);
		localDefaultCategoryDataset.addValue(todosWait, TodoItem.STATUS_WAIT, TodoItem.STATUS_WAIT);
		localDefaultCategoryDataset.addValue(todosAlarm, TodoItem.STATUS_ALARM, TodoItem.STATUS_ALARM);
		
	
		// *******************************************************************************************
		// PLOT ZEICHNEN
		

	    JFreeChart localJFreeChart = ChartFactory.createAreaChart(null, null, null, localDefaultCategoryDataset, PlotOrientation.VERTICAL, false, true, false);
	    localJFreeChart.setBackgroundPaint(getBackground());
	    

	    CategoryPlot localCategoryPlot = (CategoryPlot)localJFreeChart.getPlot();
	    localCategoryPlot.setForegroundAlpha(0.3F);
	    
	    /* Achsen ausblenden falls erforderlich */
//	    localCategoryPlot.getRangeAxis().setVisible(false);
//	    localCategoryPlot.getDomainAxis().setVisible(false);
	    
        localCategoryPlot.setDomainGridlinesVisible(false);
        localCategoryPlot.setRangeGridlinesVisible(false);
        localCategoryPlot.setRangeCrosshairVisible(false);

        
        // Den aktuellen Renderer holen damit später auf ihn zurückgegriffen werden kann (beim Tool-Tip holen)
        CategoryItemRenderer cir = localCategoryPlot.getRenderer();
        
        /**
         * Renderer für die Farben des Charts sowie die Tool-Tips
         * @author Tobias Schaber
         *
         */
        class CustomRenderer extends AreaRenderer {
        	
        	private CategoryItemRenderer cir;
        	
        	public CustomRenderer(CategoryItemRenderer cir) {
        		this.cir = cir;
        	}

        	@Override
        	public Paint getItemPaint(final int row, final int column) {
        		if(column == 0) return Color.orange;
        		if(column == 1) return Color.blue;
        		if(column == 2) return Color.blue;
        		if(column == 3) return Color.red;
        		return Color.white;
        	}

			@Override
			public CategoryToolTipGenerator getToolTipGenerator(int row, int column) {
				return cir.getToolTipGenerator(row, column);
			}

        }
        
        localCategoryPlot.setRenderer(new CustomRenderer(cir));
        
	    ChartUtilities.applyCurrentTheme(localJFreeChart);
	    localJFreeChart.setBackgroundPaint(getBackground());
	    
	    CategoryAxis localCategoryAxis = localCategoryPlot.getDomainAxis();
	    localCategoryAxis.setLowerMargin(0.0D);
	    localCategoryAxis.setUpperMargin(0.0D);
	    
	    NumberAxis localNumberAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
	    localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    localNumberAxis.setLabelAngle(0.0D);
	    
	    // außenrahmen
	    localJFreeChart.getPlot().setOutlinePaint(null);
	    localJFreeChart.getPlot().setBackgroundPaint(getBackground());
	    
	    
	    ChartPanel cp = new ChartPanel(localJFreeChart);
	    cp.setSize(400, 200);
//	    cp.setMaximumSize(new Dimension(400, 200));
	    cp.setPreferredSize(new Dimension(400, 200));
	    
	    summaryPanel.setOpaque(true);
	    summaryPanel.removeAll();
	    
	    summaryPanel.add(cp);
	    summaryPanel.repaint();
	    
	    // *******************************************************************************************
	    
	}




	

	

	
	/**
	 * gibt an, ob die grafische zusammenfassung angezeigt wird oder nicht
	 * @param show
	 */
	public void showSummary(boolean show) {
		summaryIsActive = show;
		summaryPanel.setVisible(show);
		repaint();
	}
	
	
	/**
	 * gibt an ob die liste mit höchster priorität angezeigt wird oder nicht
	 * @param show
	 */
	public void showPriorityList(boolean show) {
		priorityPanel.setVisible(show);
		repaint();
	}
	


	
	@Override
	public void mouseDragged(MouseEvent e) {}
	
	
	/**
	 * überwacht ob die maus über dem time panel ist oder nicht. wenn ja, dann aktiviert er das menü
	 * prüft außerdem, ob die maus über einer box ist, und das box-menü angezeigt werden muss
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if(scrollPaneTimePanel.getBounds().contains(e.getPoint())) {
			
			// menü aktivieren
			timePanel.setMenuVisible(true);
		} else {
			// menü deaktivieren
			timePanel.setMenuVisible(false);
		}
		
//		// wenn die Maus über einer box ist, zeige das menü an
//		if(getBoxAtClickPosition(e.getPoint()) != null)  {
//		
//			this.boxSummaryPosition = e.getPoint();
//			repaint();
//		} else {
//			if(this.boxSummaryPosition != null) {
//				this.boxSummaryPosition = null;
//				repaint();
//			}
//		}
		
		
	}



	public TodoList getSelectedTodoList() {
		return boxSummaryPanel.getSelectedTodoList();
	}
	
	public void setStateChanged() {
		boxSummaryPanel.sumStateChanged = true;
	}



	/**
	 * zeigt ein menü beim hovern über eine box an, in dem die top ten oder sowas drin stehen
	 * @param g
	 */
//	public void paintBoxSummary(Graphics g) {
//		
//		if(boxSummaryPosition != null) {
//			
//			int boxX = boxSummaryPosition.x+12;
//			int boxY = boxSummaryPosition.y;
//			
//			g.setColor(Color.black);
//			g.drawRect(boxX, boxY, 200, 100);
//
//			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
//			
//			g.setColor(Color.green);
//			((Graphics2D)g).fillRect(boxX, boxY, 200, 100);
//			// transparenz wieder deaktivieren
//			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
//			
//			
//			
//			// Wirft noch fehler in IAP5 tdo beim gelockten element
//			for(int i=0; i<3; i++) {
//				Box b = getBoxAtClickPosition(boxSummaryPosition);
//				
//				if(b.tl.getTodoList().size() > 0) {
//					
//					g.setColor(Color.black);
//					String name = b.tl.getTodoList().get(i).getName();
//	
//					int maxIndx = (name.length() > 30 ? 30 : name.length());
//					
//					g.drawString(b.tl.getTodoList().get(i).getName().substring(0, maxIndx), boxX+2, boxY+10+(i*12));
//				}
//			}
//			
//		}
//		
//	}
	
	public OverviewTabPanel getContentPane() {
		return this;
	}
	
}

