package controller;

import gui.models.TodoListModel;

import java.io.File;
import java.util.Locale;

import main.Logger;

import jxl.*;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;


/**
 * Enthält Export-Handler die unterschiedliche Exporte anbieten.
 * Derzeit ist nur ein Excel-Export enthalten
 * 
 * @author Tobias Schaber
 *
 */
public final class ExportController {
	
	/* Anzahl Zellen links bevor die Liste beginnt */
	private static final int horizontalOffsetFields = 1;
	
	/* Anzahl Zellen oben bevor die Liste beginnt */
	private static final int verticalOffsetFields = 3;
	
	/* The default font to use in all cells */
	
	
	/**
	 * Exportiert eine Todo-Liste in eine Excel-Datei
	 * @param todoListModel Die zu exportierende Todo-Liste
	 * @param targetFile Die Datei in die exportiert wird
	 * @throws Exception
	 */
	public static void exportTodoListToExcel(TodoListModel todoListModel,  File targetFile) throws Exception {
		
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("de", "DE"));
		
		WritableWorkbook workbook = Workbook.createWorkbook(targetFile, wbSettings);
		
		/* Setze Namen des Sheets auf Namen des Todo-Projekts */
		workbook.createSheet(todoListModel.getTodoProject().getListName(), 0);
		
		WritableSheet excelSheet = workbook.getSheet(0);
		
		CellView descriptionView = new CellView();
		CellView datumView = new CellView();
		
		/* Definiere Standard-Fonts für header und normalen Text */
		WritableFont defaultWritableFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont headerFont	  = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
		
		/* The default cell format to use in all cells */
		WritableCellFormat defaultCellFormat = new WritableCellFormat(defaultWritableFont);
		WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
		
		/* Header-Zellen bekommen unten einen Rahmen */
		headerCellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		
		descriptionView.setSize(100000);
		datumView.setSize(8000);

		/* Füge die Header-Zellen hinzu */
		excelSheet.addCell(new Label(horizontalOffsetFields,   verticalOffsetFields, "ID", headerCellFormat));
		excelSheet.addCell(new Label(horizontalOffsetFields+1, verticalOffsetFields, "Status", headerCellFormat));
		excelSheet.addCell(new Label(horizontalOffsetFields+2, verticalOffsetFields, "Beschreibung", headerCellFormat));
		excelSheet.setColumnView(horizontalOffsetFields+2, descriptionView);
		
		excelSheet.addCell(new Label(horizontalOffsetFields+3, verticalOffsetFields, "Datum", headerCellFormat));
		excelSheet.setColumnView(horizontalOffsetFields+3, datumView);
		
		excelSheet.addCell(new Label(horizontalOffsetFields+4, verticalOffsetFields, "Priorität", headerCellFormat));
		
		/* Füge alle Datensätze ein */
		for(int row = 0; row<todoListModel.getRowCount(); row++) {
			
			/* Für jede Spalte aus der Ursprungs-Tabelle */
			for(int col = 0; col<todoListModel.getColumnCount(); col++) {
			
			    excelSheet.addCell(new Label(col+horizontalOffsetFields, row+1+verticalOffsetFields, ""+todoListModel.getValueAt(row, col), defaultCellFormat));    
			}

		}

		try {
		    workbook.write();
		    
		} finally {
			Logger.getInstance().log("Closing file handler in excel export", Logger.LOGLEVEL_DEBUG);
			workbook.close();
		}
	}
}


