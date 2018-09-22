package com.fwcd.sketch.view.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.DashedStroke;
import com.fwcd.fructose.swing.Renderable;
import com.fwcd.fructose.swing.SwingGraphics;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItem;

public class ItemSelection implements Renderable, AutoCloseable {
	private final SketchBoardView board;
	private final List<ResizeHandle> resizeHandles = new ArrayList<>();
	private final List<Consumer<SketchItem>> activeListeners = new ArrayList<>();
	private final BoardItem item;
	
	private Vector2D lastPos = null;
	private ResizeHandle activeHandle = null;
	
	public ItemSelection(BoardItem item, SketchBoardView board) {
		this.board = board;
		this.item = item;
		
		for (ResizeHandle.Corner corner : ResizeHandle.Corner.values()) {
			resizeHandles.add(new ResizeHandle(item.get().getHitBox().getBoundingBox(), corner));
		}
		
		setupListeners();
	}
	
	public BoardItem getItem() {
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
		
		item.get().getHitBox().getBoundingBox().draw(new SwingGraphics(g2d));
		
		for (ResizeHandle handle : resizeHandles) {
			handle.render(g2d, canvasSize);
		}
	}
	
	private void setupListeners() {
		for (ResizeHandle handle : resizeHandles) {
			Consumer<SketchItem> listener = it -> handle.update(it.getHitBox().getBoundingBox());
			item.listen(listener);
			activeListeners.add(listener);
		}
	}
	
	private void removeListeners() {
		for (Consumer<SketchItem> listener : activeListeners) {
			item.unlisten(listener);
		}
		activeListeners.clear();
	}
	
	@Override
	public void close() {
		removeListeners();
	}
}
