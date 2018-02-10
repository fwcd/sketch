package com.fwcd.sketch.canvas;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.UnaryOperator;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.sketch.model.SketchItem;

public class MultiItemSelection implements Rendereable, Iterable<SketchItem> {
	private final Map<SketchItem, ItemSelection> items = new HashMap<>();
	private final SketchBoard board;
	
	public MultiItemSelection(SketchBoard board) {
		this.board = board;
	}
	
	public void add(SketchItem item) {
		items.put(item, new ItemSelection(item, board));
	}
	
	public void remove(SketchItem item) {
		items.remove(item);
	}
	
	private void replace(SketchItem item, SketchItem replacement) {
		remove(item);
		add(replacement);
	}
	
	public void clear() {
		items.clear();
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	public int amount() {
		return items.size();
	}
	
	public boolean onMouseDown(Vector2D pos) {
		for (ItemSelection item : items.values()) {
			if (item.onMouseDown(pos)) {
				return true;
			}
		}
		return false;
	}
	
	public void onMouseDrag(Vector2D pos) {
		items.values().forEach(item -> item.onMouseDrag(pos));
	}
	
	public void onMouseUp(Vector2D pos) {
		items.values().forEach(item -> item.onMouseUp(pos));
	}
	
	public void modify(UnaryOperator<SketchItem> modifier) {
		for (SketchItem item : new ArrayList<>(items.keySet())) {
			SketchItem modified = modifier.apply(item);
			
			board.modify(item, modified);
			replace(item, modified);
		}
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		for (ItemSelection selection : items.values()) {
			selection.render(g2d, canvasSize);
		}
	}

	@Override
	public Iterator<SketchItem> iterator() {
		return items.keySet().iterator();
	}

	public SketchItem firstItem() {
		return items.keySet().iterator().next();
	}
}
