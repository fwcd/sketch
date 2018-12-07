package fwcd.sketch.view.canvas;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.Renderable;
import fwcd.sketch.model.items.BoardItem;
import fwcd.sketch.model.items.SketchItem;

public class MultiItemSelection implements Renderable, Iterable<BoardItem> {
	private final Map<BoardItem, ItemSelection> items = new LinkedHashMap<>();
	private final SketchBoardView board;
	
	public MultiItemSelection(SketchBoardView board) {
		this.board = board;
	}
	
	public void add(BoardItem item) {
		items.put(item, new ItemSelection(item, board));
	}
	
	public void remove(BoardItem item) {
		items.remove(item);
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
	
	public void apply(UnaryOperator<SketchItem> mapper) {
		for (BoardItem item : new ArrayList<>(items.keySet())) {
			item.apply(mapper);
		}
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		for (ItemSelection selection : items.values()) {
			selection.render(g2d, canvasSize);
		}
	}

	@Override
	public Iterator<BoardItem> iterator() {
		return items.keySet().iterator();
	}

	public BoardItem firstItem() {
		return items.keySet().iterator().next();
	}
}
