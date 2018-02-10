package com.fwcd.sketch.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.canvas.SketchBoard;

public class TextTool extends BasicSketchTool {
	private static final ImageIcon ICON = new ResourceImage("/resources/textIcon.png").getAsIcon();
	
	private final TextEditingTool editingTool = new TextEditingTool();
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoard drawBoard) {
		editingTool.onMouseDown(pos, drawBoard);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoard drawBoard) {
		editingTool.onMouseDrag(pos, drawBoard);
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoard drawBoard) {
		editingTool.onMouseUp(pos, drawBoard);
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoard drawBoard) {
		editingTool.render(g2d, canvasSize, drawBoard);
	}
	
	@Override
	public void onKeyPress(KeyEvent e, SketchBoard sketchBoard) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			sketchBoard.add(editingTool.get(sketchBoard));
			editingTool.clear();
		} else {
			editingTool.onKeyPress(e, sketchBoard);
		}
		
		sketchBoard.repaint();
	}
}
