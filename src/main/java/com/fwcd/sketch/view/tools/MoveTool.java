package com.fwcd.sketch.view.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.view.canvas.ItemEditingToolProvider;
import com.fwcd.sketch.view.canvas.MouseSelection;
import com.fwcd.sketch.view.canvas.MultiItemSelection;
import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.sketch.model.items.SketchItem;

public class MoveTool implements SketchTool {
	private static final ImageIcon ICON = new ResourceImage("/moveIcon.png").getAsIcon();
	private Optional<MouseSelection> selection = Optional.empty();
	private Optional<EditingTool<?>> editTool = Optional.empty();
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseDown(pos, board);
		} else if (!selection.isPresent()) {
			selection = Optional.of(new MouseSelection(board));
		}
		
		selection.ifPresent(sel -> sel.onMouseDown(pos));
		board.repaint();
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseDrag(pos, board);
		} else {
			selection.ifPresent(sel -> sel.onMouseDrag(pos));
		}
		board.repaint();
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseUp(pos, board);
		} else {
			selection.ifPresent(sel -> sel.onMouseUp(pos));
		}
	}

	@Override
	public void onMouseClick(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).onMouseClick(pos, board);
		} else {
			selection.ifPresent(sel -> sel.onMouseClick(pos));
		}
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (editTool.isPresent()) {
			editTool.orElse(null).render(g2d, canvasSize, board);
		} else {
			selection.ifPresent(sel -> sel.render(g2d, canvasSize));
		}
	}

	@Override
	public void onMouseDoubleClick(Vector2D pos, SketchBoardView board) {
		selection.ifPresent(sel -> {
			MultiItemSelection items = sel.getItems();
			if (!items.isEmpty()) {
				SketchItem item = items.firstItem();
				item.accept(new ItemEditingToolProvider(tool -> {
					editTool = Optional.of(tool);
					board.getModel().getItems().remove(item);
					tool.tryEditing(item);
				}));
			}
		});
		board.repaint();
	}

	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView board) {
		if (editTool.isPresent()) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				editTool.ifPresent(tool -> board.getModel().getItems().add(tool.get(board)));
				editTool = Optional.empty();
			} else {
				editTool.orElse(null).onKeyPress(e, board);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
			selection.ifPresent(sel -> sel.getItems().forEach(board.getModel().getItems()::remove));
		} else 
		board.repaint();
	}
}
