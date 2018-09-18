package com.fwcd.sketch.view.tools;

import javax.swing.ImageIcon;

import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.items.SketchItem;

public abstract class EditingTool<T extends SketchItem> implements SketchTool {
	public abstract void edit(T item);
	
	public abstract T getItem(SketchBoardView board);
	
	@SuppressWarnings("unchecked")
	public void tryEditing(SketchItem item) {
		edit((T) item);
	}
	
	@Override
	public final ImageIcon getIcon() {
		throw new UnsupportedOperationException("Icons are not (yet) supported by EditingTool");
	}
	
	@Override
	public final void onMouseDoubleClick(Vector2D pos, SketchBoardView board) {
		throw new UnsupportedOperationException("Double click is not (yet) supported by EditingTool");
	}
}
