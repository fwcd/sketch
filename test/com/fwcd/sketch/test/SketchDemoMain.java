package com.fwcd.sketch.test;

import com.fwcd.fructose.swing.PanelFrame;
import com.fwcd.sketch.canvas.SketchPane;

public class SketchDemoMain {
	public static void main(String[] args) {
		new PanelFrame("Sketch Demo", 640, 480, new SketchPane());
	}
}
