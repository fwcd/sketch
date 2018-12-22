package fwcd.sketch.view.tools;

import java.util.function.Consumer;

import javax.swing.ImageIcon;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.geometry.LineSeg2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.ResourceImage;
import fwcd.sketch.model.BrushProperties;
import fwcd.sketch.model.items.ColoredLine;
import fwcd.sketch.model.items.ColoredPath;
import fwcd.sketch.model.items.SketchItem;

public class Brush extends DrawTool<ColoredPath> {
	private static final ImageIcon ICON = new ResourceImage("/brushIcon.png").getAsIcon();
	private final EventListenerList<SketchItem> partListeners = new EventListenerList<>();
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
			LineSeg2D part = new LineSeg2D(last, pos);
			if (partListeners.size() > 0) {
				partListeners.fire(new ColoredLine(part, item.getColor(), item.getThickness()));
			}
			return item.withLine(part);
		} else {
			return item;
		}
	}
	
	@Override
	public void addAddedPartListener(Consumer<? super SketchItem> listener) {
		partListeners.add(listener);
	}
	
	@Override
	public void removeAddedPartListener(Consumer<? super SketchItem> listener) {
		partListeners.remove(listener);
	}
}
