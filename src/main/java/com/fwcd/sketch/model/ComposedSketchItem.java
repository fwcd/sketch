package com.fwcd.sketch.model;

import java.util.Collection;

public interface ComposedSketchItem extends SketchItem {
	Collection<SketchItem> decompose();
}
