package com.fwcd.sketch.model;

import java.util.Collection;

import com.fwcd.sketch.model.items.SketchItem;

public interface ComposedSketchItem extends SketchItem {
	Collection<SketchItem> decompose();
}
