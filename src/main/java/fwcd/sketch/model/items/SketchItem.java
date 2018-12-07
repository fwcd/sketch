package fwcd.sketch.model.items;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import fwcd.fructose.geometry.DoubleMatrix;
import fwcd.fructose.geometry.Polygon2D;
import fwcd.fructose.geometry.Rectangle2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.sketch.model.SketchItemPart;

/**
 * An immutable, drawable item.
 * 
 * <p><b>Subtypes are required to implement a value-based
 * hashCode and equals.</b></p>
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
