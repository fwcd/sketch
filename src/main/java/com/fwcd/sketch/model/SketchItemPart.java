package com.fwcd.sketch.model;

import com.fwcd.sketch.model.items.SketchItem;

public class SketchItemPart {
	private final SketchItem parent;
	private final SketchItem item;
	
	public SketchItemPart(SketchItem parent, SketchItem item) {
		this.parent = parent;
		this.item = item;
	}
	
	public SketchItem getItem() { return item; }
	
	public SketchItem getParent() { return parent; }
}
