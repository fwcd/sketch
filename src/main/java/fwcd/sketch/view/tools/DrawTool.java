package fwcd.sketch.view.tools;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.Option;
import fwcd.fructose.function.Subscription;
import fwcd.fructose.geometry.Vector2D;
import fwcd.sketch.model.BrushProperties;
import fwcd.sketch.model.items.BoardItemStack;
import fwcd.sketch.model.items.SketchItem;
import fwcd.sketch.view.canvas.ItemRenderer;
import fwcd.sketch.view.canvas.SketchBoardView;

public abstract class DrawTool<T extends SketchItem> implements SketchTool {
	private final EventListenerList<SketchItem> completionListeners = new EventListenerList<>();
	private T item = null;
	private BrushProperties props;
	private Vector2D start = null;
	private Vector2D current = null;
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView drawBoard) {
		start = pos;
		current = pos;
		props = drawBoard.getBrushProperties();
		item = getSketchItem(pos, props);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView drawBoard) {
		T newItem = updateItem(item, start, current, pos);
		
		if (!newItem.equals(item)) {
			item = newItem;
			current = pos;
		}
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView drawBoard) {
		T completed = prepareItemForBoard(item);
		completionListeners.fire(completed);
		drawBoard.getModel().addItem(new BoardItemStack(completed));
		
		start = null;
		current = null;
		item = null;
	}
	
	protected BrushProperties getBrushProperties() {
		return props;
	}
	
	protected abstract T getSketchItem(Vector2D startPos, BrushProperties props);
	
	protected abstract T updateItem(T item, Vector2D start, Vector2D last, Vector2D pos);
	
	/**
	 * Allows subclasses to perform additional
	 * processing with an item prior to placement.
	 */
	protected T prepareItemForBoard(T item) {
		return item;
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (item != null) {
			item.accept(new ItemRenderer(g2d));
		}
	}
	
	@Override
	public Option<Subscription> subscribeToCompletions(Consumer<? super SketchItem> listener) {
		return Option.of(completionListeners.subscribe(listener));
	}
}
