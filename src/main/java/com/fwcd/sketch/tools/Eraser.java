package com.fwcd.sketch.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import com.fwcd.fructose.Pair;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.canvas.SketchBoard;
import com.fwcd.sketch.model.SketchItem;

public class Eraser extends BasicSketchTool {
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
	public void onMouseDown(Vector2D pos, SketchBoard drawBoard) {
		isActive = true;
		this.pos = pos;
		
		radius = 1;
		
		erase(drawBoard);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoard drawBoard) {
		lastPos = this.pos;
		this.pos = pos;
		
		radius += (int) Math.signum((pos.sub(lastPos).length() * 5) - radius);
		
		erase(drawBoard);
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoard drawBoard) {
		isActive = false;
		pos = null;
	}

	private void erase(SketchBoard drawBoard) {
		for (Pair<SketchItem, SketchItem> itemPair : drawBoard.getDecomposedItems()) {
			// SketchItem A is always the parent item, whilst
			// SketchItem B may either be a (decomposed) sub-item or the parent item
			//
			// An example for a sub-item is a stroke inside a path
			
			if (pos.sub(itemPair.getRight().getPos()).length() < radius) { // TODO: Use hitboxes instead
				drawBoard.remove(itemPair.getLeft());
			}
		}
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoard drawBoard) {
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
