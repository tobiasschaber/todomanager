package gui.components;

import gui.BaseFrame;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import data.TodoList;

/**
 * diese klasse wird verwendet um den reiter der tabs mit einem button
 * zum schließen zu versehen.
 * @author Tobias Schaber
 */
public class CloseableTabHead extends JPanel {
	
	private final BaseFrame parent;
	private final TodoList 	tp;
	private final JLabel 	projName;
	
	private String 			name;
	
	
	/**
	 * diese klasse ist für den schließen-button im tab reiter zuständig
	 * @author Tobias Schaber
	 *
	 */
	private class TabButton extends JButton implements ActionListener {
		
		
		/**
		 * constructor
		 */
		private TabButton() {
			
			/* size of the close button icon */
			int size = 17;    
			
			setPreferredSize(new Dimension(size, size));   
			setToolTipText("schließen");         
			setUI(new BasicButtonUI());            
			setContentAreaFilled(false);         
			setFocusable(false);            
			setBorder(BorderFactory.createEtchedBorder());       
			setBorderPainted(false);             
			addMouseListener(buttonMouseListener);      
			setRolloverEnabled(true);                   
			addActionListener(this);  
		}     
		
		
		/* action listener für den schließen-button */
		public void actionPerformed(ActionEvent e) {   
			
			// Gib dem parent mit, welches tab geschlossen werden soll
			parent.closePanel(CloseableTabHead.this);
						
		}    
		
		
		/* die grafik muss nicht neu gezeichnet werden */
		@Override
		public void updateUI() {}
		
		
		/**
		 * zeichnet das kreuz zum schließen
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g); 
			Graphics2D g2 = (Graphics2D) g.create();  
			
			if (getModel().isPressed()) {
				g2.translate(1, 1);   
			}
			
			g2.setStroke(new BasicStroke(2)); 
			g2.setColor(Color.gray);  
			if (getModel().isRollover()) { 
				g2.setColor(Color.red);     
			}
			
			int delta = 6;   
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);    
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();   
		}  
	}
	
	
	
	private final static MouseListener buttonMouseListener = new MouseAdapter() {   
			 
		/**
		 * für hover-effekte über dem button zu verwenden
		 */
		public void mouseExited(MouseEvent e) { 
			Component component = e.getComponent();       
			if (component instanceof AbstractButton) {   
				AbstractButton button = (AbstractButton) component;  
				button.setBorderPainted(false);    
			}      
		} 
	};


	
	/**
	 * constructor
	 * @param name
	 * @param parent
	 */
	public CloseableTabHead(TodoList tp, BaseFrame parent) {
		this.name = tp.getListName();
		this.tp = tp;
		
		/* das parent wird benötigt um später das fenster beim schließen zuordnen zu können */
		this.parent = parent;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setOpaque(false);
		
		projName = new JLabel(name);
		add(projName);
		
		/* abstände um das label setzen */
		projName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		
		JButton closBut = new TabButton();
		add(closBut);
		
		/* abstände um den button setzen */
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}
	

	/**
	 * diese Methode wird überschrieben, um tabs mit/ohne alarm anzeigen zu können
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		if(tp.hasAlarm()) {
			projName.setForeground(Color.red);
		} else {
			projName.setForeground(Color.black);
		}
		
	}
	

	
	@Override
	public void setName(String newName) {
		super.setName(newName);
		this.name = newName;
		projName.setText(name);
		projName.repaint();
	}
}
