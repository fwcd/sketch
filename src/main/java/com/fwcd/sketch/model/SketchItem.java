package com.fwcd.sketch.model;

import java.io.Serializable;
import java.util.Optional;

import com.fwcd.fructose.geometry.DoubleMatrix;
import com.fwcd.fructose.geometry.Polygon2D;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.sketch.view.tools.EditingTool;

/**
 * Represents a CanvasItem.<br><br>
 * 
 * <b>NOTE that all
 * implementations MUST be immutable!!</b>
 * 
 * @author Fredrik
 *
 */
public interface SketchItem extends Rendereable, Serializable {
	Polygon2D getHitBox();
	
	Vector2D getPos();
	
	SketchItem movedBy(Vector2D delta);
	
	SketchItem transformedBy(DoubleMatrix transform);
	
	SketchItem resizedBy(Vector2D delta);
	
	Optional<EditingTool<?>> getEditingTool();
}
