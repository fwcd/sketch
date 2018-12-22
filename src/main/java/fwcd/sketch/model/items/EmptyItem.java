package fwcd.sketch.model.items;

import java.util.Collection;
import java.util.Collections;

import fwcd.fructose.geometry.DoubleMatrix;
import fwcd.fructose.geometry.Polygon2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.sketch.model.SketchItemPart;
import fwcd.sketch.model.items.SketchItem;
import fwcd.sketch.model.items.SketchItemVisitor;

public class EmptyItem implements SketchItem {
	private static final long serialVersionUID = -52002363538160948L;
	public static final EmptyItem INSTANCE = new EmptyItem();
	private final Polygon2D hitBox = new Polygon2D(Vector2D.ZERO);
	
	private EmptyItem() {}
	
	@Override
	public void accept(SketchItemVisitor visitor) {}
	
	@Override
	public Polygon2D getHitBox() { return hitBox; }
	
	@Override
	public Vector2D getPos() { return Vector2D.ZERO; }
	
	@Override
	public SketchItem movedBy(Vector2D delta) { return this; }
	
	@Override
	public SketchItem transformedBy(DoubleMatrix transform) { return this; }
	
	@Override
	public boolean canBeDisposed() { return false; }
	
	@Override
	public Collection<SketchItemPart> decompose() { return Collections.emptyList(); }
	
	@Override
	public SketchItem resizedBy(Vector2D delta) { return this; }
}
