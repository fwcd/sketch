package fwcd.sketch.view.utils;

public class Indexed<T> {
	private final int index;
	private final T value;
	
	public Indexed(int index, T value) {
		this.index = index;
		this.value = value;
	}
	
	public int getIndex() { return index; }
	
	public T get() { return value; }
}
