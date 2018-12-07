package fwcd.sketch.view.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.ImageIcon;

import fwcd.fructose.Option;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.ResourceImage;
import fwcd.sketch.model.items.BoardItem;
import fwcd.sketch.view.canvas.ItemEditingToolProvider;
import fwcd.sketch.view.canvas.MouseSelection;
import fwcd.sketch.view.canvas.MultiItemSelection;
import fwcd.sketch.view.canvas.SketchBoardView;

public class MoveTool implements SketchTool {
	private static final ImageIcon ICON = new ResourceImage("/moveIcon.png").getAsIcon();
	private Option<MouseSelection> selection = Option.empty();
	private Option<EditingTool<?>> editTool = Option.empty();
	private Option<BoardItem> editedItem = Option.empty();
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}
	
	private void withEditTool(Consumer<EditingTool<?>> consumer, SketchBoardView board) {
		EditingTool<?> tool = editTool.unwrap("Editing tool not present");
		BoardItem boardItem = editedItem.unwrap("Edited item not present");
		consumer.accept(tool);
		boardItem.set(tool.getItem(board));
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			withEditTool(tool -> tool.onMouseDown(pos, board), board);
		} else if (!selection.isPresent()) {
			selection = Option.of(new MouseSelection(board));
		}
		
		selection.ifPresent(sel -> sel.onMouseDown(pos));
		board.repaint();
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			withEditTool(tool -> tool.onMouseDrag(pos, board), board);
		} else {
			selection.ifPresent(sel -> sel.onMouseDrag(pos));
		}
		board.repaint();
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			withEditTool(tool -> tool.onMouseUp(pos, board), board);
		} else {
			selection.ifPresent(sel -> sel.onMouseUp(pos));
		}
	}

	@Override
	public void onMouseClick(Vector2D pos, SketchBoardView board) {
		if (editTool.isPresent()) {
			withEditTool(tool -> tool.onMouseClick(pos, board), board);
		} else {
			selection.ifPresent(sel -> sel.onMouseClick(pos));
		}
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (editTool.isPresent()) {
			editTool.unwrap().render(g2d, canvasSize, board);
		} else {
			selection.ifPresent(sel -> sel.render(g2d, canvasSize));
		}
	}

	@Override
	public void onMouseDoubleClick(Vector2D pos, SketchBoardView board) {
		selection.ifPresent(sel -> {
			MultiItemSelection items = sel.getItems();
			if (!items.isEmpty()) {
				BoardItem item = items.firstItem();
				item.accept(new ItemEditingToolProvider(tool -> {
					editTool = Option.of(tool);
					editedItem = Option.of(item);
					tool.tryEditing(item.get());
				}));
			}
		});
		board.repaint();
	}

	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView board) {
		if (editTool.isPresent()) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				editTool = Option.empty();
				editedItem = Option.empty();
			} else {
				withEditTool(tool -> tool.onKeyPress(e, board), board);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
			selection.ifPresent(sel -> sel.getItems().forEach(board.getModel()::removeItem));
		}
		board.repaint();
	}
}
