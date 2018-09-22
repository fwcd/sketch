package com.fwcd.sketch.model;

import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItem;

public class SketchItemPart {
	private final BoardItem parent;
	private final SketchItem item;
	
	public SketchItemPart(BoardItem parent, SketchItem item) {
		this.parent = parent;
		this.item = item;
	}
	
	public SketchItem getItem() { return item; }
	
	public BoardItem getParent() { return parent; }
}
