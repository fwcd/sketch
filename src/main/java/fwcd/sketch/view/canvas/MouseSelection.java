package fwcd.sketch.view.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.function.UnaryOperator;

import fwcd.fructose.Option;
import fwcd.fructose.geometry.Rectangle2D;
import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.Renderable;
import fwcd.fructose.swing.SwingGraphics;
import fwcd.sketch.model.items.BoardItemStack;
import fwcd.sketch.model.items.SketchItem;

public class MouseSelection implements Renderable {
	private static final Color FRAME_COLOR = new Color(100, 100, 255, 80); // Transparent gray
	
	private final SketchBoardView board;
	private final MultiItemSelection items;
	
	private Vector2D startPos = null;
	private Rectangle2D frame = null;
	
	private boolean selecting = false;
	
	public MouseSelection(SketchBoardView board) {
		this.board = board;
		items = new MultiItemSelection(board);
	}

	private Option<BoardItemStack> itemAt(Vector2D pos) {
		for (BoardItemStack item : board.getModel().descendingItems()) {
			if (item.get().getHitBox().contains(pos)) {
				return Option.of(item); // To allow only one selected item
			}
		}
		
		return Option.empty();
	}
	
	private boolean clickSelect(Vector2D pos) {
		clear();
		
		Option<BoardItemStack> item = itemAt(pos);
		item.ifPresent(items::add);
		return item.isPresent();
	}
	
	private void start(Vector2D pos) {
		boolean emptySpot = !itemAt(pos).isPresent();
		
		if (emptySpot) {
			if (!items.isEmpty()) {
				items.clear();
			}
			frame = new Rectangle2D(pos, pos);
			selecting = true;
			startPos = pos;
			update(pos);
		} else if (items.amount() < 2) {
			clickSelect(pos);
		}
	}
	
	private void update(Vector2D pos) {
		if (selecting) {
			items.clear();
			frame = new Rectangle2D(startPos, pos);
			
			for (BoardItemStack item : board.getModel().getItems()) {
				if (frame.intersects(item.get().getHitBox().getBoundingBox())) {
					items.add(item);
				}
			}
		}
	}
	
	private void clearFrame() {
		frame = null;
		selecting = false;
	}
	
	private void clear() {
		items.clear();
		clearFrame();
	}
	
	public void onMouseClick(Vector2D pos) {
		clickSelect(pos);
	}
	
	public void onMouseDown(Vector2D pos) {
		if (!items.onMouseDown(pos)) {
			start(pos);
		}
	}
	
	public void onMouseDrag(Vector2D pos) {
		items.onMouseDrag(pos);
		update(pos);
	}
	
	public void onMouseUp(Vector2D pos) {
		items.onMouseUp(pos);
		clearFrame();
		
		startPos = null;
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(FRAME_COLOR);
		
		if (frame != null) {
			frame.fill(new SwingGraphics(g2d));
		}
		
		items.render(g2d, canvasSize);
	}

	public MultiItemSelection getItems() {
		return items;
	}
	
	public void apply(UnaryOperator<SketchItem> operator) {
		if (!selecting) {
			items.apply(operator);
		}
	}
}
