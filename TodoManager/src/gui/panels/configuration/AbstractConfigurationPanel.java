package gui.panels.configuration;

import java.util.Properties;

import javax.swing.JPanel;


/**
 * vorlage für alle config panels. somit hat jedes config panel eine eigene history
 * @author Tobias Schaber
 *
 */
public abstract class AbstractConfigurationPanel extends JPanel {
	
	// enthält alle properties, die in diesem panel geändert wurden
	private Properties 	changedProperties = new Properties();
	
	// gibt an ob es in diesem panel Änderungen gab
	private boolean		changed = false;
	
	
	
	/**
	 * fügt ein geändertes property hinzu
	 * @param key
	 * @param value
	 */
	public void updateProperty(String key, String value) {
		changed = true;
		changedProperties.setProperty(key, value);
	}
	

	/**
	 * liefert alle während der Bearbeitung geänderten Properties 
	 * dieses einzelnen config panels zurück
	 * @return
	 */
	protected Properties getChangedProperties() {
		
		return changedProperties;
	}
	
	
	/**
	 * liefert zurück, ob in diesem panel etwas geändert wurde (z.B. um beim 
	 * cancel-button nachzufragen)
	 * @return
	 */
	protected boolean hasChanged() {
		return changed;
	}
	

	
	protected String getHeadline() {
		return "Überschrift";
	}
	
	
	/**
	 * setzt die bisher gemachten Änderungen zurück.
	 */
	protected void resetChangedProperties() {
		changedProperties = null;
		changedProperties = new Properties();
		initComponents();
		
		
	}
	
	/**
	 * Diese Methode ist zu überschreiben. In ihr werden alle Felder mit dem Wert
	 * aus der Konfiguration belegt. Deswegen müssen die Felder auch außerhalb instanziiert
	 * und dann in dieser Methode nur noch beschrieben werden.
	 */
	protected abstract void initComponents();
	

}
