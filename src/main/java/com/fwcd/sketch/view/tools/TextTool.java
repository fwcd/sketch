package com.fwcd.sketch.view.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.view.canvas.SketchBoardView;

public class TextTool implements SketchTool {
	private static final ImageIcon ICON = new ResourceImage("/textIcon.png").getAsIcon();
	
	private final TextEditingTool editingTool = new TextEditingTool();
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		editingTool.onMouseDown(pos, board);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		editingTool.onMouseDrag(pos, board);
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		editingTool.onMouseUp(pos, board);
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		editingTool.render(g2d, canvasSize, board);
	}
	
	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView sketchBoard) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			sketchBoard.getModel().getItems().add(editingTool.getItem(sketchBoard));
			editingTool.clear();
		} else {
			editingTool.onKeyPress(e, sketchBoard);
		}
		
		sketchBoard.repaint();
	}
}
