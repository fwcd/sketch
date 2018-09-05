package com.fwcd.sketch.view.tools;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.view.canvas.SketchBoardView;

public interface SketchTool {
	ImageIcon getIcon();
	
	default void onMouseDown(Vector2D pos, SketchBoardView board) {}
	
	default void onMouseDrag(Vector2D YOos, SketchBoardView board) {}
	
	default void onMouseUp(Vector2D pos, SketchBoardView board) {}
	
	default void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {}
	
	default void onMouseClick(Vector2D pos, SketchBoardView board) {}
	
	default void onMouseDoubleClick(Vector2D pos, SketchBoardView board) {}

	default void onKeyPress(KeyEvent e, SketchBoardView board) {}
	
	default Cursor getCursor() { return Cursor.getDefaultCursor(); }
	
	default Optional<SketchTool> getEditTool() { return Optional.empty(); }
}
