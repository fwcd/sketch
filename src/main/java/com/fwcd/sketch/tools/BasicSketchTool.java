package com.fwcd.sketch.tools;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Optional;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.canvas.SketchBoard;

/**
 * Template implementation of a sketch tool containing
 * mostly empty implementations.
 * 
 * @author Fredrik
 *
 */
public abstract class BasicSketchTool implements SketchTool {
	@Override
	public void onMouseDown(Vector2D pos, SketchBoard board) {
		
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoard board) {
		
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoard board) {
		
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoard drawBoard) {
		
	}

	@Override
	public void onMouseClick(Vector2D pos, SketchBoard board) {
		
	}

	@Override
	public void onKeyPress(KeyEvent e, SketchBoard board) {
		
	}

	@Override
	public Cursor getCursor() {
		return Cursor.getDefaultCursor();
	}

	@Override
	public Optional<SketchTool> getEditTool() {
		return Optional.empty();
	}

	@Override
	public void onMouseDoubleClick(Vector2D pos, SketchBoard board) {
		
	}
}
