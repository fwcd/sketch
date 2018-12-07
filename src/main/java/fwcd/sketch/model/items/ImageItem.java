package fwcd.sketch.model.items;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import fwcd.fructose.geometry.DoubleMatrix;
import fwcd.fructose.geometry.Polygon2D;
import fwcd.fructose.geometry.Rectangle2D;
import fwcd.fructose.geometry.Vector2D;

public class ImageItem implements SketchItem {
	private static final long serialVersionUID = 874941690820073711L;
	
	private final Vector2D topLeft;
	private final byte[] rawImage;
	private final int width;
	private final int height;
	
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
		width = image.getWidth();
		height = image.getHeight();
		cachedImage = image;
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(image, "PNG", out);
			rawImage = out.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Creates a new {@link ImageItem} using a {@link BufferedImage}
	 * and a custom width/height.
	 * 
	 * <p>This operation is potentially expensive as the constructor
	 * will encode the image to a byte array.</p>
	 */
	public ImageItem(Vector2D topLeft, BufferedImage image, int width, int height) {
		this.topLeft = topLeft;
		this.width = width;
		this.height = height;
		cachedImage = image;
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(image, "PNG", out);
			rawImage = out.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public ImageItem(Vector2D topLeft, byte[] rawImage, int width, int height) {
		this.topLeft = topLeft;
		this.rawImage = rawImage;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Private constructor which assumes that {@code rawImage} and
	 * {@code cachedImage} represent the same image data.
	 */
	private ImageItem(Vector2D topLeft, byte[] rawImage, BufferedImage cachedImage, int width, int height) {
		this.topLeft = topLeft;
		this.rawImage = rawImage;
		this.cachedImage = cachedImage;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitImage(this);
	}
	
	/**
	 * Fetches the (lazily) decoded, unscaled image.
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
			hitBox = new Rectangle2D(topLeft, width, height);
		}
		
		return hitBox;
	}

	@Override
	public Vector2D getPos() { return topLeft; }
	
	public int getWidth() { return width; }
	
	public int getHeight() { return height; }
	
	@Override
	public ImageItem movedBy(Vector2D delta) {
		// TODO: Clone the image?
		return new ImageItem(topLeft.add(delta), rawImage, cachedImage, width, height);
	}
	
	@Override
	public ImageItem resizedBy(Vector2D delta) {
		return new ImageItem(topLeft, rawImage, cachedImage, width + (int) delta.getX(), height + (int) delta.getY());
	}
	
	@Override
	public ImageItem transformedBy(DoubleMatrix transform) {
		// TODO: Image transformations
		return this;
	}
}
