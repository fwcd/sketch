package fwcd.sketch.view.tools;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.ImageIcon;

import fwcd.fructose.geometry.Vector2D;
import fwcd.sketch.model.items.SketchItem;
import fwcd.sketch.view.canvas.SketchBoardView;

public interface SketchTool {
	ImageIcon getIcon();
	
	default void onMouseDown(Vector2D pos, SketchBoardView board) {}
	
	default void onMouseDrag(Vector2D pos, SketchBoardView board) {}
	
	default void onMouseUp(Vector2D pos, SketchBoardView board) {}
	
	default void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {}
	
	default void onMouseClick(Vector2D pos, SketchBoardView board) {}
	
	default void onMouseDoubleClick(Vector2D pos, SketchBoardView board) {}

	default void onKeyPress(KeyEvent e, SketchBoardView board) {}
	
	default Cursor getCursor() { return Cursor.getDefaultCursor(); }
	
	default void addAddedPartListener(Consumer<? super SketchItem> listener) {}
	
	default void removeAddedPartListener(Consumer<? super SketchItem> listener) {}
}
