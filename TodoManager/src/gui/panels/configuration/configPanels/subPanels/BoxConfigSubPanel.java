package gui.panels.configuration.configPanels.subPanels;

import gui.components.Box;
import gui.dialogs.ChooseColorInputDialog;
import gui.panels.configuration.configPanels.ConfigBoxPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.Logger;
import controller.ImageController;

public class BoxConfigSubPanel extends JPanel implements MouseListener, MouseMotionListener {
	
	
	
	private final ConfigBoxPanel parent;
	
	
	// abstand der box nach oben und unten
	private static final int boxSpace = 20;
	
	private boolean isResizing 			= false;
	private boolean isMovingWarning 	= false;
	private boolean isMovingReminder 	= false;
		
	
	public BoxConfigSubPanel(ConfigBoxPanel parent) {
		this.parent = parent;
	
		setSize(300, 150);
		setPreferredSize(new Dimension(300, 150));
		
//		this.addMouseMotionListener(parent);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

	}
	
	
	

	
	
	/**
	 * liefert das aktuellste property, jedoch als integer
	 * @param key
	 * @return
	 */
	private int getPropertyAsInt(String key) {
		try {
			return Integer.parseInt(parent.getNewestProperty(key));
		} catch(Exception e) {
			Logger.getInstance().logException("Kann Property nicht laden", e);
			return 0;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		GradientPaint gradient1 = new GradientPaint(boxSpace,boxSpace+getPropertyAsInt("box.width"), new Color(getPropertyAsInt(parent.getBoxLeftCase())), boxSpace+getPropertyAsInt("box.width")+40 ,boxSpace-80, new Color(getPropertyAsInt("box.color.right")),false);
		((Graphics2D)g).setPaint(gradient1);
		
		g.fillRect(boxSpace+Box.sidebarWidth, boxSpace, getPropertyAsInt("box.width"), getPropertyAsInt("box.height"));
		g.setColor(Color.black);
		g.drawRect(boxSpace+Box.sidebarWidth, boxSpace, getPropertyAsInt("box.width"), getPropertyAsInt("box.height"));
		
		
		// zeichne resize-balken rechts unten
		g.drawLine(boxSpace+getPropertyAsInt("box.width")-4+Box.sidebarWidth,
				   boxSpace+getPropertyAsInt("box.height"), 
				   boxSpace+getPropertyAsInt("box.width")+Box.sidebarWidth, 
				   boxSpace+getPropertyAsInt("box.height")-4);
		g.drawLine(boxSpace+getPropertyAsInt("box.width")-7+Box.sidebarWidth,
				   boxSpace+getPropertyAsInt("box.height"), 
				   boxSpace+getPropertyAsInt("box.width")+Box.sidebarWidth, 
				   boxSpace+getPropertyAsInt("box.height")-7);

		g.setFont(Box.DEFAULT_HEADLINE_FONT);
		g.drawString("Name der Liste", boxSpace+15+Box.sidebarWidth, boxSpace+17);
		g.setFont(Box.DEFAULT_FONT);
		g.drawString("3 / 12 Todos offen", boxSpace+Box.sidebarWidth+15, boxSpace+30);	
		
		// zeichne warnung
		g.drawImage(ImageController.iconAlarm.getImage(), boxSpace + Box.sidebarWidth + getPropertyAsInt("box.warning.x"), boxSpace+ getPropertyAsInt("box.warning.y"), null);
		// zeichne reminder
		g.drawImage(ImageController.iconReminder.getImage(), boxSpace + Box.sidebarWidth + getPropertyAsInt("box.reminder.x"), boxSpace+ getPropertyAsInt("box.reminder.y"), null);
		
		double targetWidth = 100;
		//double targetWidth = (tl.countUnsolvedTodos()*(100/tl.countTodos()));

		// berechnung der maximalen breite des balkens (20px abstand)
		int fullWidth = getPropertyAsInt("box.height")-4;
		
		// bunten balken zeichnen
        for (int i = 0; i< (fullWidth * (targetWidth/100))/4; i++) {
        	
        	int red = i*18;
        	int green = 255-(i*20);
        	int blue = 0;
        	
        	if(green < 0) green = 0;
        	if(red > 255) red = 255;
        	
        	g.setColor(new Color(red, green, blue));

            g.fillRect(boxSpace+2, boxSpace+2+i*4, 5, 3);
        }
		
		
	}
	
	
	public void mouseClicked(MouseEvent e) {
		
		int pX = e.getPoint().x;
		int pY = e.getPoint().y;
		
		// klick innerhalb der box
		if(pX > boxSpace && pX < (boxSpace+getPropertyAsInt("box.width")) &&
		   pY > boxSpace && pY < (boxSpace+getPropertyAsInt("box.height"))) {
			
			
			if(pX > (boxSpace + (getPropertyAsInt("box.width")/2))) {
				Color c = ChooseColorInputDialog.showInputDialog("Farbe auswählen (Box rechts)", new Color(getPropertyAsInt("box.color.right")));
				
				if(c != null)	// auf abbrechen reagieren
					parent.updateProperty("box.color.right", ""+c.getRGB());
				   
			} else {
				Color c = ChooseColorInputDialog.showInputDialog("Farbe auswählen (Box links)", new Color(getPropertyAsInt(parent.getBoxLeftCase())));
				
				if(c != null)	// auf abbrechen reagieren
					parent.updateProperty(parent.getBoxLeftCase(), ""+c.getRGB());
			}
			   repaint();
//			
		}
			
		
		

		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {
		isResizing 			= false;
		isMovingWarning 	= false;
		isMovingReminder 	= false;
	}
	
	public void mouseMoved(MouseEvent e) {
		
		int pX = e.getPoint().x;
		int pY = e.getPoint().y;
		
		if((pX > getPropertyAsInt("box.width")+boxSpace-3+Box.sidebarWidth && pX < getPropertyAsInt("box.width")+boxSpace+3+Box.sidebarWidth) && (pY > getPropertyAsInt("box.height")+boxSpace-3 && pY < getPropertyAsInt("box.height")+boxSpace+3)) {
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
				
		}
		else {
			if(!isResizing && ((pX > boxSpace+getPropertyAsInt("box.warning.x") && pX < boxSpace+getPropertyAsInt("box.warning.x")+25) && (pY > boxSpace+getPropertyAsInt("box.warning.y") && pY < boxSpace+getPropertyAsInt("box.warning.y")+25) ||
					((pX > boxSpace+getPropertyAsInt("box.reminder.x") && pX < boxSpace+getPropertyAsInt("box.reminder.x")+25) && (pY > boxSpace+getPropertyAsInt("box.reminder.y") && pY < boxSpace+getPropertyAsInt("box.reminder.y")+25)))) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				
			} else {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
		}

		
	}
	public void mouseDragged(MouseEvent e) {
		
		// mauszeiger bei move, aber nicht bei resize ausblenden
		if(isMovingReminder || isMovingWarning) {
			setCursor(java.awt.Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR),new java.awt.Point(0,0),"NOCURSOR"));
		}
	
		int pX = e.getPoint().x;
		int pY = e.getPoint().y;

		if(isResizing && !isMovingWarning && !isMovingReminder) {
			
			// resize box
			parent.updateProperty("box.width",  ""+(e.getPoint().x-boxSpace)); 
			parent.updateProperty("box.height", ""+(e.getPoint().y-boxSpace));
			
			// größenbegrenzung nach oben und unten einrichten
			if(getPropertyAsInt("box.height") < 10) parent.updateProperty("box.height", ""+10);
			if(getPropertyAsInt("box.width") < 10)  parent.updateProperty("box.width", ""+10);
			if(getPropertyAsInt("box.height") > getHeight()-30) parent.updateProperty("box.height", ""+(getHeight()-30));
			if(getPropertyAsInt("box.width") > getWidth()-30)  parent.updateProperty("box.width", ""+(getWidth()-30));

			repaint();
			
		}
		
		if(isMovingWarning && !isMovingReminder) {
			
			// move warning
			parent.updateProperty("box.warning.x", ""+(e.getPoint().x - boxSpace-10));
			parent.updateProperty("box.warning.y", ""+(e.getPoint().y - boxSpace-10));
			
			if(getPropertyAsInt("box.warning.x") < -20) parent.updateProperty("box.warning.x", "-20");
			if(getPropertyAsInt("box.warning.y") < -20)  parent.updateProperty("box.warning.y", "-20");
			if(getPropertyAsInt("box.warning.x") > getWidth()-60) parent.updateProperty("box.warning.x", ""+(getWidth()-60));
			if(getPropertyAsInt("box.warning.y") > getHeight()-60)  parent.updateProperty("box.warning.y", ""+(getHeight()-60));

			
			repaint();
		}
		
		if(!isMovingWarning && isMovingReminder) {
			
			// move warning
			parent.updateProperty("box.reminder.x", ""+(e.getPoint().x - boxSpace-10));
			parent.updateProperty("box.reminder.y", ""+(e.getPoint().y - boxSpace-10));
			
			if(getPropertyAsInt("box.reminder.x") < -20) parent.updateProperty("box.reminder.x", "-20");
			if(getPropertyAsInt("box.reminder.y") < -20)  parent.updateProperty("box.reminder.y", "-20");
			if(getPropertyAsInt("box.reminder.x") > getWidth()-50) parent.updateProperty("box.reminder.x", ""+(getWidth()-50));
			if(getPropertyAsInt("box.reminder.y") > getHeight()-50)  parent.updateProperty("box.reminder.y", ""+(getHeight()-50));
			
			repaint();
		}
		
		
		
		
		if((pX > getPropertyAsInt("box.width")+boxSpace-3+Box.sidebarWidth  && 
			pX < getPropertyAsInt("box.width")+boxSpace+3+Box.sidebarWidth) && 
		   (pY > getPropertyAsInt("box.height")+boxSpace-3 && 
			pY < getPropertyAsInt("box.height")+boxSpace+3)) {
			
			isResizing = true;
		}
		
		if(!isMovingWarning && 
		   !isResizing && 
		   (pX > boxSpace+getPropertyAsInt("box.reminder.x") && 
		    pX < boxSpace+getPropertyAsInt("box.reminder.x")+20) && 
		   (pY > boxSpace+getPropertyAsInt("box.reminder.y") && 
		    pY < boxSpace+getPropertyAsInt("box.reminder.y")+20)) {

			isMovingReminder = true;
		}
		
		if(!isMovingReminder && !isResizing && (pX > boxSpace+getPropertyAsInt("box.warning.x") && pX < boxSpace+getPropertyAsInt("box.warning.x")+20) && (pY > boxSpace+getPropertyAsInt("box.warning.y") && pY < boxSpace+getPropertyAsInt("box.warning.y")+20)) {

			isMovingWarning = true;
		}
		


	}
	

}
