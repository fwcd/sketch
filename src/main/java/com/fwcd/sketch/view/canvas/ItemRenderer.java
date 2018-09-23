package com.fwcd.sketch.view.canvas;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import com.fwcd.fructose.draw.DrawGraphics;
import com.fwcd.fructose.geometry.LineSeg2D;
import com.fwcd.fructose.swing.SwingGraphics;
import com.fwcd.sketch.model.items.ColoredLine;
import com.fwcd.sketch.model.items.ColoredPath;
import com.fwcd.sketch.model.items.ColoredRect;
import com.fwcd.sketch.model.items.ColoredText;
import com.fwcd.sketch.model.items.ImageItem;
import com.fwcd.sketch.model.items.SketchItemVisitor;

public class ItemRenderer implements SketchItemVisitor {
	private final Graphics2D g2d;
	
	public ItemRenderer(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void visitLine(ColoredLine line) {
		g2d.setColor(line.getColor());
		g2d.setStroke(new BasicStroke(line.getThickness()));
		line.getLine().draw(new SwingGraphics(g2d));
	}

	@Override
	public void visitPath(ColoredPath path) {
		g2d.setColor(path.getColor());
		g2d.setStroke(new BasicStroke(path.getThickness()));
		
		DrawGraphics graphics = new SwingGraphics(g2d);
		for (LineSeg2D line : path.getLines()) {
			line.fill(graphics);
		}
	}

	@Override
	public void visitRect(ColoredRect rect) {
		g2d.setColor(rect.getColor());
		g2d.setStroke(new BasicStroke(rect.getThickness()));
		rect.getRect().draw(new SwingGraphics(g2d));
	}

	@Override
	public void visitText(ColoredText text) {
		g2d.setColor(text.getColor());
		g2d.setFont(text.getFont(g2d.getFont()));
		
		int lineCount = text.lineCount();
		int lineHeight = g2d.getFontMetrics().getHeight();
		int x = (int) text.getPos().getX();
		int y = (int) text.getPos().getY();
		
		for (int line=0; line<lineCount; line++) {
			g2d.drawString(text.getLine(line), x, y);
			y += lineHeight;
		}
	}
	
	@Override
	public void visitImage(ImageItem image) {
		int x = (int) image.getPos().getX();
		int y = (int) image.getPos().getY();
		g2d.drawImage(image.getImage(), x, y, image.getWidth(), image.getHeight(), null);
	}
}
