package fwcd.sketch.view.model;

public class TextPosition {
	private final int line;
	private final int column;
	
	public TextPosition(int line, int column) {
		this.line = Math.max(0, line);
		this.column = Math.max(0, column);
	}
	
	public int getLine() { return line; }
	
	public int getColumn() { return column; }
	
	public TextPosition withLine(int newLine) { return new TextPosition(newLine, column); }
	
	public TextPosition withColumn(int newColumn) { return new TextPosition(line, newColumn); }
	
	public TextPosition plusLines(int delta) { return new TextPosition(line + delta, column); }
	
	public TextPosition plusColumns(int delta) { return new TextPosition(line, column + delta); }
}
