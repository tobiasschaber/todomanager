package gui.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Diese Component dient zur Zeichnung eines Prioritäts-Strahls in der Todo Liste.
 * Je höher die Priorität, desto länger und roter wird der Strahl.
 * @author Tobias Schaber
 *
 */
public class PriorityComponent extends JPanel {
	
	// eigenschaften zum Zeichnen des Priority-Strahls
	private static final int width = 4;			// Breite der einzelnen Felder
	private static final int distance = 1;		// Abstand zwischen den Feldern
	private static final int whiteborder = 4;	// Rahmen oberhalb und unterhalb
	private static final int interval	= 5;	// In welches Intervall sollen 100% aufgeteilt werden
	
	// die priorität des todos
	private int prio;
	
	// hintergrundfarbe
	private static Color bg = new Color(0, 0, 0, 0);

	
	/**
	 * Default Constructor
	 * @param prio
	 */
	public PriorityComponent(int prio) {
		
		
		// Es soll IMMER eine prio angezeigt werden, auch wenn diese 0 ist. Dann halt ganz klein
		if(prio == 0) prio = 1;
		
		this.prio = prio;
		
	}
	
	
	/**
	 * übernimmt das Zeichnen der Komponente
	 */
	@Override
	public void paintComponent(Graphics g) {

		// Hintergrund mit der original Hintegrundfarbe füllen
		g.setColor(bg);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int ct = 0;
		
		for(int i=0; i<prio; i = i+interval) {
			
			
			g.setColor(new Color(10+(ct*12), 255-(ct*12), 0));
			
			// zeichne prio-strahl
			g.fillRect(	
					distance + (width*ct)+(ct*distance),
					whiteborder,
					width, 
					getHeight()-(2*whiteborder));
			++ct;
		}
		
	}
	
	@Override
	public void setBackground(Color col) {
		bg = col;
	}

}
