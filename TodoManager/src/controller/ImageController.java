package controller;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Dieser Controller enthält alle Grafiken die im gesamten Programm
 * eingesetzt werden. Auf die Grafiken kann statisch zugegriffen werden.
 * @author Tobias Schaber
 */
public class ImageController {
	
	public static final Image imgHintOverview		= new ImageIcon("gfx/hintOverview.gif").getImage();
	public static final Image imgHintSingleList		= new ImageIcon("gfx/hintSingleList.gif").getImage();
	public static final Image imgHintTableBg		= new ImageIcon("gfx/tableBg.gif").getImage();
	public static final Image imgTrayIcon			= new ImageIcon("gfx/trayIcon.gif").getImage();
	public static final Image imgTrayIconDisabled	= new ImageIcon("gfx/trayIconDisabled.gif").getImage();
	public static final Image imgLock				= new ImageIcon("gfx/lockBig.gif").getImage();
	public static final Image imgCursorLocked 		= new ImageIcon("gfx/cursorLocked.gif").getImage();
	public static final Image imgArrowLeft			= new ImageIcon("gfx/arrowLeft.gif").getImage();
	
	public static final ImageIcon iconOpenEditor 	= new ImageIcon("gfx/openEditor.gif");
	public static final ImageIcon iconAdd			= new ImageIcon("gfx/add.gif");
	public static final ImageIcon iconAddHover		= new ImageIcon("gfx/addHover.gif");
	
	public static final ImageIcon iconExportCsv		= new ImageIcon("gfx/exportCsv.gif");
	public static final ImageIcon iconExportCsvHov	= new ImageIcon("gfx/exportCsvHover.gif");
	
	public static final ImageIcon iconSave 			= new ImageIcon("gfx/save.gif");
	public static final ImageIcon iconSaveUnder 	= new ImageIcon("gfx/saveUnder.gif");
	public static final ImageIcon iconExit 			= new ImageIcon("gfx/exit.gif");
	public static final ImageIcon iconNewFile 		= new ImageIcon("gfx/newFile.gif");
	public static final ImageIcon iconOpenFile 		= new ImageIcon("gfx/openFile.gif");
	
	public static final ImageIcon iconDirectoryOpen = new ImageIcon("gfx/directoryOpen.gif");
	public static final ImageIcon iconDirectory 	= new ImageIcon("gfx/directoryClose.gif");
	
	public static final ImageIcon iconList 			= new ImageIcon("gfx/list.gif");
	public static final ImageIcon iconSearch 		= new ImageIcon("gfx/search.gif");
	public static final ImageIcon iconDone 			= new ImageIcon("gfx/done.gif");
	public static final ImageIcon iconNoAlarm		= new ImageIcon("gfx/noAlarm.gif");
	public static final ImageIcon iconAlarm			= new ImageIcon("gfx/warningBig.gif");
	public static final ImageIcon iconSplashBg		= new ImageIcon("gfx/splashBg.png"); 
	public static final ImageIcon iconInfo			= new ImageIcon("gfx/info.gif");
	public static final ImageIcon iconLock			= new ImageIcon("gfx/lock.gif");
	public static final ImageIcon iconUnlock		= new ImageIcon("gfx/unlock.gif");
	public static final ImageIcon iconRename		= new ImageIcon("gfx/rename.gif");
	public static final ImageIcon iconLogo			= new ImageIcon("gfx/logo.gif");
	public static final ImageIcon iconReminder		= new ImageIcon("gfx/alarm.gif");
	public static final ImageIcon iconReset			= new ImageIcon("gfx/reset.gif");
	public static final ImageIcon iconHelp			= new ImageIcon("gfx/help.gif");
	public static final ImageIcon iconSwitch		= new ImageIcon("gfx/switch.gif");
	public static final ImageIcon iconPlus			= new ImageIcon("gfx/plus.gif");
	public static final ImageIcon iconMinus			= new ImageIcon("gfx/minus.gif");
	public static final ImageIcon iconLockMini		= new ImageIcon("gfx/lockMini.gif");
	public static final ImageIcon iconSleep			= new ImageIcon("gfx/sleepingBig.gif");
	
	
}

