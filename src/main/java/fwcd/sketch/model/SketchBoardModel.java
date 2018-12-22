package fwcd.sketch.model;

import java.awt.Color;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fwcd.fructose.Observable;
import fwcd.sketch.model.event.BoardItemEventBus;
import fwcd.sketch.model.items.BoardItemStack;
import fwcd.sketch.model.items.SketchItem;
import fwcd.sketch.model.utils.Base64Serializer;
import fwcd.sketch.model.utils.PolymorphicSerializer;
import fwcd.sketch.view.utils.DescendingIterator;
import fwcd.sketch.view.utils.Indexed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * The central data model of Sketch. Holds the drawable
 * elements and properties about the board.
 */
public class SketchBoardModel {
	private static final Type ITEMS_TYPE = new TypeToken<List<SketchItem>>() {}.getType();
	private final Gson gson = new GsonBuilder()
		.registerTypeAdapter(byte[].class, new Base64Serializer())
		.registerTypeAdapter(SketchItem.class, new PolymorphicSerializer<SketchItem>())
		.create();
	
	private final Observable<Color> background = new Observable<>(Color.WHITE);
	private final Observable<Boolean> showGrid = new Observable<>(false);
	private final Observable<Boolean> snapToGrid = new Observable<>(false);
	
	private final BoardItemEventBus itemEventBus = new BoardItemEventBus();
	private List<BoardItemStack> items = new ArrayList<>();
			
	public Observable<Color> getBackground() { return background; }
	
	public Observable<Boolean> getShowGrid() { return showGrid; }
	
	public Observable<Boolean> getSnapToGrid() { return snapToGrid; }
	
	public BoardItemEventBus getItemEventBus() { return itemEventBus; }
	
	public Iterable<BoardItemStack> getItems() { return items; }
	
	public Stream<BoardItemStack> streamItems() { return items.stream(); }
	
	public DescendingIterator<BoardItemStack> descendingItems() { return new DescendingIterator<>(items); }
	
	public int itemCount() { return items.size(); }
	
	public void addItem(BoardItemStack item) {
		items.add(item);
		
		int index = items.size() - 1;
		item.listen(it -> {
			itemEventBus.getItemChangeListeners().fire(new Indexed<>(index, item));
			itemEventBus.getChangeListeners().fire(items);
		});
		
		itemEventBus.getAddListeners().fire(item);
		itemEventBus.getChangeListeners().fire(items);
	}
	
	public void removeItem(BoardItemStack item) {
		items.remove(item);
		
		itemEventBus.getModifyListeners().fire(items);
		itemEventBus.getChangeListeners().fire(items);
	}
	
	public void clear() {
		items.clear();
		
		itemEventBus.getModifyListeners().fire(items);
		itemEventBus.getChangeListeners().fire(items);
	}
	
	public Collection<SketchItemPart> getDecomposedItems() {
		return items.stream()
			.flatMap(item -> {
				Collection<SketchItemPart> decomposed = item.get().decompose();
				if (decomposed.isEmpty()) {
					return Stream.of(new SketchItemPart(item.get(), () -> removeItem(item)));
				} else {
					return decomposed.stream()
						.map(part -> part.withRemoveListener(() -> {
							itemEventBus.getItemChangeListeners().fire(new Indexed<>(items.indexOf(item), item));
							itemEventBus.getChangeListeners().fire(items);
						}));
				}
			})
			.collect(Collectors.toList());
	}
	
	private List<SketchItem> getSketchItems() {
		return items.stream().map(BoardItemStack::get).collect(Collectors.toList());
	}
	
	private void setSketchItems(List<SketchItem> sketchItems) {
		items = sketchItems.stream().map(BoardItemStack::new).collect(Collectors.toCollection(ArrayList::new));
		itemEventBus.getModifyListeners().fire(items);
		itemEventBus.getChangeListeners().fire(items);
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
