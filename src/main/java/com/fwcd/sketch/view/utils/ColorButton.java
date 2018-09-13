package com.fwcd.sketch.view.utils;

import java.awt.Color;
import java.awt.Dimension;

import com.fwcd.fructose.swing.DrawGraphicsButton;

public class ColorButton extends DrawGraphicsButton {
	private static final Dimension DEFAULT_SIZE = new Dimension(24, 24);
	private static final long serialVersionUID = 4338739485739845L;
	
	public ColorButton(Color color) {
		this(DEFAULT_SIZE, color);
	}
	
	public ColorButton(Color color, Runnable onClick) {
		this(DEFAULT_SIZE, color, onClick);
	}
	
	public ColorButton(Dimension size, Color color) {
		super(size, (g2d, canvasSize) -> {
			g2d.setColor(color);
			
			int w = (int) canvasSize.getWidth();
			int h = (int) canvasSize.getHeight();
			int iconSize = Math.min(w, h);
			
			g2d.fillOval((w / 2) - (iconSize / 2), (h / 2) - (iconSize / 2), iconSize, iconSize);
		});
	}
	
	public ColorButton(Dimension size, Color color, Runnable onClick) {
		this(size, color);
		addActionListener(l -> onClick.run());
	}
}
