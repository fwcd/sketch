package fwcd.sketch.view.utils;

import java.util.Iterator;
import java.util.List;

public class DescendingIterator<T> implements Iterator<T>, Iterable<T> {
	private final List<T> list;
	private int i;
	
	public DescendingIterator(List<T> list) {
		this.list = list;
		i = list.size();
	}
	
	@Override
	public boolean hasNext() {
		return i > 0;
	}
	
	@Override
	public T next() {
		i--;
		T element = list.get(i);
		return element;
	}
	
	@Override
	public Iterator<T> iterator() { return this; }
}
