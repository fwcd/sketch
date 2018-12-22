package fwcd.sketch.model.event;

import java.util.List;

import fwcd.fructose.EventListenerList;
import fwcd.sketch.model.SketchBoardModel;
import fwcd.sketch.model.items.BoardItemStack;
import fwcd.sketch.view.utils.Indexed;

/**
 * An aggregate of multiple listener lists that publish
 * events when the items on a {@link SketchBoardModel} change.
 */
public class BoardItemEventBus {
	private final EventListenerList<BoardItemStack> addListeners = new EventListenerList<>();
	private final EventListenerList<Indexed<BoardItemStack>> itemChangeListeners = new EventListenerList<>();
	private final EventListenerList<List<BoardItemStack>> modifyListeners = new EventListenerList<>();
	private final EventListenerList<List<BoardItemStack>> changeListeners = new EventListenerList<>();
	
	/** Fires whenever an item is added. */
	public EventListenerList<BoardItemStack> getAddListeners() { return addListeners; }
	
	/** Fires whenever a single board item is swapped. */
	public EventListenerList<Indexed<BoardItemStack>> getItemChangeListeners() { return itemChangeListeners; }
	
	/**
	 * Fires whenever an item is removed/any other modification to the list is
	 * made that is NOT covered by other listener lists.
	 */
	public EventListenerList<List<BoardItemStack>> getModifyListeners() { return modifyListeners; }
	
	/**
	 * Fires whenever ANY modification to the list of items occurs (thus when other
	 * listener lists are fired too).
	 */
	public EventListenerList<List<BoardItemStack>> getChangeListeners() { return changeListeners; }
}
