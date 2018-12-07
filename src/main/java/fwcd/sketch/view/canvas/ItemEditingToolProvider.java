package fwcd.sketch.view.canvas;

import java.util.function.Consumer;

import fwcd.sketch.model.items.ColoredText;
import fwcd.sketch.model.items.SketchItemVisitor;
import fwcd.sketch.view.tools.EditingTool;
import fwcd.sketch.view.tools.TextEditingTool;

public class ItemEditingToolProvider implements SketchItemVisitor {
	private final Consumer<EditingTool<?>> toolConsumer;
	
	public ItemEditingToolProvider(Consumer<EditingTool<?>> toolConsumer) {
		this.toolConsumer = toolConsumer;
	}
	
	@Override
	public void visitText(ColoredText text) {
		toolConsumer.accept(new TextEditingTool());
	}
}
