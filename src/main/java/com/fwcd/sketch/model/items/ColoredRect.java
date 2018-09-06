package com.fwcd.sketch.model.items;

import java.awt.Color;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;

public class ColoredRect implements ColoredSketchItem {
	private static final long serialVersionUID = 48975483798754L;
	private final Rectangle2D rect;
	private final Color color;
	private final float thickness;
	
	public ColoredRect(Rectangle2D rect, Color color, float thickness) {
		this.rect = rect;
		this.color = color;
		this.thickness = thickness;
	}
	
	public ColoredRect(Rectangle2D rect, BrushProperties props) {
		this(rect, props.getColor(), props.getThicknessProperty().getValue());
	}

	public Rectangle2D getRect() {
		return rect;
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitRect(this);
	}
	
	public float getThickness() {
		return thickness;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Vector2D getPos() {
		return rect.getCenter();
	}

	@Override
	public Polygon2D getHitBox() {
		return rect;
	}

	@Override
	public ColoredRect movedBy(Vector2D delta) {
		return new ColoredRect(rect.movedBy(delta), color, thickness);
	}

	@Override
	public ColoredRect transformedBy(DoubleMatrix transform) {
		return new ColoredRect(rect.transformedBy(transform), color, thickness);
	}

	@Override
	public ColoredRect resizedBy(Vector2D delta) {
		return new ColoredRect(rect.resizedBy(delta), color, thickness);
	}
}
