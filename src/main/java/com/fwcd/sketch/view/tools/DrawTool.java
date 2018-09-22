package com.fwcd.sketch.view.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItem;
import com.fwcd.sketch.view.canvas.ItemRenderer;
import com.fwcd.sketch.view.canvas.SketchBoardView;

public abstract class DrawTool<T extends SketchItem> implements SketchTool {
	private T item = null;
	private BrushProperties props;
	private Vector2D start = null;
	private Vector2D current = null;
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView drawBoard) {
		start = pos;
		current = pos;
		props = drawBoard.getBrushProperties();
		item = getSketchItem(pos, props);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView drawBoard) {
		T newItem = updateItem(item, start, current, pos);
		
		if (!newItem.equals(item)) {
			item = newItem;
			current = pos;
		}
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView drawBoard) {
		drawBoard.getModel().getItems().add(new BoardItem(item));
		
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
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (item != null) {
			item.accept(new ItemRenderer(g2d));
		}
	}
}
