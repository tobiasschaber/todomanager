package controller;

import java.io.File;
import java.io.IOException;


/**
 * Interface für Konfigurations-Handler. Muss verwendet werden, wenn die Quelle
 * der Konfiguration einmal geändert werden sollte, z.B. in eine
 * Datenbank-Konfiguration oder sonst eine Netzwerk-basierte Konfiguration
 * @author Tobias Schaber
 *
 */
public interface ConfigurationHandlerInterface {
	/**
	 * initializes the configuration.
	 * @param sourceFolder the folder where all config files are stored
	 * @throws IOException for errors reading the config files from disk or
	 * by invalid property content
	 */
    void initConfiguration(File sourceFolder) throws IOException;

	/**
	 * returns a value for the given key.
	 * @param key the key
	 * @return the value
	 */
    String getProperty(String key);
}
