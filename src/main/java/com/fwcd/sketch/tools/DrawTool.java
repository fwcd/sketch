package com.fwcd.sketch.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.canvas.SketchBoard;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.SketchItem;

public abstract class DrawTool<T extends SketchItem> extends BasicSketchTool {
	private T item = null;
	private BrushProperties props;
	private Vector2D start = null;
	private Vector2D current = null;
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoard drawBoard) {
		start = pos;
		current = pos;
		props = drawBoard.getBrushProperties();
		item = getSketchItem(pos, props);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoard drawBoard) {
		T newItem = updateItem(item, start, current, pos);
		
		if (!newItem.equals(item)) {
			item = newItem;
			current = pos;
		}
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoard drawBoard) {
		drawBoard.add(item);
		
		start = null;
		current = null;
		item = null;
	}
	
	protected BrushProperties getBrushProperties() {
		return props;
	}
	
	protected abstract T getSketchItem(Vector2D startPos, BrushProperties props);
	
	protected abstract T updateItem(T item, Vector2D start, Vector2D last, Vector2D pos);

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoard drawBoard) {
		if (item != null) {
			item.render(g2d, canvasSize);
		}
	}
}
