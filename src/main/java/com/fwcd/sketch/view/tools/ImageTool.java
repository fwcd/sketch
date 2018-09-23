package com.fwcd.sketch.view.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.ResourceImage;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.items.ImageItem;

public class ImageTool extends DrawTool<ImageItem> {
	private static final BufferedImage PLACEHOLDER = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private static final ImageIcon ICON = new ResourceImage("/imageToolIcon.png").getAsIcon();
	
	static {
		Graphics2D g2d = PLACEHOLDER.createGraphics();
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, PLACEHOLDER.getWidth(), PLACEHOLDER.getHeight());
		g2d.dispose();
	}
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}

	@Override
	protected ImageItem getSketchItem(Vector2D startPos, BrushProperties props) {
		return new ImageItem(startPos, PLACEHOLDER, 0, 0);
	}

	@Override
	protected ImageItem updateItem(ImageItem item, Vector2D start, Vector2D last, Vector2D pos) {
		return item.resizedBy(pos.sub(last));
	}
	
	@Override
	protected ImageItem prepareItemForBoard(ImageItem item) {
		return item;
	}
}
