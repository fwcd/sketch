package fwcd.sketch.model.items;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fwcd.fructose.geometry.DoubleMatrix;
import fwcd.fructose.geometry.Polygon2D;
import fwcd.fructose.geometry.Rectangle2D;
import fwcd.fructose.geometry.Vector2D;

/**
 * An item that is composed from others.
 */
public class CompositeItem implements SketchItem {
	private static final long serialVersionUID = -8514234977562329771L;
	private final List<SketchItem> childs;
	
	public CompositeItem(SketchItem... childs) {
		this.childs = Arrays.asList(childs);
	}
	
	public CompositeItem(List<? extends SketchItem> childs) {
		this.childs = Collections.unmodifiableList(childs);
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitComposite(this);
	}
	
	@Override
	public Polygon2D getHitBox() {
		return childs.stream()
			.map(it -> it.getHitBox().getBoundingBox())
			.reduce(Rectangle2D::merge)
			.orElseGet(() -> new Rectangle2D(0, 0, 0, 0));
	}
	
	@Override
	public Vector2D getPos() {
		return childs.stream()
			.map(it -> it.getPos())
			.reduce(Vector2D::min)
			.orElse(Vector2D.ZERO);
	}
	
	@Override
	public SketchItem movedBy(Vector2D delta) {
		return new CompositeItem(childs.stream()
			.map(it -> it.movedBy(delta))
			.collect(Collectors.toList()));
	}
	
	@Override
	public SketchItem transformedBy(DoubleMatrix transform) {
		return new CompositeItem(childs.stream()
			.map(it -> it.transformedBy(transform))
			.collect(Collectors.toList()));
	}
	
	public List<? extends SketchItem> getChilds() { return childs; }
}
