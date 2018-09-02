package com.fwcd.sketch.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.canvas.MouseSelection;
import com.fwcd.sketch.canvas.MultiItemSelection;
import com.fwcd.sketch.canvas.SketchBoard;
import com.fwcd.sketch.model.SketchItem;

public class MoveTool extends BasicSketchTool {
	private static final ImageIcon ICON = new ResourceImage("/resources/moveIcon.png").getAsIcon();
	private Optional<MouseSelection> selection = Optional.empty();
	private Optional<EditingTool<?>> editTool = Optional.empty();
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoard board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseDown(pos, board);
		} else if (!selection.isPresent()) {
			selection = Optional.of(new MouseSelection(board));
		}
		
		selection.ifPresent(sel -> sel.onMouseDown(pos));
		board.repaint();
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoard board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseDrag(pos, board);
		} else {
			selection.ifPresent(sel -> sel.onMouseDrag(pos));
		}
		board.repaint();
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoard board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseUp(pos, board);
		} else {
			selection.ifPresent(sel -> sel.onMouseUp(pos));
		}
	}

	@Override
	public void onMouseClick(Vector2D pos, SketchBoard board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseClick(pos, board);
		} else {
			selection.ifPresent(sel -> sel.onMouseClick(pos));
		}
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoard board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).render(g2d, canvasSize, board);
		} else {
			selection.ifPresent(sel -> sel.render(g2d, canvasSize));
		}
	}

	@Override
	public void onMouseDoubleClick(Vector2D pos, SketchBoard board) {
		selection.ifPresent(sel -> {
			MultiItemSelection items = sel.getItems();
			if (!items.isEmpty()) {
				SketchItem item = items.firstItem();
				Optional<EditingTool<?>> editTool = item.getEditingTool();
				
				if (editTool.isPresent()) {
					this.editTool = editTool;
					board.remove(item);
					editTool.orElse(null).tryEditing(item);
				}
			}
		});
		board.repaint();
	}

	@Override
	public void onKeyPress(KeyEvent e, SketchBoard board) {
		if (editTool.isPresent()) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				editTool.ifPresent(tool -> board.add(tool.get(board)));
				editTool = Optional.empty();
			} else {
				editTool.orElse(null).onKeyPress(e, board);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
			selection.ifPresent(sel -> sel.getItems().forEach(board::remove));
		} else 
		board.repaint();
	}
}
