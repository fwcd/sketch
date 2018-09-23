package com.fwcd.sketch.model;

import java.awt.Color;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fwcd.fructose.Observable;
import com.fwcd.sketch.model.event.BoardItemEventBus;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItem;
import com.fwcd.sketch.model.utils.PolymorphicSerializer;
import com.fwcd.sketch.view.utils.DescendingIterator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SketchBoardModel {
	private static final Type ITEMS_TYPE = new TypeToken<List<SketchItem>>() {}.getType();
	private final Gson gson = new GsonBuilder()
		.registerTypeAdapter(SketchItem.class, new PolymorphicSerializer<SketchItem>())
		.create();
	
	private final Observable<Color> background = new Observable<>(Color.WHITE);
	private final Observable<Boolean> showGrid = new Observable<>(false);
	private final Observable<Boolean> snapToGrid = new Observable<>(false);
	private final BoardItemEventBus itemEventBus = new BoardItemEventBus();
	private List<BoardItem> items = new ArrayList<>();
			
	public Observable<Color> getBackground() { return background; }
	
	public Observable<Boolean> getShowGrid() { return showGrid; }
	
	public Observable<Boolean> getSnapToGrid() { return snapToGrid; }
	
	public BoardItemEventBus getItemEventBus() { return itemEventBus; }
	
	public Iterable<BoardItem> getItems() { return items; }
	
	public Stream<BoardItem> streamItems() { return items.stream(); }
	
	public DescendingIterator<BoardItem> descendingItems() { return new DescendingIterator<>(items); }
	
	public int itemCount() { return items.size(); }
	
	public void addItem(BoardItem item) {
		items.add(item);
		itemEventBus.getAddListeners().fire(item);
		itemEventBus.getUpdateListeners().fire(items);
	}
	
	public void removeItem(BoardItem item) {
		items.remove(item);
		itemEventBus.getModifyListeners().fire(items);
		itemEventBus.getUpdateListeners().fire(items);
	}
	
	public Collection<SketchItemPart> getDecomposedItems() {
		return items.stream()
			.flatMap(item -> {
				Collection<SketchItemPart> decomposed = item.get().decompose();
				if (decomposed.isEmpty()) {
					return Stream.of(new SketchItemPart(item.get(), () -> items.remove(item)));
				} else {
					return decomposed.stream();
				}
			})
			.collect(Collectors.toList());
	}
	
	private List<SketchItem> getSketchItems() {
		return items.stream().map(BoardItem::get).collect(Collectors.toList());
	}
	
	private void setSketchItems(List<SketchItem> sketchItems) {
		items = sketchItems.stream().map(BoardItem::new).collect(Collectors.toCollection(ArrayList::new));
		itemEventBus.getModifyListeners().fire(items);
		itemEventBus.getUpdateListeners().fire(items);
	}
	
	public String getItemsAsJSON() {
		return gson.toJson(items);
	}
	
	public void loadItemsFromJSON(String json) {
		setSketchItems(gson.fromJson(json, ITEMS_TYPE));
	}
	
	public void writeItemsAsJSON(Writer writer) {
		gson.toJson(getSketchItems(), ITEMS_TYPE, writer);
	}
	
	public void readItemsFromJSON(Reader reader) {
		setSketchItems(gson.fromJson(reader, ITEMS_TYPE));
	}
}
