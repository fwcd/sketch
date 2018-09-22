package com.fwcd.sketch.model;

import com.fwcd.sketch.model.items.SketchItem;

public class SketchItemPart {
	private final SketchItem item;
	private final Runnable remover;
	
	public SketchItemPart(SketchItem item, Runnable remover) {
		this.item = item;
		this.remover = remover;
	}
	
	public SketchItem getItem() { return item; }
	
	public void removeFromParent() { remover.run(); }
}
