package gui.components;

import gui.panels.overviewpage.BoxSummaryPanel;

import java.awt.*;

import main.annotations.OverriddenByConfiguration;

import controller.ConfigurationHandlerImpl;
import controller.ImageController;
import data.TodoList;

	/**
	 * Eine box ist ein grafischer kasten der eine todo liste repräsentiert.
	 * @author Tobias Schaber
	 */
	public class Box {
		
		// farben für halbsichtbare boxen (ohne todos)
		private static Color frontColorNoTodos		= new Color(164, 164, 164);
		
		public static Font DEFAULT_FONT  			= new Font("Arial",Font.PLAIN, 10);
		public static Font DEFAULT_HEADLINE_FONT	= new Font("Arial",Font.PLAIN, 12);
		
		/* breite der sidebar */
		public static int sidebarWidth				= 9;

		/* startpunkte von denen aus gezeichnet wird */
		public int posX;
		public int posY;
		
		/* Koordinaten, an denen die tatsächliche Box anfängt */
		private int posXbox;
		private int posYbox;	
		
		public TodoList tl;
		private BoxSummaryPanel parent;
		
		
		/**
		 * constructor
		 * @param tl
		 */
		public Box(TodoList tl, BoxSummaryPanel parent) {
			this.parent = parent;


			this.tl = tl;
			
		}
		
		
		/**
		 * gibt zurück, ob ein punkt innerhalb einer box liegt
		 * @param p der punkt der geprüft werden soll
		 * @return true wenn der punkt in einer box liegt
		 */
		public boolean isInBox(Point p) {
			
			// prüfen ob die box evtl. ausgeblendet ist
			if(tl.isLocked() && !parent.showLocked) return false;
			
			if(p.getX() > posX && p.getX() < (posX+Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.width")))
					&& p.getY() > posY && p.getY() < (posY+Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.height")))) return true;
			return false;
		}
		

		
		
		/**
		 * zeichnet das Übersichtspanel dieser einen Box
		 * @param g
		 */
		public void paintComponent(Graphics g) {
			
			int boxWidth  = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.width"));
			int boxHeight = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.height"));
			
			/* berechnung wo die echte box anfängt */
			posXbox = posX+sidebarWidth;
			posYbox = posY;
			
			Graphics2D g2 = (Graphics2D) g;
		

			
			/* Zeichne die derzeit aktive Box mit einem Schatten */
			Box b = parent.getSelectedBox();
			if(b != null && b.equals(this)) {
				g2.setColor(Color.gray);
				g2.fillRect(posXbox+2, posYbox+2, boxWidth, boxHeight);
			}
			
			
			GradientPaint gradient1;
			
			@OverriddenByConfiguration
			Color gradC1;
			
			int sumTodos = parent.countAllTodos();

			// Hintegrundfarbe der Kästen für spezialfälle anpassen
			if(tl.hasAlarm()) {
				gradC1 = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.color.left.alarm")));
				
			} else {
				// keine offenen todos
				if(tl.countUnsolvedTodos() == 0) {
					gradC1 = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.color.left.clean")));
					
					
				} else {
					// default farbe
					gradC1 = new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.color.left")));
				}
			}
			
			// farbverlauf
			gradient1 = new GradientPaint(posX,posY+boxWidth, gradC1, posX+boxWidth+40 ,posY-80, new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.color.right"))), false);
			((Graphics2D)g).setPaint(gradient1);
		
			g2.fillRect(posXbox, posYbox, boxWidth, boxHeight);
			g2.setFont(DEFAULT_HEADLINE_FONT);
			
			if(tl.countUnsolvedTodos() == 0) {
				g2.setColor(frontColorNoTodos);
				
			} else {
				// default farbe
				g2.setColor(Color.black);
			}
		
			// rahmen zeichnen
			g2.drawRect(posXbox, posYbox, boxWidth, boxHeight);
			
			// headline zeichnen
			g2.drawString(tl.getListName(), posXbox+15, posYbox+17);
			
			// alarm icon zeichnen
			if(tl.hasAlarm()) {
				int gfxX = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.warning.x"));
				int gfxY = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.warning.y"));
				g2.drawImage(ImageController.iconAlarm.getImage(), posXbox+gfxX, posYbox+gfxY, null);
				
			}
			
			if(tl.hasActiveReminders()) {
				int remX = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.reminder.x"));
				int remY = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.reminder.y"));
				g2.drawImage(ImageController.iconReminder.getImage(), posXbox+remX, posYbox+remY, null);
			}
			
			g2.setFont(DEFAULT_FONT);
			
			g2.drawString(tl.countUnsolvedTodos() + " / " + tl.countTodos() + " Todos offen", posXbox+15, posYbox+30);
			
			
			/* nur malen wenn es überhaupt einen prozentsatz gibt */
			if(tl.countTodos() != 0 && sumTodos != 0) {

				// ziel-breite in prozent des gesamt-balkens
				
				double targetWidth = (tl.countUnsolvedTodos()*(100/sumTodos));
				//double targetWidth = (tl.countUnsolvedTodos()*(100/tl.countTodos()));

				// berechnung der maximalen breite des balkens (20px abstand)
				int fullWidth = boxHeight-4;
				
				// bunten balken zeichnen
		        for (int i = 0; i< (fullWidth * (targetWidth/100))/4; i++) {
		        	
		        	int red = i*18;
		        	int green = 255-(i*20);
		        	int blue = 0;
		        	
		        	if(green < 0) green = 0;
		        	if(red > 255) red = 255;
		        	
		        	g2.setColor(new Color(red, green, blue));
	
	                g2.fillRect(posX+2, posY+2+i*4, 5, 3);
		        }
			}
			
			
			
			// nur waiting-todos -> Schlaf-Grafik zeichnen
			if(tl.hasOnlyWaitingTodos()) {
				int gfxX = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.warning.x"));
				int gfxY = Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.warning.y"));
				g2.drawImage(ImageController.iconSleep.getImage(), posXbox+gfxX, posYbox+gfxY, null);
				
			
			}			
			
			
			// transparent übergrauen und locked-grafik zeichnen wenn locked status aktiv
			if(tl.isLocked()) {
				
				// transparenz aktivieren
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
				
				g2.setColor(new Color(Integer.parseInt(ConfigurationHandlerImpl.getInstance().getProperty("box.color.left.locked"))));
				
				// transparenten kasten drüberzeichnen
				g2.fillRect(posX+9, posY, boxWidth, boxHeight);
				
				// Schloss-Symbol zeichnen
				int iconPosX = posX + (boxWidth/2) - ImageController.imgLock.getWidth(null)/2 + 10;
				int iconPosY = posY + (boxHeight/2) - ImageController.imgLock.getHeight(null)/2;
				g2.drawImage(ImageController.imgLock, iconPosX, iconPosY, null);
				
				// transparenz wieder deaktivieren
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

			}
		}
	}