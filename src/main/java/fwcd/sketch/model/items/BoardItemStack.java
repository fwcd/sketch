package fwcd.sketch.model.items;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import fwcd.fructose.EventListenerList;
import fwcd.fructose.structs.ArrayStack;
import fwcd.fructose.structs.Stack;

/**
 * A mutable stack of items on the sketch board.
 */
public class BoardItemStack {
	private final EventListenerList<Iterable<SketchItem>> listeners = new EventListenerList<>();
	private Stack<SketchItem> stack;
	
	public BoardItemStack(SketchItem item) {
		this.stack = new ArrayStack<>(item);
	}
	
	public BoardItemStack(SketchItem... items) {
		this.stack = new ArrayStack<>();
		for (SketchItem item : items) {
			stack.push(item);
		}
	}
	
	public void accept(SketchItemVisitor visitor) {
		for (SketchItem item : stack) {
			item.accept(visitor);
		}
	}
	
	public SketchItem get() { return stack.peek(); }
	
	public SketchItem pop() {
		SketchItem result = stack.pop();
		listeners.fire(stack);
		return result;
	}
	
	public void push(SketchItem newItem) {
		stack.push(newItem);
		listeners.fire(stack);
	}
	
	public void set(SketchItem newItem) {
		stack.rebase(newItem);
		listeners.fire(stack);
	}
	
	public void listen(Consumer<Iterable<SketchItem>> consumer) { listeners.add(consumer); }
	
	public void unlisten(Consumer<Iterable<SketchItem>> consumer) { listeners.remove(consumer); }
	
	public void apply(UnaryOperator<SketchItem> mapper) {
		Stack<SketchItem> newStack = new ArrayStack<>();
		for (SketchItem item : stack) {
			newStack.push(mapper.apply(item));
		}
		stack = newStack;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof BoardItemStack)) return false;
		return ((BoardItemStack) obj).stack.equals(stack);
	}
	
	@Override
	public int hashCode() {
		return stack.hashCode();
	}
}
