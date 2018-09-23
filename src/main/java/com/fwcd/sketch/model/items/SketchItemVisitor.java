package com.fwcd.sketch.model.items;

public interface SketchItemVisitor {
	default void visitLine(ColoredLine line) {}
	
	default void visitPath(ColoredPath path) {}
	
	default void visitRect(ColoredRect rect) {}
	
	default void visitText(ColoredText text) {}
	
	default void visitImage(ImageItem image) {}
}
