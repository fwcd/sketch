package com.fwcd.sketch.model.items;

import java.awt.Color;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;

public class ColoredLine implements ColoredSketchItem {
	private static final long serialVersionUID = 48975483798754L;
	private final LineSeg2D line;
	private final Color color;
	private final float thickness;
	
	private transient Rectangle2D boundingBox;
	
	public ColoredLine(LineSeg2D line, Color color, float thickness) {
		this.line = line;
		this.color = color;
		this.thickness = thickness;
	}
	
	public ColoredLine(LineSeg2D line, BrushProperties props) {
		this(line, props.getColor(), props.getThicknessProperty().getValue());
	}
	
	public LineSeg2D getLine() {
		return line;
	}
	
	public float getThickness() {
		return thickness;
	}
	
	private Rectangle2D getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = new Rectangle2D(line.getStart(), line.getEnd());
		}
		
		return boundingBox;
	}

	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitLine(this);
	}

	@Override
	public Vector2D getPos() {
		return line.getCenter();
	}

	public float length() {
		return (float) line.length();
	}

	@Override
	public Polygon2D getHitBox() {
		return getBoundingBox();
	}
	
	@Override
	public String toString() {
		return line.toString();
	}

	@Override
	public ColoredLine movedBy(Vector2D delta) {
		return new ColoredLine(line.movedBy(delta), color, thickness);
	}

	@Override
	public ColoredLine transformedBy(DoubleMatrix transform) {
		return new ColoredLine(
				line.transformedBy(transform),
				color,
				thickness
		);
	}

	@Override
	public ColoredLine resizedBy(Vector2D delta) {
		return new ColoredLine(
				new LineSeg2D(line.getStart(), line.getEnd().add(delta)),
				color,
				thickness
		);
	}
}
