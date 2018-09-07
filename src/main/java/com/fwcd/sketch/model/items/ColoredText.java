package com.fwcd.sketch.model.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;

public class ColoredText implements ColoredSketchItem {
	private static final long serialVersionUID = 48975483798754L;
	private static final JLabel FAKE_LABEL = new JLabel(); // Hacky approach to deal with font metrics
	
	private final String[] text;
	private final Color color;
	private final float size;
	private final Vector2D pos;
	
	private Vector2D lastLinePos;
	
	private transient Rectangle2D hitBox;
	
	public ColoredText(String text, Color color, float size, Vector2D pos) {
		if (text == null || text.length() == 0) {
			this.text = new String[] {""};
		} else {
			this.text = text.split("\n", -1);
		}
		
		this.color = color;
		this.size = size;
		this.pos = pos;
	}
	
	private ColoredText(String[] text, Color color, float size, Vector2D pos) {
		this.text = text;
		this.color = color;
		this.size = size;
		this.pos = pos;
	}

	public float getSize() {
		return size;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	public void setLastLinePos(Vector2D lastLinePos) {
		this.lastLinePos = lastLinePos;
	}
	
	public Vector2D getLastLinePos() {
		return lastLinePos;
	}
	
	@Override
	public void accept(SketchItemVisitor visitor) {
		visitor.visitText(this);
	}
	
	@Override
	public Vector2D getPos() {
		return pos;
	}
	
	public Font getFont(Font base) {
		return new Font(
			base.getFontName(),
			Font.PLAIN,
			(int) size * 8
		);
	}
	
	public int lineCount() {
		return text.length;
	}

	public String getLine(int i) {
		return text[i];
	}

	public String lastLine() {
		return text[text.length - 1];
	}
	
	public String getTotalText() {
		StringBuilder total = new StringBuilder();
		
		int i = 0;
		for (String line : text) {
			total.append(line);
			
			if (i < (text.length - 1)) {
				total.append("\n");
			}
			
			i++;
		}
		
		return total.toString();
	}

	private FontMetrics getMetrics() {
		return FAKE_LABEL.getFontMetrics(getFont(FAKE_LABEL.getFont()));
	}

	@Override
	public Polygon2D getHitBox() {
		if (hitBox == null) {
			FontMetrics metrics = getMetrics();
			
			int width = 0;
			int height = 0;
			
			for (String line : text) {
				width = Math.max(width, metrics.stringWidth(line));
				height += metrics.getHeight();
			}
			
			hitBox = new Rectangle2D(
					pos.sub(new Vector2D(0, metrics.getHeight())),
					width,
					height
			);
		}
		
		return hitBox;
	}

	@Override
	public ColoredText movedBy(Vector2D delta) {
		return new ColoredText(text, color, size, pos.add(delta));
	}

	@Override
	public ColoredText transformedBy(DoubleMatrix transform) {
		// TODO: Implement text resizing (by changing font size proportionally?)
		return this;
	}
}
