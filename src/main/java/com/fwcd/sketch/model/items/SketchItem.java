package com.fwcd.sketch.model.items;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.SketchItemPart;

/**
 * An immutable, drawable item.
 */
public interface SketchItem extends Serializable {
	Polygon2D getHitBox();
	
	Vector2D getPos();
	
	SketchItem movedBy(Vector2D delta);
	
	SketchItem transformedBy(DoubleMatrix transform);
	
	void accept(SketchItemVisitor visitor);
	
	default Collection<SketchItemPart> decompose() { return Collections.emptySet(); }
	
	default boolean canBeDisposed() { return false; }
	
	default SketchItem resizedBy(Vector2D delta) {
		Rectangle2D hb = getHitBox().getBoundingBox();
		double scaleX = (hb.width() + delta.getX()) / hb.width();
		double scaleY = (hb.height() + delta.getY()) / hb.height();
		
		return movedBy(hb.getTopLeft().invert())
			.transformedBy(new DoubleMatrix(new double[][] {
				{scaleX, 0},
				{0, scaleY}
			}))
			.movedBy(hb.getTopLeft());
	}
}
