package com.fwcd.sketch.model;

import java.util.Optional;

import com.fwcd.fructose.geometry.Matrix;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.tools.EditingTool;

public abstract class BasicSketchItem implements SketchItem {
	private static final long serialVersionUID = 97843987534L;
	
	@Override
	public SketchItem resizedBy(Vector2D delta) {
		Rectangle2D hb = getHitBox().getBoundingBox();
		double scaleX = (hb.width() + delta.getX()) / hb.width();
		double scaleY = (hb.height() + delta.getY()) / hb.height();
		
		return movedBy(hb.getTopLeft().invert())
				.transformedBy(new Matrix(new double[][] {
					{scaleX, 0},
					{0, scaleY}
				}))
				.movedBy(hb.getTopLeft());
	}

	@Override
	public Optional<EditingTool<?>> getEditingTool() {
		return Optional.empty();
	}
}
