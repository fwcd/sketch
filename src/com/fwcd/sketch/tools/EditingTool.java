package com.fwcd.sketch.tools;

import javax.swing.ImageIcon;

import com.fwcd.sketch.canvas.SketchBoard;
import com.fwcd.sketch.model.SketchItem;

public abstract class EditingTool<T extends SketchItem> extends BasicSketchTool {
	public abstract void edit(T item);
	
	public abstract T get(SketchBoard board);
	
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
