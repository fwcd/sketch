package com.fwcd.sketch.view.tools;

public enum CommonSketchTool {
	MOVE(new MoveTool()),
	BRUSH(new Brush()),
	ERASER(new Eraser()),
	LINE(new LineTool()),
	RECT(new RectTool()),
	TEXT(new TextTool());
	
	private final SketchTool tool;
	
	private CommonSketchTool(SketchTool tool) {
		this.tool = tool;
	}
	
	public SketchTool get() {
		return tool;
	}
}
