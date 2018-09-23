package com.fwcd.sketch.model.event;

import java.util.List;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.sketch.model.items.BoardItem;

public class BoardItemEventBus {
	private final EventListenerList<BoardItem> addListeners = new EventListenerList<>();
	private final EventListenerList<List<BoardItem>> modifyListeners = new EventListenerList<>();
	private final EventListenerList<List<BoardItem>> updateListeners = new EventListenerList<>();
	
	/** Fires whenever an item is added. */
	public EventListenerList<BoardItem> getAddListeners() { return addListeners; }
	
	/**
	 * Fires whenever an item is removed/any other modification to the list is
	 * made that is NOT covered by other listener lists.
	 */
	public EventListenerList<List<BoardItem>> getModifyListeners() { return modifyListeners; }
	
	/**
	 * Fires whenever ANY modification to the list occurs (thus when other
	 * listener lists are fired too).
	 */
	public EventListenerList<List<BoardItem>> getUpdateListeners() { return updateListeners; }
}
