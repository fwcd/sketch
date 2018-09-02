package com.fwcd.sketch.tools;

public enum EnumSketchTool {
	MOVE(new MoveTool()),
	BRUSH(new Brush()),
	ERASER(new Eraser()),
	LINE(new LineTool()),
	RECT(new RectTool()),
	TEXT(new TextTool());
	
	private final SketchTool tool;
	
	private EnumSketchTool(SketchTool tool) {
		this.tool = tool;
	}
	
	public SketchTool get() {
		return tool;
	}
}
