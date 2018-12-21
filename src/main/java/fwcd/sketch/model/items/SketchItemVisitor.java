package fwcd.sketch.model.items;

public interface SketchItemVisitor {
	default void visitOtherItem(SketchItem item) {}
	
	default void visitLine(ColoredLine line) { visitOtherItem(line); }
	
	default void visitPath(ColoredPath path) { visitOtherItem(path); }
	
	default void visitRect(ColoredRect rect) { visitOtherItem(rect); }
	
	default void visitText(ColoredText text) { visitOtherItem(text); }
	
	default void visitImage(ImageItem image) { visitOtherItem(image); }
	
	default void visitComposite(CompositeItem item) { visitOtherItem(item); }
}
