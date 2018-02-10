package com.fwcd.sketch.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fwcd.fructose.Pair;

public class SketchBoardModel implements Serializable {
	private static final long serialVersionUID = 938798223123734L;
	private Color background = Color.WHITE;
	private final List<SketchItem> items = new ArrayList<>();
	
	private transient Iterable<Pair<SketchItem, SketchItem>> decomposedItems;

	public void add(SketchItem item) {
		items.add(item);
		onChange();
	}
	
	public void remove(SketchItem item) {
		items.remove(item);
		onChange();
	}
	
	public void replace(SketchItem item, SketchItem replacement) {
		if (items.contains(item)) {
			int index = items.indexOf(item);
			items.remove(item);
			items.add(index, replacement);
			onChange();
		}
	}
	
	public void clear() {
		items.clear();
		onChange();
	}
	
	private void onChange() {
		decomposedItems = null;
	}

	public Iterable<Pair<SketchItem, SketchItem>> getDecomposedItems() {
		if (decomposedItems == null) {
			decomposedItems = items.stream()
					.flatMap(item -> {
						if (item instanceof ComposedSketchItem) {
							return ((ComposedSketchItem) item).decompose().stream()
									.map(decomposed -> new Pair<>(item, decomposed));
						} else {
							return Stream.of(new Pair<>(item, item));
						}
					})
					.collect(Collectors.toList());
		}
		
		return decomposedItems;
	}
	
	public Iterable<SketchItem> getItems() {
		return new ArrayList<>(items);
	}

	public void setBackground(Color background) {
		this.background = background;
	}
	
	public Color getBackground() {
		return background;
	}
}
