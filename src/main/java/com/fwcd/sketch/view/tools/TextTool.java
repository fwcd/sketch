package com.fwcd.sketch.view.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.model.items.SketchItem;
import com.fwcd.sketch.view.canvas.SketchBoardView;

public class TextTool implements SketchTool {
	private static final ImageIcon ICON = new ResourceImage("/textIcon.png").getAsIcon();
	private final TextEditingTool editTool = new TextEditingTool();
	private Option<SketchItem> editedItem = Option.empty();
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}
	
	private void updateItemOn(SketchBoardView board) {
		SketchItem newItem = editTool.getItem(board);
		if (editedItem.isPresent()) {
			board.getModel().replaceItem(editedItem.unwrap(), newItem);
		} else {
			board.getModel().getItems().add(newItem);
		}
		editedItem = Option.of(newItem);
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		editTool.onMouseDown(pos, board);
		updateItemOn(board);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		editTool.onMouseDrag(pos, board);
		updateItemOn(board);
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		editTool.onMouseUp(pos, board);
		updateItemOn(board);
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		editTool.render(g2d, canvasSize, board);
	}
	
	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView board) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			editTool.clear();
			editedItem = Option.empty();
		} else {
			editTool.onKeyPress(e, board);
			updateItemOn(board);
		}
		
		board.repaint();
	}
}
