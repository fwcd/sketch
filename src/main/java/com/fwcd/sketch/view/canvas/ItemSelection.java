package com.fwcd.sketch.view.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.DashedStroke;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.SwingGraphics;
import com.fwcd.sketch.model.SketchItem;

public class ItemSelection implements Rendereable {
	private final SketchBoardView board;
	private final List<ResizeHandle> resizeHandles = new ArrayList<>();

	private SketchItem item;
	private Rectangle2D boundingBox;
	
	private Vector2D lastPos = null;
	private ResizeHandle activeHandle = null;
	
	public ItemSelection(SketchItem item, SketchBoardView board) {
		this.board = board;
		setItem(item);
		
		for (ResizeHandle.Corner corner : ResizeHandle.Corner.values()) {
			resizeHandles.add(new ResizeHandle(boundingBox, corner));
		}
	}
	
	public SketchItem getItem() {
		return item;
	}
	
	private void setItem(SketchItem item) {
		board.getModel().replaceItem(this.item, item);
		this.item = item;
		boundingBox = item.getHitBox().getBoundingBox();
		
		for (ResizeHandle handle : resizeHandles) {
			handle.update(boundingBox);
		}
	}
	
	private void move(Vector2D delta) {
		setItem(item.movedBy(delta));
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
				setItem(activeHandle.resize(item, lastPos, pos));
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
		g2d.setStroke(new DashedStroke(2, 10));
		g2d.setColor(Color.LIGHT_GRAY);
		
		boundingBox.draw(new SwingGraphics(g2d));
		
		for (ResizeHandle handle : resizeHandles) {
			handle.render(g2d, canvasSize);
		}
	}
}
