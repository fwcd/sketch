package com.fwcd.sketch.model.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;

public class ImageItem implements SketchItem {
	private static final long serialVersionUID = 874941690820073711L;
	
	private final Vector2D topLeft;
	private final byte[] rawImage;
	
	private transient BufferedImage cachedImage;
	private transient Rectangle2D hitBox;
	
	/**
	 * Creates a new {@link ImageItem} using a {@link BufferedImage}.
	 * 
	 * <p>This operation is potentially expensive as the constructor
	 * will encode the image to a byte array.</p>
	 */
	public ImageItem(Vector2D topLeft, BufferedImage image) {
		this.topLeft = topLeft;
		cachedImage = image;
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(image, "PNG", out);
			rawImage = out.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public ImageItem(Vector2D topLeft, byte[] rawImage) {
		
		this.topLeft = topLeft;
		this.rawImage = rawImage;
	}
	
	/**
	 * Private constructor which assumes that {@code rawImage} and
	 * {@code cachedImage} represent the same image data.
	 */
	private ImageItem(Vector2D topLeft, byte[] rawImage, BufferedImage cachedImage) {
		this.topLeft = topLeft;
		this.rawImage = rawImage;
		this.cachedImage = cachedImage;
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitImage(this);
	}
	
	/**
	 * Fetches the (lazily) decoded image.
	 * 
	 * <p>This operation is potentially expensive.</p>
	 */
	public BufferedImage getImage() {
		if (cachedImage == null) {
			try (InputStream in = new ByteArrayInputStream(rawImage)) {
				cachedImage = ImageIO.read(in);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		return cachedImage;
	}

	@Override
	public Polygon2D getHitBox() {
		if (hitBox == null) {
			BufferedImage image = getImage();
			hitBox = new Rectangle2D(topLeft, image.getWidth(), image.getHeight());
		}
		
		return hitBox;
	}

	@Override
	public Vector2D getPos() {
		return topLeft;
	}

	@Override
	public ImageItem movedBy(Vector2D delta) {
		// TODO: Clone the image?
		return new ImageItem(topLeft.add(delta), rawImage, cachedImage);
	}
	
	@Override
	public ImageItem resizedBy(Vector2D delta) {
		BufferedImage image = getImage();
		int scaledWidth = image.getWidth() + (int) delta.getX();
		int scaledHeight = image.getHeight() + (int) delta.getY();
		BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
		Graphics2D g2d = scaledImage.createGraphics();
		
		g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		
		return new ImageItem(topLeft, scaledImage);
	}
	
	@Override
	public ImageItem transformedBy(DoubleMatrix transform) {
		// TODO: Image transformations
		return this;
	}
}
