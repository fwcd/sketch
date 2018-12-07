package fwcd.sketch.view.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fwcd.fructose.geometry.Vector2D;
import fwcd.fructose.swing.ResourceImage;
import fwcd.sketch.model.BrushProperties;
import fwcd.sketch.model.items.ImageItem;

public class ImageTool extends DrawTool<ImageItem> {
	private static final BufferedImage PLACEHOLDER = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private static final ImageIcon ICON = new ResourceImage("/imageToolIcon.png").getAsIcon();
	private final JFileChooser fileChooser = new JFileChooser();
	
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
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				try {
					return new ImageItem(item.getPos(), ImageIO.read(file), item.getWidth(), item.getHeight());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getClass().getSimpleName() + " while reading image file: " + e.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(null, "No image file selected!");
			}
		}
		return item;
	}
}
