package com.fwcd.sketch.view.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.function.Function;

import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Square2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.SwingGraphics;
import com.fwcd.sketch.model.SketchItem;

public class ResizeHandle implements Rendereable {
	public static enum Corner {
		TOP_LEFT(Rectangle2D::getTopLeft),
		TOP_RIGHT(Rectangle2D::getTopRight),
		BOTTOM_LEFT(Rectangle2D::getBottomLeft),
		BOTTOM_RIGHT(Rectangle2D::getBottomRight);
		
		private final Function<Rectangle2D, Vector2D> vecFunc;
		
		private Corner(Function<Rectangle2D, Vector2D> vecFunc) {
			this.vecFunc = vecFunc;
		}
		
		public Vector2D getPos(Rectangle2D boundingBox) {
			return vecFunc.apply(boundingBox);
		}
	}
	
	private final int sideLength = 10;
	private final Corner corner;
	private Square2D frame;
	
	public ResizeHandle(Rectangle2D itemBoundingBox, Corner corner) {
		this.corner = corner;
		update(itemBoundingBox);
	}

	public void update(Rectangle2D itemBoundingBox) {
		moveTo(corner.getPos(itemBoundingBox));
	}
	
	public void moveTo(Vector2D pos) {
		frame = new Square2D(pos.sub(new Vector2D(sideLength / 2, sideLength / 2)), sideLength);
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(Color.DARK_GRAY);
		frame.fill(new SwingGraphics(g2d));
	}

	public boolean contains(Vector2D pos) {
		return frame.contains(pos);
	}

	public SketchItem resize(SketchItem item, Vector2D lastPos, Vector2D pos) {
		moveTo(pos);
		Vector2D diff = pos.sub(lastPos);
		
		switch (corner) {
			case BOTTOM_LEFT:
				return item
					.movedBy(diff.withoutY())
					.resizedBy(diff.invertX());
			case BOTTOM_RIGHT:
				return item
					.resizedBy(diff);
			case TOP_LEFT:
				return item
					.movedBy(diff)
					.resizedBy(diff.invert());
			case TOP_RIGHT:
				return item
					.movedBy(diff.withoutX())
					.resizedBy(diff.invertY());
			default: throw new RuntimeException("Invalid handle position!");
		}
	}
}
