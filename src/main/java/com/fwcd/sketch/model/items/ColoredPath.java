package com.fwcd.sketch.model.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;

public class ColoredPath implements ColoredSketchItem {
	private static final long serialVersionUID = 98734979343455L;
	private final List<LineSeg2D> lines;
	private final Color color;
	private final float thickness;
	private Rectangle2D hitBox;

	public ColoredPath(List<LineSeg2D> lines, Color color, float thickness) {
		this.lines = lines;
		this.color = color;
		this.thickness = thickness;
	}
	
	public ColoredPath(List<LineSeg2D> lines, BrushProperties props) {
		this(lines, props.getColor(), props.getThicknessProperty().getValue());
	}
	
	public ColoredPath(BrushProperties props) {
		this(Collections.emptyList(), props);
	}

	public ColoredPath withLine(LineSeg2D line) {
		List<LineSeg2D> newLines = new ArrayList<>(lines);
		newLines.add(line);
		return new ColoredPath(newLines, color, thickness);
	}
	
	public float getThickness() {
		return thickness;
	}
	
	public List<LineSeg2D> getLines() {
		return lines;
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitPath(this);
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Vector2D getPos() {
		return lines.get(lines.size() / 2).getCenter();
	}

	@Override
	public Rectangle2D getHitBox() {
		if (hitBox == null) {
			Vector2D topLeft = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
			Vector2D bottomRight = new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
			
			for (LineSeg2D line : lines) {
				topLeft = topLeft.min(line.getStart());
				topLeft = topLeft.min(line.getEnd());
				
				bottomRight = bottomRight.max(line.getStart());
				bottomRight = bottomRight.max(line.getEnd());
			}
			
			hitBox = new Rectangle2D(topLeft, bottomRight);
		}
		
		return hitBox;
	}

	@Override
	public Collection<SketchItem> decompose() {
		return lines.stream()
				.map(line -> new ColoredLine(line, color, thickness))
				.collect(Collectors.toList());
	}

	@Override
	public ColoredPath movedBy(Vector2D delta) {
		return new ColoredPath(
				lines.stream()
						.map(line -> line.movedBy(delta))
						.collect(Collectors.toList()),
				color,
				thickness
		);
	}

	@Override
	public ColoredPath transformedBy(DoubleMatrix transform) {
		return new ColoredPath(
				lines.stream()
						.map(line -> line.transformedBy(transform))
						.collect(Collectors.toList()),
				color,
				thickness
		);
	}
}
