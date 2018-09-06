package com.fwcd.sketch.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.SwingGraphics;

public class ColoredLine implements SketchItem {
	private static final long serialVersionUID = 48975483798754L;
	private final LineSeg2D line;
	private final Color color;
	private final float brushThickness;
	
	private transient Rectangle2D boundingBox;
	
	public ColoredLine(LineSeg2D line, Color color, float brushThickness) {
		this.line = line;
		this.color = color;
		this.brushThickness = brushThickness;
	}
	
	public ColoredLine(LineSeg2D line, BrushProperties props) {
		this(line, props.getColor(), props.getThicknessProperty().getValue());
	}
	
	private Rectangle2D getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = new Rectangle2D(line.getStart(), line.getEnd());
		}
		
		return boundingBox;
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(brushThickness));
		line.draw(new SwingGraphics(g2d));
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
		return new ColoredLine(line.movedBy(delta), color, brushThickness);
	}

	@Override
	public ColoredLine transformedBy(DoubleMatrix transform) {
		return new ColoredLine(
				line.transformedBy(transform),
				color,
				brushThickness
		);
	}

	@Override
	public ColoredLine resizedBy(Vector2D delta) {
		return new ColoredLine(
				new LineSeg2D(line.getStart(), line.getEnd().add(delta)),
				color,
				brushThickness
		);
	}
}
