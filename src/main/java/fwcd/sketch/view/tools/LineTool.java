package fwcd.sketch.view.tools;

import javax.swing.ImageIcon;

import fwcd.fructose.geometry.LineSeg2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.ResourceImage;
import fwcd.sketch.model.BrushProperties;
import fwcd.sketch.model.items.ColoredLine;

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
