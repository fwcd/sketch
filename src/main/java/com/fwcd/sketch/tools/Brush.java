package com.fwcd.sketch.tools;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.ColoredPath;

public class Brush extends DrawTool<ColoredPath> {
	private static final ImageIcon ICON = new ResourceImage("/resources/brushIcon.png").getAsIcon();
	private int precision = 0;
	
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}

	@Override
	protected ColoredPath getSketchItem(Vector2D startPos, BrushProperties props) {
		return new ColoredPath(props);
	}

	@Override
	protected ColoredPath updateItem(ColoredPath item, Vector2D start, Vector2D last, Vector2D pos) {
		if (pos.sub(last).length() > precision) {
			return item.withLine(new LineSeg2D(last, pos));
		} else {
			return item;
		}
	}
}
