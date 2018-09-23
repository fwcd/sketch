package com.fwcd.sketch.model.items;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import com.fwcd.fructose.Observable;

/**
 * A swappable item on the sketch board.
 */
public class BoardItem {
	private Observable<SketchItem> item;
	
	public BoardItem(SketchItem item) {
		this.item = new Observable<>(item);
	}
	
	public void accept(SketchItemVisitor visitor) { item.get().accept(visitor); }
	
	public SketchItem get() { return item.get(); }
	
	public void set(SketchItem newItem) { item.set(newItem); }
	
	public void listen(Consumer<SketchItem> consumer) { item.listen(consumer); }
	
	public void unlisten(Consumer<SketchItem> consumer) { item.unlisten(consumer); }
	
	public void apply(UnaryOperator<SketchItem> mapper) { item.set(mapper.apply(item.get())); }
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof BoardItem)) return false;
		return ((BoardItem) obj).item.get().equals(item.get());
	}
	
	@Override
	public int hashCode() {
		return item.get().hashCode();
	}
}
