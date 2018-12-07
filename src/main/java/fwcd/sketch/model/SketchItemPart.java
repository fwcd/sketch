package fwcd.sketch.model;

import fwcd.sketch.model.items.SketchItem;

/**
 * An immutable part of a {@link SketchItem}.
 */
public class SketchItemPart {
	private final SketchItem item;
	private final Runnable remover;
	
	public SketchItemPart(SketchItem item, Runnable remover) {
		this.item = item;
		this.remover = remover;
	}
	
	public SketchItem getItem() { return item; }
	
	public void removeFromParent() { remover.run(); }
	
	public SketchItemPart withRemoveListener(Runnable removeListener) {
		return new SketchItemPart(item, () -> {
			remover.run();
			removeListener.run();
		});
	}
}
