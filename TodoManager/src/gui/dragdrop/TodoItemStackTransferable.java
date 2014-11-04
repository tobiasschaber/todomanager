package gui.dragdrop;

import java.awt.datatransfer.*;
import java.io.IOException;

import main.Logger;
import data.TodoItemStack;


/**
 * Dieses Transferable dient dazu, TodoItemStacks per Drag&Drop zu bewegen.
 * Es dient als Transport-Container für TodoItemStacks durch den Drag&Drop-Vorgang.
 * @author Tobias Schaber
 */
public class TodoItemStackTransferable implements Transferable {
	
	// Eigenen Flavor-Definieren
	public static final DataFlavor TIS_FLAVOR = new DataFlavor(TodoItemStack.class, "TodoItemStack");
	
	// Hält den TodoItemStack, der übermittelt wird
	private final TodoItemStack tisToTransfer;
	
	/**
	 * Constructor
	 * Dieses Transferable wird erst bei einem Drag&Drop Vorgang erzeugt
	 * @param tisToTransfer
	 */
	public TodoItemStackTransferable(TodoItemStack tisToTransfer) {
		Logger.getInstance().log("Nehme Drag Objekt an : " + tisToTransfer.getName(), Logger.LOGLEVEL_INFO);

		this.tisToTransfer = tisToTransfer;
		
	}
	
	@Override
	public TodoItemStack getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		/* nur unseren TIS_FLAVOR akzeptieren */
		if (!flavor.equals(TIS_FLAVOR)) {
            throw new UnsupportedFlavorException(flavor);
        }
		
        return tisToTransfer;
        
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		/* Unser Container kann nur TIS_FLAVORS übertragen */
		return new DataFlavor[]{TIS_FLAVOR};
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {

		return flavor.equals(TIS_FLAVOR);
	}
	
	

}
