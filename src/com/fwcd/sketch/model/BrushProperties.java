package com.fwcd.sketch.model;

import java.awt.Color;

import com.fwcd.fructose.math.InclusiveIntRange;
import com.fwcd.fructose.swing.properties.RangedProperty;

public class BrushProperties {
	private RangedProperty thicknessProperty = new RangedProperty(new InclusiveIntRange(1, 10, 1, 3));
	private Color color = Color.BLACK;
	
	public RangedProperty getThicknessProperty() {
		return thicknessProperty;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
