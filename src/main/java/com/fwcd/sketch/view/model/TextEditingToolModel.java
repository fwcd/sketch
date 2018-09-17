package com.fwcd.sketch.view.model;

public class TextEditingToolModel {
	private final TextBufferModel text;
	private TextPosition cursor = new TextPosition(0, 0);
	
	public TextEditingToolModel() {
		text = new TextBufferModel();
	}
	
	public TextEditingToolModel(String[] lines) {
		text = new TextBufferModel(lines);
	}
	
	public String getLineFragment(int line, int start, int end) {
		return text.getLine(line).substring(start, end);
	}
	
	public void insert(String delta) {
		text.getLine(cursor.getLine()).insert(cursor.getColumn(), delta);
		cursor = cursor.plusColumns(delta.length());
	}
	
	public void insert(char delta) {
		text.getLine(cursor.getLine()).insert(cursor.getColumn(), delta);
		cursor = cursor.plusColumns(1);
	}
	
	public void backspace() {
		int line = cursor.getLine();
		int col = cursor.getColumn();
		if (col <= 0) {
			if (line > 0) {
				moveCursorLeft();
				text.mergeLines(line - 1, line);
			}
		} else {
			text.getLine(cursor.getLine()).deleteCharAt(col - 1);
			cursor = cursor.plusColumns(-1);
		}
	}
	
	public void enter() {
		text.insertLineBreak(cursor.getLine(), cursor.getColumn());
		cursor = cursor
			.plusLines(1)
			.withColumn(0);
	}
	
	public void moveCursorLeft() {
		int line = cursor.getLine();
		int col = cursor.getColumn();
		if (col <= 0) {
			if (line > 0) {
				cursor = cursor.plusLines(-1).withColumn(text.getLine(line - 1).length());
			}
		} else {
			cursor = cursor.plusColumns(-1);
		}
	}
	
	public void moveCursorRight() {
		int line = cursor.getLine();
		int col = cursor.getColumn();
		if (col >= text.getLine(line).length()) {
			if (line < (text.lineCount() - 1)) {
				cursor = cursor
					.plusLines(1)
					.withColumn(0);
			}
		} else {
			cursor = cursor.plusColumns(1);
		}
	}
	
	public TextPosition getCursor() {
		return cursor;
	}
	
	public String[] toLines() {
		return text.toLines();
	}
}
