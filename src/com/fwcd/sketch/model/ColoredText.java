package com.fwcd.sketch.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Optional;

import javax.swing.JLabel;

import com.fwcd.fructose.geometry.Matrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Rectangle2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.tools.EditingTool;
import com.fwcd.sketch.tools.TextEditingTool;

public class ColoredText extends BasicSketchItem {
	private static final long serialVersionUID = 48975483798754L;
	private static final JLabel FAKE_LABEL = new JLabel(); // Hacky approach to deal with font metrics
	
	private final TextEditingTool editingTool = new TextEditingTool();
	private final String[] text;
	private final Color color;
	private final float brushThickness;
	private final Vector2D pos;
	
	private Vector2D lastLinePos;
	
	private transient Rectangle2D hitBox;
	
	public ColoredText(String text, Color color, float brushThickness, Vector2D pos) {
		if (text == null || text.length() == 0) {
			this.text = new String[] {""};
		} else {
			this.text = text.split("\n", -1);
		}
		
		this.color = color;
		this.brushThickness = brushThickness;
		this.pos = pos;
	}
	
	private ColoredText(String[] text, Color color, float brushThickness, Vector2D pos) {
		this.text = text;
		this.color = color;
		this.brushThickness = brushThickness;
		this.pos = pos;
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(color);
		g2d.setFont(getFont(g2d));
		
		int lineHeight = g2d.getFontMetrics().getHeight();
		int x = (int) pos.getX();
		int y = (int) pos.getY();
		int lastY = y;
		
		for (int line=0; line<lines(); line++) {
			lastY = y;
			
			g2d.drawString(text[line], x, y);
			
			y += lineHeight;
		}
		
		lastLinePos = new Vector2D(x, lastY);
	}

	private Font getFont(Graphics2D g2d) {
		return getFont(g2d.getFont());
	}
	
	private Font getFont(Font defaultFont) {
		return new Font(
				defaultFont.getFontName(),
				Font.PLAIN,
				(int) brushThickness * 8
		);
	}

	public Vector2D getLastLinePos() {
		return lastLinePos;
	}
	
	@Override
	public Vector2D getPos() {
		return pos;
	}
	
	public int lines() {
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
	public SketchItem movedBy(Vector2D delta) {
		return new ColoredText(text, color, brushThickness, pos.add(delta));
	}

	@Override
	public SketchItem transformedBy(Matrix transform) {
		// TODO: Implement text resizing (by changing font size proportionally?)
		return this;
	}

	@Override
	public Optional<EditingTool<?>> getEditingTool() {
		return Optional.of(editingTool);
	}
}
