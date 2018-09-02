package com.fwcd.sketch.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.SwingGraphics;

public class ColoredRect extends BasicSketchItem {
	private static final long serialVersionUID = 48975483798754L;
	private final Rectangle2D rect;
	private final Color color;
	private final float brushThickness;
	
	public ColoredRect(Rectangle2D rect, Color color, float brushThickness) {
		this.rect = rect;
		this.color = color;
		this.brushThickness = brushThickness;
	}
	
	public ColoredRect(Rectangle2D rect, BrushProperties props) {
		this(rect, props.getColor(), props.getThicknessProperty().getValue());
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(brushThickness));
		rect.draw(new SwingGraphics(g2d));
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
		return new ColoredRect(rect.movedBy(delta), color, brushThickness);
	}

	@Override
	public ColoredRect transformedBy(DoubleMatrix transform) {
		return new ColoredRect(rect.transformedBy(transform), color, brushThickness);
	}

	@Override
	public ColoredRect resizedBy(Vector2D delta) {
		return new ColoredRect(rect.resizedBy(delta), color, brushThickness);
	}
}
