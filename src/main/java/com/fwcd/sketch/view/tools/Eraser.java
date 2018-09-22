package com.fwcd.sketch.view.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.model.SketchItemPart;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItem;
import com.fwcd.sketch.view.canvas.SketchBoardView;

public class Eraser implements SketchTool {
	private static final ImageIcon ICON = new ResourceImage("/eraserIcon.png").getAsIcon();
	
	private boolean isActive = false;
	private int radius = 1; // Radius is dynamically set based off eraser speed

	private Vector2D pos;
	private Vector2D lastPos;
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}

	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		isActive = true;
		this.pos = pos;
		
		radius = 1;
		
		erase(board);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		lastPos = this.pos;
		this.pos = pos;
		
		radius += (int) Math.signum((pos.sub(lastPos).length() * 5) - radius);
		
		erase(board);
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		isActive = false;
		pos = null;
	}

	private void erase(SketchBoardView board) {
		for (SketchItemPart part : board.getModel().getDecomposedItems()) {
			SketchItem item = part.getItem();
			if (pos.sub(item.getPos()).length() < radius) { // TODO: Use hitboxes instead
				part.removeFromParent();
			}
		}
		
		Iterator<BoardItem> itemIterator = board.getModel().getItems().iterator();
		while (itemIterator.hasNext()) {
			if (itemIterator.next().get().canBeDisposed()) {
				itemIterator.remove();
			}
		}
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (isActive) {
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.setStroke(new BasicStroke(2));
			g2d.fillOval(
					(int) pos.getX() - radius,
					(int) pos.getY() - radius,
					radius * 2,
					radius * 2
			);
		}
	}
}
