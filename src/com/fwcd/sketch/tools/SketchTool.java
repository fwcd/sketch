package com.fwcd.sketch.tools;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.canvas.SketchBoard;

public interface SketchTool {
	ImageIcon getIcon();
	
	void onMouseDown(Vector2D pos, SketchBoard board);
	
	void onMouseDrag(Vector2D pos, SketchBoard board);
	
	void onMouseUp(Vector2D pos, SketchBoard board);
	
	void render(Graphics2D g2d, Dimension canvasSize, SketchBoard drawBoard);
	
	void onMouseClick(Vector2D pos, SketchBoard board);
	
	void onMouseDoubleClick(Vector2D pos, SketchBoard board);

	void onKeyPress(KeyEvent e, SketchBoard board);
	
	Cursor getCursor();
	
	Optional<SketchTool> getEditTool();
}
