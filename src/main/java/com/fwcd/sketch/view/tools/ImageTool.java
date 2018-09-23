package com.fwcd.sketch.view.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.items.ImageItem;

public class ImageTool extends DrawTool<ImageItem> {
	private static final BufferedImage ICON_IMAGE;
	private static final ImageIcon ICON;
	
	static {
		try (InputStream in = ImageTool.class.getResourceAsStream("/imageToolIcon.png")) {
			ICON_IMAGE = ImageIO.read(in);
			ICON = new ImageIcon(ICON_IMAGE);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	@Override
	public ImageIcon getIcon() {
		return ICON;
	}

	@Override
	protected ImageItem getSketchItem(Vector2D startPos, BrushProperties props) {
		return new ImageItem(startPos, ICON_IMAGE, 0, 0);
	}

	@Override
	protected ImageItem updateItem(ImageItem item, Vector2D start, Vector2D last, Vector2D pos) {
		return item.resizedBy(pos.sub(last));
	}
}
