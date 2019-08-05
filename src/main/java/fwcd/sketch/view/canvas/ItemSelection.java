package fwcd.sketch.view.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import fwcd.fructose.geometry.Rectangle2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.DashedStroke;
import fwcd.fructose.swing.Renderable;
import fwcd.fructose.swing.SwingGraphics;
import fwcd.fructose.util.StreamUtils;
import fwcd.sketch.model.items.BoardItemStack;
import fwcd.sketch.model.items.SketchItem;

public class ItemSelection implements Renderable, AutoCloseable {
	private final SketchBoardView board;
	private final List<ResizeHandle> resizeHandles = new ArrayList<>();
	private final List<Consumer<Iterable<SketchItem>>> activeListeners = new ArrayList<>();
	private final BoardItemStack item;
	
	private Vector2D lastPos = null;
	private ResizeHandle activeHandle = null;
	
	public ItemSelection(BoardItemStack item, SketchBoardView board) {
		this.board = board;
		this.item = item;
		
		for (ResizeHandle.Corner corner : ResizeHandle.Corner.values()) {
			resizeHandles.add(new ResizeHandle(item.getBoundingBox(), corner));
		}
		
		setupListeners();
	}
	
	public BoardItemStack getItem() {
		return item;
	}
	
	private void move(Vector2D delta) {
		item.apply(it -> it.movedBy(delta));
		board.repaint();
	}
	
	public boolean onMouseDown(Vector2D pos) {
		lastPos = pos;
		
		for (ResizeHandle handle : new ArrayList<>(resizeHandles)) {
			if (handle.contains(pos) && lastPos != null) {
				activeHandle = handle;
				return true;
			}
		}
		
		return false;
	}
	
	public void onMouseDrag(Vector2D pos) {
		if (lastPos != null) {
			if (activeHandle == null) {
				move(pos.sub(lastPos));
			} else {
				item.apply(it -> activeHandle.resize(it, lastPos, pos));
			}
		}
		
		lastPos = pos;
	}
	
	public void onMouseUp(Vector2D pos) {
		lastPos = null;
		activeHandle = null;
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setStroke(new DashedStroke(1, 5));
		g2d.setColor(Color.GRAY);
		
		item.getBoundingBox().draw(new SwingGraphics(g2d));
		
		for (ResizeHandle handle : resizeHandles) {
			handle.render(g2d, canvasSize);
		}
	}
	
	private void setupListeners() {
		for (ResizeHandle handle : resizeHandles) {
			Consumer<Iterable<SketchItem>> listener = it -> handle.update(StreamUtils.stream(it)
				.map(v -> v.getHitBox().getBoundingBox())
				.reduce(Rectangle2D::merge)
				.orElseGet(() -> new Rectangle2D(0, 0, 0, 0)));
			item.listen(listener);
			activeListeners.add(listener);
		}
	}
	
	private void removeListeners() {
		for (Consumer<Iterable<SketchItem>> listener : activeListeners) {
			item.unlisten(listener);
		}
		activeListeners.clear();
	}
	
	@Override
	public void close() {
		removeListeners();
	}
}
