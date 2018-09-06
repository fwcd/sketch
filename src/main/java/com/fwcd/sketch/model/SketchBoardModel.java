package com.fwcd.sketch.model;

import java.awt.Color;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Pair;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.sketch.model.items.SketchItem;
import com.fwcd.sketch.utils.PolymorphicSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SketchBoardModel {
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(SketchItem.class, new PolymorphicSerializer<SketchItem>())
			.create();
	private final ObservableList<SketchItem> items = new ObservableList<>();
	private final Observable<Color> background = new Observable<>(Color.WHITE);
	private final Observable<Boolean> showGrid = new Observable<>(false);
	private final Observable<Boolean> snapToGrid = new Observable<>(false);
	private Iterable<Pair<SketchItem, SketchItem>> decomposedItems;
	
	{
		items.listen(x -> {
			decomposedItems = null;
		});
	}
	
	public Observable<Color> getBackground() { return background; }
	
	public ObservableList<SketchItem> getItems() { return items; }
	
	public Observable<Boolean> getShowGrid() { return showGrid;}
	
	public Observable<Boolean> getSnapToGrid() { return snapToGrid;}
	
	public void replaceItem(SketchItem item, SketchItem replacement) {
		if (items.contains(item)) {
			int index = items.indexOf(item);
			items.set(index, replacement);
		}
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
	
	public String getItemsAsJSON() {
		return gson.toJson(items.get());
	}
	
	public void loadItemsFromJSON(String json) {
		items.set(gson.fromJson(json, new TypeToken<List<SketchItem>>() {}.getType()));
	}
	
	public void writeItemsAsJSON(Writer writer) {
		gson.toJson(items.get(), writer);
	}
	
	public void loadItemsFromJSON(Reader reader) {
		items.set(gson.fromJson(reader, new TypeToken<List<SketchItem>>() {}.getType()));
	}
}
