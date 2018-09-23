package com.fwcd.sketch.model.event;

import java.util.List;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.sketch.model.SketchBoardModel;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.view.utils.Indexed;

/**
 * An aggregate of multiple listener lists that publish
 * events when the items on a {@link SketchBoardModel} change.
 */
public class BoardItemEventBus {
	private final EventListenerList<BoardItem> addListeners = new EventListenerList<>();
	private final EventListenerList<Indexed<BoardItem>> itemChangeListeners = new EventListenerList<>();
	private final EventListenerList<List<BoardItem>> modifyListeners = new EventListenerList<>();
	private final EventListenerList<List<BoardItem>> changeListeners = new EventListenerList<>();
	
	/** Fires whenever an item is added. */
	public EventListenerList<BoardItem> getAddListeners() { return addListeners; }
	
	/** Fires whenever a single board item is swapped. */
	public EventListenerList<Indexed<BoardItem>> getItemChangeListeners() { return itemChangeListeners; }
	
	/**
	 * Fires whenever an item is removed/any other modification to the list is
	 * made that is NOT covered by other listener lists.
	 */
	public EventListenerList<List<BoardItem>> getModifyListeners() { return modifyListeners; }
	
	/**
	 * Fires whenever ANY modification to the list of items occurs (thus when other
	 * listener lists are fired too).
	 */
	public EventListenerList<List<BoardItem>> getChangeListeners() { return changeListeners; }
}
