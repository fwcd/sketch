package com.fwcd.sketch.view.tools;

import javax.swing.ImageIcon;

import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.sketch.model.items.SketchItem;

public abstract class EditingTool<T extends SketchItem> implements SketchTool {
	public abstract void edit(T item);
	
	public abstract T get(SketchBoardView board);
	
	@SuppressWarnings("unchecked")
	public void tryEditing(SketchItem item) {
		try {
			edit((T) item);
		} catch (ClassCastException e) {
			// Do nothing
		}
	}
	
	@Override
	public ImageIcon getIcon() {
		throw new UnsupportedOperationException();
	}
}
