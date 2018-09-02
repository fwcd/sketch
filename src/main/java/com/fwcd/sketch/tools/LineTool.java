package com.fwcd.sketch.tools;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.ColoredLine;

public class LineTool extends DrawTool<ColoredLine> {
	private static final ImageIcon ICON = new ResourceImage("/lineToolIcon.png").getAsIcon();

	@Override
	public ImageIcon getIcon() {
		return ICON;
	}

	@Override
	protected ColoredLine getSketchItem(Vector2D startPos, BrushProperties props) {
		return new ColoredLine(new LineSeg2D(startPos, startPos), props);
	}

	@Override
	protected ColoredLine updateItem(ColoredLine item, Vector2D start, Vector2D last, Vector2D pos) {
		return new ColoredLine(new LineSeg2D(start, pos), getBrushProperties());
	}
}
