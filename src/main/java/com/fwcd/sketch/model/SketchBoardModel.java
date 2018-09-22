package com.fwcd.sketch.model;

import java.awt.Color;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.structs.ObservableList;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItem;
import com.fwcd.sketch.model.utils.PolymorphicSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SketchBoardModel {
	private static final Type ITEMS_TYPE = new TypeToken<List<SketchItem>>() {}.getType();
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(SketchItem.class, new PolymorphicSerializer<SketchItem>())
			.create();
	private final ObservableList<BoardItem> items = new ObservableList<>();
	private final Observable<Color> background = new Observable<>(Color.WHITE);
	private final Observable<Boolean> showGrid = new Observable<>(false);
	private final Observable<Boolean> snapToGrid = new Observable<>(false);
	private Collection<SketchItemPart> decomposedItems;
	
	{
		items.listen(x -> {
			decomposedItems = null;
		});
	}
	
	public Observable<Color> getBackground() { return background; }
	
	public ObservableList<BoardItem> getItems() { return items; }
	
	public Observable<Boolean> getShowGrid() { return showGrid; }
	
	public Observable<Boolean> getSnapToGrid() { return snapToGrid; }

	public Collection<SketchItemPart> getDecomposedItems() {
		if (decomposedItems == null) {
			decomposedItems = items.stream()
				.flatMap(item -> item.get().decompose().stream().map(part -> new SketchItemPart(item, part)))
				.collect(Collectors.toList());
		}
		
		return decomposedItems;
	}
	
	public Stream<BoardItem> streamItems() {
		return items.stream();
	}
	
	public String getItemsAsJSON() {
		return gson.toJson(items.get());
	}
	
	public void loadItemsFromJSON(String json) {
		items.set(gson.fromJson(json, ITEMS_TYPE));
	}
	
	public void writeItemsAsJSON(Writer writer) {
		gson.toJson(items.get(), ITEMS_TYPE, writer);
	}
	
	public void readItemsFromJSON(Reader reader) {
		items.set(gson.fromJson(reader, ITEMS_TYPE));
	}
}
